package com.adtu.sawaary.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.adtu.sawaary.ui.screens.auth.LoginScreen
import com.adtu.sawaary.ui.screens.auth.RegisterScreen
import com.adtu.sawaary.ui.screens.auth.UserTypeSelectionScreen
import com.adtu.sawaary.ui.screens.auth.WelcomeScreen
import com.adtu.sawaary.ui.screens.driver.ActiveTripScreen
import com.adtu.sawaary.ui.screens.driver.DriverDashboardScreen
import com.adtu.sawaary.ui.screens.driver.DriverProfileScreen
import com.adtu.sawaary.ui.screens.driver.TripHistoryScreen
import com.adtu.sawaary.ui.screens.driver.TripSetupScreen
import com.adtu.sawaary.ui.screens.traveler.BusDetailsScreen
import com.adtu.sawaary.ui.screens.traveler.BusListScreen
import com.adtu.sawaary.ui.screens.traveler.MapViewScreen
import com.adtu.sawaary.ui.screens.traveler.ShareLocationScreen
import com.adtu.sawaary.ui.screens.traveler.TravelerDashboardScreen
import com.adtu.sawaary.ui.screens.traveler.TravelerSettingsScreen

@Composable
fun SawaaryNavigation(
    navController: NavHostController,
    startDestination: String = Screen.Welcome.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Authentication screens
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }
        
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToUserTypeSelection = {
                    navController.navigate(Screen.UserTypeSelection.route)
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                },
                onNavigateToUserTypeSelection = {
                    navController.navigate(Screen.UserTypeSelection.route)
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.UserTypeSelection.route) {
            UserTypeSelectionScreen(
                onDriverSelected = {
                    navController.navigate(Screen.DriverDashboard.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onTravelerSelected = {
                    navController.navigate(Screen.TravelerDashboard.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }
        
        // Driver screens
        composable(Screen.DriverDashboard.route) {
            DriverDashboardScreen(
                onNavigateToProfile = {
                    navController.navigate(Screen.DriverProfile.route)
                },
                onNavigateToTripSetup = {
                    navController.navigate(Screen.TripSetup.route)
                },
                onNavigateToTripHistory = {
                    navController.navigate(Screen.TripHistory.route)
                },
                onStartTrip = { tripId ->
                    navController.navigate("${Screen.ActiveTrip.route}/$tripId")
                }
            )
        }
        
        composable(Screen.DriverProfile.route) {
            DriverProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.TripSetup.route) {
            TripSetupScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onTripCreated = { tripId ->
                    navController.navigate("${Screen.ActiveTrip.route}/$tripId") {
                        popUpTo(Screen.DriverDashboard.route)
                    }
                }
            )
        }
        
        composable("${Screen.ActiveTrip.route}/{tripId}") { backStackEntry ->
            val tripId = backStackEntry.arguments?.getString("tripId") ?: ""
            ActiveTripScreen(
                tripId = tripId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onTripCompleted = {
                    navController.navigate(Screen.DriverDashboard.route) {
                        popUpTo(Screen.DriverDashboard.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.TripHistory.route) {
            TripHistoryScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        // Traveler screens
        composable(Screen.TravelerDashboard.route) {
            TravelerDashboardScreen(
                onNavigateToMapView = {
                    navController.navigate(Screen.MapView.route)
                },
                onNavigateToBusList = {
                    navController.navigate(Screen.BusList.route)
                },
                onNavigateToShareLocation = {
                    navController.navigate(Screen.ShareLocation.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
        
        composable(Screen.MapView.route) {
            MapViewScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onBusSelected = { busId ->
                    navController.navigate("${Screen.BusDetails.route}/$busId")
                }
            )
        }
        
        composable(Screen.BusList.route) {
            BusListScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onBusSelected = { busId ->
                    navController.navigate("${Screen.BusDetails.route}/$busId")
                }
            )
        }
        
        composable("${Screen.BusDetails.route}/{busId}") { backStackEntry ->
            val busId = backStackEntry.arguments?.getString("busId") ?: ""
            BusDetailsScreen(
                busId = busId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.ShareLocation.route) {
            ShareLocationScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Settings.route) {
            TravelerSettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
