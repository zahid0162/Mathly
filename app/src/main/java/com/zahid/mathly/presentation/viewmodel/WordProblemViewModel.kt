package com.zahid.mathly.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahid.mathly.domain.model.WordProblem
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
    private val solveWordProblemUseCase: SolveWordProblemUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(WordProblemState())
    val state: StateFlow<WordProblemState> = _state.asStateFlow()

    fun solveWordProblem(problem: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

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
} 