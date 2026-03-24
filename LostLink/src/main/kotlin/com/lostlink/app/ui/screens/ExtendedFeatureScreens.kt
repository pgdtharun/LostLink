package com.lostlink.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lostlink.app.ui.components.ItemCard
import com.lostlink.app.data.model.ItemStatus
import com.lostlink.app.ui.viewmodel.FirebaseViewModel
import com.lostlink.app.ui.viewmodel.AIViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedItemsScreen(
    onItemClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    val savedItems = listOf(
        "1", "2", "4", "5"
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("Saved Items") },
            navigationIcon = {
                IconButton(onClick = onMenuClick) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
        
        if (savedItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.BookmarkBorder,
                        contentDescription = "No saved items",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        "No Saved Items",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Text(
                        "Items you save will appear here",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(savedItems) { itemId ->
                    ItemCard(
                        title = "Item $itemId",
                        description = "Saved item description",
                        location = "Location $itemId",
                        category = "Category",
                        imageUrl = "saved_item_$itemId",
                        status = if (itemId.toInt() % 2 == 0) "Lost" else "Found",
                        postedBy = "User Name",
                        onClick = { onItemClick(itemId) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagingScreen(
    onConversationClick: (conversationId: String) -> Unit,
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    var conversations by remember { mutableStateOf(emptyList<Pair<String, Pair<String, String>>>()) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("Messages") },
            navigationIcon = {
                IconButton(onClick = onMenuClick) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            items(conversations) { (convId, data) ->
                val (personName, lastMessage) = data
                ConversationItem(
                    personName = personName,
                    lastMessage = lastMessage,
                    onClick = { onConversationClick(convId) }
                )
            }
        }
    }
}

@Composable
fun ConversationItem(
    personName: String,
    lastMessage: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.background),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(50.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        personName.first().toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    personName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.W600
                )
                Text(
                    lastMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    maxLines = 1
                )
            }
            
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "Open",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
    HorizontalDivider()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    conversationId: String,
    personName: String,
    itemId: String = "",
    onBackClick: () -> Unit,
    viewModel: FirebaseViewModel = viewModel(),
    aiViewModel: AIViewModel = viewModel()
) {
    var messages by remember { mutableStateOf(emptyList<String>()) }
    var inputText by remember { mutableStateOf("") }
    
    val suggestedReplies by aiViewModel.suggestedReplies.collectAsState()
    val lastSystemResponse by aiViewModel.lastSystemResponse.collectAsState()

    LaunchedEffect(itemId) {
        if (itemId.isNotEmpty() && itemId != "none") {
            aiViewModel.fetchSuggestedReplies(itemId)
        }
    }

    LaunchedEffect(lastSystemResponse) {
        lastSystemResponse?.let {
            messages = messages + "System: $it"
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text(personName) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
            )
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    "This is an automated system for item verification",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { message ->
                val isOwn = message.startsWith("You:")
                val isSystem = message.startsWith("System:")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    contentAlignment = when {
                        isOwn -> Alignment.CenterEnd
                        isSystem -> Alignment.Center
                        else -> Alignment.CenterStart
                    }
                ) {
                    Surface(
                        modifier = Modifier
                            .widthIn(max = 300.dp),
                        color = when {
                            isOwn -> MaterialTheme.colorScheme.primary 
                            isSystem -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                            else -> MaterialTheme.colorScheme.surface
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            message.removePrefix("You: ").removePrefix("System: "),
                            modifier = Modifier.padding(12.dp),
                            color = when {
                                isOwn -> MaterialTheme.colorScheme.onPrimary 
                                isSystem -> MaterialTheme.colorScheme.onSecondaryContainer
                                else -> MaterialTheme.colorScheme.onSurface
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isSystem) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
        
        HorizontalDivider()
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Type a message...") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (inputText.isNotBlank()) {
                                    messages = messages + "You: $inputText"
                                    aiViewModel.generateSystemResponse(inputText, itemId)
                                    inputText = ""
                                }
                            },
                            enabled = inputText.isNotBlank()
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                )
            }

            Text(
                "Quick Actions (AI Suggestions)",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val currentSuggestions = if (suggestedReplies.isNotEmpty()) suggestedReplies else listOf("I found it", "Where to meet?", "Is this yours?")
                currentSuggestions.forEach { suggestion ->
                    SuggestionChip(
                        onClick = {
                            messages = messages + ("You: $suggestion")
                            // Automated response using AI
                            aiViewModel.generateSystemResponse(suggestion, itemId)
                            
                            // If user says something indicating they found it, update item status to CLAIMED
                            if (itemId.isNotEmpty() && itemId != "none" && (suggestion.contains("found", ignoreCase = true) || suggestion.contains("mine", ignoreCase = true))) {
                                viewModel.updateItemStatus(itemId, ItemStatus.CLAIMED)
                            }
                        },
                        label = { Text(suggestion, maxLines = 1) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onBackClick: () -> Unit,
    onViewClaimsClick: () -> Unit,
    onViewUsersClick: () -> Unit,
    onViewReportsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("Admin Dashboard") },
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
                Text(
                    "Statistics",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard("125", "Total Items", MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                    StatCard("23", "Pending Claims", MaterialTheme.colorScheme.secondary, Modifier.weight(1f))
                }
            }
            
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard("89", "Verified", MaterialTheme.colorScheme.tertiary, Modifier.weight(1f))
                    StatCard("1,245", "Users", MaterialTheme.colorScheme.outline, Modifier.weight(1f))
                }
            }
            
            item {
                Divider()
            }
            
            item {
                Text(
                    "Management",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            item {
                AdminActionButton(
                    icon = Icons.Default.CheckCircle,
                    label = "Review Claims",
                    subtitle = "Pending approvals",
                    onClick = onViewClaimsClick
                )
            }
            
            item {
                AdminActionButton(
                    icon = Icons.Default.People,
                    label = "Manage Users",
                    subtitle = "Verify, suspend users",
                    onClick = onViewUsersClick
                )
            }
            
            item {
                AdminActionButton(
                    icon = Icons.Default.AssessmentIcon,
                    label = "View Reports",
                    subtitle = "Statistics & logs",
                    onClick = onViewReportsClick
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatCard(value: String, label: String, backgroundColor: androidx.compose.ui.graphics.Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .background(backgroundColor.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
        color = backgroundColor.copy(alpha = 0.1f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = backgroundColor
            )
            Text(
                label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun AdminActionButton(
    icon: ImageVector,
    label: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    icon,
                    contentDescription = label,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
                Column {
                    Text(
                        label,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.W600
                    )
                    Text(
                        subtitle,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// Placeholder for AssessmentIcon
val Icons.Filled.AssessmentIcon: ImageVector
    get() = Icons.Default.BarChart
