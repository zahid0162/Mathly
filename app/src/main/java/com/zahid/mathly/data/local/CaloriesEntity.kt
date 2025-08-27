package com.zahid.mathly.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zahid.mathly.domain.model.CaloriesAnalysis
import com.zahid.mathly.domain.model.Exercise
import com.zahid.mathly.domain.model.FoodItem

@Entity(tableName = "calories")
data class CaloriesEntity(
    @PrimaryKey val id: String,
    val foodDescription: String,
    val totalCalories: Int,
    val timestamp: Long,
    val breakdownJson: String,
    val exercisesJson: String
) {
    fun toDomain(): CaloriesAnalysis {
        val gson = Gson()
        val breakdownType = object : TypeToken<List<FoodItem>>() {}.type
        val exercisesType = object : TypeToken<List<Exercise>>() {}.type
        
        val breakdown = gson.fromJson<List<FoodItem>>(breakdownJson, breakdownType)
        val exercises = gson.fromJson<List<Exercise>>(exercisesJson, exercisesType)
        
        return CaloriesAnalysis(
            foodDescription = foodDescription,
            breakdown = breakdown,
            totalCalories = totalCalories,
            suggestedExercises = exercises
        )
    }
    
    companion object {
        fun fromDomain(analysis: CaloriesAnalysis, id: String = java.util.UUID.randomUUID().toString()): CaloriesEntity {
            val gson = Gson()
            val breakdownJson = gson.toJson(analysis.breakdown)
            val exercisesJson = gson.toJson(analysis.suggestedExercises)
            
            return CaloriesEntity(
                id = id,
                foodDescription = analysis.foodDescription,
                totalCalories = analysis.totalCalories,
                timestamp = System.currentTimeMillis(),
                breakdownJson = breakdownJson,
                exercisesJson = exercisesJson
            )
        }
    }
}
