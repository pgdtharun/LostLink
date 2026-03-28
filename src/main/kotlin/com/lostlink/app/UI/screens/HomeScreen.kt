package com.lostlink.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lostlink.app.ui.components.LostLinkButton
import com.lostlink.app.ui.components.ButtonVariant

import androidx.compose.ui.graphics.Brush
import com.lostlink.app.ui.theme.LostLinkGradient
import com.lostlink.app.ui.theme.LostLinkLightGradient

@Composable
fun HomeScreen(
    onBrowseClick: () -> Unit,
    onReportClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(Modifier.height(48.dp))
            }
            
            item {
                Text(
                    "LostLink",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Campus Lost & Found",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Spacer(Modifier.height(32.dp))
            }
            
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(LostLinkGradient),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "🔍\nFind What You Lost",
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(Modifier.height(32.dp))
            }
            
            item {
                Text(
                    "Welcome to LostLink",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Help your campus community find lost items and reunite people with their belongings.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Spacer(Modifier.height(24.dp))
            }
            
            item {
                FeatureCard(
                    icon = "📋",
                    title = "Browse Lost Items",
                    description = "Search through lost items reported by campus members",
                    onClick = onBrowseClick
                )
                Spacer(Modifier.height(12.dp))
            }
            
            item {
                FeatureCard(
                    icon = "📝",
                    title = "Report Items",
                    description = "Report a lost or found item to help others",
                    onClick = onReportClick
                )
                Spacer(Modifier.height(12.dp))
            }
            
            item {
                FeatureCard(
                    icon = "💬",
                    title = "Connect with Others",
                    description = "Message other members to coordinate item recovery",
                    onClick = {}
                )
                Spacer(Modifier.height(24.dp))
            }
            
            item {
                LostLinkButton(
                    text = "Browse All Items",
                    onClick = onBrowseClick,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
            }
            
            item {
                LostLinkButton(
                    text = "Get Started",
                    onClick = onLoginClick,
                    modifier = Modifier.fillMaxWidth(),
                    variant = ButtonVariant.Primary
                )
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun FeatureCard(
    icon: String,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .scale(scale),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
        ),
        elevation = CardDefaults.cardElevation(if (isPressed) 2.dp else 6.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f))
    ) {
        Button(
            onClick = onClick,
            interactionSource = interactionSource,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "$icon $title",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.W600,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        maxLines = 2
                    )
                }
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
