package com.zahid.mathly.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zahid.mathly.R
import com.zahid.mathly.presentation.viewmodel.LanguageViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageDropdown(
    languageViewModel: LanguageViewModel,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val currentLanguage = languageViewModel.currentLanguage
    val availableLanguages = languageViewModel.getAvailableLanguages()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val langChangeMsg = stringResource(R.string.language_changed)
    
    Box(modifier = modifier) {
        IconButton(
            onClick = { expanded = true }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = stringResource(R.string.language),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            availableLanguages.forEach { (languageCode, displayName) ->
                DropdownMenuItem(
                    text = { 
                        Text(
                            text = displayName,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    onClick = {
                        val previousLanguage = currentLanguage
                        expanded = false
                        
                        // Show snackbar if language actually changed
                        if (previousLanguage != languageCode) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = langChangeMsg
                                )
                                // Small delay to show the snackbar before restart
                                kotlinx.coroutines.delay(1000)
                                languageViewModel.setLanguage(languageCode)
                            }
                        }
                    },
                    leadingIcon = {
                        if (languageCode == currentLanguage) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            }
        }
    }
}
