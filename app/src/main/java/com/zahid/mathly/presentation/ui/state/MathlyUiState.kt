package com.zahid.mathly.presentation.ui.state

import com.zahid.mathly.domain.model.Equation
import com.zahid.mathly.domain.model.Solution

sealed class MathlyUiState {
    object Loading : MathlyUiState()
    object Idle : MathlyUiState()
    data class Error(val message: String) : MathlyUiState()
    data class Success(val data: Any) : MathlyUiState()
}

data class MainScreenState(
    val isLoading: Boolean = false,
    val error: String? = null
)

data class InputScreenState(
    val equation: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

data class ScanScreenState(
    val isScanning: Boolean = false,
    val scannedText: String = "",
    val error: String? = null
)

data class ResultScreenState(
    val equation: Equation? = null,
    val solution: Solution? = null,
    val isLoading: Boolean = false,
    val error: String? = null
) 