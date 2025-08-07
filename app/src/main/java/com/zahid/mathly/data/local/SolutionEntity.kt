package com.zahid.mathly.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zahid.mathly.domain.model.Solution
import com.zahid.mathly.domain.model.SolutionStep
import com.zahid.mathly.domain.model.SolutionType

@Entity(tableName = "solutions")
data class SolutionEntity(
    @PrimaryKey val id: String,
    val equationId: String,
    val originalProblem: String,
    val type: String, // Store as string in database
    val stepsJson: String,
    val finalAnswer: String,
    val timestamp: Long
) {
    fun toDomain(): Solution {
        return Solution(
            id = id,
            equationId = equationId,
            originalProblem = originalProblem,
            type = SolutionType.valueOf(type),
            steps = parseSteps(stepsJson),
            finalAnswer = finalAnswer,
            timestamp = timestamp
        )
    }
    
    companion object {
        fun fromDomain(solution: Solution): SolutionEntity {
            return SolutionEntity(
                id = solution.id,
                equationId = solution.equationId,
                originalProblem = solution.originalProblem,
                type = solution.type.name,
                stepsJson = serializeSteps(solution.steps),
                finalAnswer = solution.finalAnswer,
                timestamp = solution.timestamp
            )
        }
        
        private fun serializeSteps(steps: List<SolutionStep>): String {
            return steps.joinToString("|") { step ->
                "${step.stepNumber}:${step.description.orEmpty()}:${step.calculation.orEmpty()}:${step.result.orEmpty()}"
            }
        }
        
        private fun parseSteps(stepsJson: String): List<SolutionStep> {
            return if (stepsJson.isEmpty()) {
                emptyList()
            } else {
                try {
                    stepsJson.split("|").mapNotNull { stepStr ->
                        if (stepStr.isBlank()) return@mapNotNull null
                        
                        val parts = stepStr.split(":", limit = 4)
                        if (parts.size >= 4) {
                            SolutionStep(
                                stepNumber = parts[0].toIntOrNull() ?: 0,
                                description = parts[1].orEmpty(),
                                calculation = parts[2].orEmpty(),
                                result = parts[3].orEmpty()
                            )
                        } else null
                    }
                } catch (e: Exception) {
                    // Return empty list if parsing fails
                    emptyList()
                }
            }
        }
    }
} 