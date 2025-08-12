package com.zahid.mathly.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zahid.mathly.presentation.ui.components.EquationGraph
import com.zahid.mathly.presentation.ui.components.MathInputBottomSheet
import java.nio.file.WatchEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraphTab(
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState
) {
    var functionInput by remember { mutableStateOf("") }
    var showGraph by remember { mutableStateOf(false) }
    var showMathKeyboard by remember { mutableStateOf(false) }

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
            text = "Enter a function to plot its graph",
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
                    text = "Function (e.g., x+2, 2*x-1, x^2+3)",
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
                            functionInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text("Enter equation according to below format")
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
                    Text("Draw Graph")
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
                        text = "Graph of y = $functionInput",
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
        Spacer(modifier = Modifier.height(8.dp))
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

@Composable
fun InstructionsView(){
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
                text = "How to use:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Basic usage
            Text("• Enter a function like 'x+2' or '2*x-1'", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("• Use * for multiplication (e.g., 2*x)", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("• Use + and - for addition and subtraction", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("• Use / for division (e.g., x/2)", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("• Use ^ for powers (e.g., x^2 for x squared)", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

            // Trigonometry
            Text("• Trigonometry: sin(x), cos(x), tan(x), asin(x), acos(x), atan(x)", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("• By default, angles are in radians. Use 'deg' for degrees (e.g., sin(30 deg))", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

            // Constants
            Text("• Constants: pi, e", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

            // Advanced math
            Text("• Square root: sqrt(x)", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("• Logarithms: log(x) for base 10, ln(x) for natural log", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("• Absolute value: abs(x)", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

            // Calculus
            Text("• Derivatives: der(expression, x)", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("• Integrals: int(expression, x, start, end)", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

            // Usage tip
            Text("• Click 'Draw Graph' to visualize your function", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)


        }
    }
}

private fun extractFunctionFromEquation(equation: String): String {
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