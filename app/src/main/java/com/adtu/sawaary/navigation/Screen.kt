package com.adtu.sawaary.navigation

sealed class Screen(val route: String) {
    // Authentication screens
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object UserTypeSelection : Screen("user_type_selection")
    
    // Driver screens
    object DriverDashboard : Screen("driver_dashboard")
    object DriverProfile : Screen("driver_profile")
    object TripSetup : Screen("trip_setup")
    object ActiveTrip : Screen("active_trip")
    object TripHistory : Screen("trip_history")
    
    // Traveler screens
    object TravelerDashboard : Screen("traveler_dashboard")
    object MapView : Screen("map_view")
    object BusList : Screen("bus_list")
    object BusDetails : Screen("bus_details")
    object ShareLocation : Screen("share_location")
    object Settings : Screen("settings")
    
    // Common screens
    object Profile : Screen("profile")
    object About : Screen("about")
}

sealed class AuthScreen(val route: String) {
    object Welcome : AuthScreen("auth/welcome")
    object Login : AuthScreen("auth/login")
    object Register : AuthScreen("auth/register")
    object UserTypeSelection : AuthScreen("auth/user_type_selection")
}

sealed class DriverScreen(val route: String) {
    object Dashboard : DriverScreen("driver/dashboard")
    object Profile : DriverScreen("driver/profile")
    object TripSetup : DriverScreen("driver/trip_setup")
    object ActiveTrip : DriverScreen("driver/active_trip")
    object TripHistory : DriverScreen("driver/trip_history")
}

sealed class TravelerScreen(val route: String) {
    object Dashboard : TravelerScreen("traveler/dashboard")
    object MapView : TravelerScreen("traveler/map_view")
    object BusList : TravelerScreen("traveler/bus_list")
    object BusDetails : TravelerScreen("traveler/bus_details")
    object Settings : TravelerScreen("traveler/settings")
}
