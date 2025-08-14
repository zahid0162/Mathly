package com.zahid.mathly

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.zahid.mathly.presentation.navigation.MathlyNavigation
import com.zahid.mathly.presentation.ui.screens.SplashScreen
import com.zahid.mathly.presentation.ui.theme.MathlyTheme
import com.zahid.mathly.presentation.viewmodel.LanguageViewModel
import com.zahid.mathly.presentation.viewmodel.ThemeViewModel
import com.zahid.mathly.utils.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Apply saved language
        val prefs = getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
        val savedLanguage = prefs.getString("selected_language", "en") ?: "en"
        val context = LocaleHelper.setLocale(this, savedLanguage)
        
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val languageViewModel: LanguageViewModel = hiltViewModel()
            
            MathlyTheme(darkTheme = themeViewModel.isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var showSplash by remember { mutableStateOf(true) }
                    
                    if (showSplash) {
                        SplashScreen(
                            onSplashComplete = {
                                showSplash = false
                            }
                        )
                    } else {
                        MathlyNavigation(
                            themeViewModel = themeViewModel,
                            languageViewModel = languageViewModel
                        )
                    }
                }
            }
        }
    }
    
    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
        val savedLanguage = prefs.getString("selected_language", "en") ?: "en"
        super.attachBaseContext(LocaleHelper.setLocale(newBase, savedLanguage))
    }
}