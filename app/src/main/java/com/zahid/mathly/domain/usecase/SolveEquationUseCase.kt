package com.zahid.mathly.domain.usecase

import com.zahid.mathly.domain.model.Equation
import com.zahid.mathly.domain.model.Solution
import com.zahid.mathly.domain.repository.EquationRepository
import javax.inject.Inject

class SolveEquationUseCase @Inject constructor(
    private val repository: EquationRepository
) {
    suspend operator fun invoke(equation: Equation): Result<Solution> {
        return repository.solveEquation(equation)
    }
} 