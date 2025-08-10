package com.zahid.mathly.domain.model

enum class SolutionType {
    EQUATION,
    WORD_PROBLEM
}

data class Solution(
    val id: String = "",
    val equationId: String = "",
    val originalProblem: String = "",
    val type: SolutionType = SolutionType.EQUATION,
    val steps: List<SolutionStep> = emptyList(),
    val finalAnswer: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class SolutionStep(
    val stepNumber: Int = 0,
    val description: String = "",
    val calculation: String = "",
    val result: String = ""
)