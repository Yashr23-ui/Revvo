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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Motorcycle
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.revvo.ui.state.UiState
import com.revvo.ui.theme.RevvoDark
import com.revvo.ui.theme.RevvoGray
import com.revvo.ui.theme.RevvoGrayDark
import com.revvo.ui.theme.RevvoOrange
import com.revvo.ui.theme.RevvoOrangeLight
import com.revvo.ui.theme.RevvoSurface
import com.revvo.ui.theme.RevvoSurfaceLight
import com.revvo.ui.theme.RevvoWhite
import com.revvo.viewmodel.RideViewModel
import com.revvo.viewmodel.UserViewModel

/**
 * Home screen.
 *
 * NOTE: Several visual elements ("VORTEX-X1", "Last Ride Summary", diagnostics) are
 * placeholder/preview content. They're tagged with `// PREVIEW:` so they're easy to find
 * and replace once telemetry data exists in the data model.
 */
@Composable
fun HomeScreen(
    rideViewModel: RideViewModel,
    userViewModel: UserViewModel,
    onRideClick: (String) -> Unit,
    onCreateRide: () -> Unit
) {
    val userState by userViewModel.userState.collectAsState()
    val rideCards by rideViewModel.rideCards.collectAsState()

    val firstName: String = when (val s = userState) {
        is UiState.Success -> s.data.name.split(" ").firstOrNull().orEmpty()
        else -> ""
    }
    val totalDistance: Int = (userState as? UiState.Success)?.data?.totalDistanceKm ?: 0
    val totalRides: Int = (userState as? UiState.Success)?.data?.totalRides ?: 0

    Box(modifier = Modifier.fillMaxSize().background(RevvoDark)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            item { Spacer(modifier = Modifier.height(80.dp)) }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "SYSTEM READY",
                            style = MaterialTheme.typography.labelSmall,
                            color = RevvoOrange,
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = if (firstName.isEmpty()) "Welcome" else "Good Evening, $firstName",
                            style = MaterialTheme.typography.displayLarge,
                            color = RevvoWhite
                        )
                    }
                    IconButton(
                        onClick = onCreateRide,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(RevvoOrange.copy(alpha = 0.1f))
                    ) {
                        Icon(Icons.Default.Bolt, contentDescription = "Create", tint = RevvoOrange)
                    }
                }
            }

            // First real ride (if any) as the hero. Falls back to a placeholder when empty.
            item {
                val featured = rideCards.firstOrNull()
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
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                            .background(RevvoSurface.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        if (featured != null) {
                            Text("UPCOMING", style = MaterialTheme.typography.labelSmall, color = RevvoGray.copy(alpha = 0.7f))
                            Text(
                                featured.title.uppercase(),
                                style = MaterialTheme.typography.headlineMedium,
                                color = RevvoOrange,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text("NO RIDES YET", style = MaterialTheme.typography.labelSmall, color = RevvoGray.copy(alpha = 0.7f))
                            Text("CREATE ONE", style = MaterialTheme.typography.headlineMedium, color = RevvoOrange, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // PREVIEW: telemetry block — replace with real "last ride" data in Phase 3.
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = RevvoSurface.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, RevvoGrayDark.copy(alpha = 0.1f))
                ) {
                    Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Last Ride Summary", style = MaterialTheme.typography.headlineMedium, fontSize = 18.sp, color = RevvoGray)
                            Text("preview", style = MaterialTheme.typography.labelSmall, color = RevvoGray.copy(alpha = 0.5f))
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                            StatItem("Distance", "—", "KM")
                            VerticalDivider(modifier = Modifier.height(48.dp).width(1.dp), color = RevvoGrayDark.copy(alpha = 0.2f))
                            StatItem("Avg Speed", "—", "KM/H")
                            VerticalDivider(modifier = Modifier.height(48.dp).width(1.dp), color = RevvoGrayDark.copy(alpha = 0.2f))
                            StatItem("Time", "—", "MIN")
                        }
                    }
                }
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    QuickStatCard(Modifier.weight(1f), Icons.Default.Route, "Total Distance", "$totalDistance KM", RevvoOrangeLight)
                    QuickStatCard(Modifier.weight(1f), Icons.Default.Motorcycle, "Total Rides", "$totalRides", RevvoOrange)
                }
            }

            item { Spacer(modifier = Modifier.height(120.dp)) }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String, unit: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label.uppercase(), style = MaterialTheme.typography.labelSmall, color = RevvoOrangeLight)
        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(value, style = MaterialTheme.typography.displayLarge, fontSize = 36.sp, fontWeight = FontWeight.Bold)
            Text(unit, style = MaterialTheme.typography.labelSmall, color = RevvoGray, fontSize = 14.sp)
        }
    }
}

@Composable
private fun QuickStatCard(
    modifier: Modifier,
    icon: ImageVector,
    label: String,
    value: String,
    iconColor: Color
) {
    Surface(
        modifier = modifier,
        color = RevvoSurfaceLight,
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
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
