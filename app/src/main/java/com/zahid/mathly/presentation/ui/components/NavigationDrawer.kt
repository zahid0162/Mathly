package com.zahid.mathly.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuOpen
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zahid.mathly.R
import com.zahid.mathly.presentation.ui.theme.PlayfairDisplay
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawer(drawerState: DrawerState,selectedScreen:String,onSelected: (String)-> Unit){
    val context = LocalContext.current
    ModalDrawerSheet {
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.Start) {
            IconButton(
                onClick = {
                    onSelected(selectedScreen)
                }
            ) {
                Icon(Icons.AutoMirrored.Filled.MenuOpen, contentDescription = "Menu", tint = MaterialTheme.colorScheme.onSurface)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(R.string.calcsmart),
                fontSize = 24.sp,
                fontWeight = FontWeight.W700,
                fontFamily = PlayfairDisplay,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Calculate, contentDescription = "Equations") },
            label = { Text(stringResource(R.string.equations)) },
            selected = selectedScreen == context.getString(R.string.equations),
            onClick = {
                onSelected( context.getString(R.string.equations))

            },
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        NavigationDrawerItem(
            icon = { Icon(Icons.Default.TextFields, contentDescription = "Word Problems") },
            label = { Text(stringResource(R.string.word_problems)) },
            selected = selectedScreen == context.getString(R.string.word_problems),
            onClick = {
                onSelected(context.getString(R.string.word_problems))
            },
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        NavigationDrawerItem(
            icon = { Icon(Icons.AutoMirrored.Filled.ShowChart, contentDescription = "Graphs") },
            label = { Text(stringResource(R.string.graphs)) },
            selected = selectedScreen == context.getString(R.string.graphs),
            onClick = {
                onSelected(context.getString(R.string.graphs))
            },
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Calculate, contentDescription = "Basic Calculator") },
            label = { Text(stringResource(R.string.basic_calculator)) },
            selected = selectedScreen == context.getString(R.string.basic_calculator),
            onClick = {
                onSelected(context.getString(R.string.basic_calculator))
            },
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Restaurant, contentDescription = "Calories Counter") },
            label = { Text(stringResource(R.string.calories_counter)) },
            selected = selectedScreen == context.getString(R.string.calories_counter),
            onClick = {
                onSelected(context.getString(R.string.calories_counter))
            },
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        NavigationDrawerItem(
            icon = { Icon(Icons.Default.MonitorWeight, contentDescription = "BMI Calculator") },
            label = { Text(stringResource(R.string.bmi_calculator)) },
            selected = selectedScreen == context.getString(R.string.bmi_calculator),
            onClick = {
                onSelected(context.getString(R.string.bmi_calculator))
            },
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text(stringResource(R.string.profile)) },
            selected = selectedScreen == context.getString(R.string.profile),
            onClick = {
                onSelected(context.getString(R.string.profile))
            },
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}