package com.revvo.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.revvo.ui.theme.*

@Composable
fun ProfileScreen(onEditProfile: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(RevvoDark)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            item { Spacer(modifier = Modifier.height(60.dp)) }

            // Top Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Surface(modifier = Modifier.size(40.dp), shape = CircleShape, color = RevvoSurfaceLight) {
                            AsyncImage(
                                model = "https://i.pravatar.cc/100?u=revvo_admin",
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize().clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Text(
                            "REVVO",
                            style = MaterialTheme.typography.headlineMedium,
                            color = RevvoOrange,
                            fontWeight = FontWeight.Black,
                            fontStyle = FontStyle.Italic,
                            letterSpacing = 1.sp
                        )
                    }
                    Icon(
                        Icons.Default.Tune,
                        contentDescription = "Settings",
                        tint = RevvoWhite,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { onEditProfile() }
                    )
                }
            }

            // Hero Section
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Box(contentAlignment = Alignment.BottomCenter) {
                        Surface(
                            modifier = Modifier.size(180.dp),
                            shape = CircleShape,
                            color = RevvoDark,
                            border = BorderStroke(2.dp, Brush.sweepGradient(listOf(RevvoOrange, Color.Transparent, RevvoOrange)))
                        ) {
                            AsyncImage(
                                model = "https://images.unsplash.com/photo-1558981403-c5f9899a28bc?auto=format&fit=crop&q=80&w=400",
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize().padding(8.dp).clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Surface(
                            modifier = Modifier.offset(y = 12.dp),
                            color = RevvoOrange,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("LVL 42", modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp), color = Color.White, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black)
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Surface(
                        color = RevvoSurface.copy(alpha = 0.4f),
                        shape = CircleShape,
                        border = BorderStroke(1.dp, RevvoGrayDark.copy(alpha = 0.2f))
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(RevvoOrange))
                            Text("ASPHALT COMMANDER - SECTOR 07", style = MaterialTheme.typography.labelSmall, color = RevvoGray, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        }
                    }

                    Text("GHOST_RIDER", style = MaterialTheme.typography.displayLarge, fontSize = 42.sp, fontWeight = FontWeight.Black, fontStyle = FontStyle.Italic, color = RevvoWhite)

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        ProfileMetaItem(Icons.Default.LocationOn, "Neo Tokyo Suburbs")
                        ProfileMetaItem(Icons.Default.CalendarToday, "Joined Oct 2023")
                    }
                }
            }

            // Progress Section
            item {
                Surface(
                    modifier = Modifier
                        .width(364.dp)
                        .height(230.dp),
                    color = RevvoSurface.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {

                        // ── Top section: label + name (left) | XP numbers (right) ──
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top        // both columns anchor to top
                        ) {

                            // Left: "NEXT RANK PROGRESS" label + "APEX\nPREDATOR"
                            Column {
                                Text(
                                    text = "NEXT RANK\nPROGRESS",  // two lines like the image
                                    style = MaterialTheme.typography.labelSmall,
                                    color = RevvoGray,
                                    letterSpacing = 2.sp,
                                    lineHeight = 14.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row{
                                Text(
                                    text = "APEX\nPREDATOR",        // two lines, large bold
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Black,
                                    fontStyle = FontStyle.Italic,
                                    color = RevvoWhite,
                                    lineHeight = 30.sp               // tight line height between the two words
                                )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(
                                        horizontalAlignment = Alignment.End  // right-align both texts
                                    ) {
                                        Row(verticalAlignment = Alignment.Bottom) {
                                            Text(
                                                text = "12,450",
                                                fontSize = 28.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = RevvoOrange
                                            )
                                            Text(
                                                text = "/",
                                                fontSize = 20.sp,
                                                color = RevvoOrange,          // same color as number, inline slash
                                                modifier = Modifier.padding(bottom = 2.dp)
                                            )
                                        }
                                        Text(
                                            text = "15,000 XP",
                                            fontSize = 12.sp,
                                            color = RevvoGray,
                                            fontWeight = FontWeight.Normal
                                        )
                                    }


                                }
                            }

                            // Right: "12,450/" on one line, "15,000 XP" below it

                        }

                        // ── Progress bar ────────────────────────────────────────────
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp),                       // thin bar like the image
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            repeat(7) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .background(RevvoOrange, RoundedCornerShape(2.dp))
                                )
                            }
                            repeat(3) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .background(RevvoSurfaceLight, RoundedCornerShape(2.dp))
                                )
                            }
                        }

                        // ── Footer: rank labels ──────────────────────────────────────
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "RANK 42",
                                style = MaterialTheme.typography.labelSmall,
                                color = RevvoGray.copy(alpha = 0.5f)
                            )
                            Text(
                                "2,550 XP TO LEVEL UP",
                                style = MaterialTheme.typography.labelSmall,
                                color = RevvoGray.copy(alpha = 0.5f)
                            )
                            Text(
                                "RANK 43",
                                style = MaterialTheme.typography.labelSmall,
                                color = RevvoGray.copy(alpha = 0.5f)
                            )
                        }

                    }
                }
            }


            // Global Standing
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF2A1B14).copy(alpha = 0.3f),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, RevvoGrayDark.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("GLOBAL STANDING", style = MaterialTheme.typography.labelSmall, color = RevvoOrange, letterSpacing = 2.sp)
                        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("#124", style = MaterialTheme.typography.displayLarge, fontSize = 64.sp, fontWeight = FontWeight.Black, fontStyle = FontStyle.Italic, color = RevvoWhite)
                            Text("Top 1%", style = MaterialTheme.typography.headlineMedium, color = RevvoOrange.copy(alpha = 0.8f))
                        }
                        Text("You've climbed 14 spots this week. Keep up the lean angles to break the top 100.", style = MaterialTheme.typography.bodyLarge, fontSize = 14.sp, color = RevvoGray.copy(alpha = 0.8f))
                    }
                }
            }

            // Stats Grid
            item {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        StatCard(Modifier.weight(1f), Icons.Default.Route, "TOTAL RIDES", "284", RevvoOrangeLight)
                        StatCard(Modifier.weight(1f), Icons.Default.LocationOn, "DISTANCE", "12,482", RevvoOrange, "KM")
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        StatCard(Modifier.weight(1f), Icons.Default.Timer, "HOURS", "1,042", RevvoOrange)
                        StatCard(Modifier.weight(1f), Icons.Default.People, "FRIENDS RANK", "2nd", RevvoOrangeLight)
                    }
                }
            }

            // Achievement Badges
            item {
                Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("ACHIEVEMENT BADGES", style = MaterialTheme.typography.headlineMedium, fontSize = 24.sp, fontWeight = FontWeight.Black, fontStyle = FontStyle.Italic)
                        Text("VIEW ALL LOCKED", style = MaterialTheme.typography.labelSmall, color = RevvoGray.copy(alpha = 0.5f), letterSpacing = 1.sp)
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            BadgeCard(Modifier.weight(1f), Icons.Default.NightsStay, "NIGHT HAWK", "50 NIGHT RIDES", true)
                            BadgeCard(Modifier.weight(1f), Icons.Default.Bolt, "SPEED DEMON", "200KM/H SUSTAINED", true)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            BadgeCard(Modifier.weight(1f), Icons.Default.Groups, "PACK LEADER", "ORGANIZED 20 MEETS", true)
                            BadgeCard(Modifier.weight(1f), Icons.Default.Grain, "STORM RIDER", "RIDE IN SNOW/RAIN", true)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            BadgeCard(Modifier.weight(1f), Icons.Default.Flag, "CROSS BORDER", "5 COUNTRIES VISITED", true)
                            BadgeCard(Modifier.weight(1f), Icons.Default.Lock, "IRON BUTT", "LOCKED", false)
                        }
                    }
                }
            }

            // Performance Telemetry
            item {
                Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    Text("PERFORMANCE TELEMETRY", style = MaterialTheme.typography.headlineMedium, fontSize = 20.sp, fontWeight = FontWeight.Black, fontStyle = FontStyle.Italic)

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = RevvoSurface.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("PARAMETER", style = MaterialTheme.typography.labelSmall, color = RevvoGray.copy(alpha = 0.5f))
                                Text("PEAK", style = MaterialTheme.typography.labelSmall, color = RevvoGray.copy(alpha = 0.5f))
                            }
                            HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
                            TelemetryRow("Lean Angle (Left/Right)", "54° / 52°", RevvoOrange)
                            TelemetryRow("Top Speed Recorded", "284 KM/H", RevvoOrange)
                            TelemetryRow("Braking Intensity", "1.4 G", RevvoOrangeLight)
                            TelemetryRow("Corner Entry Speed Avg", "82 KM/H", RevvoOrange)
                        }
                    }
                }
            }

            // Recent Activity
            item {
                Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    Text("RECENT ACTIVITY", style = MaterialTheme.typography.headlineMedium, fontSize = 20.sp, fontWeight = FontWeight.Black, fontStyle = FontStyle.Italic)

                    ActivityItem(Icons.Default.Motorcycle, "MIDNIGHT LOOP", "Yesterday • 142 KM • 420 XP Earned")
                    ActivityItem(Icons.Default.MilitaryTech, "BADGE EARNED: NIGHT HAWK", "2 days ago • Career Milestone")
                    ActivityItem(Icons.Default.Explore, "SUBURBAN EXPLORATION", "3 days ago • 64 KM • 120 XP Earned")
                }
            }

            item { Spacer(modifier = Modifier.height(120.dp)) }
        }
    }
}

