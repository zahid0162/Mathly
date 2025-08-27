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
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.hilt.navigation.compose.hiltViewModel
import com.zahid.mathly.presentation.viewmodel.BMIViewModel
import com.zahid.mathly.presentation.viewmodel.CaloriesCounterViewModel
import com.zahid.mathly.presentation.viewmodel.GraphViewModel
import com.zahid.mathly.presentation.viewmodel.ProfileViewModel

@Composable
fun MathlyNavigation(
    themeViewModel: ThemeViewModel,
    languageViewModel: LanguageViewModel,
    sessionManager: SessionManager
) {
    val navController = rememberNavController()
    val sharedViewModel = hiltViewModel<SharedViewModel>()
    val wordProblemViewModel = hiltViewModel<WordProblemViewModel>()
    val profileViewModel = hiltViewModel<ProfileViewModel>()
    val graphViewModel = hiltViewModel<GraphViewModel>()
    val caloriesViewModel = hiltViewModel<CaloriesCounterViewModel>()
    val bmiViewModel = hiltViewModel<BMIViewModel>()

    // Determine start destination based on session state
    val startDestination = remember {
        when {
            !sessionManager.isLoggedIn -> "login"
            !sessionManager.hasCompletedProfile -> "profileSetup"
            else -> "home"
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") {
            LoginScreen(
                navController = navController,
                languageViewModel = languageViewModel
            )
        }

        composable("register") {
            RegisterScreen(
                navController = navController
            )
        }

        composable("profileSetup") {
            ProfileSetupScreen(
                navController = navController
            )
        }

        composable("home") {
            HomeScreen(
                navController = navController
            )
        }
        
        // New main screens for each feature
        composable("equations_main") {
            EquationsMainScreen(
                navController = navController,
                onAddClick = {
                    navController.navigate("equations_input")
                },
                sharedViewModel
            )
        }
        
        composable("word_problem_main") {
            WordProblemMainScreen(
                navController = navController,
                onAddClick = {
                    navController.navigate("word_problem_input")
                },
                sharedViewModel
            )
        }
        
        composable("graph_main") {
            GraphMainScreen(
                navController = navController,
                onAddClick = {
                    navController.navigate("graph_input")
                },
                graphViewModel = graphViewModel
            )
        }
        
        composable("calculator_main") {
            CalculatorMainScreen(
                navController = navController,
                onAddClick = {
                    navController.navigate("calculator_input")
                }
            )
        }
        
        composable("calories_main") {
            CaloriesMainScreen(
                navController = navController,
                onAddClick = {
                    navController.navigate("calories_input")
                },
                viewModel = caloriesViewModel
            )
        }
        
        composable("bmi_main") {
            BMIMainScreen(
                navController = navController,
                onAddClick = {
                    navController.navigate("bmi_input")
                },
                viewModel = bmiViewModel
            )
        }

        // Profile screen route
        composable("profile_main") {
            ProfileMainScreen(
                navController = navController,
                themeViewModel = themeViewModel,
                profileViewModel = profileViewModel,
                languageViewModel = languageViewModel
            )
        }

        // Input screens for each feature
        composable("equations_input") {
            EquationsInputScreen(
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
        
        composable("word_problem_input") {
            WordProblemsInputScreen(
                navController = navController
            )
        }
        
        composable("graph_input") {
            GraphInputScreen(
                navController = navController,
                graphViewModel = graphViewModel
            )
        }
        
        composable("calculator_input") {
            BasicCalculatorInputScreen(
                navController = navController
            )
        }
        
        composable("calories_input") {
            CaloriesCounterInputScreen(
                navController = navController,
                viewModel = caloriesViewModel
            )
        }
        
        composable("bmi_input") {
            BMICalculatorInputScreen(
                navController = navController,
                viewModel = bmiViewModel
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
                navController = navController,
                viewModel = profileViewModel
            )
        }
        
        // Graph Detail route
        composable("graph_detail") {
            GraphDetailScreen(
                navController = navController,
                graphViewModel = graphViewModel
            )
        }
        
        // Calories Detail route
        composable("calories_detail") {
            CaloriesDetailScreen(
                navController = navController,
                viewModel = caloriesViewModel
            )
        }
    }
} 