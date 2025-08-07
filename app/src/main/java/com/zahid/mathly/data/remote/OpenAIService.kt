package com.zahid.mathly.data.remote

import com.zahid.mathly.domain.model.Solution
import com.zahid.mathly.domain.model.SolutionStep
import com.zahid.mathly.domain.model.SolutionType
import com.zahid.mathly.domain.model.WordProblem
import javax.inject.Inject

class OpenAIService @Inject constructor(private val api: OpenAIApi) {
    
    companion object {
        private const val API_KEY = "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" // Replace with your actual API key
    }

    suspend fun solveEquation(equation: String): Solution {
        val prompt = """
            You are a math tutor. Solve this equation step by step: $equation
            
            Provide your response in this exact JSON format:
            {
                "equationId": "$equation",
                "steps": [
                    {
                        "stepNumber": 1,
                        "description": "Description of what we're doing",
                        "calculation": "The actual calculation",
                        "result": "The result of this step"
                    }
                ],
                "finalAnswer": "The final answer"
            }
            
            Make sure to:
            1. Show each step clearly
            2. Include the calculation for each step
            3. Provide the final answer
            4. Use proper mathematical notation
        """.trimIndent()

        val request = ChatCompletionRequest(
            model = "gpt-4.1-mini",
            messages = listOf(
                Message(role = "user", content = prompt)
            ),
            temperature = 0.3
        )

        return try {
            val response = api.createChatCompletion("Bearer $API_KEY", request)
            val content = response.choices.firstOrNull()?.message?.content
                ?: throw Exception("No response content")
            
            parseSolutionFromResponse(content, equation)
        } catch (e: Exception) {
            // Fallback: create a basic solution
            Solution(
                equationId = equation,
                originalProblem = equation,
                type = SolutionType.EQUATION,
                steps = listOf(
                    SolutionStep(
                        stepNumber = 1,
                        description = "Processing equation",
                        calculation = equation,
                        result = "Solution in progress"
                    )
                ),
                finalAnswer = "Unable to parse response: ${e.message}"
            )
        }
    }

    suspend fun solveWordProblem(wordProblem: WordProblem): WordProblem {
        val prompt = """
            You are a math tutor helping students convert word problems into equations and solve them.
            
            Word Problem: ${wordProblem.problem}
            
            Your task is to:
            1. Extract the relevant numbers and variables from the word problem
            2. Convert the word problem into a mathematical equation
            3. Solve the equation step by step
            
            Provide your response in this exact JSON format:
            {
                "extractedEquation": "The mathematical equation (e.g., speed = 60/1.5)",
                "solution": {
                    "equationId": "The equation to solve",
                    "steps": [
                        {
                            "stepNumber": 1,
                            "description": "Description of what we're doing",
                            "calculation": "The actual calculation",
                            "result": "The result of this step"
                        }
                    ],
                    "finalAnswer": "The final answer with units"
                }
            }
            
            Make sure to:
            1. Clearly identify what the problem is asking for
            2. Extract all relevant numbers and variables
            3. Formulate the correct equation
            4. Show step-by-step solution
            5. Include units in the final answer
            6. Explain the reasoning behind each step
        """.trimIndent()

        val request = ChatCompletionRequest(
            model = "gpt-4.1-mini",
            messages = listOf(
                Message(role = "user", content = prompt)
            ),
            temperature = 0.3
        )

        return try {
            val response = api.createChatCompletion("Bearer $API_KEY", request)
            val content = response.choices.firstOrNull()?.message?.content
                ?: throw Exception("No response content")
            
            parseWordProblemResponse(content, wordProblem.problem)
        } catch (e: Exception) {
            // Return the original word problem with error information
            wordProblem.copy(
                extractedEquation = "Error: Unable to process word problem - ${e.message}",
                solution = null
            )
        }
    }

    private fun parseSolutionFromResponse(response: String, originalEquation: String): Solution {
        // Extract JSON from response (handle cases where response might have extra text)
        val jsonStart = response.indexOf('{')
        val jsonEnd = response.lastIndexOf('}') + 1
        val jsonString = if (jsonStart >= 0 && jsonEnd > jsonStart) {
            response.substring(jsonStart, jsonEnd)
        } else {
            response
        }

        return try {
            val gson = com.google.gson.Gson()
            val solutionResponse = gson.fromJson(jsonString, SolutionResponse::class.java)
            
            Solution(
                equationId = solutionResponse.equationId?.takeIf { it.isNotBlank() } ?: originalEquation,
                originalProblem = originalEquation,
                type = SolutionType.EQUATION,
                steps = solutionResponse.steps?.filter { step ->
                    step.description.isNotBlank() || step.calculation.isNotBlank() || step.result.isNotBlank()
                } ?: emptyList(),
                finalAnswer = solutionResponse.finalAnswer?.takeIf { it.isNotBlank() } ?: ""
            )
        } catch (e: Exception) {
            // Fallback: create a basic solution
            Solution(
                equationId = originalEquation,
                originalProblem = originalEquation,
                type = SolutionType.EQUATION,
                steps = listOf(
                    SolutionStep(
                        stepNumber = 1,
                        description = "Processing equation",
                        calculation = originalEquation,
                        result = "Solution in progress"
                    )
                ),
                finalAnswer = "Unable to parse response: ${e.message}"
            )
        }
    }

    private fun parseWordProblemResponse(response: String, originalProblem: String): WordProblem {
        // Extract JSON from response
        val jsonStart = response.indexOf('{')
        val jsonEnd = response.lastIndexOf('}') + 1
        val jsonString = if (jsonStart >= 0 && jsonEnd > jsonStart) {
            response.substring(jsonStart, jsonEnd)
        } else {
            response
        }

        return try {
            val gson = com.google.gson.Gson()
            val wordProblemResponse = gson.fromJson(jsonString, WordProblemResponse::class.java)
            
            val extractedEquation = wordProblemResponse.extractedEquation?.takeIf { it.isNotBlank() }
            val solution = wordProblemResponse.solution?.let { sol ->
                // Ensure solution has valid data and set the correct type and original problem
                if (sol.equationId.isNotBlank() || sol.steps.isNotEmpty() || sol.finalAnswer.isNotBlank()) {
                    sol.copy(
                        originalProblem = originalProblem,
                        type = SolutionType.WORD_PROBLEM
                    )
                } else null
            }
            
            WordProblem(
                problem = originalProblem,
                extractedEquation = extractedEquation,
                solution = solution
            )
        } catch (e: Exception) {
            // Fallback: create a basic word problem
            WordProblem(
                problem = originalProblem,
                extractedEquation = "Unable to extract equation: ${e.message}",
                solution = null
            )
        }
    }

    // Data classes for JSON parsing
    private data class SolutionResponse(
        val equationId: String?,
        val steps: List<SolutionStep>?,
        val finalAnswer: String?
    )

    private data class WordProblemResponse(
        val extractedEquation: String?,
        val solution: Solution?
    )
} 