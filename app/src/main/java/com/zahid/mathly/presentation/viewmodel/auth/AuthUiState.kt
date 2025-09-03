package com.zahid.mathly.presentation.viewmodel.auth

data class AuthUiState(
    val loading: Boolean = false,
    val errorMessage: String? = null,
    val navigateTo: AuthDestination? = null
)