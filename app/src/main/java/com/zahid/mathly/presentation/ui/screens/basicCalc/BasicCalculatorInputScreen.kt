package com.zahid.mathly.presentation.ui.screens.basicCalc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.zahid.mathly.R
import com.zahid.mathly.presentation.ui.theme.PlayfairDisplay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicCalculatorInputScreen(
    navController: NavController
) {
    var displayValue by remember { mutableStateOf("0") }
    var previousValue by remember { mutableDoubleStateOf(0.0) }
    var operation by remember { mutableStateOf("") }
    var waitingForOperand by remember { mutableStateOf(false) }
    var hasDecimal by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.basic_calculator),
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Display
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Text(
                    text = displayValue,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                )
            }

            // Calculator buttons
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Row 1: AC, +/-, %, ÷
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CalculatorButton(
                        text = "AC",
                        modifier = Modifier.weight(1f),
                        backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                        textColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ) {
                        displayValue = "0"
                        previousValue = 0.0
                        operation = ""
                        waitingForOperand = false
                        hasDecimal = false
                    }

                    CalculatorButton(
                        text = "+/-",
                        modifier = Modifier.weight(1f),
                        backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                        textColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ) {
                        if (displayValue != "0") {
                            displayValue = if (displayValue.startsWith("-")) {
                                displayValue.substring(1)
                            } else {
                                "-$displayValue"
                            }
                        }
                    }

                    CalculatorButton(
                        text = "%",
                        modifier = Modifier.weight(1f),
                        backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                        textColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ) {
                        val value = displayValue.toDoubleOrNull() ?: 0.0
                        displayValue = (value / 100).toString()
                        if (displayValue.endsWith(".0")) {
                            displayValue = displayValue.substring(0, displayValue.length - 2)
                        }
                    }

                    CalculatorButton(
                        text = "÷",
                        modifier = Modifier.weight(1f),
                        backgroundColor = if (operation == "÷") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                        textColor = if (operation == "÷") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                    ) {
                        val currentValue = displayValue.toDoubleOrNull() ?: 0.0
                        when (operation) {
                            "+" -> previousValue += currentValue
                            "-" -> previousValue -= currentValue
                            "×" -> previousValue *= currentValue
                            "÷" -> {
                                if (currentValue != 0.0) {
                                    previousValue /= currentValue
                                } else {
                                    displayValue = "Error"
                                    return@CalculatorButton
                                }
                            }

                            else -> previousValue = currentValue
                        }
                        displayValue = previousValue.toString()
                        if (displayValue.endsWith(".0")) {
                            displayValue = displayValue.substring(0, displayValue.length - 2)
                        }
                        operation = "÷"
                        waitingForOperand = true
                        hasDecimal = false
                    }
                }

                // Row 2: 7, 8, 9, ×
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CalculatorButton(
                        text = "7",
                        modifier = Modifier.weight(1f),
                        backgroundColor = MaterialTheme.colorScheme.surface,
                        textColor = MaterialTheme.colorScheme.onSurface
                    ) {
                        if (waitingForOperand) {
                            displayValue = "7"
                            waitingForOperand = false
                            hasDecimal = false
                        } else {
                            if (displayValue == "0") {
                                displayValue = "7"
                            } else {
                                displayValue += "7"
                            }
                        }
                    }

                    CalculatorButton(
                        text = "8",
                        modifier = Modifier.weight(1f),
                        backgroundColor = MaterialTheme.colorScheme.surface,
                        textColor = MaterialTheme.colorScheme.onSurface
                    ) {
                        if (waitingForOperand) {
                            displayValue = "8"
                            waitingForOperand = false
                            hasDecimal = false
                        } else {
                            if (displayValue == "0") {
                                displayValue = "8"
                            } else {
                                displayValue += "8"
                            }
                        }
                    }

                    CalculatorButton(
                        text = "9",
                        modifier = Modifier.weight(1f),
                        backgroundColor = MaterialTheme.colorScheme.surface,
                        textColor = MaterialTheme.colorScheme.onSurface
                    ) {
                        if (waitingForOperand) {
                            displayValue = "9"
                            waitingForOperand = false
                            hasDecimal = false
                        } else {
                            if (displayValue == "0") {
                                displayValue = "9"
                            } else {
                                displayValue += "9"
                            }
                        }
                    }

                    CalculatorButton(
                        text = "×",
                        modifier = Modifier.weight(1f),
                        backgroundColor = if (operation == "×") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                        textColor = if (operation == "×") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                    ) {
                        val currentValue = displayValue.toDoubleOrNull() ?: 0.0
                        when (operation) {
                            "+" -> previousValue += currentValue
                            "-" -> previousValue -= currentValue
                            "×" -> previousValue *= currentValue
                            "÷" -> {
                                if (currentValue != 0.0) {
                                    previousValue /= currentValue
                                } else {
                                    displayValue = "Error"
                                    return@CalculatorButton
                                }
                            }

                            else -> previousValue = currentValue
                        }
                        displayValue = previousValue.toString()
                        if (displayValue.endsWith(".0")) {
                            displayValue = displayValue.substring(0, displayValue.length - 2)
                        }
                        operation = "×"
                        waitingForOperand = true
                        hasDecimal = false
                    }
                }

                // Row 3: 4, 5, 6, -
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CalculatorButton(
                        text = "4",
                        modifier = Modifier.weight(1f),
                        backgroundColor = MaterialTheme.colorScheme.surface,
                        textColor = MaterialTheme.colorScheme.onSurface
                    ) {
                        if (waitingForOperand) {
                            displayValue = "4"
                            waitingForOperand = false
                            hasDecimal = false
                        } else {
                            if (displayValue == "0") {
                                displayValue = "4"
                            } else {
                                displayValue += "4"
                            }
                        }
                    }

                    CalculatorButton(
                        text = "5",
                        modifier = Modifier.weight(1f),
                        backgroundColor = MaterialTheme.colorScheme.surface,
                        textColor = MaterialTheme.colorScheme.onSurface
                    ) {
                        if (waitingForOperand) {
                            displayValue = "5"
                            waitingForOperand = false
                            hasDecimal = false
                        } else {
                            if (displayValue == "0") {
                                displayValue = "5"
                            } else {
                                displayValue += "5"
                            }
                        }
                    }

                    CalculatorButton(
                        text = "6",
                        modifier = Modifier.weight(1f),
                        backgroundColor = MaterialTheme.colorScheme.surface,
                        textColor = MaterialTheme.colorScheme.onSurface
                    ) {
                        if (waitingForOperand) {
                            displayValue = "6"
                            waitingForOperand = false
                            hasDecimal = false
                        } else {
                            if (displayValue == "0") {
                                displayValue = "6"
                            } else {
                                displayValue += "6"
                            }
                        }
                    }

                    CalculatorButton(
                        text = "-",
                        modifier = Modifier.weight(1f),
                        backgroundColor = if (operation == "-") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                        textColor = if (operation == "-") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                    ) {
                        val currentValue = displayValue.toDoubleOrNull() ?: 0.0
                        when (operation) {
                            "+" -> previousValue += currentValue
                            "-" -> previousValue -= currentValue
                            "×" -> previousValue *= currentValue
                            "÷" -> {
                                if (currentValue != 0.0) {
                                    previousValue /= currentValue
                                } else {
                                    displayValue = "Error"
                                    return@CalculatorButton
                                }
                            }

                            else -> previousValue = currentValue
                        }
                        displayValue = previousValue.toString()
                        if (displayValue.endsWith(".0")) {
                            displayValue = displayValue.substring(0, displayValue.length - 2)
                        }
                        operation = "-"
                        waitingForOperand = true
                        hasDecimal = false
                    }
                }

                // Row 4: 1, 2, 3, +
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CalculatorButton(
                        text = "1",
                        modifier = Modifier.weight(1f),
                        backgroundColor = MaterialTheme.colorScheme.surface,
                        textColor = MaterialTheme.colorScheme.onSurface
                    ) {
                        if (waitingForOperand) {
                            displayValue = "1"
                            waitingForOperand = false
                            hasDecimal = false
                        } else {
                            if (displayValue == "0") {
                                displayValue = "1"
                            } else {
                                displayValue += "1"
                            }
                        }
                    }

                    CalculatorButton(
                        text = "2",
                        modifier = Modifier.weight(1f),
                        backgroundColor = MaterialTheme.colorScheme.surface,
                        textColor = MaterialTheme.colorScheme.onSurface
                    ) {
                        if (waitingForOperand) {
                            displayValue = "2"
                            waitingForOperand = false
                            hasDecimal = false
                        } else {
                            if (displayValue == "0") {
                                displayValue = "2"
                            } else {
                                displayValue += "2"
                            }
                        }
                    }

                    CalculatorButton(
                        text = "3",
                        modifier = Modifier.weight(1f),
                        backgroundColor = MaterialTheme.colorScheme.surface,
                        textColor = MaterialTheme.colorScheme.onSurface
                    ) {
                        if (waitingForOperand) {
                            displayValue = "3"
                            waitingForOperand = false
                            hasDecimal = false
                        } else {
                            if (displayValue == "0") {
                                displayValue = "3"
                            } else {
                                displayValue += "3"
                            }
                        }
                    }

                    CalculatorButton(
                        text = "+",
                        modifier = Modifier.weight(1f),
                        backgroundColor = if (operation == "+") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                        textColor = if (operation == "+") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                    ) {
                        val currentValue = displayValue.toDoubleOrNull() ?: 0.0
                        when (operation) {
                            "+" -> previousValue += currentValue
                            "-" -> previousValue -= currentValue
                            "×" -> previousValue *= currentValue
                            "÷" -> {
                                if (currentValue != 0.0) {
                                    previousValue /= currentValue
                                } else {
                                    displayValue = "Error"
                                    return@CalculatorButton
                                }
                            }

                            else -> previousValue = currentValue
                        }
                        displayValue = previousValue.toString()
                        if (displayValue.endsWith(".0")) {
                            displayValue = displayValue.substring(0, displayValue.length - 2)
                        }
                        operation = "+"
                        waitingForOperand = true
                        hasDecimal = false
                    }
                }

                // Row 5: 0, ., =
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CalculatorButton(
                        text = "0",
                        modifier = Modifier.weight(1f),
                        backgroundColor = MaterialTheme.colorScheme.surface,
                        textColor = MaterialTheme.colorScheme.onSurface
                    ) {
                        if (waitingForOperand) {
                            displayValue = "0"
                            waitingForOperand = false
                            hasDecimal = false
                        } else {
                            if (displayValue == "0") {
                                displayValue = "0"
                            } else {
                                displayValue += "0"
                            }
                        }
                    }

                    CalculatorButton(
                        text = ".",
                        modifier = Modifier.weight(1f),
                        backgroundColor = MaterialTheme.colorScheme.surface,
                        textColor = MaterialTheme.colorScheme.onSurface
                    ) {
                        if (!hasDecimal) {
                            displayValue += "."
                            hasDecimal = true
                        }
                    }

                    CalculatorButton(
                        text = "=",
                        modifier = Modifier.weight(1f),
                        backgroundColor = MaterialTheme.colorScheme.primary,
                        textColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        val currentValue = displayValue.toDoubleOrNull() ?: 0.0
                        when (operation) {
                            "+" -> previousValue += currentValue
                            "-" -> previousValue -= currentValue
                            "×" -> previousValue *= currentValue
                            "÷" -> {
                                if (currentValue != 0.0) {
                                    previousValue /= currentValue
                                } else {
                                    displayValue = "Error"
                                    return@CalculatorButton
                                }
                            }

                            else -> previousValue = currentValue
                        }
                        displayValue = previousValue.toString()
                        if (displayValue.endsWith(".0")) {
                            displayValue = displayValue.substring(0, displayValue.length - 2)
                        }
                        operation = ""
                        waitingForOperand = false
                        hasDecimal = false
                    }
                }
            }
        }
    }
}
