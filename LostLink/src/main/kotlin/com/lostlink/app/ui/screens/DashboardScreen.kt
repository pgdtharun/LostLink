package com.lostlink.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.lostlink.app.ui.theme.LostLinkGradient
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lostlink.app.data.model.LostItem
import com.lostlink.app.data.model.FoundItem
import com.lostlink.app.ui.components.ItemCard
import com.lostlink.app.ui.components.LostLinkButton
import com.lostlink.app.ui.components.ButtonVariant

import androidx.lifecycle.viewmodel.compose.viewModel
import com.lostlink.app.ui.viewmodel.FirebaseViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DashboardScreen(
    onItemClick: (String) -> Unit,
    onReportClick: () -> Unit,
    onProfileClick: () -> Unit,
    onMenuClick: () -> Unit,
    viewModel: FirebaseViewModel = viewModel()
) {
    val items by viewModel.items.collectAsState()
    val isLoadingData by viewModel.isLoading.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { 
        isVisible = true 
        viewModel.fetchItems()
        viewModel.fetchUserProfile()
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(600)) + slideInVertically(initialOffsetY = { 50 }),
        exit = fadeOut()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Header
            Box(modifier = Modifier.background(LostLinkGradient)) {
                TopAppBar(
                    title = {
                        Text(
                            "LostLink",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onMenuClick) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                        }
                    },
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(
                                Icons.Filled.Notifications, 
                                contentDescription = "Notifications",
                                tint = Color.White
                            )
                        }
                        Button(
                            onClick = onReportClick,
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(36.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White.copy(alpha = 0.2f)
                            )
                        ) {
                            Icon(
                                Icons.Filled.Add, 
                                contentDescription = "Report Item",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
            
            // Search
            SearchField(
                searchQuery,
                onQueryChange = { searchQuery = it },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            
            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("All Items") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Lost") }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Found") }
                )
            }
            
            // Item List
            if (isLoadingData && items.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else if (items.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Search, 
                            contentDescription = null, 
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "No items found",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Text(
                            "Try reporting a lost item to get started!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
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
                    val filteredItems = items.filter { item ->
                        val matchesQuery = when (item) {
                            is LostItem -> item.title.contains(searchQuery, ignoreCase = true)
                            is FoundItem -> item.title.contains(searchQuery, ignoreCase = true)
                            else -> false
                        }
                        
                        val matchesTab = when (selectedTab) {
                            0 -> true
                            1 -> item is LostItem
                            2 -> item is FoundItem
                            else -> true
                        }
                        
                        matchesQuery && matchesTab
                    }
                    
                    items(filteredItems) { item ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(animationSpec = tween(500)) + 
                                    expandVertically(animationSpec = tween(500)) +
                                    slideInVertically(initialOffsetY = { it / 2 }),
                            modifier = Modifier.animateItemPlacement()
                        ) {
                            when (item) {
                                is LostItem -> ItemCard(
                                    title = item.title,
                                    description = item.description,
                                    location = item.location,
                                    category = item.category.name,
                                    imageUrl = item.images.firstOrNull() ?: "",
                                    status = item.status.name,
                                    postedBy = item.reporterName,
                                    onClick = { onItemClick(item.id) }
                                )
                                is FoundItem -> ItemCard(
                                    title = item.title,
                                    description = item.description,
                                    location = item.location,
                                    category = item.category.name,
                                    imageUrl = item.images.firstOrNull() ?: "",
                                    status = item.status.name,
                                    postedBy = item.finderName,
                                    onClick = { onItemClick(item.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
    value: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text("Search lost items...") },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary
        )
    )
}
