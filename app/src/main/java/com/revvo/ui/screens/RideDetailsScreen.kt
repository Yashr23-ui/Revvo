package com.revvo.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.revvo.data.model.Ride
import com.revvo.ui.state.UiState
import com.revvo.ui.theme.RevvoDark
import com.revvo.ui.theme.RevvoGray
import com.revvo.ui.theme.RevvoOrange
import com.revvo.ui.theme.RevvoSurface
import com.revvo.ui.theme.RevvoWhite
import com.revvo.viewmodel.RideEvent
import com.revvo.viewmodel.RideViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RideDetailsScreen(
    rideId: String,
    rideViewModel: RideViewModel,
    onBack: () -> Unit,
    onJoined: () -> Unit
) {
    // Tell the VM which ride we're showing — and clear when we leave so the flow can stop.
    LaunchedEffect(rideId) {
        rideViewModel.selectRide(rideId)
    }
    DisposableEffect(Unit) {
        onDispose { rideViewModel.clearSelection() }
    }

    LaunchedEffect(Unit) {
        rideViewModel.events.collectLatest { event ->
            if (event is RideEvent.RideJoined && event.rideId == rideId) onJoined()
        }
    }

    val state by rideViewModel.selectedRide.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(RevvoDark)) {
        when (val s = state) {
            is UiState.Loading -> CenteredLoading()
            is UiState.Empty -> CenteredText("Ride not found")
            is UiState.Error -> CenteredText(s.message)
            is UiState.Success -> RideDetailsContent(
                ride = s.data,
                onBack = onBack,
                onJoin = { rideViewModel.joinRide(rideId) }
            )
        }
    }
}

@Composable
private fun RideDetailsContent(
    ride: Ride,
    onBack: () -> Unit,
    onJoin: () -> Unit
) {
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
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = RevvoWhite
                    )
                }
            }
        }

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
                                ride.status.name,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }

                    Column(modifier = Modifier.padding(24.dp).offset(y = (-64).dp)) {
                        Text(
                            ride.title.uppercase(),
                            style = MaterialTheme.typography.displayLarge,
                            fontSize = 24.sp,
                            color = Color.White
                        )
                        Text(
                            "HOSTED BY ${ride.hostName.uppercase()}",
                            style = MaterialTheme.typography.labelSmall,
                            color = RevvoOrange,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            RideStat("${ride.distanceKm}", "KM")
                            VerticalDivider(modifier = Modifier.height(24.dp).width(1.dp), color = Color.White.copy(alpha = 0.1f))
                            RideStat("${ride.joinedRiders}/${ride.maxRiders}", "RIDERS")
                            VerticalDivider(modifier = Modifier.height(24.dp).width(1.dp), color = Color.White.copy(alpha = 0.1f))
                            RideStat(ride.startLocation.uppercase(), "START")
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = onJoin,
                            enabled = ride.canJoin,
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = RevvoOrange)
                        ) {
                            Text(
                                if (ride.canJoin) "JOIN RIDE" else if (ride.isFull) "RIDE FULL" else "UNAVAILABLE",
                                style = MaterialTheme.typography.headlineMedium,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, null)
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "RIDERS (${ride.joinedRiders}/${ride.maxRiders})",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = RevvoGray,
                letterSpacing = 2.sp
            )
        }

        item {
            Text(
                text = ride.description.ifBlank { "No description provided." },
                style = MaterialTheme.typography.bodyLarge,
                color = RevvoWhite.copy(alpha = 0.85f)
            )
        }

        item { Spacer(modifier = Modifier.height(120.dp)) }
    }
}

@Composable
private fun RideStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.headlineMedium, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, color = RevvoGray, letterSpacing = 1.sp)
    }
}

@Composable
private fun CenteredLoading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = RevvoOrange)
    }
}

@Composable
private fun CenteredText(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text, color = RevvoWhite, style = MaterialTheme.typography.bodyLarge)
    }
}
