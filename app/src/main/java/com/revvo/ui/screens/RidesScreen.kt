package com.revvo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revvo.ui.components.RideCard
import com.revvo.ui.theme.RevvoDark
import com.revvo.ui.theme.RevvoGray
import com.revvo.ui.theme.RevvoWhite
import com.revvo.viewmodel.RideViewModel

@Composable
fun RidesScreen(
    rideViewModel: RideViewModel,
    onRideClick: (String) -> Unit
) {
    val rideCards by rideViewModel.rideCards.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RevvoDark)
    ) {
        if (rideCards.isEmpty()) {
            Text(
                text = "No rides yet",
                color = RevvoGray,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 80.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 60.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "AVAILABLE RIDES",
                    style = MaterialTheme.typography.headlineLarge,
                    color = RevvoWhite,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // KEY enables stable identity across list updates -> animations work, scroll preserved.
            items(rideCards, key = { it.rideId }) { ride ->
                RideCard(
                    ride = ride,
                    onClick = onRideClick
                )
            }
        }
    }
}
