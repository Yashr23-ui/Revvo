package com.revvo.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revvo.ui.theme.*

data class RideCardData(
    val rideId        : String,
    val title         : String,
    val organizer     : String,
    val date          : String,
    val distance      : String,
    val memberCount   : Int,
    val maxMembers    : Int,
    val status        : RideStatus,
    val startLocation : String
)

enum class RideStatus { UPCOMING, LIVE, COMPLETED, CANCELLED }

@Composable
fun RideCard(
    ride     : RideCardData,
    onClick  : (String) -> Unit,
    modifier : Modifier = Modifier
) {
    // Subtle press scale animation
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue   = if (pressed) 0.97f else 1f,
        animationSpec = tween(100),
        label         = "cardScale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            // Orange top border — the racing stripe
            .clip(RoundedCornerShape(16.dp))
            .background(RevvoSurface)
            .border(
                width = 0.5.dp,
                color = RevvoGrayDark,
                shape = RoundedCornerShape(16.dp)
            )
            // Orange left accent line
            .then(
                Modifier.border(
                    width = 2.dp,
                    color = RevvoOrange,
                    shape = RoundedCornerShape(
                        topStart    = 16.dp,
                        bottomStart = 16.dp,
                        topEnd      = 0.dp,
                        bottomEnd   = 0.dp
                    )
                )
            )
            .clickable {
                pressed = true
                onClick(ride.rideId)
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // ── Row 1: Title + Status badge ──────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    text     = ride.title.uppercase(),
                    style    = MaterialTheme.typography.titleLarge,
                    color    = RevvoWhite,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                StatusBadge(status = ride.status)
            }

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text  = "by ${ride.organizer}",
                style = MaterialTheme.typography.bodyMedium,
                color = RevvoGray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ── Row 2: Meta info ─────────────────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MetaDot(text = ride.distance)
                MetaDot(text = "${ride.memberCount}/${ride.maxMembers} riders")
                MetaDot(text = ride.startLocation)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ── CTA Button ───────────────────────────────────────────
            val btnText = when (ride.status) {
                RideStatus.UPCOMING  -> "JOIN RIDE"
                RideStatus.LIVE      -> "TRACK LIVE"
                RideStatus.COMPLETED -> "VIEW RECAP"
                RideStatus.CANCELLED -> "CANCELLED"
            }

            Button(
                onClick  = { onClick(ride.rideId) },
                enabled  = ride.status != RideStatus.CANCELLED,
                modifier = Modifier.fillMaxWidth().height(44.dp),
                shape    = RoundedCornerShape(50.dp),
                colors   = ButtonDefaults.buttonColors(
                    containerColor         = RevvoOrange,
                    disabledContainerColor = RevvoGrayDark
                )
            ) {
                Text(
                    text          = btnText,
                    fontSize      = 12.sp,
                    fontWeight    = FontWeight.Black,
                    color         = RevvoWhite,
                    letterSpacing = 2.sp
                )
            }
        }
    }
}

// ── Orange dot + text ────────────────────────────────────────────────────────
@Composable
fun MetaDot(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(5.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(RevvoOrange)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text  = text,
            style = MaterialTheme.typography.labelSmall,
            color = RevvoGray
        )
    }
}

// ── Status badge ─────────────────────────────────────────────────────────────
@Composable
fun StatusBadge(status: RideStatus) {
    val pulseAlpha = if (status == RideStatus.LIVE) pulseAlpha() else 1f
    val label = when (status) {
        RideStatus.UPCOMING  -> "UPCOMING"
        RideStatus.LIVE      -> "● LIVE"
        RideStatus.COMPLETED -> "DONE"
        RideStatus.CANCELLED -> "CANCELLED"
    }
    val color = when (status) {
        RideStatus.UPCOMING  -> RevvoYellow
        RideStatus.LIVE      -> RevvoGreen
        RideStatus.COMPLETED -> RevvoGray
        RideStatus.CANCELLED -> RevvoRed
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .alpha(pulseAlpha)
    ) {
        Text(
            text       = label,
            fontSize   = 9.sp,
            color      = color,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp
        )
    }
}