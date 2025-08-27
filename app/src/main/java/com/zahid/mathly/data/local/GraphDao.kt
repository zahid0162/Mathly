package com.zahid.mathly.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GraphDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGraph(graph: GraphEntity)
    
    @Query("SELECT * FROM graphs ORDER BY timestamp DESC")
    fun getAllGraphs(): Flow<List<GraphEntity>>
    
    @Query("SELECT * FROM graphs WHERE id = :id")
    suspend fun getGraphById(id: String): GraphEntity?
}
