package com.revvo.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Motorcycle
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.revvo.data.model.User
import com.revvo.ui.state.UiState
import com.revvo.ui.theme.RevvoDark
import com.revvo.ui.theme.RevvoGray
import com.revvo.ui.theme.RevvoGrayDark
import com.revvo.ui.theme.RevvoOrange
import com.revvo.ui.theme.RevvoOrangeLight
import com.revvo.ui.theme.RevvoSurface
import com.revvo.ui.theme.RevvoSurfaceLight
import com.revvo.ui.theme.RevvoWhite
import com.revvo.viewmodel.UserViewModel

@Composable
fun ProfileScreen(
    userViewModel: UserViewModel,
    onEditProfile: () -> Unit,
    onSignOut: () -> Unit
) {
    val state by userViewModel.userState.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(RevvoDark)) {
        when (val s = state) {
            is UiState.Loading -> CenteredProgress()
            is UiState.Empty -> CenteredMessage("Sign in to see your profile")
            is UiState.Error -> CenteredMessage(s.message)
            is UiState.Success -> ProfileContent(
                user = s.data,
                onEditProfile = onEditProfile,
                onSignOut = onSignOut
            )
        }
    }
}

@Composable
private fun ProfileContent(
    user: User,
    onEditProfile: () -> Unit,
    onSignOut: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        item { Spacer(modifier = Modifier.height(60.dp)) }

        // Top bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Surface(modifier = Modifier.size(40.dp), shape = CircleShape, color = RevvoSurfaceLight) {
                        AsyncImage(
                            model = "https://i.pravatar.cc/100?u=${user.id}",
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
                    modifier = Modifier.size(28.dp).clickable { onEditProfile() }
                )
            }
        }

        // Hero
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
                        Text(
                            "LVL ${user.xp / 100}",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    user.name.uppercase().replace(" ", "_"),
                    style = MaterialTheme.typography.displayLarge,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Black,
                    fontStyle = FontStyle.Italic,
                    color = RevvoWhite
                )

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    ProfileMetaItem(Icons.Default.LocationOn, user.location)
                    ProfileMetaItem(Icons.Default.CalendarToday, "Member")
                }
            }
        }

        // XP progress
        item {
            Surface(
                modifier = Modifier.fillMaxWidth().height(160.dp),
                color = RevvoSurface.copy(alpha = 0.4f),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            "NEXT RANK",
                            style = MaterialTheme.typography.labelSmall,
                            color = RevvoGray,
                            letterSpacing = 2.sp
                        )
                        Text(
                            "${user.xp} / ${(user.xp / 1000 + 1) * 1000} XP",
                            style = MaterialTheme.typography.labelSmall,
                            color = RevvoOrange,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    val progressUnits = (user.xp % 1000) / 100
                    Row(
                        modifier = Modifier.fillMaxWidth().height(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        repeat(progressUnits) {
                            Box(
                                modifier = Modifier.weight(1f).fillMaxHeight()
                                    .background(RevvoOrange, RoundedCornerShape(2.dp))
                            )
                        }
                        repeat(10 - progressUnits) {
                            Box(
                                modifier = Modifier.weight(1f).fillMaxHeight()
                                    .background(RevvoSurfaceLight, RoundedCornerShape(2.dp))
                            )
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("RANK ${user.xp / 1000}", style = MaterialTheme.typography.labelSmall, color = RevvoGray.copy(alpha = 0.5f))
                        Text("${1000 - (user.xp % 1000)} XP TO LEVEL UP", style = MaterialTheme.typography.labelSmall, color = RevvoGray.copy(alpha = 0.5f))
                        Text("RANK ${user.xp / 1000 + 1}", style = MaterialTheme.typography.labelSmall, color = RevvoGray.copy(alpha = 0.5f))
                    }
                }
            }
        }

        // Stats
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatCard(Modifier.weight(1f), Icons.Default.Route, "TOTAL RIDES", "${user.totalRides}", RevvoOrangeLight)
                StatCard(Modifier.weight(1f), Icons.Default.Motorcycle, "DISTANCE", "${user.totalDistanceKm}", RevvoOrange, "KM")
            }
        }

        // Sign out
        item {
            OutlinedButton(
                onClick = onSignOut,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, RevvoOrange.copy(alpha = 0.4f))
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    tint = RevvoOrange,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "SIGN OUT",
                    color = RevvoOrange,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }

        item { Spacer(modifier = Modifier.height(120.dp)) }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier,
    icon: ImageVector,
    label: String,
    value: String,
    accentColor: Color,
    unit: String? = null
) {
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
private fun ProfileMetaItem(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(icon, null, modifier = Modifier.size(14.dp), tint = RevvoGray)
        Text(text.uppercase(), style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, color = RevvoGray, letterSpacing = 1.sp)
    }
}

@Composable
private fun CenteredProgress() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = RevvoOrange)
    }
}

@Composable
private fun CenteredMessage(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text, color = RevvoWhite, style = MaterialTheme.typography.bodyLarge)
    }
}
