package com.zahid.mathly.presentation.ui.screens.profile

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.zahid.mathly.R
import com.zahid.mathly.domain.model.HomeMenuItem
import com.zahid.mathly.presentation.ui.components.ProfileAvatar
import com.zahid.mathly.presentation.ui.theme.PlayfairDisplay
import com.zahid.mathly.presentation.viewmodel.profile.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    profileViewModel.fetchProfile()
    val profileData by profileViewModel.uiState.collectAsStateWithLifecycle()

    // Define menu items with vibrant gradient colors for visual appeal
    val menuItems = listOf(
        HomeMenuItem(
            title = stringResource(R.string.equations),
            icon = Icons.Default.GraphicEq,
            route = "equations_main",
            startColor = Color(0xFF1B4C5B),  // Dark teal
            endColor = Color(0xFF0BD3C7),    // Bright cyan
            contentColor = Color.White,
        ),
        HomeMenuItem(
            title = stringResource(R.string.word_problems),
            icon = Icons.Default.TextFields,
            route = "word_problem_main",
            startColor = Color(0xFF51B659),  // Green
            endColor = Color(0xFF308E5B),    // Dark green
            contentColor = Color.White,
        ),
        HomeMenuItem(
            title = stringResource(R.string.graphs),
            icon = Icons.Default.AutoGraph,
            route = "graph_main",
            startColor = Color(0xFF0BD3C7),  // Bright cyan
            endColor = Color(0xFF229793),    // Teal
            contentColor = Color.White,
        ),
        HomeMenuItem(
            title = stringResource(R.string.basic_calculator),
            icon = Icons.Default.Calculate,
            route = "calculator_main",
            startColor = Color(0xFF466465),  // Gray teal
            endColor = Color(0xFF1B4C5B),    // Dark teal
            contentColor = Color.White,
        ),
        HomeMenuItem(
            title = stringResource(R.string.calories_counter),
            icon = Icons.Default.HealthAndSafety,
            route = "calories_main",
            startColor = Color(0xFF678668),  // Sage green
            endColor = Color(0xFF51B659),    // Green
            contentColor = Color.White,
        ),
        HomeMenuItem(
            title = stringResource(R.string.bmi_calculator),
            icon = Icons.Default.MonitorWeight,
            route = "bmi_main",
            startColor = Color(0xFF229793),  // Teal
            endColor = Color(0xFF0BD3C7),    // Bright cyan
            contentColor = Color.White,
        )
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.W700,
                        fontFamily = PlayfairDisplay,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                actions = {
                    // Profile button
                    IconButton(
                        onClick = {
                            navController.navigate("profile_main")
                        }
                    ) {
                        ProfileAvatar(profileData.profileData.avatarUrl)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // App Logo with enhanced design
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(bottom = 16.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(24.dp),
                            spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        ),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF1B4C5B),  // Dark teal
                                        Color(0xFF0BD3C7)   // Bright cyan
                                    )
                                )
                            )
                    ) {
                        // Decorative circles in background
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .offset((-20).dp, (-20).dp)
                                .background(Color.White.copy(alpha = 0.1f), CircleShape)
                        )
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .align(Alignment.BottomEnd)
                                .offset(20.dp, 20.dp)
                                .background(Color.White.copy(alpha = 0.1f), CircleShape)
                        )

                        // Logo and text
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.app_logo),
                                contentDescription = "App Logo",
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .shadow(8.dp, RoundedCornerShape(16.dp))
                            )

                            Spacer(modifier = Modifier.width(20.dp))

                            Column {
                                Text(
                                    text = stringResource(R.string.app_name),
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = stringResource(R.string.smart_solutions_powered_by_ai),
                                    fontSize = 14.sp,
                                    color = Color.White.copy(alpha = 0.9f)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Small pill-shaped button
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .background(Color.White.copy(alpha = 0.2f))
                                        .padding(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "AI Powered",
                                        fontSize = 12.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(menuItems) { item ->
                        HomeMenuTile(
                            item = item,
                            onClick = {
                                navController.navigate(item.route)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeMenuTile(
    item: HomeMenuItem,
    onClick: () -> Unit
) {
    // Use hover effect instead of press
    var isHovered by remember { mutableStateOf(false) }

    // Animation for hover effect
    val scale by animateFloatAsState(
        targetValue = if (isHovered) 1.03f else 1f,
        label = "scale"
    )

    // Toggle hover on click for demo effect
    LaunchedEffect(Unit) {
        isHovered = false
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .scale(scale)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null // Remove default ripple
            ) {
                isHovered = true
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(item.startColor, item.endColor)
                    )
                )
                .padding(16.dp)
        ) {
            // Circular icon background at top
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .size(56.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.2f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    modifier = Modifier.size(32.dp),
                    tint = item.contentColor
                )
            }

            // Title and description
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = item.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = item.contentColor,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

