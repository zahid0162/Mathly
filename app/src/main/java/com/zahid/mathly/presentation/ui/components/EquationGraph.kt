package com.zahid.mathly.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zahid.mathly.presentation.ui.theme.md_theme_light_primary
import com.zahid.mathly.presentation.ui.theme.md_theme_light_primaryContainer
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.LineProperties
import ir.ehsannarmani.compose_charts.models.ZeroLineProperties
import kotlin.math.*

@Composable
fun EquationGraph(
    equation: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Function Graph",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Text(
                text = "y = $equation",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            // Generate data points for the function
            val dataPoints = remember(equation) {
                generateFunctionDataPoints(equation, -5.0, 5.0, 1.0)
            }
            
            if (dataPoints.isNotEmpty()) {
                // Calculate the actual min and max values from the data
                val minY = dataPoints.minOfOrNull { it.second } ?: 0.0
                val maxY = dataPoints.maxOfOrNull { it.second } ?: 10.0
                
                LineChart(
                    modifier = Modifier
                        .width(400.dp)
                        .height(400.dp),
                    data = remember {
                        listOf(
                            Line(
                                label = "Function",
                                values = dataPoints.map { it.second },
                                color = SolidColor(md_theme_light_primary),
                                curvedEdges = true,
                                dotProperties = DotProperties(
                                    enabled = true,
                                    color = SolidColor(md_theme_light_primary),
                                    radius = 3f.dp,
                                    strokeColor = SolidColor(md_theme_light_primaryContainer),
                                    strokeWidth = 2f.dp
                                ),
                                drawStyle = DrawStyle.Stroke(width = 3.dp)
                            )
                        )
                    },
                    animationMode = AnimationMode.Together(delayBuilder = { it * 50L }),
                    zeroLineProperties = ZeroLineProperties(
                        enabled = true,
                        color = SolidColor(MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                    ),
                    minValue = minY - 1.0, // Add some padding
                    maxValue = maxY + 1.0  // Add some padding
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Unable to plot function",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

private fun generateFunctionDataPoints(
    equation: String,
    startX: Double,
    endX: Double,
    step: Double
): List<Pair<Double, Double>> {
    val dataPoints = mutableListOf<Pair<Double, Double>>()
    
    var x = startX
    while (x <= endX) {
        try {
            val y = evaluateFunction(equation, x)
            if (y.isFinite() && y >= -100 && y <= 100) { // Filter out invalid values
                dataPoints.add(x to y)
            }
        } catch (e: Exception) {
            // Skip invalid points
        }
        x += step
    }
    
    return dataPoints
}

private fun evaluateFunction(equation: String, x: Double): Double {
    return try {
        // Extract the right side of the equation (after =)
        val expression = if (equation.contains("=")) {
            equation.split("=")[1].trim()
        } else {
            equation.trim()
        }
        
        // Replace x with the actual value and evaluate
        val substitutedExpression = expression.replace("x", "($x)")
        
        val result = evaluateExpression(substitutedExpression)
        result
    } catch (e: Exception) {
        Double.NaN
    }
}

private fun evaluateExpression(expression: String): Double {
    val tokens = tokenize(expression)
    val result = evaluateTokens(tokens)
    return result
}

private fun tokenize(expression: String): List<String> {
    val tokens = mutableListOf<String>()
    var current = ""
    
    for (i in expression.replace(" ", "").indices) {
        val char = expression[i]
        when (char) {
            '+', '-', '*', '/', '(', ')' -> {
                if (current.isNotEmpty()) {
                    tokens.add(current)
                    current = ""
                }
                tokens.add(char.toString())
            }
            else -> {
                current += char
                // Check if we need to add implicit multiplication
                if (i < expression.length - 1) {
                    val nextChar = expression[i + 1]
                    if (nextChar == '(' && current.matches(Regex("-?\\d+(\\.\\d+)?"))) {
                        // Add implicit multiplication: 2( becomes 2*(
                        tokens.add(current)
                        tokens.add("*")
                        current = ""
                    }
                }
            }
        }
    }
    if (current.isNotEmpty()) {
        tokens.add(current)
    }
    
    return tokens
}

private fun evaluateTokens(tokens: List<String>): Double {
    val stack = mutableListOf<Double>()
    val operators = mutableListOf<String>()
    
    for (token in tokens) {
        when {
            token.matches(Regex("-?\\d+(\\.\\d+)?")) -> {
                stack.add(token.toDouble())
            }
            token == "(" -> {
                operators.add(token)
            }
            token == ")" -> {
                while (operators.isNotEmpty() && operators.last() != "(") {
                    val op = operators.removeAt(operators.size - 1)
                    applyOperator(stack, op)
                }
                if (operators.isNotEmpty() && operators.last() == "(") {
                    operators.removeAt(operators.size - 1)
                }
            }
            isOperator(token) -> {
                while (operators.isNotEmpty() && 
                       operators.last() != "(" && 
                       precedence(operators.last()) >= precedence(token)) {
                    val op = operators.removeAt(operators.size - 1)
                    applyOperator(stack, op)
                }
                operators.add(token)
            }
        }
    }
    
    while (operators.isNotEmpty()) {
        val op = operators.removeAt(operators.size - 1)
        applyOperator(stack, op)
    }
    
    val result = stack.firstOrNull() ?: 0.0
    return result
}

private fun isOperator(token: String): Boolean {
    return token in listOf("+", "-", "*", "/")
}

private fun precedence(operator: String): Int {
    return when (operator) {
        "*", "/" -> 2
        "+", "-" -> 1
        else -> 0
    }
}

private fun applyOperator(stack: MutableList<Double>, operator: String) {
    if (stack.size < 2) return
    
    val b = stack.removeAt(stack.size - 1)
    val a = stack.removeAt(stack.size - 1)
    
    val result = when (operator) {
        "+" -> a + b
        "-" -> a - b
        "*" -> a * b
        "/" -> if (b != 0.0) a / b else Double.NaN
        else -> Double.NaN
    }
    
    stack.add(result)
}

// Extension function for power operation
private fun Double.pow(power: Double): Double = this.pow(power)
