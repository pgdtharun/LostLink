package com.lostlink.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lostlink.app.ui.components.LostLinkButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRVerificationScreen(
    claimId: String,
    itemTitle: String,
    onQRScanned: (qrCode: String) -> Unit,
    onBackClick: () -> Unit,
    onManualVerification: () -> Unit
) {
    var qrCode by remember { mutableStateOf("") }
    var isScanning by remember { mutableStateOf(false) }
    var verificationStatus by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("Verify Handover") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    itemTitle,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    "Scan the QR code to verify handover",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(Color.Black, RoundedCornerShape(16.dp)),
                    color = Color.Black,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Default.Screenshot,
                                contentDescription = "Scan QR",
                                tint = Color.White,
                                modifier = Modifier.size(80.dp)
                            )
                            Text(
                                "Camera Preview Area",
                                color = Color.White,
                                modifier = Modifier.padding(top = 16.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                "Tap to activate camera",
                                color = Color.White.copy(alpha = 0.6f),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
            
            item {
                Divider()
                Text(
                    "OR",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                Divider()
            }
            
            item {
                TextField(
                    value = qrCode,
                    onValueChange = { qrCode = it },
                    label = { Text("Enter QR Code Manually") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors()
                )
            }
            
            item {
                LostLinkButton(
                    text = "Verify Code",
                    onClick = {
                        if (qrCode.isNotEmpty()) {
                            verificationStatus = "Verifying..."
                            onQRScanned(qrCode)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = qrCode.isNotEmpty()
                )
            }
            
            item {
                LostLinkButton(
                    text = "Manual Verification",
                    onClick = { onManualVerification() },
                    modifier = Modifier.fillMaxWidth(),
                    variant = com.lostlink.app.ui.components.ButtonVariant.Ghost
                )
            }
        }
    }
}

@Composable
fun VerificationSuccessScreen(
    itemTitle: String,
    verificationCode: String,
    timestamp: String,
    onDone: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
                .size(80.dp)
                .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(50.dp)),
            color = MaterialTheme.colorScheme.secondary,
            shape = RoundedCornerShape(50.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    tint = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
        
        Text(
            "Handover Verified!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp)),
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    "Item: $itemTitle",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.W600
                )
                Divider(Modifier.padding(vertical = 12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            "Verification Code",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            verificationCode,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            "Time",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            timestamp,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
        
        Text(
            "The item handover has been successfully verified and logged in the system.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.padding(vertical = 16.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        
        LostLinkButton(
            text = "Done",
            onClick = onDone,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampusMapScreen(
    onItemLocationSelected: (locationName: String) -> Unit,
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    val locations = listOf(
        "Security Office" to Icons.Default.Security,
        "Lost & Found Counter" to Icons.Default.Inbox,
        "Admin Office" to Icons.Default.Settings,
        "Cafeteria" to Icons.Default.Restaurant,
        "Library" to Icons.Default.LibraryBooks,
        "Main Gate" to Icons.Default.MeetingRoom
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("Campus Map") },
            navigationIcon = {
                IconButton(onClick = onMenuClick) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
            },
            actions = {
                IconButton(onClick = { }) {
                    Icon(Icons.Default.LocationOn, contentDescription = "Center on Campus")
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                // Interactive Google Map Placeholder
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = 2.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.primary)
                            Text("NSBM Campus Map Overview", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                            Text("(Interactive Map Disabled for Maintenance)", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
            
            item {
                Text(
                    "Key Locations",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            
            items(locations.size) { index ->
                val (location, icon) = locations[index]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { 
                            onItemLocationSelected(location)
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
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
                                contentDescription = location,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                location,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.W500
                            )
                        }
                        Icon(
                            Icons.Default.ChevronRight,
                            contentDescription = "Navigate",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
