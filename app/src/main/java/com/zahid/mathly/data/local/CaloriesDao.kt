package com.zahid.mathly.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CaloriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCaloriesEntry(caloriesEntity: CaloriesEntity)
    
    @Query("SELECT * FROM calories ORDER BY timestamp DESC")
    fun getAllCaloriesEntries(): Flow<List<CaloriesEntity>>
    
    @Query("SELECT * FROM calories WHERE id = :id")
    suspend fun getCaloriesEntryById(id: String): CaloriesEntity?
}
