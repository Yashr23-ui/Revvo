package com.revvo.ui.navigation

/**
 * All route strings live here so typos don't compile.
 *
 * For Phase 3+ consider migrating to type-safe Navigation 2.8 with `@Serializable` route
 * objects. Strings are fine for now — we're a small graph.
 */
object Routes {
    const val HOME = "home"
    const val RIDES = "rides"
    const val CREATE_RIDE = "create_ride"
    const val MAP = "map"
    const val PROFILE = "profile"

    /** Detail screen takes a rideId argument: navigate("ride_details/abc123"). */
    const val RIDE_DETAILS_ARG = "rideId"
    const val RIDE_DETAILS = "ride_details/{$RIDE_DETAILS_ARG}"

    fun rideDetails(rideId: String) = "ride_details/$rideId"

    /** The five tabs that show the bottom nav. */
    val bottomNavRoutes = setOf(HOME, RIDES, CREATE_RIDE, MAP, PROFILE)
}
