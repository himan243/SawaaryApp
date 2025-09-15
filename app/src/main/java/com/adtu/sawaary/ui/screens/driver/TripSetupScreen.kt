package com.adtu.sawaary.ui.screens.driver

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripSetupScreen(
    onNavigateBack: () -> Unit,
    onTripCreated: (String) -> Unit
) {
    var showMapSelection by remember { mutableStateOf(false) }
    
    if (showMapSelection) {
        TripSetupWithMap(
            onNavigateBack = { showMapSelection = false },
            onTripStarted = { routeName, startPoint, endPoint ->
                onTripCreated(routeName)
            }
        )
    } else {
    var startLocation by remember { mutableStateOf("") }
    var endLocation by remember { mutableStateOf("") }
    var routeName by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Setup New Trip") },
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
            // Route Name
            OutlinedTextField(
                value = routeName,
                onValueChange = { routeName = it },
                label = { Text("Route Name") },
                leadingIcon = {
                    Icon(Icons.Default.Route, contentDescription = "Route")
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            
            // Start Location
            OutlinedTextField(
                value = startLocation,
                onValueChange = { startLocation = it },
                label = { Text("Start Location") },
                leadingIcon = {
                    Icon(Icons.Default.MyLocation, contentDescription = "Start")
                },
                trailingIcon = {
                    IconButton(onClick = { /* Get current location */ }) {
                        Icon(Icons.Default.GpsFixed, contentDescription = "Current Location")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            
            // End Location
            OutlinedTextField(
                value = endLocation,
                onValueChange = { endLocation = it },
                label = { Text("End Location") },
                leadingIcon = {
                    Icon(Icons.Default.LocationOn, contentDescription = "End")
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Map Selection Button
            Button(
                onClick = { showMapSelection = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Map, contentDescription = "Map")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Select Route on Map",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
    }
}
