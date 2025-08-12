package com.zahid.mathly.presentation.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zahid.mathly.presentation.ui.theme.PlayfairDisplay

@Composable
fun NavigationDrawer(drawerState: DrawerState,selectedScreen:String,onSelected: (String)-> Unit){
    ModalDrawerSheet {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Mathly",
            fontSize = 24.sp,
            fontWeight = FontWeight.W700,
            fontFamily = PlayfairDisplay,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Calculate, contentDescription = "Equations") },
            label = { Text("Equations") },
            selected = selectedScreen == "Equations",
            onClick = {
                onSelected("Equations")

            },
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        NavigationDrawerItem(
            icon = { Icon(Icons.Default.TextFields, contentDescription = "Word Problems") },
            label = { Text("Word Problems") },
            selected = selectedScreen == "WordProblems",
            onClick = {
                onSelected("WordProblems")
            },
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        NavigationDrawerItem(
            icon = { Icon(Icons.AutoMirrored.Filled.ShowChart, contentDescription = "Graphs") },
            label = { Text("Graphs") },
            selected = selectedScreen == "Graphs",
            onClick = {
                onSelected("Graphs")
            },
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Calculate, contentDescription = "Basic Calculator") },
            label = { Text("Basic Calculator") },
            selected = selectedScreen == "BasicCalculator",
            onClick = {
                onSelected("BasicCalculator")
            },
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Restaurant, contentDescription = "Calories Counter") },
            label = { Text("Calories Counter") },
            selected = selectedScreen == "CaloriesCounter",
            onClick = {
                onSelected("CaloriesCounter")
            },
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        NavigationDrawerItem(
            icon = { Icon(Icons.Default.MonitorWeight, contentDescription = "BMI Calculator") },
            label = { Text("BMI Calculator") },
            selected = selectedScreen == "BMICalculator",
            onClick = {
                onSelected("BMICalculator")
            },
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = selectedScreen == "Profile",
            onClick = {
                onSelected("Profile")
            },
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}