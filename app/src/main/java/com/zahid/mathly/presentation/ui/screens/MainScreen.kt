package com.zahid.mathly.presentation.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.zahid.mathly.presentation.ui.components.NavigationDrawer
import com.zahid.mathly.presentation.ui.theme.PlayfairDisplay
import com.zahid.mathly.presentation.viewmodel.CaloriesCounterViewModel
import com.zahid.mathly.presentation.viewmodel.SharedViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: SharedViewModel
) {
    val state by viewModel.state.collectAsState()
    val history by viewModel.history.collectAsState()
    var selectedScreen by remember { mutableStateOf("Equations") }
    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawer(drawerState,selectedScreen) {
                scope.launch {
                    drawerState.close()
                }
                selectedScreen = it
            }
        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = selectedScreen,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.W700,
                            fontFamily = PlayfairDisplay,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        ) { paddingValues ->
            when (selectedScreen) {
                "Equations" -> EquationsTab(
                    navController = navController,
                    viewModel = viewModel,
                    state = state,
                    history = history,
                    paddingValues = paddingValues
                )
                "WordProblems" -> WordProblemsTab(
                    navController = navController,
                    paddingValues = paddingValues
                )
                "Graphs" -> GraphTab(
                    paddingValues = paddingValues,
                    snackbarHostState
                )
                "BasicCalculator" -> BasicCalculatorScreen(
                    navController = navController,
                    paddingValues = paddingValues
                )
                "CaloriesCounter" -> {
                    val openAIService = androidx.hilt.navigation.compose.hiltViewModel<CaloriesCounterViewModel>().openAIService
                    CaloriesCounterScreen(
                        navController = navController,
                        paddingValues = paddingValues,
                        openAIService = openAIService
                    )
                }
                "BMICalculator" -> BMICalculatorScreen(
                    navController = navController,
                    paddingValues = paddingValues
                )
                "Profile" -> ProfileScreen(
                    navController = navController,
                    viewModel = viewModel,
                    paddingValues = paddingValues
                )
            }
        }
    }
}