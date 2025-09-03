package com.zahid.mathly.domain.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class HomeMenuItem(
    val title: String,
    val icon: ImageVector,
    val route: String,
    val startColor: Color,
    val endColor: Color,
    val contentColor: Color,
)
