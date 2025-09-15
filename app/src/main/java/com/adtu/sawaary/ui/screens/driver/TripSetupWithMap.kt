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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripSetupWithMap(
    onNavigateBack: () -> Unit,
    onTripStarted: (String, LatLng, LatLng) -> Unit
) {
    var startPoint by remember { mutableStateOf<LatLng?>(null) }
    var endPoint by remember { mutableStateOf<LatLng?>(null) }
    var isSelectingStart by remember { mutableStateOf(true) }
    var routeName by remember { mutableStateOf("") }
    
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
                title = { Text("Setup Trip Route") },
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
            // Instructions
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelectingStart) 
                        MaterialTheme.colorScheme.primaryContainer 
                    else 
                        MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isSelectingStart) Icons.Default.PlayArrow else Icons.Default.Flag,
                        contentDescription = "Selection Mode",
                        tint = if (isSelectingStart) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.secondary
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Text(
                        text = if (isSelectingStart) 
                            "Tap on the map to select START point" 
                        else 
                            "Tap on the map to select END point",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isSelectingStart) 
                            MaterialTheme.colorScheme.onPrimaryContainer 
                        else 
                            MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
            
            // Map
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
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
                    ),
                    onMapClick = { latLng ->
                        if (isSelectingStart) {
                            startPoint = latLng
                            isSelectingStart = false
                        } else {
                            endPoint = latLng
                        }
                    }
                ) {
                    // Start point marker
                    startPoint?.let { point ->
                        Marker(
                            state = MarkerState(position = point),
                            title = "Start Point",
                            snippet = "Trip starting location"
                        )
                    }
                    
                    // End point marker
                    endPoint?.let { point ->
                        Marker(
                            state = MarkerState(position = point),
                            title = "End Point",
                            snippet = "Trip destination"
                        )
                    }
                    
                    // Draw route if both points are selected
                    if (startPoint != null && endPoint != null) {
                        Polyline(
                            points = listOf(startPoint!!, endPoint!!),
                            color = Color.Blue,
                            width = 8f
                        )
                    }
                }
            }
            
            // Route details
            if (startPoint != null && endPoint != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Route Details",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        OutlinedTextField(
                            value = routeName,
                            onValueChange = { routeName = it },
                            label = { Text("Route Name (Optional)") },
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = {
                                Icon(Icons.Default.Route, contentDescription = "Route")
                            }
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    startPoint = null
                                    endPoint = null
                                    isSelectingStart = true
                                    routeName = ""
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Clear")
                            }
                            
                            Button(
                                onClick = {
                                    val finalRouteName = routeName.ifEmpty { "Custom Route" }
                                    onTripStarted(finalRouteName, startPoint!!, endPoint!!)
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = "Start")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Start Trip")
                            }
                        }
                    }
                }
            }
            
            // Selection buttons
            if (startPoint == null || endPoint == null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { isSelectingStart = true },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (isSelectingStart) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Start")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Select Start")
                    }
                    
                    OutlinedButton(
                        onClick = { isSelectingStart = false },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (!isSelectingStart) 
                                MaterialTheme.colorScheme.secondary 
                            else 
                                MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Icon(Icons.Default.Flag, contentDescription = "End")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Select End")
                    }
                }
            }
        }
    }
}

