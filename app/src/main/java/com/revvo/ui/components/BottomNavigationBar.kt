package com.revvo.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.revvo.ui.theme.*
import androidx.compose.ui.unit.dp

// Define each tab
sealed class BottomNavItem(
    val route : String,
    val label : String,
    val icon  : ImageVector
) {
    object Home    : BottomNavItem("home",    "Home",    Icons.Default.Home)
    object Rides   : BottomNavItem("rides",   "Rides",   Icons.Default.DirectionsBike)
    object Create  : BottomNavItem("create",  "Create",  Icons.Default.AddCircle)
    object Map     : BottomNavItem("map",     "Map",     Icons.Default.Map)
    object Profile : BottomNavItem("profile", "Profile", Icons.Default.Person)
}

@Composable
fun BottomNavigationBar(
    currentRoute    : String,
    onItemSelected  : (String) -> Unit
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Rides,
        BottomNavItem.Create,
        BottomNavItem.Map,
        BottomNavItem.Profile
    )

    NavigationBar(
        containerColor = RevvoSurface,
        tonalElevation = 0.dp
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route

            NavigationBarItem(
                selected = selected,
                onClick  = { onItemSelected(item.route) },
                icon     = {
                    Icon(
                        imageVector        = item.icon,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(text = item.label, style = MaterialTheme.typography.labelSmall)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor   = RevvoOrange,
                    selectedTextColor   = RevvoOrange,
                    unselectedIconColor = RevvoGray,
                    unselectedTextColor = RevvoGray,
                    indicatorColor      = RevvoOrange.copy(alpha = 0.15f)
                )
            )
        }
    }
}