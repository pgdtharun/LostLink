package com.lostlink.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoliciesScreen(
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("App Policies") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                PolicySection(
                    title = "Privacy Policy",
                    content = "At LostLink, we value your privacy. We only collect necessary data such as your name, student/staff ID, and contact details to facilitate the lost and found process. Your information is securely stored and never shared with third parties without your consent."
                )
            }
            
            item {
                PolicySection(
                    title = "Terms of Service",
                    content = "By using LostLink, you agree to provide truthful and accurate information when reporting items. Fake reporting or spamming the platform may result in account suspension. Users are responsible for coordinating the safe handover of items."
                )
            }
            
            item {
                PolicySection(
                    title = "AI Features Data Usage",
                    content = "The AI matching feature uses GitHub Models (Azure AI) to compare item metadata. No personal data beyond the item description and images is sent to the AI processing service. The GitHub Personal Access Token provided by the user is stored locally on the device."
                )
            }
            
            item {
                PolicySection(
                    title = "Verification Protocol",
                    content = "Handover verification via QR code is intended to provide a secure audit trail for both the claimant and the finder. Please ensure the QR code is scanned at the physical time of exchange."
                )
            }
            
            item {
                Spacer(Modifier.height(32.dp))
                Text(
                    "Last updated: March 2026",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
fun PolicySection(title: String, content: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(8.dp))
        Text(
            content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 20.sp
        )
    }
}
