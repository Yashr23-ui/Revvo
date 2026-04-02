package com.revvo.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.revvo.ui.theme.*

@Composable
fun CreateRideScreen(
    onBack: () -> Unit = {},
    onRideCreated: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize().background(RevvoDark)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            item { 
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 60.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = RevvoWhite)
                    }
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "NEW RIDE",
                        style = MaterialTheme.typography.labelSmall,
                        color = RevvoOrange,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "Create a Ride",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        color = RevvoWhite
                    )
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    RevvoInput("RIDE TITLE", "Morning Canyon Blast", Icons.Default.Motorcycle)
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        RevvoInput("START", "Santa Monica Pier", Icons.Default.Navigation, Modifier.weight(1f))
                        RevvoInput("DESTINATION", "Mulholland Drive", Icons.Default.LocationOn, Modifier.weight(1f))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        RevvoInput("DATE & TIME", "mm/dd/yyyy, --:--", Icons.Default.CalendarToday, Modifier.weight(1f))
                        RevvoInput("MAX RIDERS", "12", Icons.Default.Groups, Modifier.weight(1f))
                    }
                    RevvoInput("DESCRIPTION", "Briefing: Intermediate pace...", Icons.Default.EditNote, minHeight = 120.dp)
                }
            }

            item {
                Surface(
                    modifier = Modifier.fillMaxWidth().height(128.dp),
                    color = RevvoSurface,
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                ) {
                    Box {
                        AsyncImage(
                            model = "https://images.unsplash.com/photo-1524661135-423995f22d0b?auto=format&fit=crop&q=80&w=800",
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize().alpha(0.4f),
                            contentScale = ContentScale.Crop
                        )
                        Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, RevvoDark))))
                        Row(modifier = Modifier.align(Alignment.BottomStart).padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(RevvoOrange))
                            Text(
                                text = "ROUTE TELEMETRY CALCULATED",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = onRideCreated,
                    modifier = Modifier.fillMaxWidth().height(64.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RevvoOrange)
                ) {
                    Text(
                        text = "CREATE RIDE",
                        style = MaterialTheme.typography.headlineMedium,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        color = RevvoWhite
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(Icons.Default.Bolt, null, tint = RevvoWhite)
                }
            }

            item { Spacer(modifier = Modifier.height(120.dp)) }
        }
    }
}

@Composable
fun RevvoInput(label: String, placeholder: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier, minHeight: androidx.compose.ui.unit.Dp = 56.dp) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            color = RevvoGray.copy(alpha = 0.6f),
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )
        Surface(
            modifier = Modifier.fillMaxWidth().heightIn(min = minHeight),
            color = RevvoSurfaceLight.copy(alpha = 0.3f),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
        ) {
            Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = if (minHeight > 56.dp) Alignment.Top else Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(icon, null, modifier = Modifier.size(20.dp), tint = RevvoOrange.copy(alpha = 0.6f))
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyLarge,
                    color = RevvoGray.copy(alpha = 0.3f)
                )
            }
        }
    }
}
