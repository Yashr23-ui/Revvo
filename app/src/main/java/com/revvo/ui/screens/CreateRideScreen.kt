package com.revvo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revvo.viewmodel.RideViewModel
import com.revvo.ui.components.AnimatedScreenEntry
import com.revvo.ui.theme.*

@Composable
fun CreateRideScreen(
    onBack        : () -> Unit,
    onRideCreated : () -> Unit,
    rideViewModel : RideViewModel
) {
    var rideTitle   by remember { mutableStateOf("") }
    var startPoint  by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    var rideDate    by remember { mutableStateOf("") }
    var maxRiders   by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RevvoDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 40.dp)
        ) {

            // ── Top bar ───────────────────────────────────────────────
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
                            text          = "NEW RIDE",
                            fontSize      = 10.sp,
                            color         = RevvoOrange,
                            fontWeight    = FontWeight.Black,
                            letterSpacing = 3.sp
                        )
                        Text(
                            text       = "Create a Ride",
                            style      = MaterialTheme.typography.headlineMedium,
                            color      = RevvoWhite,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ── Form fields ───────────────────────────────────────────
            val fields = listOf(
                Triple(Icons.Default.TwoWheeler, "RIDE TITLE",     rideTitle),
                Triple(Icons.Default.NearMe,     "STARTING POINT", startPoint),
                Triple(Icons.Default.LocationOn, "DESTINATION",    destination),
                Triple(Icons.Default.CalendarMonth,"DATE & TIME",  rideDate),
                Triple(Icons.Default.People,     "MAX RIDERS",     maxRiders),
                Triple(Icons.Default.EditNote,   "DESCRIPTION",    description)
            )

            fields.forEachIndexed { index, (icon, label, value) ->
                AnimatedScreenEntry(delayMs = 80 + index * 60) {
                    AggressiveTextField(
                        value    = value,
                        onChange = { new ->
                            when (index) {
                                0 -> rideTitle   = new
                                1 -> startPoint  = new
                                2 -> destination = new
                                3 -> rideDate    = new
                                4 -> maxRiders   = new
                                5 -> description = new
                            }
                        },
                        label        = label,
                        icon         = icon,
                        keyboardType = if (index == 4) KeyboardType.Number else KeyboardType.Text,
                        maxLines     = if (index == 5) 3 else 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Create button ─────────────────────────────────────────
            AnimatedScreenEntry(delayMs = 600) {
                Button(
                    onClick  = {
                        val parsedMaxRiders = maxRiders.toIntOrNull()?.coerceAtLeast(1) ?: 1
                        rideViewModel.createRide(
                            title = rideTitle.trim(),
                            location = startPoint.trim(),
                            date = rideDate.trim(),
                            distance = destination.trim(),
                            maxRiders = parsedMaxRiders,
                            description = description.trim()
                        )
                        onRideCreated()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 20.dp),
                    shape  = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RevvoOrange)
                ) {
                    Text(
                        text          = "CREATE RIDE",
                        fontSize      = 14.sp,
                        fontWeight    = FontWeight.Black,
                        color         = RevvoWhite,
                        letterSpacing = 3.sp
                    )
                }
            }
        }
    }
}

// ── Aggressive styled text field ──────────────────────────────────────────────
@Composable
fun AggressiveTextField(
    value        : String,
    onChange     : (String) -> Unit,
    label        : String,
    icon         : androidx.compose.ui.graphics.vector.ImageVector,
    keyboardType : KeyboardType = KeyboardType.Text,
    maxLines     : Int = 1
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
    ) {
        Text(
            text          = label,
            fontSize      = 9.sp,
            color         = RevvoGray,
            fontWeight    = FontWeight.Black,
            letterSpacing = 2.sp,
            modifier      = Modifier.padding(start = 4.dp, bottom = 6.dp)
        )
        OutlinedTextField(
            value         = value,
            onValueChange = onChange,
            placeholder   = { Text("—", color = RevvoGrayDark) },
            leadingIcon   = {
                Icon(imageVector = icon, contentDescription = null, tint = RevvoOrange, modifier = Modifier.size(20.dp))
            },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            maxLines        = maxLines,
            shape           = RoundedCornerShape(12.dp),
            colors          = OutlinedTextFieldDefaults.colors(
                focusedBorderColor      = RevvoOrange,
                unfocusedBorderColor    = RevvoGrayDark,
                focusedLabelColor       = RevvoOrange,
                cursorColor             = RevvoOrange,
                focusedTextColor        = RevvoWhite,
                unfocusedTextColor      = RevvoWhite,
                focusedContainerColor   = RevvoSurface,
                unfocusedContainerColor = RevvoSurface
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}