package com.zahid.mathly.domain.model

data class Solution(
    val id: String = "",
    val equationId: String,
    val steps: List<SolutionStep>,
    val finalAnswer: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class SolutionStep(
    val stepNumber: Int,
    val description: String,
    val calculation: String,
    val result: String
) 