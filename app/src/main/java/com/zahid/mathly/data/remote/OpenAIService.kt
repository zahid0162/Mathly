package com.zahid.mathly.data.remote

import android.content.Context
import android.content.SharedPreferences
import com.zahid.mathly.domain.model.Solution
import com.zahid.mathly.domain.model.SolutionStep
import com.zahid.mathly.domain.model.SolutionType
import com.zahid.mathly.domain.model.WordProblem
import com.zahid.mathly.domain.model.CaloriesAnalysis
import com.zahid.mathly.domain.model.FoodItem
import com.zahid.mathly.domain.model.Exercise
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class OpenAIService @Inject constructor(
    private val api: OpenAIApi,
    @ApplicationContext private val context: Context
) {
    
    companion object {
        private const val API_KEY = "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" // Replace with your actual API key
    }
    
    private fun getCurrentLanguage(): String {
        val prefs: SharedPreferences = context.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
        return prefs.getString("selected_language", "en") ?: "en"
    }

    suspend fun analyzeCalories(foodDescription: String): CaloriesAnalysis {
        val language = getCurrentLanguage()
        val languageInstruction = when (language) {
            "ar" -> "Please provide your response in Arabic."
            "ur" -> "Please provide your response in Urdu."
            else -> "Please provide your response in English."
        }
        
        val prompt = """
            You are a nutritionist and fitness expert. Analyze the following food description and provide detailed calorie information and exercise recommendations.
            
            $languageInstruction
            
            Food Description: "$foodDescription"
            
            Please provide your response in this exact JSON format:
            {
                "foodDescription": "$foodDescription",
                "breakdown": [
                    {
                        "name": "Food item name",
                        "calories": 250,
                        "serving": "1 cup"
                    }
                ],
                "totalCalories": 850,
                "suggestedExercises": [
                    {
                        "name": "Exercise name",
                        "duration": "30 minutes",
                        "caloriesBurned": 300,
                        "intensity": "moderate"
                    }
                ]
            }
            
            Guidelines:
            1. Break down each food item mentioned in the description
            2. Provide realistic calorie estimates based on common serving sizes
            3. For exercises, assume a 70kg person and provide 3-4 different exercise options
            4. Exercise durations should be realistic to burn the total calories
            5. Include a mix of cardio and strength training exercises
            6. Use common exercise names (walking, running, cycling, swimming, etc.)
            7. Ensure the total calories burned by exercises equals or exceeds the total food calories
        """.trimIndent()

        val request = ChatCompletionRequest(
            model = "gpt-4",
            messages = listOf(
                Message(role = "user", content = prompt)
            ),
            temperature = 0.3,
        )

        return try {
            val response = api.createChatCompletion("Bearer $API_KEY", request)
            val content = response.choices.firstOrNull()?.message?.content
                ?: throw Exception("No response content")
            
            parseCaloriesAnalysisFromResponse(content, foodDescription)
        } catch (e: Exception) {
            // Fallback: create a basic analysis
            CaloriesAnalysis(
                foodDescription = foodDescription,
                breakdown = listOf(
                    FoodItem(
                        name = "Food items",
                        calories = 500,
                        serving = "estimated"
                    )
                ),
                totalCalories = 500,
                suggestedExercises = listOf(
                    Exercise(
                        name = "Walking",
                        duration = "45 minutes",
                        caloriesBurned = 250,
                        intensity = "moderate"
                    ),
                    Exercise(
                        name = "Cycling",
                        duration = "20 minutes",
                        caloriesBurned = 250,
                        intensity = "moderate"
                    )
                )
            )
        }
    }

    suspend fun solveEquation(equation: String): Solution {
        val language = getCurrentLanguage()
        val languageInstruction = when (language) {
            "ar" -> "Please provide your response in Arabic."
            "ur" -> "Please provide your response in Urdu."
            else -> "Please provide your response in English."
        }
        
        val prompt = """
            You are a math tutor. Solve this equation step by step: $equation
            
            $languageInstruction
            
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
            temperature = 0.3,
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
        val language = getCurrentLanguage()
        val languageInstruction = when (language) {
            "ar" -> "Please provide your response in Arabic."
            "ur" -> "Please provide your response in Urdu."
            else -> "Please provide your response in English."
        }
        
        val prompt = """
            You are a math tutor helping students convert word problems into equations and solve them.
            
            $languageInstruction
            
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
            temperature = 0.3,
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
                type = SolutionType.WORD_PROBLEM,
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

    private fun parseCaloriesAnalysisFromResponse(response: String, foodDescription: String): CaloriesAnalysis {
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
            val caloriesAnalysisResponse = gson.fromJson(jsonString, CaloriesAnalysisResponse::class.java)
            
            val breakdown = caloriesAnalysisResponse.breakdown?.map { item ->
                FoodItem(
                    name = item.name,
                    calories = item.calories,
                    serving = item.serving
                )
            } ?: emptyList()

            val totalCalories = caloriesAnalysisResponse.totalCalories ?: 0

            val suggestedExercises = caloriesAnalysisResponse.suggestedExercises?.map { exercise ->
                Exercise(
                    name = exercise.name,
                    duration = exercise.duration,
                    caloriesBurned = exercise.caloriesBurned,
                    intensity = exercise.intensity
                )
            } ?: emptyList()

            CaloriesAnalysis(
                foodDescription = foodDescription,
                breakdown = breakdown,
                totalCalories = totalCalories,
                suggestedExercises = suggestedExercises
            )
        } catch (e: Exception) {
            // Fallback: create a basic analysis
            CaloriesAnalysis(
                foodDescription = foodDescription,
                breakdown = listOf(
                    FoodItem(
                        name = "Food items",
                        calories = 500,
                        serving = "estimated"
                    )
                ),
                totalCalories = 500,
                suggestedExercises = listOf(
                    Exercise(
                        name = "Walking",
                        duration = "45 minutes",
                        caloriesBurned = 250,
                        intensity = "moderate"
                    ),
                    Exercise(
                        name = "Cycling",
                        duration = "20 minutes",
                        caloriesBurned = 250,
                        intensity = "moderate"
                    )
                )
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

    private data class CaloriesAnalysisResponse(
        val foodDescription: String?,
        val breakdown: List<FoodItemResponse>?,
        val totalCalories: Int?,
        val suggestedExercises: List<ExerciseResponse>?
    )

    private data class FoodItemResponse(
        val name: String,
        val calories: Int,
        val serving: String
    )

    private data class ExerciseResponse(
        val name: String,
        val duration: String,
        val caloriesBurned: Int,
        val intensity: String
    )
} 