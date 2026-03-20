package com.revvo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revvo.ui.components.*
import com.revvo.ui.theme.*
import com.revvo.ui.model.toRideCardData
import com.revvo.viewmodel.RideViewModel

@Composable
fun RideDetailsScreen(
    rideId : String,
    onBack : () -> Unit,
    onJoin : (String) -> Unit,
    rideViewModel: RideViewModel
) {
    val rideCards by rideViewModel.rideCards.collectAsState()
    val rideModel = rideViewModel.getRideById(rideId)
    val ride = rideCards.firstOrNull { it.rideId == rideId } ?: rideModel?.toRideCardData()

    if (ride == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(RevvoDark),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Ride not found",
                color = RevvoWhite,
                fontWeight = FontWeight.Black
            )
        }
        return
    }

    val routePoints = listOf(
        "Clock Tower, Dehradun" to "START",
        "Sahastradhara Road"    to "WP 1",
        "Kimadi Village"        to "WP 2",
        "Mussoorie Mall Road"   to "FINISH"
    )

    val baseRiders = listOf(
        "YR" to "Yash R.",
        "AK" to "Arjun K.",
        "PS" to "Priya S.",
        "RM" to "Rohan M.",
        "ST" to "Sneha T.",
        "DP" to "Dev P."
    )
    val riders = baseRiders.take(ride.memberCount.coerceAtMost(baseRiders.size))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RevvoDark)
    ) {
        LazyColumn(
            modifier       = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 110.dp)
        ) {

            // ── Back + Title ──────────────────────────────────────────
            item {
                AnimatedScreenEntry {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp, end = 20.dp, top = 52.dp, bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBack) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(RevvoSurface),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector        = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint               = RevvoWhite,
                                    modifier           = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Text(
                                text          = "RIDE DETAILS",
                                fontSize      = 10.sp,
                                color         = RevvoOrange,
                                fontWeight    = FontWeight.Black,
                                letterSpacing = 3.sp
                            )
                            Text(
                                text       = "Overview",
                                style      = MaterialTheme.typography.headlineMedium,
                                color      = RevvoWhite,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }

            // ── Ride card ─────────────────────────────────────────────
            item {
                AnimatedScreenEntry(delayMs = 100) {
                    RideCard(
                        ride     = ride,
                        onClick  = {},
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                    )
                }
            }

            // ── Route ─────────────────────────────────────────────────
            item {
                AnimatedScreenEntry(delayMs = 200) {
                    Text(
                        text          = "ROUTE",
                        fontSize      = 10.sp,
                        color         = RevvoGray,
                        fontWeight    = FontWeight.Black,
                        letterSpacing = 3.sp,
                        modifier      = Modifier.padding(start = 20.dp, top = 24.dp, bottom = 10.dp)
                    )
                }
            }

            itemsIndexed(routePoints) { index, (place, tag) ->
                AnimatedScreenEntry(delayMs = 250 + index * 50) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(RevvoSurface)
                            .border(
                                width = 2.dp,
                                color = RevvoOrange,
                                shape = RoundedCornerShape(
                                    topStart = 12.dp, bottomStart = 12.dp,
                                    topEnd = 0.dp, bottomEnd = 0.dp
                                )
                            )
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(RevvoOrange)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = place, style = MaterialTheme.typography.bodyLarge, color = RevvoWhite)
                        }
                        Text(
                            text          = tag,
                            fontSize      = 9.sp,
                            color         = RevvoOrange,
                            fontWeight    = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            // ── Riders ────────────────────────────────────────────────
            item {
                AnimatedScreenEntry(delayMs = 400) {
                    Text(
                        text          = "RIDERS (${ride.memberCount}/${ride.maxMembers})",
                        fontSize      = 10.sp,
                        color         = RevvoGray,
                        fontWeight    = FontWeight.Black,
                        letterSpacing = 3.sp,
                        modifier      = Modifier.padding(start = 20.dp, top = 24.dp, bottom = 10.dp)
                    )
                }
            }

            itemsIndexed(riders) { index, (initials, name) ->
                AnimatedScreenEntry(delayMs = 450 + index * 40) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(RevvoSurface)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Initials avatar
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(RevvoOrange.copy(alpha = 0.12f))
                                .border(1.dp, RevvoOrange.copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text       = initials,
                                fontSize   = 11.sp,
                                fontWeight = FontWeight.Black,
                                color      = RevvoOrange
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = name, style = MaterialTheme.typography.bodyLarge, color = RevvoWhite)
                    }
                }
            }
        }

        // ── Floating JOIN button ──────────────────────────────────────
        Button(
            onClick  = {
                rideViewModel.joinRide(rideId)
                onJoin(rideId)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp)
                .height(56.dp),
            shape  = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = RevvoOrange)
        ) {
            Text(
                text          = "JOIN THIS RIDE",
                fontSize      = 14.sp,
                fontWeight    = FontWeight.Black,
                color         = RevvoWhite,
                letterSpacing = 3.sp
            )
        }
    }
}