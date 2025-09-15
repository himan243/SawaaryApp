package com.adtu.sawaary.ui.screens.traveler

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adtu.sawaary.service.RealLocationService
import com.adtu.sawaary.utils.PermissionHelper
import kotlinx.coroutines.launch
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareLocationScreen(
    onNavigateBack: () -> Unit
) {
    var isSharingLocation by remember { mutableStateOf(false) }
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var locationAccuracy by remember { mutableStateOf("High") }
    var isLoadingLocation by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    val locationService = remember { RealLocationService(context) }
    val coroutineScope = rememberCoroutineScope()
    val hasLocationPermission = PermissionHelper.hasLocationPermission(context)
    
    // Default location (Delhi)
    val defaultLocation = LatLng(28.6139, 77.2090)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLocation ?: defaultLocation, 15f)
    }
    
    // Update camera when location changes
    LaunchedEffect(currentLocation) {
        currentLocation?.let { location ->
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(location, 15f)
            )
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Share Location") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Location sharing status card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSharingLocation) 
                        MaterialTheme.colorScheme.primaryContainer 
                    else 
                        MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = if (isSharingLocation) Icons.Default.GpsFixed else Icons.Default.GpsNotFixed,
                        contentDescription = "Location Status",
                        modifier = Modifier.size(48.dp),
                        tint = if (isSharingLocation) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text(
                        text = if (isSharingLocation) "Location Sharing Active" else "Location Not Shared",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = if (isSharingLocation) 
                            "Your location is being shared with nearby buses" 
                        else 
                            "Enable location sharing to help buses find you",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // Map view showing current location
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(
                        isMyLocationEnabled = false, // Disable to avoid permission issues
                        mapType = MapType.NORMAL
                    ),
                    uiSettings = MapUiSettings(
                        myLocationButtonEnabled = false,
                        zoomControlsEnabled = true,
                        compassEnabled = true
                    )
                ) {
                    // Show current location marker
                    currentLocation?.let { location ->
                        Marker(
                            state = MarkerState(position = location),
                            title = "Your Location",
                            snippet = "Accuracy: $locationAccuracy"
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Location details
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Location Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    LocationDetailItem(
                        icon = Icons.Default.LocationOn,
                        label = "Current Location",
                        value = currentLocation?.let { 
                            "${String.format("%.4f", it.latitude)}, ${String.format("%.4f", it.longitude)}" 
                        } ?: "Not available"
                    )
                    
                    LocationDetailItem(
                        icon = Icons.Default.GpsFixed,
                        label = "Accuracy",
                        value = locationAccuracy
                    )
                    
                    LocationDetailItem(
                        icon = Icons.Default.AccessTime,
                        label = "Last Updated",
                        value = "Just now"
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Share location button
            Button(
                onClick = { 
                    isSharingLocation = !isSharingLocation
                    if (isSharingLocation) {
                        isLoadingLocation = true
                        coroutineScope.launch {
                            try {
                                if (locationService.isLocationPermissionGranted()) {
                                    val location = locationService.getCurrentLocation()
                                    location?.let {
                                        currentLocation = LatLng(it.latitude, it.longitude)
                                        locationAccuracy = when {
                                            it.accuracy <= 10f -> "High"
                                            it.accuracy <= 50f -> "Medium"
                                            else -> "Low"
                                        }
                                    } ?: run {
                                        // Fallback to default location if GPS not available
                                        currentLocation = defaultLocation
                                        locationAccuracy = "Unknown"
                                    }
                                } else {
                                    // No permission, use default location
                                    currentLocation = defaultLocation
                                    locationAccuracy = "Permission required"
                                }
                            } catch (e: Exception) {
                                // Error getting location, use default
                                currentLocation = defaultLocation
                                locationAccuracy = "Error"
                            } finally {
                                isLoadingLocation = false
                            }
                        }
                    } else {
                        currentLocation = null
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSharingLocation) 
                        MaterialTheme.colorScheme.error 
                    else 
                        MaterialTheme.colorScheme.primary
                )
            ) {
                if (isLoadingLocation) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(
                        imageVector = if (isSharingLocation) Icons.Default.Stop else Icons.Default.Share,
                        contentDescription = if (isSharingLocation) "Stop Sharing" else "Share Location"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isSharingLocation) "Stop Sharing Location" else "Share My Location",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Information card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Info",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Text(
                            text = "How it works",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "• Share your location to help nearby bus drivers find you\n" +
                               "• Your location is only visible to buses within 5km radius\n" +
                               "• Location sharing stops automatically when you board a bus\n" +
                               "• You can stop sharing anytime",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun LocationDetailItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}
