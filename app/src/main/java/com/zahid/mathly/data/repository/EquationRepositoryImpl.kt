package com.zahid.mathly.data.repository

import com.zahid.mathly.data.local.EquationDao
import com.zahid.mathly.data.local.SolutionDao
import com.zahid.mathly.data.local.SolutionEntity
import com.zahid.mathly.data.remote.OpenAIService
import com.zahid.mathly.domain.model.Equation
import com.zahid.mathly.domain.model.Solution
import com.zahid.mathly.domain.model.WordProblem
import com.zahid.mathly.domain.repository.EquationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class EquationRepositoryImpl @Inject constructor(
    private val openAIService: OpenAIService,
    private val equationDao: EquationDao,
    private val solutionDao: SolutionDao
) : EquationRepository {

    override suspend fun solveEquation(equation: Equation): Result<Solution> {
        return try {
            val solution = openAIService.solveEquation(equation.expression)
            Result.success(solution)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun solveWordProblem(wordProblem: WordProblem): WordProblem {
        return try {
            openAIService.solveWordProblem(wordProblem)
        } catch (e: Exception) {
            // Return the original word problem with error information
            wordProblem.copy(
                extractedEquation = "Error: Unable to process word problem - ${e.message}",
                solution = null
            )
        }
    }

    override suspend fun saveSolution(solution: Solution) {
        // Convert domain Solution to SolutionEntity and save
        val solutionEntity = SolutionEntity.fromDomain(solution.copy(
            id = solution.id.ifEmpty { UUID.randomUUID().toString() },
            timestamp = System.currentTimeMillis()
        ))
        solutionDao.insertSolution(solutionEntity)
    }

    override fun getRecentSolutions(): Flow<List<Solution>> {
        return solutionDao.getRecentSolutions().map { entities ->
            entities.map { it.toDomain() }
        }
    }
} 