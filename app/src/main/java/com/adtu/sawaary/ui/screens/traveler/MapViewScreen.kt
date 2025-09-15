package com.adtu.sawaary.ui.screens.traveler

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adtu.sawaary.utils.PermissionHelper
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapViewScreen(
    onNavigateBack: () -> Unit,
    onBusSelected: (String) -> Unit
) {
    val nearbyBuses = remember { getNearbyBuses() }
    val context = LocalContext.current
    val hasLocationPermission = PermissionHelper.hasLocationPermission(context)
    
    // Default location (Delhi)
    val defaultLocation = LatLng(28.6139, 77.2090)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 12f)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bus Map") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Refresh buses */ }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
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
            // Google Map
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(16.dp),
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
                    // Add markers for nearby buses
                    nearbyBuses.forEach { bus ->
                        Marker(
                            state = MarkerState(position = LatLng(bus.latitude, bus.longitude)),
                            title = bus.route,
                            snippet = "ETA: ${bus.eta} | Distance: ${bus.distance}"
                        )
                    }
                }
            }
            
            // Bus List
            Text(
                text = "Nearby Buses",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(16.dp)
            )
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(nearbyBuses) { bus ->
                    BusMapCard(
                        bus = bus,
                        onClick = { onBusSelected(bus.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun BusMapCard(
    bus: MapBusData,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DirectionsBus,
                contentDescription = "Bus",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = bus.route,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                Text(
                    text = "License: ${bus.licensePlate}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = "Location: ${String.format("%.4f", bus.latitude)}, ${String.format("%.4f", bus.longitude)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = bus.eta,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    text = bus.distance,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = "Speed",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.width(4.dp))
                    
                    Text(
                        text = "${bus.speed} km/h",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// Bus data for map view
data class MapBusData(
    val id: String,
    val route: String,
    val licensePlate: String,
    val eta: String,
    val distance: String,
    val latitude: Double,
    val longitude: Double,
    val speed: Int
)

private fun getNearbyBuses(): List<MapBusData> {
    return listOf(
        MapBusData("1", "Route 101 - Downtown", "ABC-123", "5 min", "0.8 km", 28.6140, 77.2091, 25),
        MapBusData("2", "Route 102 - Airport", "XYZ-789", "12 min", "2.1 km", 28.6150, 77.2100, 30),
        MapBusData("3", "Route 103 - University", "DEF-456", "8 min", "1.5 km", 28.6160, 77.2110, 20)
    )
}