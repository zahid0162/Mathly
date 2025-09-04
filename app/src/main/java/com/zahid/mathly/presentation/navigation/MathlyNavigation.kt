package com.zahid.mathly.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zahid.mathly.data.local.SessionManager
import com.zahid.mathly.presentation.viewmodel.SharedViewModel
import com.zahid.mathly.presentation.viewmodel.WordProblemViewModel
import com.zahid.mathly.presentation.viewmodel.ThemeViewModel
import com.zahid.mathly.presentation.viewmodel.LanguageViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.zahid.mathly.presentation.ui.screens.auth.LoginScreen
import com.zahid.mathly.presentation.ui.screens.auth.RegisterScreen
import com.zahid.mathly.presentation.ui.screens.basicCalc.BasicCalculatorInputScreen
import com.zahid.mathly.presentation.ui.screens.basicCalc.CalculatorMainScreen
import com.zahid.mathly.presentation.ui.screens.bmi.BMICalculatorInputScreen
import com.zahid.mathly.presentation.ui.screens.bmi.BMIMainScreen
import com.zahid.mathly.presentation.ui.screens.calories.CaloriesCounterInputScreen
import com.zahid.mathly.presentation.ui.screens.calories.CaloriesDetailScreen
import com.zahid.mathly.presentation.ui.screens.calories.CaloriesMainScreen
import com.zahid.mathly.presentation.ui.screens.equation.CameraScreen
import com.zahid.mathly.presentation.ui.screens.equation.EquationsInputScreen
import com.zahid.mathly.presentation.ui.screens.equation.EquationsMainScreen
import com.zahid.mathly.presentation.ui.screens.equation.MathInputScreen
import com.zahid.mathly.presentation.ui.screens.equation.ResultScreen
import com.zahid.mathly.presentation.ui.screens.equation.ScanScreen
import com.zahid.mathly.presentation.ui.screens.equation.TypeEquationScreen
import com.zahid.mathly.presentation.ui.screens.graph.GraphDetailScreen
import com.zahid.mathly.presentation.ui.screens.graph.GraphInputScreen
import com.zahid.mathly.presentation.ui.screens.graph.GraphMainScreen
import com.zahid.mathly.presentation.ui.screens.profile.EditProfileScreen
import com.zahid.mathly.presentation.ui.screens.profile.HomeScreen
import com.zahid.mathly.presentation.ui.screens.profile.ProfileMainScreen
import com.zahid.mathly.presentation.ui.screens.profile.ProfileSetupScreen
import com.zahid.mathly.presentation.ui.screens.wordProblem.WordProblemInputScreen
import com.zahid.mathly.presentation.ui.screens.wordProblem.WordProblemMainScreen
import com.zahid.mathly.presentation.ui.screens.wordProblem.WordProblemResultScreen
import com.zahid.mathly.presentation.ui.screens.wordProblem.WordProblemsInputScreen
import com.zahid.mathly.presentation.viewmodel.BMIViewModel
import com.zahid.mathly.presentation.viewmodel.CaloriesCounterViewModel
import com.zahid.mathly.presentation.viewmodel.GraphViewModel
import com.zahid.mathly.presentation.viewmodel.profile.ProfileViewModel
import com.zahid.mathly.utils.horizontalAnimatedComposable
import com.zahid.mathly.utils.verticalAnimatedComposable

enum class AppRoutes(val route: String){
    Login("login"),
    Register("register"),
    ProfileSetup("profileSetup"),
    Home("home"),
    EquationsMain("equations_main"),
    WordProblemMain("word_problem_main"),
    GraphMain("graph_main"),
    CalculatorMain("calculator_main"),
    CaloriesMain("calories_main"),
    BMIMain("bmi_main"),
    ProfileMain("profile_main"),
    EquationsInput("equations_input"),
    TypeEquation("type_equation"),
    WordProblemInput("word_problem_input"),
    GraphInput("graph_input"),
    CalculatorInput("calculator_input"),
    CaloriesInput("calories_input"),
    BMIInput("bmi_input"),
    Scan("scan"),
    Camera("camera"),
    Result("result"),
    WordProblemInputScreen("wordProblemInput"),
    WordProblemResultScreen("wordProblemResult"),
    EditProfile("editProfile"),
    GraphDetail("graph_detail"),
    CaloriesDetail("calories_detail"),
    MathInput("math_input/{initialText}"),
}

