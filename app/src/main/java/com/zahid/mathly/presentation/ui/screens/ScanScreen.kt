package com.zahid.mathly.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.zahid.mathly.presentation.ui.components.MathInputBottomSheet
import com.zahid.mathly.presentation.viewmodel.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen(
    navController: NavController,
    viewModel: SharedViewModel
) {
    val state by viewModel.state.collectAsState()
    val currentSolution by viewModel.currentSolution.collectAsState()
    var scannedText by remember { mutableStateOf("") }
    var showMathInputSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current
    
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
            navController.navigate("result")
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Scan Equation",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
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
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Scan your equation",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Point your camera at a handwritten or printed mathematical equation to scan it.",
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
                            text = "Camera Ready",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Tap the button below to start scanning",
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
                            text = "Scanned Text:",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        
                        OutlinedTextField(
                            value = scannedText,
                            onValueChange = { /* Read-only, only editable via bottom sheet */ },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text("Tap to edit scanned text...")
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            ),
                            textStyle = androidx.compose.ui.text.TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 24.sp
                            ),
                            minLines = 2,
                            maxLines = 4,
                            readOnly = true, // Make it read-only
                            trailingIcon = {
                                IconButton(onClick = { showMathInputSheet = true }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit"
                                    )
                                }
                            }
                        )
                        
                        // Edit hint
                        Text(
                            text = "💡 Tap the edit icon to open math keyboard for corrections",
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
                    onClick = { navController.navigate("camera") },
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
                            text = "Start Camera Scan",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                
                // Manual input option
                OutlinedButton(
                    onClick = {
                        // For testing, you can manually enter text
                        scannedText = "2x + 5 = 13"
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Enter Text Manually (Test)")
                }
                
                // Edit button for scanned text
                if (scannedText.isNotBlank()) {
                    Button(
                        onClick = { showMathInputSheet = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit"
                            )
                            Text(
                                text = "Edit with Math Keyboard",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
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
                                text = if (state.isLoading) "Solving..." else "Solve Scanned Equation",
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
                        Text("Clear Text")
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
    if (showMathInputSheet) {
        MathInputBottomSheet(
            initialText = scannedText,
            onTextSubmit = { newText ->
                scannedText = newText
                showMathInputSheet = false
            },
            onDismiss = {
                showMathInputSheet = false
            }
        )
    }
}