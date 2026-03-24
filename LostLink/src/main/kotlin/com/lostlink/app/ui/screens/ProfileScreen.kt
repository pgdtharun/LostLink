package com.lostlink.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lostlink.app.data.model.User
import com.lostlink.app.ui.components.LostLinkButton
import com.lostlink.app.ui.viewmodel.FirebaseViewModel
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLogoutClick: () -> Unit,
    onPoliciesClick: () -> Unit,
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit,
    viewModel: FirebaseViewModel = viewModel()
) {
    val userProfile by viewModel.userProfile.collectAsState()
    var isEditing by remember { mutableStateOf(false) }
    
    var name by remember(userProfile) { mutableStateOf(userProfile?.name ?: "") }
    var phone by remember(userProfile) { mutableStateOf(userProfile?.phone ?: "") }
    var idNumber by remember(userProfile) { mutableStateOf(userProfile?.idNumber ?: "") }

    LaunchedEffect(Unit) {
        viewModel.fetchUserProfile()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("Profile") },
            navigationIcon = {
                IconButton(onClick = onMenuClick) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
            },
            actions = {
                if (userProfile != null) {
                    IconButton(onClick = { 
                        if (isEditing) {
                            val updatedUser = userProfile!!.copy(
                                name = name,
                                phone = phone,
                                idNumber = idNumber
                            )
                            viewModel.saveUserProfile(updatedUser)
                        }
                        isEditing = !isEditing 
                    }) {
                        Icon(
                            if (isEditing) Icons.Default.Check else Icons.Default.Edit,
                            contentDescription = if (isEditing) "Save" else "Edit"
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
        
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                (userProfile?.name ?: "U").first().toString(),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                    
                    Column(modifier = Modifier.weight(1f)) {
                        if (isEditing) {
                            TextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("Name") },
                                colors = TextFieldDefaults.colors()
                            )
                        } else {
                            Text(
                                userProfile?.name ?: "User Name",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                userProfile?.email ?: "email@campus.edu",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
            
            item { HorizontalDivider() }
            
            item {
                Text(
                    "Account Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.W600
                )
                
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProfileInfoRow("Email", userProfile?.email ?: "N/A")
                    
                    if (isEditing) {
                        TextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Phone") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors()
                        )
                        Spacer(Modifier.height(8.dp))
                        TextField(
                            value = idNumber,
                            onValueChange = { idNumber = it },
                            label = { Text("Student/Staff ID") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors()
                        )
                    } else {
                        ProfileInfoRow("Phone", userProfile?.phone ?: "N/A")
                        ProfileInfoRow("ID Number", userProfile?.idNumber ?: "N/A")
                        ProfileInfoRow("Member Since", "March 2024")
                    }
                }
            }
            
            item { HorizontalDivider() }
            
            item {
                Text(
                    "My Activity",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.W600
                )
                
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ActivityRow("Items Lost", "0", Icons.Default.Warning)
                    ActivityRow("Items Found", "0", Icons.Default.CheckCircle)
                    ActivityRow("Messages", "0", Icons.Default.Mail)
                }
            }
            
            item { HorizontalDivider() }
            
            item {
                MenuButton("App Policies", Icons.Default.Policy, onClick = onPoliciesClick)
                MenuButton("Help & Support", Icons.Default.Info, onClick = {})
                MenuButton("About", Icons.Default.Info, onClick = {})
            }
            
            item {
                LostLinkButton(
                    text = "Logout",
                    onClick = onLogoutClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    variant = com.lostlink.app.ui.components.ButtonVariant.Ghost
                )
            }
        }
    }
}

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Column {
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.W500
        )
    }
}

@Composable
fun ActivityRow(label: String, count: String, icon: ImageVector) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Text(label, style = MaterialTheme.typography.bodyMedium)
        }
        Text(
            count,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun MenuButton(text: String, icon: ImageVector, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    icon,
                    contentDescription = text,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}
