package com.lostlink.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lostlink.app.data.model.FoundItem
import com.lostlink.app.data.model.LostItem
import com.lostlink.app.ui.components.ItemCard
import com.lostlink.app.ui.components.LostLinkButton
import com.lostlink.app.ui.viewmodel.AIViewModel
import com.lostlink.app.ui.viewmodel.FirebaseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIMatchScreen(
    itemId: String,
    onBackClick: () -> Unit,
    onMatchClick: (String) -> Unit,
    aiViewModel: AIViewModel = viewModel(),
    firebaseViewModel: FirebaseViewModel = viewModel()
) {
    val githubToken by aiViewModel.githubToken.collectAsState()
    val isMatching by aiViewModel.isMatching.collectAsState()
    val matchSuggestions by aiViewModel.matchSuggestions.collectAsState()
    val error by aiViewModel.error.collectAsState()
    
    val selectedItem by firebaseViewModel.selectedItem.collectAsState()
    
    var tokenInput by remember { mutableStateOf("") }
    
    LaunchedEffect(itemId) {
        firebaseViewModel.fetchItemById(itemId)
    }
    
    LaunchedEffect(githubToken, selectedItem) {
        if (githubToken.isNotEmpty() && selectedItem != null) {
            aiViewModel.findMatches(selectedItem!!)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("AI Match Suggestions") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
        
        if (githubToken.isEmpty()) {
            TokenEntryView(
                tokenInput = tokenInput,
                onTokenChange = { tokenInput = it },
                onSaveToken = { aiViewModel.saveToken(tokenInput) }
            )
        } else if (isMatching) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(16.dp))
                    Text("AI is analyzing matches...", style = MaterialTheme.typography.bodyMedium)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    // GitHub Models Notification
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier.size(32.dp),
                                color = Color.Black,
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("G", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(
                                    "AI Powered by GitHub Models",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "Matches found using GPT-4o analytics",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }

                if (error != null) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                        ) {
                            Text(
                                error!!,
                                modifier = Modifier.padding(16.dp),
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
                
                if (matchSuggestions.isEmpty() && !isMatching) {
                    item {
                        Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            Text("No AI matches found yet for this item.")
                        }
                    }
                }
                
                items(matchSuggestions) { suggestion ->
                    val otherItemId = if (selectedItem is LostItem) suggestion.foundItemId else suggestion.lostItemId
                    MatchSuggestionCard(
                        score = suggestion.matchScore,
                        reason = suggestion.matchReason,
                        onClick = { onMatchClick(otherItemId) }
                    )
                }
            }
        }
    }
}

@Composable
fun TokenEntryView(
    tokenInput: String,
    onTokenChange: (String) -> Unit,
    onSaveToken: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.AutoAwesome,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "GitHub Token Required",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            "To use AI matching features, please provide a GitHub Personal Access Token. This token is used to call GitHub Models (Azure AI).",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        
        OutlinedTextField(
            value = tokenInput,
            onValueChange = onTokenChange,
            label = { Text("GitHub Token") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        Spacer(Modifier.height(24.dp))
        
        LostLinkButton(
            text = "Enable AI Features",
            onClick = onSaveToken,
            modifier = Modifier.fillMaxWidth(),
            enabled = tokenInput.isNotEmpty()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchSuggestionCard(
    score: Float,
    reason: String,
    onClick: () -> Unit
) {
    val scorePercentage = (score * 100).toInt()
    val scoreColor = when {
        score >= 0.8f -> Color(0xFF4CAF50)
        score >= 0.5f -> Color(0xFFFF9800)
        else -> Color(0xFFF44336)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = scoreColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "AI Match Score",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Surface(
                    color = scoreColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        "$scorePercentage%",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        color = scoreColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(Modifier.height(12.dp))
            
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                Text(
                    reason,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
            
            Spacer(Modifier.height(16.dp))
            
            Text(
                "Click to view matching item details",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}
