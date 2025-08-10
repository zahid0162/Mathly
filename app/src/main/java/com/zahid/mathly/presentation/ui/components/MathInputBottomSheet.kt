package com.zahid.mathly.presentation.ui.components

import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MathInputBottomSheet(
    initialText: String = "",
    onTextSubmit: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var inputValue by remember { 
        mutableStateOf(TextFieldValue(initialText, TextRange(initialText.length)))
    }
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current
    
    // Create a custom sheet state that prevents drag-to-dismiss
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { false } // This prevents drag-to-dismiss
    )
    
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { /* Do nothing - only allow dismissal via X button */ },
        modifier = Modifier.fillMaxHeight(),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = null // Remove drag handle to prevent accidental dismissal
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with Paste Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Math Input",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Paste Button
                    IconButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clipData = clipboard.primaryClip
                            if (clipData != null && clipData.itemCount > 0) {
                                val pasteData = clipData.getItemAt(0).text.toString()
                                inputValue = TextFieldValue(inputValue.text + pasteData, TextRange(inputValue.text.length + pasteData.length))
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentPaste,
                            contentDescription = "Paste from clipboard"
                        )
                    }
                    
                    // Close Button
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close without saving"
                        )
                    }
                    
                    // Done Button
                    IconButton(
                        onClick = {
                            if (inputValue.text.isNotBlank()) {
                                onTextSubmit(inputValue.text)
                                onDismiss()
                            }
                        },
                        enabled = inputValue.text.isNotBlank()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Save and close"
                        )
                    }
                }
            }
            
            // Input Field
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Title with cursor controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Equation:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        // Cursor movement buttons
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            // Left arrow
                            IconButton(
                                onClick = {
                                    val currentSelection = inputValue.selection
                                    if (currentSelection.start > 0) {
                                        val newCursorPosition = currentSelection.start - 1
                                        inputValue = TextFieldValue(inputValue.text, TextRange(newCursorPosition))
                                    }
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowLeft,
                                    contentDescription = "Move cursor left",
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            
                            // Right arrow
                            IconButton(
                                onClick = {
                                    val currentSelection = inputValue.selection
                                    if (currentSelection.start < inputValue.text.length) {
                                        val newCursorPosition = currentSelection.start + 1
                                        inputValue = TextFieldValue(inputValue.text, TextRange(newCursorPosition))
                                    }
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowRight,
                                    contentDescription = "Move cursor right",
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                    
                    // Custom Text Field with Visible Cursor
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(16.dp)
                            .focusRequester(focusRequester)
                            .clickable { focusRequester.requestFocus() }
                    ) {
                        val text = inputValue.text
                        val cursorPosition = inputValue.selection.start
                        
                        Text(
                            text = text,
                            fontSize = 16.sp,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        // Simple blinking cursor at the current position
                        var showCursor by remember { mutableStateOf(true) }
                        
                        LaunchedEffect(Unit) {
                            while (true) {
                                delay(500)
                                showCursor = !showCursor
                            }
                        }
                        
                        if (showCursor) {
                            // Calculate cursor position based on text length
                            val cursorOffset = if (cursorPosition <= text.length) {
                                cursorPosition * 10 // Approximate character width
                            } else {
                                text.length * 10
                            }
                            
                            Box(
                                modifier = Modifier
                                    .offset(x = cursorOffset.dp)
                                    .width(2.dp)
                                    .height(20.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(1.dp)
                                    )
                                    .zIndex(1f)
                            )
                        }
                    }
                }
            }
            
            // Math Keyboard
            Card(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    MathKeyboard(
                        onKeyPress = { key ->
                            val currentText = inputValue.text
                            val currentSelection = inputValue.selection
                            val newText = currentText.substring(0, currentSelection.start) + key + currentText.substring(currentSelection.end)
                            val newCursorPosition = currentSelection.start + key.length
                            inputValue = TextFieldValue(newText, TextRange(newCursorPosition))
                        },
                        onBackspace = {
                            val currentText = inputValue.text
                            val currentSelection = inputValue.selection
                            if (currentText.isNotEmpty() && currentSelection.start > 0) {
                                val newText = currentText.substring(0, currentSelection.start - 1) + currentText.substring(currentSelection.end)
                                val newCursorPosition = currentSelection.start - 1
                                inputValue = TextFieldValue(newText, TextRange(newCursorPosition))
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
} 