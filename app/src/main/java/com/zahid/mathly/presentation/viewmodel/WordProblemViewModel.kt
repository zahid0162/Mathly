package com.zahid.mathly.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahid.mathly.domain.model.WordProblem
import com.zahid.mathly.domain.repository.EquationRepository
import com.zahid.mathly.domain.usecase.SolveWordProblemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WordProblemState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentWordProblem: WordProblem? = null
)

@HiltViewModel
class WordProblemViewModel @Inject constructor(
    private val solveWordProblemUseCase: SolveWordProblemUseCase,
    private val equationRepository: EquationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WordProblemState())
    val state: StateFlow<WordProblemState> = _state.asStateFlow()

    // Callback to notify when solution is saved
    private val _onSolutionSaved = MutableStateFlow<Boolean>(false)
    val onSolutionSaved: StateFlow<Boolean> = _onSolutionSaved.asStateFlow()

    fun solveWordProblem(problem: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            _onSolutionSaved.value = false

            try {
                val wordProblem = WordProblem(problem = problem)
                val result = solveWordProblemUseCase(wordProblem)

                result.fold(
                    onSuccess = { solvedWordProblem ->
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = null,
                            currentWordProblem = solvedWordProblem
                        )
                        
                        // Save the solution to database if it exists
                        solvedWordProblem.solution?.let { solution ->
                            try {
                                // Debug: Print solution details
                                println("Saving word problem solution:")
                                println("  ID: ${solution.id}")
                                println("  Type: ${solution.type}")
                                println("  Original Problem: ${solution.originalProblem}")
                                println("  Equation ID: ${solution.equationId}")
                                println("  Steps count: ${solution.steps.size}")
                                println("  Final answer: ${solution.finalAnswer}")
                                
                                // Validate solution before saving
                                if (solution.equationId.isNotBlank()) {
                                    equationRepository.saveSolution(solution)
                                    _onSolutionSaved.value = true
                                    println("Word problem solution saved successfully")
                                } else {
                                    println("Skipping save - equation ID is blank")
                                }
                            } catch (e: Exception) {
                                // Log error but don't fail the UI
                                println("Failed to save word problem solution: ${e.message}")
                                e.printStackTrace()
                            }
                        } ?: run {
                            println("No solution to save - solution is null")
                        }
                    },
                    onFailure = { exception ->
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = exception.message ?: "Unknown error occurred"
                        )
                    }
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun clearWordProblem() {
        _state.value = _state.value.copy(currentWordProblem = null)
    }

    fun resetSolutionSaved() {
        _onSolutionSaved.value = false
    }
} 