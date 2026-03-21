package com.revvo.ui.screens
import com.revvo.viewmodel.RideViewModel
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revvo.ui.components.*
import com.revvo.ui.theme.*
import androidx.compose.foundation.clickable

@Composable
fun HomeScreen(
    onRideClick  : (String) -> Unit,
    onCreateRide : () -> Unit,
    rideViewModel: RideViewModel
) 
    {
    val rides by rideViewModel.rideCards.collectAsState()

    val stats = listOf(
        Triple(Icons.Default.TwoWheeler, "RIDES",   "24"),
        Triple(Icons.Default.Timeline,   "KMS",     "1.2k"),
        Triple(Icons.Default.People,     "BUDDIES", "31")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RevvoDark)
    ) {
        LazyColumn(
            modifier       = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {

            // ── Header ────────────────────────────────────────────────
            item {
                AnimatedScreenEntry(delayMs = 0) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, top = 56.dp, bottom = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text          = "HEY RIDER 🏍️",
                                fontSize      = 11.sp,
                                color         = RevvoGray,
                                fontWeight    = FontWeight.Medium,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text       = "Ready to\nride?",
                                style      = MaterialTheme.typography.headlineLarge,
                                color      = RevvoWhite,
                                fontWeight = FontWeight.Black,
                                lineHeight = 32.sp
                            )
                        }
                        // Notification bell
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(RevvoSurface),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector        = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint               = RevvoGray,
                                modifier           = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }

            // ── Stats row ─────────────────────────────────────────────
            item {
                AnimatedScreenEntry(delayMs = 100) {
                    Column {
                        Text(
                            text          = "YOUR STATS",
                            fontSize      = 10.sp,
                            color         = RevvoGray,
                            fontWeight    = FontWeight.Black,
                            letterSpacing = 3.sp,
                            modifier      = Modifier.padding(start = 20.dp, top = 28.dp, bottom = 12.dp)
                        )
                        Row(
                            modifier              = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            stats.forEach { (icon, label, value) ->
                                AggressiveStatCard(
                                    icon  = icon,
                                    label = label,
                                    value = value,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            // ── Rides header ──────────────────────────────────────────
            item {
                AnimatedScreenEntry(delayMs = 200) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, top = 28.dp, bottom = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Text(
                            text          = "UPCOMING RIDES",
                            fontSize      = 10.sp,
                            color         = RevvoWhite,
                            fontWeight    = FontWeight.Black,
                            letterSpacing = 3.sp
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(RevvoOrange.copy(alpha = 0.12f))
                                .clickable { onCreateRide() }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text          = "+ CREATE",
                                fontSize      = 9.sp,
                                color         = RevvoOrange,
                                fontWeight    = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }

            // ── Ride cards ────────────────────────────────────────────
            itemsIndexed(rides) { index, ride ->
                AnimatedScreenEntry(delayMs = 300 + index * 80) {
                    RideCard(
                        ride     = ride,
                        onClick  = onRideClick,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)
                    )
                }
            }
        }
    }
}

// ── Aggressive stat card with left orange border ──────────────────────────────
@Composable
fun AggressiveStatCard(
    icon     : androidx.compose.ui.graphics.vector.ImageVector,
    label    : String,
    value    : String,
    modifier : Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(RevvoSurface)
            .then(
                Modifier.padding(start = 2.dp)
            )
    ) {
        // Left orange accent strip
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(80.dp)
                .clip(RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp))
                .background(RevvoOrange)
        )
        Column(
            modifier = Modifier.padding(start = 12.dp, top = 12.dp, bottom = 12.dp, end = 8.dp)
        ) {
            Icon(
                imageVector        = icon,
                contentDescription = label,
                tint               = RevvoOrange,
                modifier           = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text       = value,
                fontSize   = 22.sp,
                fontWeight = FontWeight.Black,
                color      = RevvoWhite
            )
            Text(
                text          = label,
                fontSize      = 9.sp,
                color         = RevvoGray,
                fontWeight    = FontWeight.Medium,
                letterSpacing = 1.sp
            )
        }
    }
}