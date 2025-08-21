package com.zahid.mathly.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zahid.mathly.data.local.SessionManager
import com.zahid.mathly.presentation.ui.screens.*
import com.zahid.mathly.presentation.viewmodel.SharedViewModel
import com.zahid.mathly.presentation.viewmodel.WordProblemViewModel
import com.zahid.mathly.presentation.viewmodel.ThemeViewModel
import com.zahid.mathly.presentation.viewmodel.LanguageViewModel
import io.github.jan.supabase.SupabaseClient

@Composable
fun MathlyNavigation(
    themeViewModel: ThemeViewModel,
    languageViewModel: LanguageViewModel,
    sessionManager: SessionManager
) {
    val navController = rememberNavController()
    val sharedViewModel = androidx.hilt.navigation.compose.hiltViewModel<SharedViewModel>()
    val wordProblemViewModel = androidx.hilt.navigation.compose.hiltViewModel<WordProblemViewModel>()

    // Determine start destination based on session state
    val startDestination = remember {
        when {
            !sessionManager.isLoggedIn -> "login"
            !sessionManager.hasCompletedProfile -> "profileSetup"
            else -> "main"
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") {
            LoginScreen(
                navController = navController
            )
        }

        composable("profileSetup") {
            ProfileSetupScreen(
                navController = navController
            )
        }

        composable("main") {
            MainScreen(
                navController = navController,
                viewModel = sharedViewModel,
                themeViewModel = themeViewModel,
                languageViewModel = languageViewModel
            )
        }

        composable("input") {
            InputScreen(
                navController = navController,
                viewModel = sharedViewModel
            )
        }

        composable("scan") {
            ScanScreen(
                navController = navController,
                viewModel = sharedViewModel
            )
        }

        composable("camera") {
            CameraScreen(
                navController = navController,
                viewModel = sharedViewModel
            )
        }

        composable("result") {
            ResultScreen(
                navController = navController,
                viewModel = sharedViewModel
            )
        }

        // Word Problem Routes
        composable("wordProblemInput") {
            WordProblemInputScreen(
                navController = navController,
                viewModel = wordProblemViewModel,
            )
        }

        composable("wordProblemResult") {
            WordProblemResultScreen(
                navController = navController,
                viewModel = wordProblemViewModel
            )
        }

        // History route for Profile navigation
        composable("history") {
            HistoryTab(
                navController = navController,
                viewModel = sharedViewModel,
            )
        }
        
        // Edit Profile route
        composable("editProfile") {
            EditProfileScreen(
                navController = navController
            )
        }
    }
} 