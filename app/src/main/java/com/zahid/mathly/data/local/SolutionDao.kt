package com.zahid.mathly.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SolutionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSolution(solution: SolutionEntity)
    
    @Query("SELECT * FROM solutions ORDER BY timestamp DESC")
    fun getRecentSolutions(): Flow<List<SolutionEntity>>
    
    @Query("SELECT * FROM solutions WHERE equationId = :equationId")
    suspend fun getSolutionByEquationId(equationId: String): SolutionEntity?
} 