package com.zahid.mathly.presentation.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    
    private val prefs: SharedPreferences = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
    private val DARK_MODE_KEY = "is_dark_mode"
    
    var isDarkMode by mutableStateOf(loadDarkModePreference())
        private set
    
    init {
        // Load the saved preference when ViewModel is created
        isDarkMode = loadDarkModePreference()
    }
    
    fun toggleDarkMode() {
        isDarkMode = !isDarkMode
        saveDarkModePreference(isDarkMode)
    }
    
    fun updateDarkMode(enabled: Boolean) {
        isDarkMode = enabled
        saveDarkModePreference(enabled)
    }
    
    private fun loadDarkModePreference(): Boolean {
        return prefs.getBoolean(DARK_MODE_KEY, false)
    }
    
    private fun saveDarkModePreference(enabled: Boolean) {
        viewModelScope.launch {
            prefs.edit().putBoolean(DARK_MODE_KEY, enabled).apply()
        }
    }
} 