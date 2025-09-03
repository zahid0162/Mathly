package com.zahid.mathly.presentation.ui.screens.equation

import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.zahid.mathly.R
import com.zahid.mathly.presentation.ui.components.MathKeyboard
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MathInputScreen(
    navController: NavController,
    initialText: String = ""
) {
    var inputValue by remember {
        mutableStateOf(TextFieldValue(initialText, TextRange(initialText.length)))
    }
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current
    var showTooltip by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.math_input)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Go back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                actions = {
                    // Paste Button
                    IconButton(
                        onClick = {
                            val clipboard =
                                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clipData = clipboard.primaryClip
                            if (clipData != null && clipData.itemCount > 0) {
                                val pasteData = clipData.getItemAt(0).text.toString()
                                inputValue = TextFieldValue(
                                    inputValue.text + pasteData,
                                    TextRange(inputValue.text.length + pasteData.length)
                                )
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentPaste,
                            contentDescription = "Paste from clipboard",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    
                    // Done Button
                    IconButton(
                        onClick = {
                            if (inputValue.text.isNotBlank()) {
                                // Return the input text to the previous screen
                                navController.previousBackStackEntry?.savedStateHandle?.set(
                                    "math_input_result",
                                    inputValue.text
                                )
                                navController.popBackStack()
                            }
                        },
                        enabled = inputValue.text.isNotBlank()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Save and return",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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
                            text = stringResource(R.string.equation_colon),
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
                                        inputValue = TextFieldValue(
                                            inputValue.text,
                                            TextRange(newCursorPosition)
                                        )
                                    }
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
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
                                        inputValue = TextFieldValue(
                                            inputValue.text,
                                            TextRange(newCursorPosition)
                                        )
                                    }
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "Move cursor right",
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    // Floating Tooltip for cursor guidance
                    if (showTooltip) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.use_these_arrows_to_move_cursor_position),
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.weight(1f)
                                )

                                IconButton(
                                    onClick = { showTooltip = false },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Close tooltip",
                                        modifier = Modifier.size(16.dp),
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
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
                        Text(
                            text = text,
                            fontSize = 16.sp,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Math Keyboard
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                    MathKeyboard(
                        onKeyPress = { key ->
                            val currentText = inputValue.text
                            val currentSelection = inputValue.selection
                            val newText = currentText.substring(
                                0,
                                currentSelection.start
                            ) + key + currentText.substring(currentSelection.end)
                            val newCursorPosition = currentSelection.start + key.length
                            inputValue = TextFieldValue(newText, TextRange(newCursorPosition))
                        },
                        onBackspace = {
                            val currentText = inputValue.text
                            val currentSelection = inputValue.selection
                            if (currentText.isNotEmpty() && currentSelection.start > 0) {
                                val newText = currentText.substring(
                                    0,
                                    currentSelection.start - 1
                                ) + currentText.substring(currentSelection.end)
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
