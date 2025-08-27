package com.zahid.mathly.presentation.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.zahid.mathly.presentation.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val profileData = uiState.profileData

    var fullName by remember { mutableStateOf(profileData.fullName) }
    var age by remember { mutableStateOf(profileData.age?.toString() ?: "") }
    var gender by remember { mutableStateOf(profileData.gender) }
    var occupation by remember { mutableStateOf(profileData.occupation) }
    var genderExpanded by remember { mutableStateOf(false) }
    var occupationExpanded by remember { mutableStateOf(false) }
    var pickedImageUri by remember { mutableStateOf<Uri?>(null) }
    LaunchedEffect(true) {
        viewModel.fetchProfile()
    }

    // Refresh state when profile data changes
    LaunchedEffect(profileData) {
        fullName = profileData.fullName
        age = profileData.age?.toString() ?: ""
        gender = profileData.gender
        occupation = profileData.occupation
    }

    val imagePicker =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            pickedImageUri = uri
        }

    LaunchedEffect(uiState.finished) {
        if (uiState.finished) {
            navController.popBackStack()
            viewModel.onNavigationHandled()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = fullName,
                    style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onPrimary),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 20.dp)
                )

                // Avatar hanging bottom
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = 24.dp, y = 50.dp), // half outside container
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar Circle
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(BorderStroke(3.dp, MaterialTheme.colorScheme.primary), CircleShape)
                            .clickable { imagePicker.launch("image/*") }
                    ) {
                        if (pickedImageUri != null) {
                            AsyncImage(
                                model = pickedImageUri,
                                contentDescription = "Profile Photo",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else if (profileData.avatarUrl != null) {
                            AsyncImage(
                                model = profileData.avatarUrl,
                                contentDescription = "Profile Photo",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(40.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Edit Button (separate from avatar)
                    Button(
                        onClick = { imagePicker.launch("image/*") },
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Edit")
                    }
                }
            }


            Spacer(modifier = Modifier.height(60.dp)) // space for avatar hanging

            // Input Fields
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Full name") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it.filter { ch -> ch.isDigit() }.take(3) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = { Icon(Icons.Default.Numbers, contentDescription = null) },
                    label = { Text("Age") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                // Gender dropdown
                ExposedDropdownMenuBox(
                    expanded = genderExpanded,
                    onExpandedChange = { genderExpanded = !genderExpanded }
                ) {
                    OutlinedTextField(
                        value = gender,
                        onValueChange = { },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        readOnly = true,
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        label = { Text("Gender") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) }
                    )
                    ExposedDropdownMenu(
                        expanded = genderExpanded,
                        onDismissRequest = { genderExpanded = false }
                    ) {
                        listOf("Male", "Female", "Other", "Prefer not to say").forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    gender = option
                                    genderExpanded = false
                                }
                            )
                        }
                    }
                }

                // Occupation dropdown
                ExposedDropdownMenuBox(
                    expanded = occupationExpanded,
                    onExpandedChange = { occupationExpanded = !occupationExpanded }
                ) {
                    OutlinedTextField(
                        value = occupation,
                        onValueChange = { },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        readOnly = true,
                        leadingIcon = { Icon(Icons.Default.Work, contentDescription = null) },
                        label = { Text("Occupation") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = occupationExpanded) }
                    )
                    ExposedDropdownMenu(
                        expanded = occupationExpanded,
                        onDismissRequest = { occupationExpanded = false }
                    ) {
                        listOf("Student", "Teacher", "Professional", "Other").forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    occupation = option
                                    occupationExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val localUri = pickedImageUri
                        val ageVal = age.toIntOrNull()
                        if (localUri != null) {
                            val mime = context.contentResolver.getType(localUri) ?: "image/jpeg"
                            val ext = when {
                                mime.endsWith("png") -> "png"
                                mime.endsWith("webp") -> "webp"
                                else -> "jpg"
                            }
                            context.contentResolver.openInputStream(localUri)?.use { stream ->
                                val bytes = stream.readBytes()
                                viewModel.updateProfile(
                                    fullName = fullName.takeIf { it.isNotBlank() },
                                    occupation = occupation,
                                    gender = gender,
                                    age = ageVal,
                                    imageBytes = bytes,
                                    fileExtension = ext
                                )
                            }
                        } else {
                            viewModel.updateProfile(
                                fullName = fullName.takeIf { it.isNotBlank() },
                                occupation = occupation,
                                gender = gender,
                                age = ageVal,
                                imageUrl = profileData.avatarUrl
                            )
                        }
                    },
                    enabled = !uiState.loading && occupation.isNotBlank() && gender.isNotBlank() && age.toIntOrNull() != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    if (uiState.loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Save Changes")
                    }
                }

                AnimatedVisibility(
                    visible = uiState.errorMessage != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        text = uiState.errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }
            }
        }
    }
}
