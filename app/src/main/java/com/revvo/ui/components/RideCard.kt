package com.revvo.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revvo.data.model.RideStatus
import com.revvo.ui.model.RideCardData
import com.revvo.ui.theme.RevvoGray
import com.revvo.ui.theme.RevvoGrayDark
import com.revvo.ui.theme.RevvoGreen
import com.revvo.ui.theme.RevvoOrange
import com.revvo.ui.theme.RevvoRed
import com.revvo.ui.theme.RevvoSurface
import com.revvo.ui.theme.RevvoWhite
import com.revvo.ui.theme.RevvoYellow

/**
 * Reusable ride list-item card.
 *
 * Note: [RideCardData] now lives in `com.revvo.ui.model` (not co-located with this
 * composable). UI state classes are owned by the model layer, not by individual components.
 */
@Composable
fun RideCard(
    ride: RideCardData,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = tween(100),
        label = "cardScale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(RevvoSurface)
            .border(width = 0.5.dp, color = RevvoGrayDark, shape = RoundedCornerShape(16.dp))
            .then(
                Modifier.border(
                    width = 2.dp,
                    color = RevvoOrange,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        bottomStart = 16.dp,
                        topEnd = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
            )
            .clickable {
                pressed = true
                onClick(ride.rideId)
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Title + Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ride.title.uppercase(),
                    style = MaterialTheme.typography.titleLarge,
                    color = RevvoWhite,
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
                text = "by ${ride.organizer}",
                style = MaterialTheme.typography.bodyMedium,
                color = RevvoGray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Meta row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MetaDot(text = ride.distance)
                MetaDot(text = "${ride.memberCount}/${ride.maxMembers} riders")
                MetaDot(text = ride.startLocation)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // CTA — text reflects status, not "is full"
            val isFull = ride.memberCount >= ride.maxMembers
            val btnText = when (ride.status) {
                RideStatus.UPCOMING -> if (isFull) "RIDE FULL" else "JOIN RIDE"
                RideStatus.LIVE -> "TRACK LIVE"
                RideStatus.COMPLETED -> "VIEW RECAP"
                RideStatus.CANCELLED -> "CANCELLED"
            }
            val btnEnabled = ride.status != RideStatus.CANCELLED &&
                !(ride.status == RideStatus.UPCOMING && isFull)

            Button(
                onClick = { onClick(ride.rideId) },
                enabled = btnEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RevvoOrange,
                    disabledContainerColor = RevvoGrayDark
                )
            ) {
                Text(
                    text = btnText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = RevvoWhite,
                    letterSpacing = 2.sp
                )
            }
        }
    }
}

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
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = RevvoGray
        )
    }
}

@Composable
fun StatusBadge(status: RideStatus) {
    val pulseAlphaValue = if (status == RideStatus.LIVE) pulseAlpha() else 1f
    val label = when (status) {
        RideStatus.UPCOMING -> "UPCOMING"
        RideStatus.LIVE -> "● LIVE"
        RideStatus.COMPLETED -> "DONE"
        RideStatus.CANCELLED -> "CANCELLED"
    }
    val color = when (status) {
        RideStatus.UPCOMING -> RevvoYellow
        RideStatus.LIVE -> RevvoGreen
        RideStatus.COMPLETED -> RevvoGray
        RideStatus.CANCELLED -> RevvoRed
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .alpha(pulseAlphaValue)
    ) {
        Text(
            text = label,
            fontSize = 9.sp,
            color = color,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp
        )
    }
}
