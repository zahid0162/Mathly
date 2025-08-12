package com.zahid.mathly.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.zahid.mathly.R
import com.zahid.mathly.presentation.ui.theme.PlayfairDisplay
import kotlin.math.pow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BMICalculatorScreen(
    navController: NavController,
    paddingValues: PaddingValues
) {
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var bmiResult by remember { mutableStateOf<Double?>(null) }
    var bmiCategory by remember { mutableStateOf("") }
    var showResult by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Calculate your Body Mass Index",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        // Input Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Enter Your Details",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W600,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(24.dp))

                // Height Input
                OutlinedTextField(
                    value = height,
                    onValueChange = { height = it },
                    label = { Text("Height (cm)") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Height,
                            contentDescription = "Height"
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                // Weight Input
                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Weight (kg)") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.MonitorWeight,
                            contentDescription = "Weight"
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(24.dp))

                // Calculate Button
                Button(
                    onClick = {
                        calculateBMI(height, weight)?.let { bmi ->
                            bmiResult = bmi
                            bmiCategory = getBMICategory(bmi)
                            showResult = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = height.isNotEmpty() && weight.isNotEmpty()
                ) {
                    Icon(
                        imageVector = Icons.Default.Calculate,
                        contentDescription = "Calculate"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Calculate BMI")
                }
            }
        }

        // Result Card
        if (showResult && bmiResult != null) {
            Spacer(modifier = Modifier.height(24.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = getBMIColor(bmiResult!!)
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Your BMI Result",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W600,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = String.format("%.1f", bmiResult),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.W700,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = bmiCategory,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W600,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = getBMIDescription(bmiResult!!),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Reset Button
            OutlinedButton(
                onClick = {
                    height = ""
                    weight = ""
                    bmiResult = null
                    bmiCategory = ""
                    showResult = false
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reset"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Calculate Again")
            }
        }

        // BMI Categories Info
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "BMI Categories",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W600,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                BMICategoryItem("Underweight", "< 18.5", MaterialTheme.colorScheme.primary)
                BMICategoryItem("Normal weight", "18.5 - 24.9", MaterialTheme.colorScheme.secondary)
                BMICategoryItem("Overweight", "25.0 - 29.9", MaterialTheme.colorScheme.tertiary)
                BMICategoryItem("Obese", "≥ 30.0", MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun BMICategoryItem(
    category: String,
    range: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = category,
            fontSize = 16.sp,
            fontWeight = FontWeight.W500,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = range,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun calculateBMI(height: String, weight: String): Double? {
    return try {
        val heightM = height.toDouble() / 100 // Convert cm to meters
        val weightKg = weight.toDouble()
        weightKg / (heightM.pow(2))
    } catch (e: NumberFormatException) {
        null
    }
}

private fun getBMICategory(bmi: Double): String {
    return when {
        bmi < 18.5 -> "Underweight"
        bmi < 25.0 -> "Normal weight"
        bmi < 30.0 -> "Overweight"
        else -> "Obese"
    }
}

private fun getBMIDescription(bmi: Double): String {
    return when {
        bmi < 18.5 -> "You may be underweight. Consider consulting a healthcare provider for guidance on healthy weight gain."
        bmi < 25.0 -> "Congratulations! Your BMI is in the healthy range. Maintain a balanced diet and regular exercise."
        bmi < 30.0 -> "You may be overweight. Consider lifestyle changes like diet and exercise to improve your health."
        else -> "You may be obese. It's recommended to consult a healthcare provider for personalized advice."
    }
}

@Composable
private fun getBMIColor(bmi: Double): Color {
    return when {
        bmi < 18.5 -> MaterialTheme.colorScheme.primaryContainer
        bmi < 25.0 -> MaterialTheme.colorScheme.secondaryContainer
        bmi < 30.0 -> MaterialTheme.colorScheme.tertiaryContainer
        else -> MaterialTheme.colorScheme.errorContainer
    }
} 