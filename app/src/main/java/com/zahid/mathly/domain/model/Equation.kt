package com.zahid.mathly.domain.model

data class Equation(
    val id: String = "",
    val expression: String,
    val timestamp: Long = System.currentTimeMillis(),
    val source: EquationSource = EquationSource.MANUAL
)

enum class EquationSource {
    SCAN,
    MANUAL
} 