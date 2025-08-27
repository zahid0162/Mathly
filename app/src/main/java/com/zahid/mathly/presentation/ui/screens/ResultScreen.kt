package com.zahid.mathly.presentation.ui.screens

import com.zahid.mathly.R

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.zahid.mathly.domain.model.SolutionStep
import com.zahid.mathly.domain.model.SolutionType
import com.zahid.mathly.presentation.ui.components.EquationGraph
import com.zahid.mathly.presentation.viewmodel.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    navController: NavController,
    viewModel: SharedViewModel
) {
    val state by viewModel.state.collectAsState()
    val currentSolution by viewModel.currentSolution.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.solution),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { 
                        viewModel.clearSolution()
                        navController.navigateUp() 
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { 
                            currentSolution?.let { solution ->
                                shareSolution(context, solution)
                            }
                        },
                        enabled = currentSolution != null
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = stringResource(R.string.solving_equation),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Display actual solution from API
                currentSolution?.let { solution ->
                    item {
                        SolutionCard(
                            equation = solution.equationId,
                            steps = solution.steps,
                            finalAnswer = solution.finalAnswer
                        )
                    }

                    // Show graph for equations that can be plotted
                    if (solution.type == SolutionType.EQUATION && canPlotEquation(solution.equationId)) {
                        item {
                            EquationGraph(
                                equation = extractFunctionFromEquation(solution.equationId),
                                modifier = Modifier.fillMaxWidth(),
                                snackbarHostState
                            )
                        }
                    }
                } ?: run {
                    // Show placeholder if no solution available
                    item {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = stringResource(R.string.no_solution_available),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = stringResource(R.string.try_solving_an_equation_first),
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
        
        // Error Display
        state.error?.let { error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = error,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}

private fun shareSolution(context: android.content.Context, solution: com.zahid.mathly.domain.model.Solution) {
    val shareText = buildString {
        appendLine("Problem:")
        appendLine(solution.equationId)
        appendLine()
        appendLine("Solution:")
        if (solution.finalAnswer.isNotBlank()) {
            appendLine("Answer: ${solution.finalAnswer}")
            appendLine()
        }
        appendLine("All Steps:")
        solution.steps.forEach { step ->
            appendLine("Step ${step.stepNumber}: ${step.description}")
            if (step.calculation.isNotBlank()) {
                appendLine("  Calculation: ${step.calculation}")
            }
            if (step.result.isNotBlank()) {
                appendLine("  Result: ${step.result}")
            }
            appendLine()
        }
    }
    
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, shareText)
        putExtra(Intent.EXTRA_SUBJECT, "Here is my Solution")
    }
    
    val chooser = Intent.createChooser(shareIntent, "Share Solution")
    context.startActivity(chooser)
}

// Helper functions for graph plotting
private fun canPlotEquation(equation: String): Boolean {
    val cleanEquation = equation.lowercase().replace(" ", "")
    
    // Check if it's a simple function of x (y = f(x))
    return cleanEquation.contains("x") && (
        cleanEquation.startsWith("y=") || 
        cleanEquation.startsWith("f(x)=") ||
        cleanEquation.matches(Regex(".*[a-z]\\s*=\\s*.*x.*")) ||
        cleanEquation.matches(Regex(".*x.*[+\\-*/^]"))
    )
}

@Composable
fun SolutionCard(
    equation: String,
    steps: List<SolutionStep>,
    finalAnswer: String
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Equation
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.equation_colon),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = equation,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            
            // Steps
            if (steps.isNotEmpty()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.step_by_step_solution),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    steps.forEach { step ->
                        StepCard(step = step)
                    }
                }
            }
            
            // Final Answer
            if (finalAnswer.isNotBlank()) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.final_answer),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = finalAnswer,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StepCard(step: SolutionStep) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.step, step.stepNumber, step.description),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            if (step.calculation.isNotBlank()) {
                Text(
                    text = stringResource(R.string.calculation, step.calculation),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            if (step.result.isNotBlank()) {
                Text(
                    text = stringResource(R.string.result, step.result),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
} 