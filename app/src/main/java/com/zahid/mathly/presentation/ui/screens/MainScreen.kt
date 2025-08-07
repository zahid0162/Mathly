package com.zahid.mathly.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.zahid.mathly.presentation.viewmodel.SharedViewModel
import com.zahid.mathly.R

val PlayfairDisplay = FontFamily(
    Font(R.font.title_font, FontWeight.Normal),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: SharedViewModel
) {
    val state by viewModel.state.collectAsState()
    val history by viewModel.history.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(

                title = {
                    Text(
                        text = "Mathly",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.W700,
                        fontFamily = PlayfairDisplay,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Calculate, contentDescription = "Equations") },
                    label = { Text("Equations") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.TextFields, contentDescription = "Word Problems") },
                    label = { Text("Word Problems") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.History, contentDescription = "History") },
                    label = { Text("History") },
                    selected = selectedTab == 2,
                    onClick = { 
                        selectedTab = 2
                    }
                )
            }
        }
    ) { paddingValues ->
        when (selectedTab) {
            0 -> EquationsTab(
                navController = navController,
                viewModel = viewModel,
                state = state,
                history = history,
                paddingValues = paddingValues
            )
            1 -> WordProblemsTab(
                navController = navController,
                paddingValues = paddingValues
            )
            2 -> HistoryTab(
                navController = navController,
                viewModel = viewModel,
                paddingValues = paddingValues
            )
        }
    }
}