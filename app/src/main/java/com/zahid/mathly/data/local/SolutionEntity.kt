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
    val stepsJson: String,
    val finalAnswer: String,
    val timestamp: Long,
    val type: SolutionType
) {
    fun toDomain(): Solution {
        return Solution(
            id = id,
            equationId = equationId,
            steps = parseSteps(stepsJson),
            finalAnswer = finalAnswer,
            timestamp = timestamp,
            type = type
        )
    }
    
    companion object {
        fun fromDomain(solution: Solution): SolutionEntity {
            return SolutionEntity(
                id = solution.id,
                equationId = solution.equationId,
                stepsJson = serializeSteps(solution.steps),
                finalAnswer = solution.finalAnswer,
                timestamp = solution.timestamp,
                type = solution.type
            )
        }
        
        private fun serializeSteps(steps: List<SolutionStep>): String {
            return steps.joinToString("|") { step ->
                "${step.stepNumber}:${step.description}:${step.calculation}:${step.result}"
            }
        }
        
        private fun parseSteps(stepsJson: String): List<SolutionStep> {
            return if (stepsJson.isEmpty()) {
                emptyList()
            } else {
                stepsJson.split("|").map { stepStr ->
                    val parts = stepStr.split(":", limit = 4)
                    SolutionStep(
                        stepNumber = parts[0].toInt(),
                        description = parts[1],
                        calculation = parts[2],
                        result = parts[3]
                    )
                }
            }
        }
    }
} 