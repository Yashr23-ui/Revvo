package com.revvo.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.revvo.ui.navigation.Routes
import com.revvo.ui.theme.*

// Define each tab — route strings come from [Routes] so the bottom-bar and the nav
// graph can never drift out of sync.
sealed class BottomNavItem(
    val route : String,
    val label : String,
    val icon  : ImageVector
) {
    object Home    : BottomNavItem(Routes.HOME,        "Home",    Icons.Default.Home)
    object Rides   : BottomNavItem(Routes.RIDES,       "Rides",   Icons.Default.DirectionsBike)
    object Create  : BottomNavItem(Routes.CREATE_RIDE, "Create",  Icons.Default.AddCircle)
    object Map     : BottomNavItem(Routes.MAP,         "Map",     Icons.Default.Map)
    object Profile : BottomNavItem(Routes.PROFILE,     "Profile", Icons.Default.Person)
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