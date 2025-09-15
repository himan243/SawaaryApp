package com.adtu.sawaary.ui.screens.traveler

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusDetailsScreen(
    busId: String,
    onNavigateBack: () -> Unit
) {
    val busDetails = remember { getBusDetails(busId) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bus Details") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Bus Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = busDetails.route,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Text(
                                text = "License: ${busDetails.licensePlate}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        Icon(
                            imageVector = Icons.Default.DirectionsBus,
                            contentDescription = "Bus",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        InfoItem(
                            icon = Icons.Default.Schedule,
                            label = "ETA",
                            value = busDetails.eta
                        )
                        
                        InfoItem(
                            icon = Icons.Default.Straighten,
                            label = "Distance",
                            value = busDetails.distance
                        )
                        
                        InfoItem(
                            icon = Icons.Default.Speed,
                            label = "Speed",
                            value = busDetails.speed
                        )
                    }
                }
            }
            
            // Route Information
            Card(
                modifier = Modifier.fillMaxWidth(),
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
                    
                    RouteStop(
                        stop = "Start: ${busDetails.startLocation}",
                        isCompleted = true
                    )
                    
                    RouteStop(
                        stop = "Current: ${busDetails.currentLocation}",
                        isCompleted = false,
                        isCurrent = true
                    )
                    
                    RouteStop(
                        stop = "End: ${busDetails.endLocation}",
                        isCompleted = false
                    )
                }
            }
            
            // Bus Status
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Bus Status",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatusItem(
                            icon = Icons.Default.People,
                            label = "Passengers",
                            value = "${busDetails.passengers}/50"
                        )
                        
                        StatusItem(
                            icon = Icons.Default.AccessTime,
                            label = "Last Update",
                            value = busDetails.lastUpdate
                        )
                    }
                }
            }
            
            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { /* Set alert */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = "Alert",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Set Alert")
                }
                
                Button(
                    onClick = { /* Share location */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.Share,
                        contentDescription = "Share",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Share")
                }
            }
        }
    }
}

@Composable
private fun InfoItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun RouteStop(
    stop: String,
    isCompleted: Boolean,
    isCurrent: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = when {
                isCompleted -> Icons.Default.CheckCircle
                isCurrent -> Icons.Default.RadioButtonChecked
                else -> Icons.Default.RadioButtonUnchecked
            },
            contentDescription = stop,
            modifier = Modifier.size(20.dp),
            tint = when {
                isCompleted -> MaterialTheme.colorScheme.primary
                isCurrent -> MaterialTheme.colorScheme.secondary
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Text(
            text = stop,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isCurrent) 
                MaterialTheme.colorScheme.primary 
            else 
                MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun StatusItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Column {
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

data class BusDetailsData(
    val id: String,
    val route: String,
    val licensePlate: String,
    val eta: String,
    val distance: String,
    val speed: String,
    val startLocation: String,
    val currentLocation: String,
    val endLocation: String,
    val passengers: Int,
    val lastUpdate: String
)

private fun getBusDetails(busId: String): BusDetailsData {
    return BusDetailsData(
        id = busId,
        route = "Route 101 - Downtown",
        licensePlate = "ABC-123",
        eta = "5 min",
        distance = "0.8 km",
        speed = "25 km/h",
        startLocation = "Central Station",
        currentLocation = "Main Street",
        endLocation = "Downtown Mall",
        passengers = 35,
        lastUpdate = "2 min ago"
    )
}

