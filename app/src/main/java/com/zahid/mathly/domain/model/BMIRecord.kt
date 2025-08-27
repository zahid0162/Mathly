package com.zahid.mathly.domain.model

import java.util.UUID

data class BMIRecord(
    val id: String = UUID.randomUUID().toString(),
    val height: Double,
    val weight: Double,
    val bmiValue: Double,
    val category: String,
    val timestamp: Long = System.currentTimeMillis()
)
