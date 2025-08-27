package com.zahid.mathly.presentation.ui.screens.bmi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.zahid.mathly.R
import java.util.Locale
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
    val context = LocalContext.current

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
            text = stringResource(R.string.calculate_your_body_mass_index),
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
                    text = stringResource(R.string.enter_your_details),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W600,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Height Input
                OutlinedTextField(
                    value = height,
                    onValueChange = { height = it },
                    label = { Text(stringResource(R.string.height_cm)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Height,
                            contentDescription = stringResource(R.string.height)
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
                    label = { Text(stringResource(R.string.weight_kg)) },
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
                            bmiCategory = context.getString(getBMICategory(bmi))
                            showResult = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = height.isNotEmpty() && weight.isNotEmpty()
                ) {
                    Icon(
                        imageVector = Icons.Default.Calculate,
                        contentDescription = stringResource(R.string.calculate)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.calculate_bmi))
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
                        text = stringResource(R.string.your_bmi_result),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W600,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = String.format(Locale.US, "%.1f", bmiResult),
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
                        text = stringResource(getBMIDescription(bmiResult!!)),
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
                    contentDescription = stringResource(R.string.reset)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.calculate_again))
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
                    text = stringResource(R.string.bmi_categories),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W600,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                BMICategoryItem(
                    stringResource(R.string.underweight),
                    "< 18.5",
                    MaterialTheme.colorScheme.primary
                )
                BMICategoryItem(
                    stringResource(R.string.normal_weight),
                    "18.5 - 24.9",
                    MaterialTheme.colorScheme.secondary
                )
                BMICategoryItem(
                    stringResource(R.string.overweight),
                    "25.0 - 29.9",
                    MaterialTheme.colorScheme.tertiary
                )
                BMICategoryItem(
                    stringResource(R.string.obese),
                    "≥ 30.0",
                    MaterialTheme.colorScheme.error
                )
            }
        }
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
private fun getBMICategory(bmi: Double): Int {
    return when {
        bmi < 18.5 -> R.string.underweight
        bmi < 25.0 -> R.string.normal_weight
        bmi < 30.0 -> R.string.overweight
        else -> R.string.obese
    }
}

private fun getBMIDescription(bmi: Double): Int {
    return when {
        bmi < 18.5 -> R.string.you_may_be_underweight_consider_consulting_a_healthcare_provider_for_guidance_on_healthy_weight_gain
        bmi < 25.0 -> R.string.congratulations_your_bmi_is_in_the_healthy_range_maintain_a_balanced_diet_and_regular_exercise
        bmi < 30.0 -> R.string.you_may_be_overweight_consider_lifestyle_changes_like_diet_and_exercise_to_improve_your_health
        else -> R.string.you_may_be_obese_it_s_recommended_to_consult_a_healthcare_provider_for_personalized_advice
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