package com.zahid.mathly.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
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
    NumbersKeyboard(onKeyPress, onBackspace)
}

@Composable
fun NumbersKeyboard(
    onKeyPress: (String) -> Unit,
    onBackspace: () -> Unit
) {
    val numbers = listOf(
        "9", "8", "7",
        "6", "5", "4",
        "3", "2", "1",
        "0", ".", "=",
        "x", "y", "z", "a",
        "b", "c", "d", "e",
        "f", "g", "h", "i",
        "j", "k", "l", "m",
        "x²", "x³", "x⁴", "xⁿ",
        "y²", "y³", "y⁴", "yⁿ",
        "z²", "z³", "z⁴", "zⁿ",
        "a²", "a³", "a⁴", "aⁿ",
        "+", "-", "*", "/",
        "(", ")", "^", "√",
        "<", ">", "≤", "≥",
        "≠", "≈", "±", "∞",
        "sin", "cos", "tan",
        "log", "ln", "exp",
        "abs", "sqrt", "cbrt",
        "sin⁻¹", "cos⁻¹", "tan⁻¹",
        "π", "e", "i", "θ",
        "α", "β", "γ", "δ",
        "∑", "∏", "∫", "∂",
        "→", "←", "↔", "∴"
    )
    
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 40.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp),
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
                icon = Icons.AutoMirrored.Filled.Backspace,
                onClick = onBackspace,
                modifier = Modifier.aspectRatio(1.2f)
            )
        }
    }
}

@Composable
fun KeyboardKey(
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    onClick: () -> Unit,
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
            .padding(2.dp),
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