package com.zahid.mathly.presentation.viewmodel.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahid.mathly.R
import com.zahid.mathly.data.local.SessionManager
import com.zahid.mathly.domain.model.ProfileData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.Calendar
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val sessionManager: SessionManager
): ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        fetchProfile()
    }

    fun fetchProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(loading = true)
            try {
                val userId = sessionManager.userId
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

    fun getTimeBasedGreeting(context: Context): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        return when (hour) {
            in 5..11 -> context.getString(R.string.good_morning)
            in 12..16 -> context.getString(R.string.good_afternoon)
            in 17..20 -> context.getString(R.string.good_evening)
            else -> context.getString(R.string.good_night)
        }
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