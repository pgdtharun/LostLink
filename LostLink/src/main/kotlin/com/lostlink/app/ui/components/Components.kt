package com.lostlink.app.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lostlink.app.data.model.ItemCategory

import androidx.compose.ui.graphics.Brush
import com.lostlink.app.ui.theme.LostLinkGradient
import com.lostlink.app.ui.theme.LostLinkLightGradient

@Composable
fun LostLinkButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    variant: ButtonVariant = ButtonVariant.Primary
) {
    val gradient = when (variant) {
        ButtonVariant.Primary -> LostLinkGradient
        ButtonVariant.Secondary -> LostLinkLightGradient
        ButtonVariant.Ghost -> Brush.linearGradient(colors = listOf(Color.Transparent, Color.Transparent))
    }
    
    val textColor = when (variant) {
        ButtonVariant.Primary, ButtonVariant.Secondary -> Color.White
        ButtonVariant.Ghost -> MaterialTheme.colorScheme.primary
    }
    
    val borderColor = when (variant) {
        ButtonVariant.Ghost -> MaterialTheme.colorScheme.primary
        else -> Color.Transparent
    }
    
    Button(
        onClick = onClick,
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
        enabled = enabled && !isLoading,
        border = if (variant == ButtonVariant.Ghost) {
            BorderStroke(1.dp, borderColor)
        } else null,
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (enabled && !isLoading) gradient else Brush.linearGradient(colors = listOf(MaterialTheme.colorScheme.outline, MaterialTheme.colorScheme.outline))),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = textColor,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text,
                    color = textColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W600
                )
            }
        }
    }
}

enum class ButtonVariant {
    Primary, Secondary, Ghost
}

@Composable
fun ItemCard(
    title: String,
    description: String,
    location: String,
    category: String,
    imageUrl: String,
    status: String,
    postedBy: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isVisible = true }
    
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.85f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy),
        label = "item_scale"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
    ) {
        Column(Modifier.fillMaxWidth()) {
            // Category-specific SVG icon
            val categoryEnum = try { ItemCategory.valueOf(category) } catch (e: Exception) { ItemCategory.OTHER }
            val categoryIcon = getIconForCategory(categoryEnum)
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    categoryIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    modifier = Modifier.size(64.dp)
                )
            }
            
            Column(Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .marginBottom(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    StatusBadge(status)
                }
                
                Text(
                    description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 2
                )
                
                Spacer(Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .marginBottom(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            "Location",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            location,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.W600
                        )
                    }
                    
                    Column {
                        Text(
                            "Category",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            category,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.W600
                        )
                    }
                }
                
                Divider(Modifier.margin(vertical = 12.dp))
                
                Text(
                    "Posted by $postedBy",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val backgroundColor = when (status) {
        "Lost" -> MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
        "Found" -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
    }
    
    val textColor = when (status) {
        "Lost" -> MaterialTheme.colorScheme.error
        "Found" -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.onSurface
    }
    
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp)),
        color = backgroundColor
    ) {
        Text(
            status,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.W600
        )
    }
}

fun Modifier.margin(vertical: androidx.compose.ui.unit.Dp = 0.dp, horizontal: androidx.compose.ui.unit.Dp = 0.dp) =
    this.padding(vertical = vertical, horizontal = horizontal)

fun Modifier.marginBottom(value: androidx.compose.ui.unit.Dp) =
    this.padding(bottom = value)

fun getIconForCategory(category: ItemCategory): ImageVector {
    return when (category) {
        ItemCategory.BAGS -> Icons.Default.Backpack
        ItemCategory.BOOKS -> Icons.Default.MenuBook
        ItemCategory.ELECTRONICS -> Icons.Default.Smartphone
        ItemCategory.ID_CARDS -> Icons.Default.Badge
        ItemCategory.KEYS -> Icons.Default.VpnKey
        ItemCategory.CLOTHING -> Icons.Default.Checkroom
        ItemCategory.WALLETS -> Icons.Default.AccountBalanceWallet
        ItemCategory.JEWELRY -> Icons.Default.Diamond
        ItemCategory.DOCUMENTS -> Icons.Default.Description
        ItemCategory.OTHER -> Icons.Default.Category
    }
}
