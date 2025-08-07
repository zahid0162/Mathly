package com.zahid.mathly.domain.usecase

import com.zahid.mathly.domain.model.WordProblem
import com.zahid.mathly.domain.repository.EquationRepository
import javax.inject.Inject

class SolveWordProblemUseCase @Inject constructor(
    private val equationRepository: EquationRepository
) {
    suspend operator fun invoke(wordProblem: WordProblem): Result<WordProblem> {
        return try {
            val result = equationRepository.solveWordProblem(wordProblem)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 