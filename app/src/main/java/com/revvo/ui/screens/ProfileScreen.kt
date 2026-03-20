package com.revvo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revvo.ui.components.*
import com.revvo.ui.theme.*
import com.revvo.viewmodel.UserViewModel

@Composable
fun ProfileScreen(
    onEditProfile: () -> Unit,
    userViewModel: UserViewModel
) {
    val user by userViewModel.user.collectAsState()

    val initials = user.name
        .split(" ")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .take(2)
        .map { it.first().uppercase() }
        .joinToString("")

    val kmsText = if (user.totalDistance >= 1000) {
        "${user.totalDistance / 1000}k"
    } else {
        user.totalDistance.toString()
    }

    val stats = listOf(
        Triple(Icons.Default.TwoWheeler, "RIDES",   user.totalRides.toString()),
        Triple(Icons.Default.Timeline,   "KMS",     kmsText),
        Triple(Icons.Default.People,     "BUDDIES", "31"),
        Triple(Icons.Default.EmojiEvents,"BADGES",  "7")
    )

    val recentRides = listOf(
        "Mussoorie Night Ride" to "120 km",
        "Rishikesh Highway"    to "75 km",
        "Chakrata Forest Trail" to "200 km"
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

            // ── Top bar ───────────────────────────────────────────────
            item {
                AnimatedScreenEntry {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 12.dp, top = 56.dp, bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Text(
                            text          = "PROFILE",
                            style         = MaterialTheme.typography.headlineLarge,
                            color         = RevvoWhite,
                            fontWeight    = FontWeight.Black,
                            letterSpacing = 2.sp
                        )
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(RevvoSurface),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector        = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint               = RevvoOrange,
                                modifier           = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // ── Profile hero section ──────────────────────────────────
            item {
                AnimatedScreenEntry(delayMs = 100) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(RevvoSurface)
                            .border(0.5.dp, RevvoGrayDark, RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape)
                                .background(RevvoDark)
                                .border(2.dp, RevvoOrange, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text       = initials,
                                fontSize   = 24.sp,
                                fontWeight = FontWeight.Black,
                                color      = RevvoOrange
                            )
                        }
                        Column {
                            Text(
                                text       = user.name,
                                fontSize   = 20.sp,
                                fontWeight = FontWeight.Black,
                                color      = RevvoWhite
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text  = "Dehradun, Uttarakhand",
                                style = MaterialTheme.typography.bodyMedium,
                                color = RevvoGray
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(RevvoOrange.copy(alpha = 0.12f))
                                    .padding(horizontal = 10.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text       = user.bike,
                                    fontSize   = 10.sp,
                                    color      = RevvoOrange,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }
            }

            // ── Stats 2x2 grid ────────────────────────────────────────
            item {
                AnimatedScreenEntry(delayMs = 200) {
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Text(
                            text          = "YOUR STATS",
                            fontSize      = 10.sp,
                            color         = RevvoGray,
                            fontWeight    = FontWeight.Black,
                            letterSpacing = 3.sp,
                            modifier      = Modifier.padding(bottom = 10.dp)
                        )
                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            stats.take(2).forEach { (icon, label, value) ->
                                AggressiveStatCard(
                                    icon     = icon,
                                    label    = label,
                                    value    = value,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            stats.drop(2).forEach { (icon, label, value) ->
                                AggressiveStatCard(
                                    icon     = icon,
                                    label    = label,
                                    value    = value,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            // ── Recent rides ──────────────────────────────────────────
            item {
                AnimatedScreenEntry(delayMs = 300) {
                    Text(
                        text          = "RECENT RIDES",
                        fontSize      = 10.sp,
                        color         = RevvoGray,
                        fontWeight    = FontWeight.Black,
                        letterSpacing = 3.sp,
                        modifier      = Modifier.padding(start = 20.dp, top = 24.dp, bottom = 10.dp)
                    )
                }
            }

            itemsIndexed(recentRides) { index, (name, dist) ->
                AnimatedScreenEntry(delayMs = 350 + index * 60) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(RevvoSurface)
                            .border(
                                width = 0.5.dp,
                                color = RevvoGrayDark,
                                shape = RoundedCornerShape(12.dp)
                            )
                            // Left orange strip
                            .border(
                                width = 2.dp,
                                color = RevvoOrange,
                                shape = RoundedCornerShape(
                                    topStart = 12.dp, bottomStart = 12.dp,
                                    topEnd = 0.dp, bottomEnd = 0.dp
                                )
                            )
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Text(
                            text       = name,
                            style      = MaterialTheme.typography.bodyLarge,
                            color      = RevvoWhite,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text       = dist,
                            fontSize   = 12.sp,
                            color      = RevvoOrange,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }
    }
}