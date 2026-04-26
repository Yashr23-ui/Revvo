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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Motorcycle
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.revvo.ui.theme.RevvoDark
import com.revvo.ui.theme.RevvoGray
import com.revvo.ui.theme.RevvoOrange
import com.revvo.ui.theme.RevvoRed
import com.revvo.ui.theme.RevvoSurface
import com.revvo.ui.theme.RevvoSurfaceLight
import com.revvo.ui.theme.RevvoWhite
import com.revvo.viewmodel.CreateRideForm
import com.revvo.viewmodel.RideEvent
import com.revvo.viewmodel.RideViewModel
import kotlinx.coroutines.flow.collectLatest

/**
 * Create Ride screen.
 *
 * Form state lives here as `rememberSaveable` (survives config changes & process death).
 * Submission delegates to [RideViewModel.createRide], which validates & emits a
 * [RideEvent.RideCreated] with the new id — the screen then asks the host to navigate.
 */
@Composable
fun CreateRideScreen(
    rideViewModel: RideViewModel,
    onBack: () -> Unit,
    onRideCreated: (rideId: String) -> Unit
) {
    var title by rememberSaveable { mutableStateOf("") }
    var start by rememberSaveable { mutableStateOf("") }
    var destination by rememberSaveable { mutableStateOf("") }
    var dateTime by rememberSaveable { mutableStateOf("") }
    var maxRiders by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    var fieldErrors by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    // Listen for one-shot events from the VM (success / validation / error).
    LaunchedEffect(Unit) {
        rideViewModel.events.collectLatest { event ->
            when (event) {
                is RideEvent.RideCreated -> onRideCreated(event.rideId)
                is RideEvent.ValidationFailed -> fieldErrors = event.errors
                is RideEvent.Error -> { /* TODO: show snackbar */ }
                is RideEvent.RideJoined -> Unit
            }
        }
    }

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
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = RevvoWhite
                        )
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
                    RevvoInput(
                        label = "RIDE TITLE",
                        placeholder = "Morning Canyon Blast",
                        icon = Icons.Default.Motorcycle,
                        value = title,
                        onValueChange = { title = it; fieldErrors = fieldErrors - "title" },
                        error = fieldErrors["title"]
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        RevvoInput(
                            label = "START",
                            placeholder = "Santa Monica Pier",
                            icon = Icons.Default.Navigation,
                            modifier = Modifier.weight(1f),
                            value = start,
                            onValueChange = { start = it; fieldErrors = fieldErrors - "startLocation" },
                            error = fieldErrors["startLocation"]
                        )
                        RevvoInput(
                            label = "DESTINATION",
                            placeholder = "Mulholland Drive",
                            icon = Icons.Default.LocationOn,
                            modifier = Modifier.weight(1f),
                            value = destination,
                            onValueChange = { destination = it; fieldErrors = fieldErrors - "endLocation" },
                            error = fieldErrors["endLocation"]
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        RevvoInput(
                            label = "DATE & TIME",
                            placeholder = "mm/dd/yyyy, --:--",
                            icon = Icons.Default.CalendarToday,
                            modifier = Modifier.weight(1f),
                            value = dateTime,
                            onValueChange = { dateTime = it; fieldErrors = fieldErrors - "dateTime" },
                            error = fieldErrors["dateTime"]
                        )
                        RevvoInput(
                            label = "MAX RIDERS",
                            placeholder = "12",
                            icon = Icons.Default.Groups,
                            modifier = Modifier.weight(1f),
                            value = maxRiders,
                            onValueChange = { maxRiders = it; fieldErrors = fieldErrors - "maxRiders" },
                            error = fieldErrors["maxRiders"]
                        )
                    }
                    RevvoInput(
                        label = "DESCRIPTION",
                        placeholder = "Briefing: Intermediate pace...",
                        icon = Icons.Default.EditNote,
                        minHeight = 120.dp,
                        value = description,
                        onValueChange = { description = it }
                    )
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
                        Row(
                            modifier = Modifier.align(Alignment.BottomStart).padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
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
                    onClick = {
                        rideViewModel.createRide(
                            CreateRideForm(
                                title = title,
                                startLocation = start,
                                endLocation = destination,
                                dateTime = dateTime,
                                maxRiders = maxRiders,
                                description = description
                            )
                        )
                    },
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
private fun RevvoInput(
    label: String,
    placeholder: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    minHeight: Dp = 56.dp,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    error: String? = null
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            color = RevvoGray.copy(alpha = 0.6f),
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            isError = error != null,
            modifier = Modifier.fillMaxWidth().heightIn(min = minHeight),
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyLarge,
                    color = RevvoGray.copy(alpha = 0.3f)
                )
            },
            leadingIcon = {
                Icon(icon, null, modifier = Modifier.size(20.dp), tint = RevvoOrange.copy(alpha = 0.6f))
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = RevvoSurfaceLight.copy(alpha = 0.3f),
                unfocusedContainerColor = RevvoSurfaceLight.copy(alpha = 0.3f),
                errorContainerColor = RevvoRed.copy(alpha = 0.1f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                cursorColor = RevvoOrange,
                focusedTextColor = RevvoWhite,
                unfocusedTextColor = RevvoWhite,
                errorTextColor = RevvoWhite
            ),
            shape = RoundedCornerShape(12.dp)
        )
        if (error != null) {
            Text(
                text = error,
                style = MaterialTheme.typography.labelSmall,
                color = RevvoRed,
                fontSize = 11.sp
            )
        }
    }
}
