package com.lostlink.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lostlink.app.data.model.FoundItem
import com.lostlink.app.data.model.LostItem
import com.lostlink.app.ui.components.ItemCard
import com.lostlink.app.ui.components.LostLinkButton
import com.lostlink.app.ui.components.GitHubIcon
import com.lostlink.app.ui.viewmodel.AIViewModel
import com.lostlink.app.ui.viewmodel.FirebaseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIChatScreen(
    onItemClick: (String) -> Unit,
    onMenuClick: () -> Unit,
    aiViewModel: AIViewModel = viewModel()
) {
    val githubToken by aiViewModel.githubToken.collectAsState()
    val isMatching by aiViewModel.isMatching.collectAsState()
    val matchSuggestions by aiViewModel.matchSuggestions.collectAsState()
    val error by aiViewModel.error.collectAsState()
    
    var tokenInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("AI Assistant")
                }
            },
            navigationIcon = {
                IconButton(onClick = onMenuClick) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
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
        } else {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                // GitHub Models Notification
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // GitHub Icon representation
                        Icon(
                            imageVector = GitHubIcon,
                            contentDescription = "GitHub",
                            modifier = Modifier.size(32.dp),
                            tint = Color.Black
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                "Powered by GitHub Models",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Using GPT-4o via Azure AI Inference",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }
                }

                if (isMatching) {
                    Box(Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (error != null) {
                            item {
                                Text(error!!, color = MaterialTheme.colorScheme.error)
                            }
                        }
                        
                        item {
                            Text(
                                "AI Smart Recommendations",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        
                        if (matchSuggestions.isEmpty() && !isMatching) {
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                ) {
                                    Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                        Spacer(Modifier.height(8.dp))
                                        Text(
                                            "No active recommendations at the moment. The AI will automatically suggest matches once items are reported.",
                                            style = MaterialTheme.typography.bodySmall,
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                        
                        items(matchSuggestions) { suggestion ->
                            val itemId = if (suggestion.lostItemId == "query") suggestion.foundItemId else suggestion.lostItemId
                            MatchSuggestionCard(
                                score = suggestion.matchScore,
                                reason = suggestion.matchReason,
                                onClick = { onItemClick(itemId) }
                            )
                        }
                    }
                }
            }
        }
    }
}
