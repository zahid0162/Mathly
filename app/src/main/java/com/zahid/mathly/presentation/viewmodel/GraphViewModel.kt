package com.zahid.mathly.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahid.mathly.data.local.GraphDao
import com.zahid.mathly.data.local.GraphEntity
import com.zahid.mathly.domain.model.Graph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class GraphViewModel @Inject constructor(
    private val graphDao: GraphDao
) : ViewModel() {

    private val _graphs = MutableStateFlow<List<Graph>>(emptyList())
    val graphs: StateFlow<List<Graph>> = _graphs

    var selectedGraph by mutableStateOf<Graph?>(null)
        private set

    init {
        loadGraphs()
    }

    private fun loadGraphs() {
        viewModelScope.launch {
            graphDao.getAllGraphs()
                .map { entities -> entities.map { it.toDomain() } }
                .collect { graphs ->
                    _graphs.value = graphs
                }
        }
    }

    fun saveGraph(equation: String) {
        viewModelScope.launch {
            val graph = Graph(
                id = UUID.randomUUID().toString(),
                equation = equation,
                timestamp = System.currentTimeMillis()
            )
            graphDao.insertGraph(GraphEntity.fromDomain(graph))
        }
    }

    fun selectGraph(graph: Graph) {
        selectedGraph = graph
    }
}

