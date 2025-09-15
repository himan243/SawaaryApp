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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.LatLng

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveTripScreen(
    tripId: String,
    startPoint: LatLng? = null,
    endPoint: LatLng? = null,
    onNavigateBack: () -> Unit,
    onTripCompleted: () -> Unit
) {
    var isTracking by remember { mutableStateOf(true) }
    var isPaused by remember { mutableStateOf(false) }
    var currentSpeed by remember { mutableStateOf(0) }
    var distanceTraveled by remember { mutableStateOf(0.0) }
    var passengersCount by remember { mutableStateOf(0) }
    var showRouteView by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Active Trip") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showRouteView = !showRouteView }) {
                        Icon(
                            imageVector = if (showRouteView) Icons.Default.Dashboard else Icons.Default.Map,
                            contentDescription = if (showRouteView) "Dashboard" else "Route View"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (showRouteView) {
            // Google Maps Route View
            DriverMapView(
                isTracking = isTracking,
                isPaused = isPaused,
                onPauseResume = { isPaused = !isPaused },
                onStopTrip = onTripCompleted,
                startPoint = startPoint,
                endPoint = endPoint,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        } else {
            // Dashboard View
            DashboardView(
                isTracking = isTracking,
                isPaused = isPaused,
                currentSpeed = currentSpeed,
                distanceTraveled = distanceTraveled,
                passengersCount = passengersCount,
                onPauseResume = { isPaused = !isPaused },
                onStopTrip = onTripCompleted,
                onPassengerCountChange = { passengersCount = it },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
private fun RouteView(
    tripId: String,
    isTracking: Boolean,
    isPaused: Boolean,
    onPauseResume: () -> Unit,
    onStopTrip: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        // Route Map Placeholder
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Route,
                        contentDescription = "Route",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Route View",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Interactive route map with real-time location",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Text(
                                text = "Start: Central Station",
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Text(
                                text = "End: Downtown Mall",
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
        
        // Route Information
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
                    text = "Route Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                RouteInfoItem(
                    icon = Icons.Default.Straighten,
                    label = "Total Distance",
                    value = "25.3 km"
                )
                
                RouteInfoItem(
                    icon = Icons.Default.Schedule,
                    label = "Estimated Time",
                    value = "1h 15m"
                )
                
                RouteInfoItem(
                    icon = Icons.Default.Speed,
                    label = "Average Speed",
                    value = "20 km/h"
                )
                
                RouteInfoItem(
                    icon = Icons.Default.LocationOn,
                    label = "Next Stop",
                    value = "Main Street (2.1 km)"
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Trip Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
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
}

@Composable
private fun DashboardView(
    isTracking: Boolean,
    isPaused: Boolean,
    currentSpeed: Int,
    distanceTraveled: Double,
    passengersCount: Int,
    onPauseResume: () -> Unit,
    onStopTrip: () -> Unit,
    onPassengerCountChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Trip Status Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isTracking && !isPaused) 
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
                    imageVector = if (isPaused) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPaused) "Paused" else "Active",
                    modifier = Modifier.size(48.dp),
                    tint = if (isTracking && !isPaused) 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = if (isPaused) "Trip Paused" else "Trip Active",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = if (isPaused) 
                        "Location tracking is paused" 
                    else 
                        "Sharing location with passengers",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        // Trip Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
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
        
        // Trip Statistics
        Text(
            text = "Trip Statistics",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Speed",
                value = "${currentSpeed} km/h",
                icon = Icons.Default.Speed,
                modifier = Modifier.weight(1f)
            )
            
            StatCard(
                title = "Distance",
                value = "${String.format("%.1f", distanceTraveled)} km",
                icon = Icons.Default.Straighten,
                modifier = Modifier.weight(1f)
            )
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Passengers",
                value = passengersCount.toString(),
                icon = Icons.Default.People,
                modifier = Modifier.weight(1f)
            )
            
            StatCard(
                title = "Duration",
                value = "1h 23m",
                icon = Icons.Default.Schedule,
                modifier = Modifier.weight(1f)
            )
        }
        
        // Passenger Management
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Passenger Count",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { if (passengersCount > 0) onPassengerCountChange(passengersCount - 1) }
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease")
                    }
                    
                    Text(
                        text = passengersCount.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    IconButton(
                        onClick = { onPassengerCountChange(passengersCount + 1) }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase")
                    }
                }
            }
        }
    }
}

@Composable
private fun RouteInfoItem(
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

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}