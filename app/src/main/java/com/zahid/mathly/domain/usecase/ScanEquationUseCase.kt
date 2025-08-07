package com.zahid.mathly.domain.usecase

import com.zahid.mathly.domain.model.Equation
import com.zahid.mathly.domain.repository.EquationRepository
import javax.inject.Inject

class ScanEquationUseCase @Inject constructor(
    private val repository: EquationRepository
) {
    suspend operator fun invoke(imageData: ByteArray): Result<Equation> {
        // This will be implemented in the data layer with ML Kit OCR
        return Result.failure(Exception("Not implemented yet"))
    }
} 