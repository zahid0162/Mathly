package com.zahid.mathly.domain.model

data class CaloriesAnalysis(
    val foodDescription: String,
    val breakdown: List<FoodItem>,
    val totalCalories: Int,
    val suggestedExercises: List<Exercise>
)

data class FoodItem(
    val name: String,
    val calories: Int,
    val serving: String = "1 serving"
)

data class Exercise(
    val name: String,
    val duration: String,
    val caloriesBurned: Int,
    val intensity: String = "moderate"
) 