@Composable
fun MathlyNavigation(
    themeViewModel: ThemeViewModel,
    languageViewModel: LanguageViewModel,
    sessionManager: SessionManager
) {
    val navController = rememberNavController()
    val sharedViewModel = hiltViewModel<SharedViewModel>()
    val wordProblemViewModel = hiltViewModel<WordProblemViewModel>()
    val graphViewModel = hiltViewModel<GraphViewModel>()
    val caloriesViewModel = hiltViewModel<CaloriesCounterViewModel>()
    val bmiViewModel = hiltViewModel<BMIViewModel>()

    // Determine start destination based on session state
    val startDestination = remember {
        when {
            !sessionManager.isLoggedIn -> AppRoutes.Login.route
            !sessionManager.hasCompletedProfile -> AppRoutes.ProfileSetup.route
            else -> AppRoutes.Home.route
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(AppRoutes.Login.route) {
            LoginScreen(
                navController = navController,
                languageViewModel = languageViewModel
            )
        }

        horizontalAnimatedComposable(AppRoutes.Register.route) {
            RegisterScreen(
                navController = navController
            )
        }

        horizontalAnimatedComposable(AppRoutes.ProfileSetup.route) {
            ProfileSetupScreen(
                navController = navController
            )
        }

        verticalAnimatedComposable(AppRoutes.Home.route) { backStack->
            val profileViewModel = hiltViewModel<ProfileViewModel>()
            HomeScreen(
                navController = navController,
                profileViewModel.uiState
            )
        }
        
        // New main screens for each feature
        horizontalAnimatedComposable(AppRoutes.EquationsMain.route) {backStack ->
            val parentEntry = remember(backStack) {
                navController.getBackStackEntry(AppRoutes.Home.route)
            }
            val profileViewModel = hiltViewModel<ProfileViewModel>(parentEntry)
            EquationsMainScreen(
                navController = navController,
                onAddClick = {
                    navController.navigate(AppRoutes.EquationsInput.route)
                },
                sharedViewModel,
                profileViewModel
            )
        }
        
        horizontalAnimatedComposable(AppRoutes.WordProblemMain.route) {backStack ->
            val parentEntry = remember(backStack) {
                navController.getBackStackEntry(AppRoutes.Home.route)
            }
            val profileViewModel = hiltViewModel<ProfileViewModel>(parentEntry)
            WordProblemMainScreen(
                navController = navController,
                onAddClick = {
                    navController.navigate(AppRoutes.WordProblemInput.route)
                },
                sharedViewModel,
                profileViewModel
            )
        }
        
        horizontalAnimatedComposable(AppRoutes.GraphMain.route) {backStack ->
            val parentEntry = remember(backStack) {
                navController.getBackStackEntry(AppRoutes.Home.route)
            }
            val profileViewModel = hiltViewModel<ProfileViewModel>(parentEntry)
            GraphMainScreen(
                navController = navController,
                onAddClick = {
                    navController.navigate(AppRoutes.GraphInput.route)
                },
                graphViewModel = graphViewModel,
                profileViewModel
            )
        }
        
        horizontalAnimatedComposable(AppRoutes.CalculatorMain.route) {backStack ->
            val parentEntry = remember(backStack) {
                navController.getBackStackEntry(AppRoutes.Home.route)
            }
            val profileViewModel = hiltViewModel<ProfileViewModel>(parentEntry)
            CalculatorMainScreen(
                navController = navController,
                onAddClick = {
                    navController.navigate(AppRoutes.CalculatorInput.route)
                },
                profileViewModel
            )
        }
        
        horizontalAnimatedComposable(AppRoutes.CaloriesMain.route) {backStack ->
            val parentEntry = remember(backStack) {
                navController.getBackStackEntry(AppRoutes.Home.route)
            }
            val profileViewModel = hiltViewModel<ProfileViewModel>(parentEntry)
            CaloriesMainScreen(
                navController = navController,
                onAddClick = {
                    navController.navigate(AppRoutes.CaloriesInput.route)
                },
                viewModel = caloriesViewModel,
                profileViewModel = profileViewModel
            )
        }
        
        horizontalAnimatedComposable(AppRoutes.BMIMain.route) {backStack ->
            val parentEntry = remember(backStack) {
                navController.getBackStackEntry(AppRoutes.Home.route)
            }
            val profileViewModel = hiltViewModel<ProfileViewModel>(parentEntry)
            BMIMainScreen(
                navController = navController,
                onAddClick = {
                    navController.navigate(AppRoutes.BMIInput.route)
                },
                viewModel = bmiViewModel,
                profileViewModel
            )
        }

        // Profile screen route
        verticalAnimatedComposable(AppRoutes.ProfileMain.route) {backStack ->
            val parentEntry = remember(backStack) {
                navController.getBackStackEntry(AppRoutes.Home.route)
            }
            val profileViewModel = hiltViewModel<ProfileViewModel>(parentEntry)
            ProfileMainScreen(
                navController = navController,
                themeViewModel = themeViewModel,
                profileViewModel = profileViewModel,
                languageViewModel = languageViewModel
            )
        }

        // Input screens for each feature
        horizontalAnimatedComposable(AppRoutes.EquationsInput.route) {
            EquationsInputScreen(
                navController = navController,
                viewModel = sharedViewModel
            )
        }

        horizontalAnimatedComposable(AppRoutes.TypeEquation.route) {
            TypeEquationScreen(
                navController = navController,
                viewModel = sharedViewModel
            )
        }
        
        horizontalAnimatedComposable(AppRoutes.WordProblemInput.route) {
            WordProblemsInputScreen(
                navController = navController
            )
        }
        
        horizontalAnimatedComposable(AppRoutes.GraphInput.route) {
            GraphInputScreen(
                navController = navController,
                graphViewModel = graphViewModel
            )
        }
        
        horizontalAnimatedComposable(AppRoutes.CalculatorInput.route) {
            BasicCalculatorInputScreen(
                navController = navController
            )
        }
        
        horizontalAnimatedComposable(AppRoutes.CaloriesInput.route) {
            CaloriesCounterInputScreen(
                navController = navController,
                viewModel = caloriesViewModel
            )
        }
        
        horizontalAnimatedComposable(AppRoutes.BMIInput.route) {
            BMICalculatorInputScreen(
                navController = navController,
                viewModel = bmiViewModel
            )
        }

        horizontalAnimatedComposable(AppRoutes.Scan.route) {
            ScanScreen(
                navController = navController,
                viewModel = sharedViewModel
            )
        }

        verticalAnimatedComposable(AppRoutes.Camera.route) {
            CameraScreen(
                navController = navController,
                viewModel = sharedViewModel
            )
        }

        horizontalAnimatedComposable(AppRoutes.Result.route) {
            ResultScreen(
                navController = navController,
                viewModel = sharedViewModel
            )
        }

        // Word Problem Routes
        horizontalAnimatedComposable(AppRoutes.WordProblemInputScreen.route) {
            WordProblemInputScreen(
                navController = navController,
                viewModel = wordProblemViewModel,
            )
        }

        horizontalAnimatedComposable(AppRoutes.WordProblemResultScreen.route) {
            WordProblemResultScreen(
                navController = navController,
                viewModel = wordProblemViewModel
            )
        }
        
        // Edit Profile route
        horizontalAnimatedComposable(AppRoutes.EditProfile.route) {backStack ->
            val parentEntry = remember(backStack) {
                navController.getBackStackEntry(AppRoutes.Home.route)
            }
            val profileViewModel = hiltViewModel<ProfileViewModel>(parentEntry)
            EditProfileScreen(
                navController = navController,
                viewModel = profileViewModel
            )
        }
        
        // Graph Detail route
        horizontalAnimatedComposable(AppRoutes.GraphDetail.route) {
            GraphDetailScreen(
                navController = navController,
                graphViewModel = graphViewModel
            )
        }
        
        // Calories Detail route
        horizontalAnimatedComposable(AppRoutes.CaloriesDetail.route) {
            CaloriesDetailScreen(
                navController = navController,
                viewModel = caloriesViewModel
            )
        }
        
        // Math Input Screen route
        verticalAnimatedComposable(
            route = AppRoutes.MathInput.route,
            arguments = listOf(
                navArgument("initialText") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val initialText = backStackEntry.arguments?.getString("initialText") ?: ""
            MathInputScreen(
                navController = navController,
                initialText = initialText
            )
        }
    }
} 