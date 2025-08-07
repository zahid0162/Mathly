package com.zahid.mathly.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zahid.mathly.domain.model.Equation
import com.zahid.mathly.domain.model.EquationSource

@Entity(tableName = "equations")
data class EquationEntity(
    @PrimaryKey val id: String,
    val expression: String,
    val timestamp: Long,
    val source: String
) {
    fun toDomain(): Equation {
        return Equation(
            id = id,
            expression = expression,
            timestamp = timestamp,
            source = EquationSource.valueOf(source)
        )
    }
    
    companion object {
        fun fromDomain(equation: Equation): EquationEntity {
            return EquationEntity(
                id = equation.id,
                expression = equation.expression,
                timestamp = equation.timestamp,
                source = equation.source.name
            )
        }
    }
} 