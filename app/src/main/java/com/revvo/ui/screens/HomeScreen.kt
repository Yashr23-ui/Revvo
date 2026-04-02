package com.revvo.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Motorcycle
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.revvo.ui.theme.*

@Composable
fun HomeScreen(
    onRideClick: (String) -> Unit = {},
    onCreateRide: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize().background(RevvoDark)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            item { Spacer(modifier = Modifier.height(80.dp)) }

            // Greeting
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "SYSTEM READY",
                        style = MaterialTheme.typography.labelSmall,
                        color = RevvoOrange,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "Good Evening, Rider",
                        style = MaterialTheme.typography.displayLarge,
                        color = RevvoWhite
                    )
                }
            }

            // Hero Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .clip(RoundedCornerShape(24.dp))
                ) {
                    AsyncImage(
                        model = "https://images.unsplash.com/photo-1558981403-c5f9899a28bc?auto=format&fit=crop&q=80&w=800",
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, RevvoDark.copy(alpha = 0.8f)),
                                    startY = 300f
                                )
                            )
                    )

                    // Battery Badge
                    Surface(
                        modifier = Modifier.align(Alignment.TopEnd).padding(16.dp),
                        color = RevvoSurface.copy(alpha = 0.8f),
                        shape = CircleShape,
                        border = BorderStroke(1.dp, RevvoOrange.copy(alpha = 0.2f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.BatteryChargingFull, null, tint = RevvoOrange, modifier = Modifier.size(14.dp))
                            Text("88%", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Unit Info
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                            .background(RevvoSurface.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Text("ACTIVE UNIT", style = MaterialTheme.typography.labelSmall, color = RevvoGray.copy(alpha = 0.7f))
                        Text("VORTEX-X1", style = MaterialTheme.typography.headlineMedium, color = RevvoOrange, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Stats Grid
            item {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Last Ride Summary
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = RevvoSurface.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(1.dp, RevvoGrayDark.copy(alpha = 0.1f)),
                        onClick = { onRideClick("LAST_RIDE_ID") }
                    ) {
                        Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Last Ride Summary", style = MaterialTheme.typography.headlineMedium, fontSize = 18.sp, color = RevvoGray)
                                Text("2h ago", style = MaterialTheme.typography.labelSmall, color = RevvoGray.copy(alpha = 0.5f))
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                                StatItem("Distance", "42.8", "KM")
                                VerticalDivider(modifier = Modifier.height(48.dp).width(1.dp), color = RevvoGrayDark.copy(alpha = 0.2f))
                                StatItem("Avg Speed", "64", "KM/H")
                                VerticalDivider(modifier = Modifier.height(48.dp).width(1.dp), color = RevvoGrayDark.copy(alpha = 0.2f))
                                StatItem("Time", "48", "MIN")
                            }
                        }
                    }

                    // Quick Stats
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        QuickStatCard(Modifier.weight(1f), Icons.Default.Route, "Total Distance", "1,204 KM", RevvoOrangeLight)
                        QuickStatCard(Modifier.weight(1f), Icons.Default.Motorcycle, "Total Rides", "84", RevvoOrange)
                    }
                }
            }

            // Diagnostics
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = RevvoSurface,
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, RevvoGrayDark.copy(alpha = 0.05f))
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("System Diagnostics", style = MaterialTheme.typography.headlineMedium, fontSize = 20.sp)
                                Text("All modules reporting optimal", style = MaterialTheme.typography.bodyLarge, fontSize = 14.sp, color = RevvoGray.copy(alpha = 0.6f))
                            }
                            Surface(color = RevvoOrange.copy(alpha = 0.2f), shape = CircleShape) {
                                Text("LIVE", modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), color = RevvoOrange, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            DiagnosticItem(Modifier.weight(1f), Icons.Default.Thermostat, "Core Temp", "32°C")
                            DiagnosticItem(Modifier.weight(1f), Icons.Default.Bolt, "PSI F/R", "34 / 36")
                            DiagnosticItem(Modifier.weight(1f), Icons.Default.History, "Health", "98%", RevvoOrange)
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(120.dp)) }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, unit: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label.uppercase(), style = MaterialTheme.typography.labelSmall, color = RevvoOrangeLight)
        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(value, style = MaterialTheme.typography.displayLarge, fontSize = 36.sp, fontWeight = FontWeight.Bold)
            Text(unit, style = MaterialTheme.typography.labelSmall, color = RevvoGray, fontSize = 14.sp)
        }
    }
}

@Composable
fun QuickStatCard(modifier: Modifier, icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String, iconColor: Color) {
    Surface(
        modifier = modifier,
        color = RevvoSurfaceLight,
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Surface(color = iconColor.copy(alpha = 0.1f), shape = RoundedCornerShape(12.dp)) {
                Icon(icon, null, modifier = Modifier.padding(8.dp).size(20.dp), tint = iconColor)
            }
            Column {
                Text(label.uppercase(), style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, color = RevvoGray)
                Text(value, style = MaterialTheme.typography.headlineMedium, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun DiagnosticItem(modifier: Modifier, icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String, valueColor: Color = RevvoWhite) {
    Surface(
        modifier = modifier,
        color = RevvoSurfaceLight.copy(alpha = 0.3f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(icon, null, modifier = Modifier.size(18.dp), tint = RevvoGray)
            Column {
                Text(label.uppercase(), style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, color = RevvoGray)
                Text(value, style = MaterialTheme.typography.headlineMedium, fontSize = 18.sp, color = valueColor)
            }
        }
    }
}
