package com.zahid.mathly.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zahid.mathly.domain.model.BMIRecord
import java.util.UUID

@Entity(tableName = "bmi_records")
data class BMIEntity(
    @PrimaryKey val id: String,
    val height: Double,
    val weight: Double,
    val bmiValue: Double,
    val category: String,
    val timestamp: Long
) {
    fun toDomain(): BMIRecord {
        return BMIRecord(
            id = id,
            height = height,
            weight = weight,
            bmiValue = bmiValue,
            category = category,
            timestamp = timestamp
        )
    }
    
    companion object {
        fun fromDomain(record: BMIRecord): BMIEntity {
            return BMIEntity(
                id = record.id,
                height = record.height,
                weight = record.weight,
                bmiValue = record.bmiValue,
                category = record.category,
                timestamp = record.timestamp
            )
        }
        
        fun create(height: Double, weight: Double, bmiValue: Double, category: String): BMIEntity {
            return BMIEntity(
                id = UUID.randomUUID().toString(),
                height = height,
                weight = weight,
                bmiValue = bmiValue,
                category = category,
                timestamp = System.currentTimeMillis()
            )
        }
    }
}
