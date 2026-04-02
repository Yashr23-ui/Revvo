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
fun RideDetailsScreen(
    rideId: String,
    onBack: () -> Unit,
    onJoin: () -> Unit
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

            // Featured Ride Card
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = RevvoSurface.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                ) {
                    Column {
                        Box(modifier = Modifier.height(256.dp).fillMaxWidth()) {
                            AsyncImage(
                                model = "https://images.unsplash.com/photo-1470770841072-f978cf4d019e?auto=format&fit=crop&q=80&w=800",
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                                contentScale = ContentScale.Crop,
                                alpha = 0.6f
                            )
                            Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, RevvoDark))))

                            Surface(
                                modifier = Modifier.padding(16.dp),
                                color = RevvoOrange.copy(alpha = 0.9f),
                                shape = CircleShape,
                                border = BorderStroke(1.dp, RevvoOrange.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    "UPCOMING",
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            }
                        }

                        Column(modifier = Modifier.padding(24.dp).offset(y = (-64).dp)) {
                            Text("MUSSOORIE NIGHT RIDE", style = MaterialTheme.typography.displayLarge, fontSize = 24.sp, color = Color.White)
                            Text("HOSTED BY YASH R.", style = MaterialTheme.typography.labelSmall, color = RevvoOrange, letterSpacing = 1.sp)

                            Spacer(modifier = Modifier.height(24.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                RideStat("120", "KM")
                                VerticalDivider(modifier = Modifier.height(24.dp).width(1.dp), color = Color.White.copy(alpha = 0.1f))
                                RideStat("8/15", "RIDERS")
                                VerticalDivider(modifier = Modifier.height(24.dp).width(1.dp), color = Color.White.copy(alpha = 0.1f))
                                RideStat("DEHRADUN", "START")
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = onJoin,
                                modifier = Modifier.fillMaxWidth().height(56.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = RevvoOrange)
                            ) {
                                Text("JOIN RIDE", style = MaterialTheme.typography.headlineMedium, fontSize = 16.sp, fontWeight = FontWeight.Black)
                                Spacer(modifier = Modifier.width(12.dp))
                                Icon(Icons.Default.ArrowForward, null)
                            }
                        }
                    }
                }
            }

            // Tactical Route
            item {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("TACTICAL ROUTE", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = RevvoGray, letterSpacing = 2.sp)
                        Icon(Icons.Default.Route, null, tint = RevvoOrange, modifier = Modifier.size(16.dp))
                    }

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = RevvoSurface.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                    ) {
                        Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(32.dp)) {
                            WaypointItem("START", "CLOCK TOWER", "09:00 PM ASSEMBLY", Icons.Default.RadioButtonChecked, isStart = true)
                            WaypointItem("WP 1", "SAHASTRADHARA ROAD", "FUEL & REGROUP", Icons.Default.LocationOn)
                            WaypointItem("WP 2", "KIMADI VILLAGE", "STEEP CLIMB INITIATED", Icons.Default.Terrain)
                            WaypointItem("FINISH", "MUSSOORIE MALL ROAD", "01:30 AM ETA", Icons.Default.Flag, isFinish = true)
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(120.dp)) }
        }
    }
}

@Composable
fun RideStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.headlineMedium, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, color = RevvoGray, letterSpacing = 1.sp)
    }
}

@Composable
fun WaypointItem(label: String, title: String, sub: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isStart: Boolean = false, isFinish: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
        Box(modifier = Modifier.width(24.dp), contentAlignment = Alignment.TopCenter) {
            if (!isFinish) {
                VerticalDivider(modifier = Modifier.padding(top = 24.dp).width(2.dp).fillMaxHeight(), color = if (isStart) RevvoOrange else Color.White.copy(alpha = 0.1f))
            }
            Surface(
                modifier = Modifier.size(24.dp),
                color = if (isStart) RevvoOrange else RevvoSurfaceLight,
                shape = CircleShape,
                border = if (!isStart) BorderStroke(1.dp, RevvoOrange.copy(alpha = 0.5f)) else null
            ) {
                Icon(icon, null, modifier = Modifier.padding(4.dp), tint = if (isStart) Color.White else RevvoOrange)
            }
        }
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, color = if (isStart || isFinish) RevvoOrange else RevvoGray, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Text(title, style = MaterialTheme.typography.headlineMedium, fontSize = 16.sp, color = Color.White)
            Text(sub, style = MaterialTheme.typography.bodyLarge, fontSize = 12.sp, color = RevvoGray)
        }
    }
}
