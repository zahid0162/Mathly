package com.zahid.mathly.domain.repository

import com.zahid.mathly.domain.model.Equation
import com.zahid.mathly.domain.model.Solution
import com.zahid.mathly.domain.model.WordProblem
import kotlinx.coroutines.flow.Flow

interface EquationRepository {
    suspend fun solveEquation(equation: Equation): Result<Solution>
    suspend fun solveWordProblem(wordProblem: WordProblem): WordProblem
    suspend fun saveSolution(solution: Solution)
    fun getRecentSolutions(): Flow<List<Solution>>
} 