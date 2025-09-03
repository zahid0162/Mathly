package com.zahid.mathly.presentation.viewmodel.profile

import com.zahid.mathly.domain.model.ProfileData

data class ProfileUiState(
    val errorMessage: String? = null,
    val finished: Boolean = false,
    val loading: Boolean = false,
    val profileData: ProfileData = ProfileData()
)