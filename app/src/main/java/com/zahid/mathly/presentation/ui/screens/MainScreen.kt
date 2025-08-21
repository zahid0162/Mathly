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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.zahid.mathly.data.local.SessionManager
import com.zahid.mathly.presentation.ui.components.LanguageDropdown
import com.zahid.mathly.presentation.ui.components.NavigationDrawer
import com.zahid.mathly.presentation.ui.theme.PlayfairDisplay
import com.zahid.mathly.presentation.viewmodel.CaloriesCounterViewModel
import com.zahid.mathly.presentation.viewmodel.LanguageViewModel
import com.zahid.mathly.presentation.viewmodel.SharedViewModel
import com.zahid.mathly.presentation.viewmodel.ThemeViewModel
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.launch
import  com.zahid.mathly.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: SharedViewModel,
    themeViewModel: ThemeViewModel,
    languageViewModel: LanguageViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val history by viewModel.history.collectAsState()
    var selectedScreen by remember { mutableStateOf(context.getString(R.string.equations)) }
    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
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
                            color = MaterialTheme.colorScheme.onPrimary
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
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = MaterialTheme.colorScheme.onPrimary)
                        }
                    },
                    actions = {
                        LanguageDropdown(
                            languageViewModel = languageViewModel,
                            snackbarHostState = snackbarHostState
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        ) { paddingValues ->
            when (selectedScreen) {
                context.getString(R.string.equations) -> EquationsTab(
                    navController = navController,
                    viewModel = viewModel,
                    state = state,
                    history = history,
                    paddingValues = paddingValues
                )
                context.getString(R.string.word_problems) -> WordProblemsTab(
                    navController = navController,
                    paddingValues = paddingValues
                )
                context.getString(R.string.graphs) -> GraphTab(
                    paddingValues = paddingValues,
                    snackbarHostState
                )
                context.getString(R.string.basic_calculator) -> BasicCalculatorScreen(
                    navController = navController,
                    paddingValues = paddingValues
                )
                context.getString(R.string.calories_counter) -> {
                    val openAIService = androidx.hilt.navigation.compose.hiltViewModel<CaloriesCounterViewModel>().openAIService
                    CaloriesCounterScreen(
                        navController = navController,
                        paddingValues = paddingValues,
                        openAIService = openAIService
                    )
                }
                context.getString(R.string.bmi_calculator) -> BMICalculatorScreen(
                    navController = navController,
                    paddingValues = paddingValues
                )
                context.getString(R.string.profile) -> ProfileScreen(
                    navController = navController,
                    paddingValues = paddingValues,
                    themeViewModel = themeViewModel
                )
            }
        }
    }
}