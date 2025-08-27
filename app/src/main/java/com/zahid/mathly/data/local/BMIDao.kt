package com.zahid.mathly.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BMIDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBMIRecord(bmiEntity: BMIEntity)
    
    @Query("SELECT * FROM bmi_records ORDER BY timestamp DESC")
    fun getAllBMIRecords(): Flow<List<BMIEntity>>
    
    @Query("SELECT * FROM bmi_records WHERE id = :id")
    suspend fun getBMIRecordById(id: String): BMIEntity?
}
