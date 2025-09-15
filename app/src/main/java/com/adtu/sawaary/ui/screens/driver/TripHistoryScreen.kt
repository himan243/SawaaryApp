package com.adtu.sawaary.ui.screens.driver

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripHistoryScreen(
    onNavigateBack: () -> Unit
) {
    val tripHistory = remember { getTripHistory() }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trip History") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(tripHistory) { trip ->
                TripHistoryCard(trip = trip)
            }
        }
    }
}

@Composable
private fun TripHistoryCard(
    trip: TripHistoryData
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = trip.route,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                Text(
                    text = trip.status,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = trip.date,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = trip.duration,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Distance: ${trip.distance} km",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = "Passengers: ${trip.passengers}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

data class TripHistoryData(
    val id: String,
    val route: String,
    val date: String,
    val duration: String,
    val distance: String,
    val passengers: Int,
    val status: String
)

private fun getTripHistory(): List<TripHistoryData> {
    return listOf(
        TripHistoryData("1", "Route 101 - Downtown", "Today, 2:30 PM", "1h 15m", "25.3", 45, "Completed"),
        TripHistoryData("2", "Route 102 - Airport", "Today, 1:15 PM", "45m", "18.7", 32, "Completed"),
        TripHistoryData("3", "Route 103 - University", "Today, 12:00 PM", "1h 30m", "22.1", 38, "Completed"),
        TripHistoryData("4", "Route 101 - Downtown", "Yesterday, 5:45 PM", "1h 20m", "24.8", 42, "Completed"),
        TripHistoryData("5", "Route 102 - Airport", "Yesterday, 4:30 PM", "50m", "19.2", 28, "Completed")
    )
}

