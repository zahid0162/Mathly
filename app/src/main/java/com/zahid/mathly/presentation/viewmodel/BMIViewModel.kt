package com.zahid.mathly.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahid.mathly.data.local.BMIDao
import com.zahid.mathly.data.local.BMIEntity
import com.zahid.mathly.domain.model.BMIRecord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BMIViewModel @Inject constructor(
    private val bmiDao: BMIDao
) : ViewModel() {

    private val _bmiRecords = MutableStateFlow<List<BMIRecord>>(emptyList())
    val bmiRecords: StateFlow<List<BMIRecord>> = _bmiRecords
    
    init {
        loadBMIRecords()
    }
    
    private fun loadBMIRecords() {
        viewModelScope.launch {
            bmiDao.getAllBMIRecords()
                .map { entities -> entities.map { it.toDomain() } }
                .collect { records ->
                    _bmiRecords.value = records
                }
        }
    }
    
    fun saveBMIRecord(height: Double, weight: Double, bmiValue: Double, category: String) {
        viewModelScope.launch {
            val entity = BMIEntity.create(
                height = height,
                weight = weight,
                bmiValue = bmiValue,
                category = category
            )
            bmiDao.insertBMIRecord(entity)
        }
    }
}
