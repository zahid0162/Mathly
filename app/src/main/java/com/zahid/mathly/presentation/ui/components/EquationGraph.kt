package com.zahid.mathly.presentation.ui.components

import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.zahid.mathly.presentation.ui.theme.md_theme_light_onPrimaryContainer
import com.zahid.mathly.presentation.ui.theme.md_theme_light_primary
import com.zahid.mathly.presentation.ui.theme.md_theme_light_primaryContainer
import kotlinx.coroutines.launch
import org.mariuszgromada.math.mxparser.Argument
import org.mariuszgromada.math.mxparser.Expression

@Composable
fun EquationGraph(
    equation: String,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Generate data points for the function
            val dataPoints = remember(equation) {
                generateDataPoints(equation, -5.0, 5.0, 0.2)
            }

            if (dataPoints.isNotEmpty()) {
                // Calculate the actual min and max values from the data
                dataPoints.forEach {
                    Log.d("Data Points", "${it.first},${it.second}")
                }
                Row(modifier = Modifier.horizontalScroll(rememberScrollState(), true)) {
                    GraphViewCompose(
                        points = dataPoints,
                        modifier = Modifier
                            .width(500.dp)
                            .height(500.dp),
                        snackbarHostState
                    )
                }

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

fun generateDataPoints(
    equation: String,
    start: Double = -5.0,
    end: Double = 5.0,
    step: Double = 1.0
): MutableList<Pair<Double, Double>> {
    val points = mutableListOf<Pair<Double, Double>>()
    val xArg = Argument("x", 0.0) // Define 'x' variable

    // Parse the equation with x as an argument
    val exp = Expression(equation, xArg)

    var xVal = start
    while (xVal <= end) {
        xArg.argumentValue = xVal
        val yVal = exp.calculate()

        // Handle invalid numbers (NaN, Infinity) gracefully
        if (!yVal.isNaN() && yVal.isFinite()) {
            points.add(Pair(xVal, yVal))
        }

        xVal += step
    }

    return points
}

// Extension function for power operation
private fun Double.pow(power: Double): Double = this.pow(power)

@Composable
fun GraphViewCompose(
    points: List<Pair<Double, Double>>,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()
    AndroidView(
        factory = { context ->
            GraphView(context).apply {
                val dataPoints = points.map { DataPoint(it.first, it.second) }.toTypedArray()
                val series = LineGraphSeries(dataPoints)

                addSeries(series)

                // Style the series
                series.color = md_theme_light_primary.hashCode()
                series.isDrawDataPoints = true
                series.dataPointsRadius = 6f
                series.thickness = 4
                series.setOnDataPointTapListener { points, p ->
                    scope.launch {
                        snackbarHostState.showSnackbar(message = "x = ${p.x}, y = ${p.y}")
                    }

                }
                series.backgroundColor = md_theme_light_primaryContainer.hashCode()
                series.setAnimated(true)
                // Viewport bounds
                viewport.isXAxisBoundsManual = true
                viewport.isYAxisBoundsManual = true
                viewport.setMinX(points.minOf { it.first })
                viewport.setMaxX(points.maxOf { it.first })
                viewport.setMinY(points.minOf { it.second })
                viewport.setMaxY(points.maxOf { it.second })


                // Enable zooming & scrolling
                viewport.isScalable = true
                viewport.isScrollable = true
            }
        },
        modifier = modifier
    )
}
