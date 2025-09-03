package com.zahid.mathly.presentation.ui.screens.equation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.zahid.mathly.R
import com.zahid.mathly.presentation.navigation.AppRoutes
import com.zahid.mathly.presentation.ui.components.MathInputBottomSheet
import com.zahid.mathly.presentation.viewmodel.SharedViewModel
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen(
    navController: NavController,
    viewModel: SharedViewModel
) {
    val state by viewModel.state.collectAsState()
    val currentSolution by viewModel.currentSolution.collectAsState()
    var scannedText by remember { mutableStateOf("") }
    
    // Get scanned text from camera screen
    val scannedTextFromCamera = navController.currentBackStackEntry?.savedStateHandle?.get<String>("scanned_text")
    
    // Update scanned text when received from camera
    LaunchedEffect(scannedTextFromCamera) {
        scannedTextFromCamera?.let { text ->
            scannedText = text
            // Clear the saved state after retrieving it
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>("scanned_text")
        }
    }
    
    // Navigate to result screen when solution is received
    LaunchedEffect(currentSolution) {
        currentSolution?.let {
            navController.navigate(AppRoutes.Result.route)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.scan_equation),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState(), true),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Instructions
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.scan_your_equation),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.point_your_camera_at_a_handwritten_or_printed_mathematical_equation_to_scan_it),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
            
            // Camera Preview Placeholder
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Camera",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = stringResource(R.string.camera_ready),
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = stringResource(R.string.tap_the_button_below_to_start_scanning),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            // Scanned Text Display
            if (scannedText.isNotBlank()) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.scanned_text),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        
                        OutlinedTextField(
                            value = scannedText,
                            onValueChange = { /* Read-only, only editable via bottom sheet */ },
                            modifier = Modifier.fillMaxWidth().pointerInput(Unit) {
                                detectTapGestures {
                                    val encodedText = URLEncoder.encode(scannedText, "UTF-8")
                                    navController.navigate("math_input/$encodedText")
                                }
                            },
                            placeholder = {
                                Text(stringResource(R.string.tap_to_edit_scanned_text))
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            ),
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 24.sp
                            ),
                            minLines = 2,
                            maxLines = 4,
                            readOnly = true, // Make it read-only
                            trailingIcon = {
                                IconButton(onClick = {
                                    val encodedText = URLEncoder.encode(scannedText, "UTF-8")
                                    navController.navigate("math_input/$encodedText")
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit"
                                    )
                                }
                            }
                        )
                        
                        // Edit hint
                        Text(
                            text = stringResource(R.string.tap_the_edit_icon_to_open_math_keyboard_for_corrections),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
            
            // Action Buttons
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { navController.navigate(AppRoutes.Camera.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Camera,
                            contentDescription = "Scan"
                        )
                        Text(
                            text = stringResource(R.string.start_camera_scan),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                
                // Solve button for scanned/manual input
                if (scannedText.isNotBlank()) {
                    Button(
                        onClick = {
                            viewModel.solveEquation(scannedText)
                        },
                        enabled = scannedText.isNotBlank() && !state.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Solve"
                            )
                            Text(
                                text = if (state.isLoading) stringResource(R.string.solving) else stringResource(
                                    R.string.solve_scanned_equation
                                ),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    
                    // Clear button
                    OutlinedButton(
                        onClick = { scannedText = "" },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.clear_text))
                    }
                }
            }
            
            // Error Display
            state.error?.let { error ->
                Card(
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
    
    // Math Input Bottom Sheet
    LaunchedEffect(Unit) {
        navController.currentBackStackEntry?.savedStateHandle?.get<String>("math_input_result")?.let { result ->
            scannedText = result
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>("math_input_result")
        }
    }
}