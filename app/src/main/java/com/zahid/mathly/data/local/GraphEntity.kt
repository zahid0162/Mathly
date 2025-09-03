package com.zahid.mathly.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zahid.mathly.domain.model.Graph

@Entity(tableName = "graphs")
data class GraphEntity(
    @PrimaryKey val id: String,
    val equation: String,
    val timestamp: Long
) {
    fun toDomain(): Graph {
        return Graph(
            id = id,
            equation = equation,
            timestamp = timestamp
        )
    }
    
    companion object {
        fun fromDomain(graph: Graph): GraphEntity {
            return GraphEntity(
                id = graph.id,
                equation = graph.equation,
                timestamp = graph.timestamp
            )
        }
    }
}

