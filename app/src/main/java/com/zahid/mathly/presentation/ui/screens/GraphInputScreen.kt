package com.zahid.mathly.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.zahid.mathly.R
import com.zahid.mathly.presentation.ui.components.EquationGraph
import com.zahid.mathly.presentation.ui.components.MathInputBottomSheet
import com.zahid.mathly.presentation.ui.theme.PlayfairDisplay
import com.zahid.mathly.presentation.viewmodel.GraphViewModel
import io.github.jan.supabase.realtime.Column
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraphInputScreen(
    navController: NavController,
    graphViewModel: GraphViewModel = hiltViewModel()
) {
    var functionInput by remember { mutableStateOf("") }
    var showGraph by remember { mutableStateOf(false) }
    var showMathKeyboard by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.graphs),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.W700,
                        fontFamily = PlayfairDisplay,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate("profile_main")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState(), true),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = stringResource(R.string.enter_a_function_to_plot_its_graph),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Input Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = stringResource(R.string.function_e_g_x_2_2_x_1_x_2_3),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = functionInput,
                            onValueChange = {
                                showGraph = false
                                functionInput = it
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(stringResource(R.string.enter_equation_according_to_below_format))
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            ),
                            textStyle = androidx.compose.ui.text.TextStyle(
                                fontSize = 18.sp,
                                lineHeight = 24.sp
                            ),
                            minLines = 3,
                            maxLines = 6,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                if (functionInput.isNotBlank()) {
                                    showGraph = false
                                    showGraph = true
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = functionInput.isNotBlank()
                        ) {
                            Icon(
                                Icons.Default.Draw,
                                contentDescription = "Draw",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stringResource(R.string.draw_graph))
                        }

                        Button(
                            onClick = {
                                if (functionInput.isNotBlank()) {
                                    graphViewModel.saveGraph(functionInput)
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Graph equation saved")
                                    }

                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = functionInput.isNotBlank()
                        ) {
                            Icon(
                                Icons.Default.Save,
                                contentDescription = "Save",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Save Graph")
                        }
                    }
                }
            }

            // Graph Section
            if (showGraph) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.graph_of_y, functionInput),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            EquationGraph(
                                equation = extractFunctionFromEquation(functionInput),
                                modifier = Modifier.fillMaxWidth(),
                                snackbarHostState
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Instructions
            InstructionsView()
        }

        // Math Input Bottom Sheet
        if (showMathKeyboard) {
            MathInputBottomSheet(
                initialText = functionInput,
                onTextSubmit = { functionInput = it },
                onDismiss = { showMathKeyboard = false },
            )
        }
    }
}

@Composable
fun InstructionsView() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.how_to_use),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Basic usage
            Text(
                stringResource(R.string.enter_a_function_like_x_2_or_2_x_1),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                stringResource(R.string.use_for_multiplication_e_g_2_x),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                stringResource(R.string.use_and_for_addition_and_subtraction),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                stringResource(R.string.use_for_division_e_g_x_2),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                stringResource(R.string.use_for_powers_e_g_x_2_for_x_squared),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Trigonometry
            Text(
                stringResource(R.string.trigonometry_sin_x_cos_x_tan_x_asin_x_acos_x_atan_x),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                stringResource(R.string.by_default_angles_are_in_radians_use_deg_for_degrees_e_g_sin_30_deg),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Constants
            Text(
                stringResource(R.string.constants_pi_e),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Advanced math
            Text(
                stringResource(R.string.square_root_sqrt_x),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                stringResource(R.string.logarithms_log_x_for_base_10_ln_x_for_natural_log),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                stringResource(R.string.absolute_value_abs_x),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Calculus
            Text(
                stringResource(R.string.derivatives_der_expression_x),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                stringResource(R.string.integrals_int_expression_x_start_end),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Usage tip
            Text(
                stringResource(R.string.click_draw_graph_to_visualize_your_function),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

fun extractFunctionFromEquation(equation: String): String {
    val cleanEquation = equation.replace(" ", "")

    return when {
        cleanEquation.startsWith("y=") -> cleanEquation.substring(2)
        cleanEquation.startsWith("f(x)=") -> cleanEquation.substring(5)
        cleanEquation.contains("=") -> {
            val parts = cleanEquation.split("=")
            if (parts.size == 2) {
                // Try to extract the function part
                val left = parts[0]
                val right = parts[1]

                when {
                    left.contains("x") -> left
                    right.contains("x") -> right
                    else -> right // Default to right side
                }
            } else {
                cleanEquation
            }
        }

        else -> cleanEquation
    }
}
