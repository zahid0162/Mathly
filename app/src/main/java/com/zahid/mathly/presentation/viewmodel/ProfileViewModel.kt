package com.zahid.mathly.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahid.mathly.data.local.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.UploadStatus
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.uploadAsFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileData(
    val fullName: String = "",
    val age: Int? = null,
    val gender: String = "",
    val occupation: String = "",
    val avatarUrl: String? = null
)

data class ProfileUiState(
    val errorMessage: String? = null,
    val finished: Boolean = false,
    val loading: Boolean = false,
    val profileData: ProfileData = ProfileData()
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val sessionManager: SessionManager
): ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState
    
    fun fetchProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(loading = true)
            try {
                val userId = supabaseClient.auth.currentUserOrNull()?.id
                    ?: throw IllegalStateException("Not authenticated")
                
                val response = supabaseClient.postgrest["profiles"]
                    .select {
                        filter {
                            eq("id", userId)
                        }
                    }
                    .decodeSingle<Map<String, String?>>()
                
                val profileData = ProfileData(
                    fullName = response["full_name"] ?: "",
                    age = response["age"]?.toIntOrNull(),
                    gender = response["gender"] ?: "",
                    occupation = response["occupation"] ?: "",
                    avatarUrl = response["avatar_url"]
                )
                
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    profileData = profileData
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    errorMessage = "Failed to load profile: ${e.message}"
                )
            }
        }
    }

    fun saveProfile(
        fullName: String?,
        occupation: String,
        gender: String,
        age: Int?,
        imageBytes: ByteArray? = null,
        fileExtension: String? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(loading = true, errorMessage = null)
            try {
                val userId = supabaseClient.auth.currentUserOrNull()?.id
                    ?: throw IllegalStateException("Not authenticated")

                val payload = mutableMapOf(
                    "id" to userId,
                    "occupation" to occupation,
                    "gender" to gender
                )
                if (age != null) payload["age"] = age.toString()
                if (!fullName.isNullOrBlank()) payload["full_name"] = fullName

                // Upload image if provided
                if (imageBytes != null && fileExtension != null) {
                    try {
                        val avatarUrl = uploadAvatar(imageBytes, fileExtension)
                        payload["avatar_url"] = avatarUrl
                    } catch (e: Exception) {
                        // Log but don't fail the whole profile save if image upload fails
                        e.printStackTrace()
                    }
                }

                // Upsert profile
                supabaseClient.postgrest["profiles"].upsert(payload)

                // Update session state
                sessionManager.hasCompletedProfile = true
                sessionManager.lastScreen = "main"
                _uiState.value = ProfileUiState(finished = true, loading = false)
            } catch (e: Exception) {
                _uiState.value = ProfileUiState(errorMessage = e.message, loading = false)
            }
        }
    }

    private suspend fun uploadAvatar(imageBytes: ByteArray, fileExtension: String): String {
        val userId = supabaseClient.auth.currentUserOrNull()?.id
            ?: throw IllegalStateException("Not authenticated")

        val path = "$userId/avatar_${System.currentTimeMillis()}.$fileExtension"
        supabaseClient.storage.from("avatars").upload(path,imageBytes) {
            upsert = true
        }
        return supabaseClient.storage.from("avatars").publicUrl(path)

    }



    fun updateProfile(
        fullName: String?,
        occupation: String,
        gender: String,
        age: Int?,
        imageUrl : String? = null,
        imageBytes: ByteArray? = null,
        fileExtension: String? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(loading = true, errorMessage = null)
            try {
                val userId = supabaseClient.auth.currentUserOrNull()?.id
                    ?: throw IllegalStateException("Not authenticated")

                val payload = mutableMapOf(
                    "id" to userId,
                    "occupation" to occupation,
                    "gender" to gender
                )
                if (age != null) payload["age"] = age.toString()
                if (!fullName.isNullOrBlank()) payload["full_name"] = fullName

                // Upload image if provided
                if (imageBytes != null && fileExtension != null) {
                    try {
                        val avatarUrl = uploadAvatar(imageBytes, fileExtension)
                        payload["avatar_url"] = avatarUrl
                    } catch (e: Exception) {
                        // Log but don't fail the whole profile update if image upload fails
                        e.printStackTrace()
                    }
                }
                else if (imageUrl!=null){
                    payload["avatar_url"] = imageUrl
                }

                // Update profile
                supabaseClient.postgrest["profiles"].update(payload) {
                    filter {
                        eq("id", userId)
                    }
                }

                // Fetch updated profile
                fetchProfile()
                
                _uiState.value = _uiState.value.copy(finished = true, loading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message, loading = false)
            }
        }
    }

    fun onNavigationHandled() {
        _uiState.value = _uiState.value.copy(finished = false)
    }
}