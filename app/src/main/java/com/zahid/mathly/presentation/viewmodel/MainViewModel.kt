package com.zahid.mathly.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahid.mathly.domain.model.Equation
import com.zahid.mathly.domain.model.Solution
import com.zahid.mathly.domain.usecase.SolveEquationUseCase
import com.zahid.mathly.presentation.ui.state.MainScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val solveEquationUseCase: SolveEquationUseCase
) : ViewModel() {
    
    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state.asStateFlow()
    
    private val _currentSolution = MutableStateFlow<Solution?>(null)
    val currentSolution: StateFlow<Solution?> = _currentSolution.asStateFlow()
    
    fun solveEquation(equation: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            try {
                val result = solveEquationUseCase(
                    Equation(expression = equation)
                )
                
                result.fold(
                    onSuccess = { solution ->
                        _currentSolution.value = solution
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = null
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
    
    fun clearSolution() {
        _currentSolution.value = null
    }
} 