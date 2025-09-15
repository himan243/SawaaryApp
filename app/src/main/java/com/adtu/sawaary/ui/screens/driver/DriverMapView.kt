package com.adtu.sawaary.ui.screens.driver

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
import com.adtu.sawaary.utils.PermissionHelper
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun DriverMapView(
    isTracking: Boolean,
    isPaused: Boolean,
    onPauseResume: () -> Unit,
    onStopTrip: () -> Unit,
    startPoint: LatLng? = null,
    endPoint: LatLng? = null,
    modifier: Modifier = Modifier
) {
    val defaultLocation = LatLng(28.6139, 77.2090) // Delhi coordinates
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 15f)
    }
    
    val context = LocalContext.current
    val hasLocationPermission = PermissionHelper.hasLocationPermission(context)
    
    // Use provided points or default
    val actualStartPoint = startPoint ?: defaultLocation
    val actualEndPoint = endPoint ?: LatLng(28.6155, 77.2105)
    
    // Simulate driver's current location (start at start point)
    var driverLocation by remember { mutableStateOf(actualStartPoint) }
    
    // Create route waypoints
    val routeWaypoints = remember(actualStartPoint, actualEndPoint) {
        listOf(actualStartPoint, actualEndPoint)
    }
    
    Box(modifier = modifier) {
        // Google Map
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
            // Draw route polyline
            Polyline(
                points = routeWaypoints,
                color = Color.Blue,
                width = 8f
            )
            
            // Start marker
            Marker(
                state = MarkerState(position = actualStartPoint),
                title = "Start Point",
                snippet = "Trip starting location"
            )
            
            // End marker
            Marker(
                state = MarkerState(position = actualEndPoint),
                title = "End Point",
                snippet = "Trip destination"
            )
            
            // Current driver location
            Marker(
                state = MarkerState(position = driverLocation),
                title = "Your Location",
                snippet = if (isTracking && !isPaused) "Sharing location" else "Location paused"
            )
        }
        
        // Top overlay with trip info
        Card(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Route 101 - Downtown",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = if (isTracking && !isPaused) "Live tracking" else "Paused",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isTracking && !isPaused) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Icon(
                        imageVector = if (isTracking && !isPaused) Icons.Default.GpsFixed else Icons.Default.GpsNotFixed,
                        contentDescription = "GPS Status",
                        tint = if (isTracking && !isPaused) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TripInfoItem("Distance", "12.5 km")
                    TripInfoItem("ETA", "45 min")
                    TripInfoItem("Speed", "25 km/h")
                }
            }
        }
        
        // Bottom controls
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onPauseResume,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPaused) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Icon(
                        imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                        contentDescription = if (isPaused) "Resume" else "Pause"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isPaused) "Resume" else "Pause")
                }
                
                Button(
                    onClick = onStopTrip,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(Icons.Default.Stop, contentDescription = "Stop")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("End Trip")
                }
            }
        }
        
        // Floating action button for navigation
        FloatingActionButton(
            onClick = { /* Navigate to next waypoint */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                Icons.Default.Directions,
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun TripInfoItem(
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
