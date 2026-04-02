package com.revvo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revvo.ui.theme.*

@Composable
fun StatsCard(
    icon    : ImageVector,
    label   : String,       // e.g. "Total Rides"
    value   : String,       // e.g. "24"
    unit    : String = ""   // e.g. "km" or "hrs" — optional
) {
    Card(
        modifier  = Modifier
            .width(150.dp)
            .height(120.dp),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = RevvoSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement   = Arrangement.SpaceBetween
        ) {
            // Icon with orange circular background
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(RevvoOrange.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = icon,
                    contentDescription = label,
                    tint               = RevvoOrange,
                    modifier           = Modifier.size(20.dp)
                )
            }

            // Value + unit
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text       = value,
                    fontSize   = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color      = RevvoWhite
                )
                if (unit.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text     = unit,
                        fontSize = 13.sp,
                        color    = RevvoGray,
                        modifier = Modifier.padding(bottom = 3.dp)
                    )
                }
            }

            // Label
            Text(
                text  = label,
                style = MaterialTheme.typography.bodyMedium,
                color = RevvoGray
            )
        }
    }
}

@Composable
fun AggressiveStatCard(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(RevvoSurface)
            .padding(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = RevvoOrange,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            color = RevvoWhite
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = RevvoGray,
            fontWeight = FontWeight.Bold
        )
    }
}
