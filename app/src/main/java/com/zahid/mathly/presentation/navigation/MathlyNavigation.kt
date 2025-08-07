package com.zahid.mathly.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zahid.mathly.presentation.ui.screens.*
import com.zahid.mathly.presentation.viewmodel.SharedViewModel
import com.zahid.mathly.presentation.viewmodel.WordProblemViewModel
import dagger.hilt.android.AndroidEntryPoint

@Composable
fun MathlyNavigation() {
    val navController = rememberNavController()
    val sharedViewModel = androidx.hilt.navigation.compose.hiltViewModel<SharedViewModel>()
    val wordProblemViewModel = androidx.hilt.navigation.compose.hiltViewModel<WordProblemViewModel>()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MainScreen(
                navController = navController,
                viewModel = sharedViewModel
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

        composable("history") {
            HistoryScreen(
                navController = navController,
                viewModel = sharedViewModel
            )
        }

        // Word Problem Routes
        composable("wordProblemInput") {
            WordProblemInputScreen(
                navController = navController,
                viewModel = wordProblemViewModel
            )
        }

        composable("wordProblemResult") {
            WordProblemResultScreen(
                navController = navController,
                viewModel = wordProblemViewModel
            )
        }
    }
} 