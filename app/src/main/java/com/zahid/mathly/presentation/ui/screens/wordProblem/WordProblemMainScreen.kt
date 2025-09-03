package com.zahid.mathly.presentation.ui.screens.wordProblem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.zahid.mathly.R
import com.zahid.mathly.domain.model.SolutionType
import com.zahid.mathly.presentation.ui.components.EmptyStateView
import com.zahid.mathly.presentation.ui.components.ProfileAvatar
import com.zahid.mathly.presentation.ui.screens.profile.HistorySolutionCard
import com.zahid.mathly.presentation.ui.theme.PlayfairDisplay
import com.zahid.mathly.presentation.viewmodel.SharedViewModel
import com.zahid.mathly.presentation.viewmodel.profile.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordProblemMainScreen(
    navController: NavController,
    onAddClick: () -> Unit,
    sharedViewModel: SharedViewModel,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val state by sharedViewModel.history.collectAsStateWithLifecycle()
    val profileData by profileViewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.word_problems),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.W700,
                        fontFamily = PlayfairDisplay,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Back to Home",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                actions = {
                    // Profile button
                    IconButton(
                        onClick = {
                            navController.navigate("profile_main")
                        }
                    ) {
                        ProfileAvatar(profileData.profileData.avatarUrl)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Word Problem")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.any { it.type == SolutionType.WORD_PROBLEM }){
                LazyColumn(
                    modifier = Modifier.padding(8.dp)
                ) {
                    items(state.filter { it.type == SolutionType.WORD_PROBLEM }) { solution ->
                        HistorySolutionCard(
                            solution = solution,
                            onClick = {
                                sharedViewModel.setSolution(solution)
                                navController.navigate("result")
                            }
                        )
                    }
                }
            }
            else{
                EmptyStateView(
                    title = stringResource(R.string.no_word_problems_yet),
                    message = stringResource(R.string.solve_word_problems_with_detailed_explanations),
                    icon = Icons.Default.TextFields
                )
            }
        }
    }
}
