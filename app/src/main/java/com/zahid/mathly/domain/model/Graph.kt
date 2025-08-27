package com.zahid.mathly.domain.model

import java.util.UUID

data class Graph(
    val id: String = UUID.randomUUID().toString(),
    val equation: String,
    val timestamp: Long = System.currentTimeMillis()
)
