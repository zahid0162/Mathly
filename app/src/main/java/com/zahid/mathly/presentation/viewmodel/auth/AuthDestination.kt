package com.zahid.mathly.presentation.viewmodel.auth

sealed class AuthDestination {
    data object ProfileSetup: AuthDestination()
    data object Main: AuthDestination()
}