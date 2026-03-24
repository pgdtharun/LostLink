package com.lostlink.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lostlink.app.ui.components.LostLinkButton
import com.lostlink.app.ui.components.ButtonVariant
import com.lostlink.app.ui.components.getIconForCategory
import com.lostlink.app.data.model.ItemCategory
import com.lostlink.app.data.model.ItemStatus
import com.lostlink.app.data.model.LostItem
import com.lostlink.app.data.model.FoundItem
import com.lostlink.app.ui.viewmodel.FirebaseViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ReportScreen(
    onBackClick: () -> Unit,
    onSubmit: () -> Unit,
    viewModel: FirebaseViewModel = viewModel()
) {
    var itemTitle by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(ItemCategory.OTHER) }
    var contactInfo by remember { mutableStateOf("") }
    var isLost by remember { mutableStateOf(true) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("Report Item") },
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
                    "What would you like to report?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    LostLinkButton(
                        text = "Lost Item",
                        onClick = { isLost = true },
                        modifier = Modifier.weight(1f),
                        variant = if (isLost) ButtonVariant.Primary else ButtonVariant.Ghost
                    )
                    LostLinkButton(
                        text = "Found Item",
                        onClick = { isLost = false },
                        modifier = Modifier.weight(1f),
                        variant = if (!isLost) ButtonVariant.Primary else ButtonVariant.Ghost
                    )
                }
            }
            
            item {
                TextField(
                    value = itemTitle,
                    onValueChange = { itemTitle = it },
                    label = { Text("Item Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors()
                )
            }
            
            item {
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 4,
                    colors = TextFieldDefaults.colors()
                )
            }
            
            item {
                TextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors()
                )
            }
            
            item {
                Text("Category", style = MaterialTheme.typography.labelMedium)
                FlowRow(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ItemCategory.entries.forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat.name.replace("_", " ")) },
                        )
                    }
                }
            }
            
            item {
                TextField(
                    value = contactInfo,
                    onValueChange = { contactInfo = it },
                    label = { Text("Contact Information") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors()
                )
            }
            
            item {
                LostLinkButton(
                    text = "Submit Report",
                    onClick = {
                        if (isLost) {
                            viewModel.reportLostItem(itemTitle, description, location, category, contactInfo)
                        } else {
                            viewModel.reportFoundItem(itemTitle, description, location, category, contactInfo)
                        }
                        onSubmit()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    enabled = itemTitle.isNotEmpty() && description.isNotEmpty()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(
    itemId: String,
    onBackClick: () -> Unit,
    onContactClick: () -> Unit,
    onFindMatchesClick: (String) -> Unit,
    viewModel: FirebaseViewModel = viewModel()
) {
    val selectedItem by viewModel.selectedItem.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(itemId) {
        viewModel.fetchItemById(itemId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("Item Details") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
        
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (selectedItem == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Item not found")
            }
        } else {
            val title = when (val it = selectedItem) {
                is LostItem -> it.title
                is FoundItem -> it.title
                else -> ""
            }
            val description = when (val it = selectedItem) {
                is LostItem -> it.description
                is FoundItem -> it.description
                else -> ""
            }
            val location = when (val it = selectedItem) {
                is LostItem -> it.location
                is FoundItem -> it.location
                else -> ""
            }
            val category = when (val it = selectedItem) {
                is LostItem -> it.category
                is FoundItem -> it.category
                else -> ItemCategory.OTHER
            }
            val status = when (val it = selectedItem) {
                is LostItem -> it.status.name
                is FoundItem -> it.status.name
                else -> ""
            }
            val reportedBy = when (val it = selectedItem) {
                is LostItem -> it.reporterName
                is FoundItem -> it.finderName
                else -> ""
            }
            val date = when (val it = selectedItem) {
                is LostItem -> it.dateReported.toString()
                is FoundItem -> it.dateFound.toString()
                else -> ""
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            getIconForCategory(category),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }
                
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Surface(
                            modifier = Modifier.background(
                                if (status == "ACTIVE") MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                                else MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            ),
                            color = Color.Transparent
                        ) {
                            Text(
                                status,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                color = if (status == "ACTIVE") MaterialTheme.colorScheme.error 
                                        else MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
                
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        DetailRow("Location", location)
                        DetailRow("Category", category.name)
                        DetailRow("Reported", date)
                    }
                }
                
                item {
                    Column {
                        Text(
                            "Description",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.W600
                        )
                        Text(
                            description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
                
                item { Divider() }

                item {
                    Text(
                        "Posted by $reportedBy",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                
                item {
                    val item = selectedItem
                    if (item != null) {
                        val isFound = item is FoundItem
                        val primaryText = if (isFound) "This is Mine" else "I Found This"
                        val secondaryText = if (isFound) "Contact Finder" else "Contact Reporter"
                        
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            LostLinkButton(
                                text = "Find AI Matches",
                                onClick = { onFindMatchesClick(itemId) },
                                modifier = Modifier.fillMaxWidth()
                            )

                            LostLinkButton(
                                text = primaryText,
                                onClick = {
                                    val id = when (item) {
                                        is LostItem -> item.id
                                        is FoundItem -> item.id
                                        else -> ""
                                    }
                                    if (id.isNotEmpty()) {
                                        viewModel.updateItemStatus(id, ItemStatus.CLAIMED)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = when (item) {
                                    is LostItem -> item.status == ItemStatus.ACTIVE
                                    is FoundItem -> item.status == ItemStatus.ACTIVE
                                    else -> false
                                }
                            )
                            
                            LostLinkButton(
                                text = secondaryText,
                                onClick = onContactClick,
                                modifier = Modifier.fillMaxWidth(),
                                variant = ButtonVariant.Ghost
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.W600
        )
    }
}
