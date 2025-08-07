package com.zahid.mathly.domain.model

data class WordProblem(
    val problem: String,
    val extractedEquation: String? = null,
    val solution: Solution? = null
) 