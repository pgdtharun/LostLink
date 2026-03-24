package com.lostlink.app.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lostlink.app.ui.components.LostLinkButton
import com.lostlink.app.ui.components.ButtonVariant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClaimSubmissionScreen(
    itemId: String,
    itemTitle: String,
    onSubmitClaim: (proofType: String, proofContent: String) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedProofType by remember { mutableStateOf("TEXT") }
    var proofContent by remember { mutableStateOf("") }
    var additionalInfo by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("Claim Item") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(12.dp)),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "Claiming Item",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            itemTitle,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
            
            item {
                Text(
                    "Proof of Ownership",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Please provide proof that this item belongs to you",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            
            item {
                Text(
                    "Proof Type",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.W600
                )
                
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(
                        "TEXT" to "Description",
                        "IMAGE" to "Photo/Screenshot",
                        "SERIAL" to "Serial Number",
                        "ID" to "ID/Receipt Number"
                    ).forEach { (type, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (selectedProofType == type)
                                        MaterialTheme.colorScheme.primaryContainer
                                    else
                                        MaterialTheme.colorScheme.surface,
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedProofType == type,
                                onClick = { selectedProofType = type }
                            )
                            Text(
                                label,
                                modifier = Modifier.padding(start = 12.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
            
            item {
                TextField(
                    value = proofContent,
                    onValueChange = { proofContent = it },
                    label = { Text("Provide your proof") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 5,
                    colors = TextFieldDefaults.colors()
                )
            }
            
            item {
                TextField(
                    value = additionalInfo,
                    onValueChange = { additionalInfo = it },
                    label = { Text("Additional Information (Optional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    maxLines = 4,
                    colors = TextFieldDefaults.colors()
                )
            }
            
            item {
                LostLinkButton(
                    text = "Submit Claim",
                    onClick = {
                        if (proofContent.isNotEmpty()) {
                            isSubmitting = true
                            onSubmitClaim(selectedProofType, proofContent)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isLoading = isSubmitting,
                    enabled = proofContent.isNotEmpty()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClaimStatusScreen(
    claimId: String,
    itemTitle: String,
    status: String,
    submittedDate: String,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("Claim Status") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            itemTitle,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Claim ID: $claimId",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
            
            item {
                StatusTimeline(status, submittedDate)
            }
            
            when (status) {
                "PENDING" -> {
                    item {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.tertiaryContainer, RoundedCornerShape(12.dp)),
                            color = MaterialTheme.colorScheme.tertiaryContainer
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Icon(
                                    Icons.Default.Schedule,
                                    contentDescription = "Pending",
                                    tint = MaterialTheme.colorScheme.tertiary,
                                    modifier = Modifier.size(32.dp)
                                )
                                Text(
                                    "Under Review",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                                Text(
                                    "Your claim is being reviewed by administrators. You'll receive a notification once a decision is made.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
                "APPROVED" -> {
                    item {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(12.dp)),
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = "Approved",
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(32.dp)
                                )
                                Text(
                                    "Approved!",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                                Text(
                                    "Your claim has been approved. Contact the poster to arrange hand over.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
                "REJECTED" -> {
                    item {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.errorContainer, RoundedCornerShape(12.dp)),
                            color = MaterialTheme.colorScheme.errorContainer
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Icon(
                                    Icons.Default.Cancel,
                                    contentDescription = "Rejected",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(32.dp)
                                )
                                Text(
                                    "Rejected",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                                Text(
                                    "Your claim could not be verified. Contact support if you have questions.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusTimeline(status: String, submittedDate: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(
            "PENDING" to "Submitted",
            "APPROVED" to "Approved",
            "VERIFIED_HANDOVER" to "Verified"
        ).forEachIndexed { index, (stepStatus, label) ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(32.dp),
                    color = if (stepStatus == status || 
                        (status == "APPROVED" && index <= 1) ||
                        (status == "VERIFIED_HANDOVER" && index <= 2)
                    ) MaterialTheme.colorScheme.primary 
                    else MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = label,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                
                Text(
                    label,
                    modifier = Modifier.padding(start = 12.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (stepStatus == status) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}
