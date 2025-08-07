package com.zahid.mathly.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EquationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEquation(equation: EquationEntity)
    
    @Query("SELECT * FROM equations ORDER BY timestamp DESC LIMIT 10")
    fun getRecentEquations(): Flow<List<EquationEntity>>
    
    @Query("SELECT * FROM equations WHERE id = :id")
    suspend fun getEquationById(id: String): EquationEntity?
} 