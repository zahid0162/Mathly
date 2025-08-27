package com.zahid.mathly.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahid.mathly.data.local.CaloriesDao
import com.zahid.mathly.data.local.CaloriesEntity
import com.zahid.mathly.data.remote.OpenAIService
import com.zahid.mathly.domain.model.CaloriesAnalysis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CaloriesCounterViewModel @Inject constructor(
    val openAIService: OpenAIService,
    private val caloriesDao: CaloriesDao
) : ViewModel() {

    private val _caloriesEntries = MutableStateFlow<List<CaloriesAnalysis>>(emptyList())
    val caloriesEntries: StateFlow<List<CaloriesAnalysis>> = _caloriesEntries
    
    var selectedCaloriesEntry by mutableStateOf<CaloriesAnalysis?>(null)
        private set
    
    var isLoading by mutableStateOf(false)
        private set
    
    var errorMessage by mutableStateOf("")
        private set
    
    init {
        loadCaloriesEntries()
    }
    
    private fun loadCaloriesEntries() {
        viewModelScope.launch {
            caloriesDao.getAllCaloriesEntries()
                .map { entities -> entities.map { it.toDomain() } }
                .collect { entries ->
                    _caloriesEntries.value = entries
                }
        }
    }
    
    fun analyzeCalories(foodDescription: String, onSuccess: () -> Unit) {
        if (foodDescription.isBlank()) return
        
        isLoading = true
        errorMessage = ""
        
        viewModelScope.launch {
            try {
                val analysis = openAIService.analyzeCalories(foodDescription)
                saveCaloriesAnalysis(analysis)
                onSuccess()
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
    
    private suspend fun saveCaloriesAnalysis(analysis: CaloriesAnalysis) {
        val entity = CaloriesEntity.fromDomain(analysis, UUID.randomUUID().toString())
        caloriesDao.insertCaloriesEntry(entity)
    }
    
    fun selectCaloriesEntry(entry: CaloriesAnalysis) {
        selectedCaloriesEntry = entry
    }
    
    fun getCaloriesEntryById(id: String) {
        viewModelScope.launch {
            caloriesDao.getCaloriesEntryById(id)?.let {
                selectedCaloriesEntry = it.toDomain()
            }
        }
    }
}