package com.zahid.mathly.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MathKeyboard(
    onKeyPress: (String) -> Unit,
    onBackspace: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("Numbers") }
    
    val categories = listOf("Numbers", "Variables", "Operators", "Functions", "Symbols", "Exponents")
    
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp)
    ) {
        // Category Dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedCategory,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            )
            
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(MaterialTheme.colorScheme.surface)
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category) },
                        onClick = {
                            selectedCategory = category
                            expanded = false
                        }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Keyboard content based on selected category
        when (selectedCategory) {
            "Numbers" -> NumbersKeyboard(onKeyPress, onBackspace)
            "Variables" -> VariablesKeyboard(onKeyPress, onBackspace)
            "Operators" -> OperatorsKeyboard(onKeyPress, onBackspace)
            "Functions" -> FunctionsKeyboard(onKeyPress, onBackspace)
            "Symbols" -> SymbolsKeyboard(onKeyPress, onBackspace)
            "Exponents" -> ExponentsKeyboard(onKeyPress, onBackspace)
        }
    }
}

@Composable
fun NumbersKeyboard(
    onKeyPress: (String) -> Unit,
    onBackspace: () -> Unit
) {
    val numbers = listOf(
        "7", "8", "9",
        "4", "5", "6",
        "1", "2", "3",
        "0", ".", "="
    )
    
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxHeight()
    ) {
        items(numbers) { number ->
            KeyboardKey(
                text = number,
                onClick = { onKeyPress(number) },
                modifier = Modifier.aspectRatio(1.2f)
            )
        }
        
        item {
            KeyboardKey(
                text = "Space",
                onClick = { onKeyPress(" ") },
                modifier = Modifier.aspectRatio(1.2f),
                fontSize = 10.sp
            )
        }
        
        item {
            KeyboardKey(
                icon = Icons.Default.Backspace,
                onClick = onBackspace,
                modifier = Modifier.aspectRatio(1.2f)
            )
        }
    }
}

@Composable
fun VariablesKeyboard(
    onKeyPress: (String) -> Unit,
    onBackspace: () -> Unit
) {
    val variables = listOf(
        "x", "y", "z", "a",
        "b", "c", "d", "e",
        "f", "g", "h", "i",
        "j", "k", "l", "m"
    )
    
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxHeight()
    ) {
        items(variables) { variable ->
            KeyboardKey(
                text = variable,
                onClick = { onKeyPress(variable) },
                modifier = Modifier.aspectRatio(1.2f)
            )
        }
        
        item {
            KeyboardKey(
                text = "Space",
                onClick = { onKeyPress(" ") },
                modifier = Modifier.aspectRatio(1.2f),
                fontSize = 10.sp
            )
        }
        
        item {
            KeyboardKey(
                icon = Icons.Default.Backspace,
                onClick = onBackspace,
                modifier = Modifier.aspectRatio(1.2f)
            )
        }
    }
}

@Composable
fun ExponentsKeyboard(
    onKeyPress: (String) -> Unit,
    onBackspace: () -> Unit
) {
    val exponents = listOf(
        "x²", "x³", "x⁴", "xⁿ",
        "y²", "y³", "y⁴", "yⁿ",
        "z²", "z³", "z⁴", "zⁿ",
        "a²", "a³", "a⁴", "aⁿ"
    )
    
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxHeight()
    ) {
        items(exponents) { exponent ->
            KeyboardKey(
                text = exponent,
                onClick = { onKeyPress(exponent) },
                modifier = Modifier.aspectRatio(1.2f),
                fontSize = 12.sp
            )
        }
        
        item {
            KeyboardKey(
                text = "Space",
                onClick = { onKeyPress(" ") },
                modifier = Modifier.aspectRatio(1.2f),
                fontSize = 10.sp
            )
        }
        
        item {
            KeyboardKey(
                icon = Icons.Default.Backspace,
                onClick = onBackspace,
                modifier = Modifier.aspectRatio(1.2f)
            )
        }
    }
}

@Composable
fun OperatorsKeyboard(
    onKeyPress: (String) -> Unit,
    onBackspace: () -> Unit
) {
    val operators = listOf(
        "+", "-", "×", "÷",
        "(", ")", "^", "√",
        "<", ">", "≤", "≥",
        "≠", "≈", "±", "∞"
    )
    
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxHeight()
    ) {
        items(operators) { operator ->
            KeyboardKey(
                text = operator,
                onClick = { onKeyPress(operator) },
                modifier = Modifier.aspectRatio(1.2f)
            )
        }
        
        item {
            KeyboardKey(
                text = "Space",
                onClick = { onKeyPress(" ") },
                modifier = Modifier.aspectRatio(1.2f),
                fontSize = 10.sp
            )
        }
        
        item {
            KeyboardKey(
                icon = Icons.Default.Backspace,
                onClick = onBackspace,
                modifier = Modifier.aspectRatio(1.2f)
            )
        }
    }
}

@Composable
fun FunctionsKeyboard(
    onKeyPress: (String) -> Unit,
    onBackspace: () -> Unit
) {
    val functions = listOf(
        "sin", "cos", "tan",
        "log", "ln", "exp",
        "abs", "sqrt", "cbrt",
        "sin⁻¹", "cos⁻¹", "tan⁻¹"
    )
    
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxHeight()
    ) {
        items(functions) { function ->
            KeyboardKey(
                text = function,
                onClick = { onKeyPress("$function(") },
                modifier = Modifier.aspectRatio(1.5f),
                fontSize = 11.sp
            )
        }
        
        item {
            KeyboardKey(
                text = "Space",
                onClick = { onKeyPress(" ") },
                modifier = Modifier.aspectRatio(1.5f),
                fontSize = 10.sp
            )
        }
        
        item {
            KeyboardKey(
                icon = Icons.Default.Backspace,
                onClick = onBackspace,
                modifier = Modifier.aspectRatio(1.5f)
            )
        }
    }
}

@Composable
fun SymbolsKeyboard(
    onKeyPress: (String) -> Unit,
    onBackspace: () -> Unit
) {
    val symbols = listOf(
        "π", "e", "i", "θ",
        "α", "β", "γ", "δ",
        "∑", "∏", "∫", "∂",
        "→", "←", "↔", "∴"
    )
    
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxHeight()
    ) {
        items(symbols) { symbol ->
            KeyboardKey(
                text = symbol,
                onClick = { onKeyPress(symbol) },
                modifier = Modifier.aspectRatio(1.2f)
            )
        }
        
        item {
            KeyboardKey(
                text = "Space",
                onClick = { onKeyPress(" ") },
                modifier = Modifier.aspectRatio(1.2f),
                fontSize = 10.sp
            )
        }
        
        item {
            KeyboardKey(
                icon = Icons.Default.Backspace,
                onClick = onBackspace,
                modifier = Modifier.aspectRatio(1.2f)
            )
        }
    }
}

@Composable
fun KeyboardKey(
    text: String? = null,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    fontSize: androidx.compose.ui.unit.TextUnit = 14.sp
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable { onClick() }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        if (text != null) {
            Text(
                text = text,
                fontSize = fontSize,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        } else if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = "Backspace",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(16.dp)
            )
        }
    }
} 