package com.zahid.mathly.domain.model

data class ProfileData(
    val fullName: String = "",
    val age: Int? = null,
    val gender: String = "",
    val occupation: String = "",
    val avatarUrl: String? = null
)
