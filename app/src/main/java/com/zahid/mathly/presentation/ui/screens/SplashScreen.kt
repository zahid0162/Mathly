package com.zahid.mathly.presentation.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zahid.mathly.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }
    
    // Logo scale animation with bounce effect
    val logoScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1200,
            easing = EaseOutBack
        ),
        label = "logo_scale"
    )
    
    // Logo rotation animation
    val logoRotation by animateFloatAsState(
        targetValue = if (startAnimation) 360f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 200,
            easing = EaseOutCubic
        ),
        label = "logo_rotation"
    )
    
    // Text fade-in animation
    val textAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 800,
            delayMillis = 600
        ),
        label = "text_alpha"
    )
    
    // Text scale animation
    val textScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.8f,
        animationSpec = tween(
            durationMillis = 600,
            delayMillis = 600,
            easing = EaseOutBack
        ),
        label = "text_scale"
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2500) // Show splash for 2.5 seconds
        onSplashComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B4C5B)), // Dark teal from new palette
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Animated Logo Container with rotation
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .scale(logoScale)
                    .graphicsLayer(
                        rotationZ = logoRotation
                    )
                    .background(
                        color = Color(0xFFEBC7A4), // Light cream from palette
                        shape = MaterialTheme.shapes.large
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Inner Logo Circle
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(
                            color = Color(0xFF0BD3C7), // Bright cyan from palette
                            shape = MaterialTheme.shapes.medium
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // Math Symbol
                    Text(
                        text = "∑",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0D2B38) // Dark blue text
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // App Name with animations
            Text(
                text = "CALCSMART",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFEBC7A4), // Light cream text
                modifier = Modifier
                    .alpha(textAlpha)
                    .scale(textScale)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Tagline with animations
            Text(
                text = stringResource(R.string.smart_solutions_powered_by_ai),
                fontSize = 18.sp,
                color = Color(0xFFEBC7A4).copy(alpha = 0.8f), // Light cream with transparency
                modifier = Modifier
                    .alpha(textAlpha)
                    .scale(textScale)
            )
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // Loading indicator
            CircularProgressIndicator(
                color = Color(0xFF51B659), // Green from palette
                modifier = Modifier
                    .size(32.dp)
                    .alpha(textAlpha)
            )
        }
    }
}
