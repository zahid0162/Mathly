package com.zahid.mathly.presentation.viewmodel

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import com.zahid.mathly.MainActivity
import com.zahid.mathly.utils.LocaleHelper
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    
    private val prefs: SharedPreferences = context.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
    
    var currentLanguage by mutableStateOf(loadLanguagePreference())
        private set
    
    init {
        // Load the saved preference when ViewModel is created
        currentLanguage = loadLanguagePreference()
    }
    
    fun setLanguage(languageCode: String) {
        if (currentLanguage != languageCode) {
            currentLanguage = languageCode
            saveLanguagePreference(languageCode)
            restartActivity()
        }
    }
    
    private fun loadLanguagePreference(): String {
        return prefs.getString("selected_language", "en") ?: "en"
    }
    
    private fun saveLanguagePreference(languageCode: String) {
        prefs.edit().putString("selected_language", languageCode).apply()
    }
    
    private fun restartActivity() {
        viewModelScope.launch {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
    
    fun getLanguageDisplayName(languageCode: String): String {
        return when (languageCode) {
            "en" -> "English"
            "ar" -> "العربية"
            "ur" -> "اردو"
            else -> "English"
        }
    }
    
    fun getAvailableLanguages(): List<Pair<String, String>> {
        return listOf(
            "en" to "English",
            "ar" to "العربية", 
            "ur" to "اردو"
        )
    }
}
