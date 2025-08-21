package com.zahid.mathly.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahid.mathly.data.local.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.contentOrNull
import javax.inject.Inject

sealed class AuthDestination {
    data object ProfileSetup: AuthDestination()
    data object Main: AuthDestination()
}

data class AuthUiState(
    val loading: Boolean = false,
    val errorMessage: String? = null,
    val navigateTo: AuthDestination? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    init {
        // Check session state on initialization
        viewModelScope.launch(Dispatchers.IO) {
            checkSession()
        }
    }

    private suspend fun checkSession() {
        if (sessionManager.isLoggedIn) {
            val userId = supabaseClient.auth.currentUserOrNull()?.id
            if (userId != null) {
                sessionManager.userId = userId
                if (sessionManager.hasCompletedProfile) {
                    _uiState.value = AuthUiState(navigateTo = AuthDestination.Main)
                } else {
                    _uiState.value = AuthUiState(navigateTo = AuthDestination.ProfileSetup)
                }
            } else {
                sessionManager.clearSession()
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(loading = true, errorMessage = null)
            try {
                supabaseClient.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }

                val userId = supabaseClient.auth.currentUserOrNull()?.id
                if (userId == null) {
                    _uiState.value = AuthUiState(loading = false, errorMessage = "Auth failed")
                    return@launch
                }

                // Set session as logged in
                sessionManager.isLoggedIn = true
                sessionManager.userId = userId

                val profiles = supabaseClient.postgrest["profiles"].select {
                    filter { eq("id", userId) }
                    limit(1)
                }.decodeList<JsonObject>()

                val first = profiles.firstOrNull()
                val occupation = first?.get("occupation")?.jsonPrimitive?.contentOrNull
                val gender = first?.get("gender")?.jsonPrimitive?.contentOrNull
                val age = first?.get("age")?.jsonPrimitive?.intOrNull

                val hasCompletedProfile = !(first == null || occupation.isNullOrBlank() || gender.isNullOrBlank() || age == null)
                sessionManager.hasCompletedProfile = hasCompletedProfile

                val dest = if (hasCompletedProfile) {
                    sessionManager.lastScreen = "main"
                    AuthDestination.Main
                } else {
                    sessionManager.lastScreen = "profileSetup"
                    AuthDestination.ProfileSetup
                }

                _uiState.value = AuthUiState(loading = false, navigateTo = dest)
            } catch (e: Exception) {
                _uiState.value = AuthUiState(loading = false, errorMessage = e.message)
                sessionManager.clearSession()
            }
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(loading = true, errorMessage = null)
            try {
                supabaseClient.auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                }

                val userId = supabaseClient.auth.currentUserOrNull()?.id
                if (userId != null) {
                    sessionManager.isLoggedIn = true
                    sessionManager.userId = userId
                    sessionManager.hasCompletedProfile = false
                    sessionManager.lastScreen = "profileSetup"
                }

                _uiState.value = AuthUiState(loading = false, navigateTo = AuthDestination.ProfileSetup)
            } catch (e: Exception) {
                _uiState.value = AuthUiState(loading = false, errorMessage = e.message)
                sessionManager.clearSession()
            }
        }
    }

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                supabaseClient.auth.signOut()
            } finally {
                sessionManager.clearSession()
                _uiState.value = AuthUiState()
            }
        }
    }

    fun onNavigationHandled() {
        _uiState.value = _uiState.value.copy(navigateTo = null)
    }

    fun getCurrentUserEmail(): String{
        return supabaseClient.auth.currentUserOrNull()?.email ?:"abc@gmail.com"
    }
}