@Composable
fun StatCard(modifier: Modifier, icon: ImageVector, label: String, value: String, accentColor: Color, unit: String? = null) {
    Surface(
        modifier = modifier.height(140.dp),
        color = RevvoSurface.copy(alpha = 0.4f),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Icon(icon, null, modifier = Modifier.size(24.dp), tint = accentColor)
            Column {
                Text(label, style = MaterialTheme.typography.labelSmall, color = RevvoGray.copy(alpha = 0.6f), letterSpacing = 1.sp)
                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(value, style = MaterialTheme.typography.displayLarge, fontSize = 32.sp, fontWeight = FontWeight.Black, color = RevvoWhite)
                    if (unit != null) {
                        Text(unit, style = MaterialTheme.typography.labelSmall, color = RevvoGray.copy(alpha = 0.4f), modifier = Modifier.padding(bottom = 4.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun BadgeCard(modifier: Modifier, icon: ImageVector, title: String, sub: String, unlocked: Boolean) {
    Surface(
        modifier = modifier.height(160.dp),
        color = RevvoSurface.copy(alpha = 0.4f),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Surface(
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                color = if (unlocked) RevvoOrange.copy(alpha = 0.1f) else RevvoSurfaceLight.copy(alpha = 0.2f),
                border = BorderStroke(1.dp, if (unlocked) RevvoOrange.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.05f))
            ) {
                Icon(icon, null, modifier = Modifier.padding(14.dp), tint = if (unlocked) RevvoOrange else RevvoGray.copy(alpha = 0.2f))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(title, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = if (unlocked) RevvoWhite else RevvoGray.copy(alpha = 0.3f))
            Text(sub, style = MaterialTheme.typography.labelSmall, fontSize = 8.sp, color = RevvoGray.copy(alpha = 0.4f))
        }
    }
}

@Composable
fun TelemetryRow(label: String, value: String, valueColor: Color) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(label, style = MaterialTheme.typography.bodyLarge, color = RevvoWhite.copy(alpha = 0.8f))
        Text(value, style = MaterialTheme.typography.headlineMedium, fontSize = 18.sp, color = valueColor, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ActivityItem(icon: ImageVector, title: String, sub: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = RevvoSurface.copy(alpha = 0.4f),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Surface(modifier = Modifier.size(48.dp), shape = CircleShape, color = RevvoSurfaceLight.copy(alpha = 0.3f)) {
                Icon(icon, null, modifier = Modifier.padding(12.dp), tint = RevvoOrange)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.headlineMedium, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = RevvoWhite)
                Text(sub, style = MaterialTheme.typography.bodyLarge, fontSize = 12.sp, color = RevvoGray.copy(alpha = 0.5f))
            }
            Icon(Icons.Default.ChevronRight, null, tint = RevvoGray.copy(alpha = 0.3f))
        }
    }
}

@Composable
fun ProfileMetaItem(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(icon, null, modifier = Modifier.size(14.dp), tint = RevvoGray)
        Text(text.uppercase(), style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, color = RevvoGray, letterSpacing = 1.sp)
    }
}
