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
fun TravelerSettingsScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
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
            // Profile Section
            Text(
                text = "Profile",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            
            ProfileOption(
                icon = Icons.Default.Person,
                title = "Edit Profile",
                onClick = { /* TODO */ }
            )
            
            ProfileOption(
                icon = Icons.Default.Security,
                title = "Change Password",
                onClick = { /* TODO */ }
            )
            
            // Notifications Section
            Text(
                text = "Notifications",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            
            NotificationOption(
                title = "Bus Arrival Alerts",
                description = "Get notified when your bus is approaching",
                isEnabled = true,
                onToggle = { /* TODO */ }
            )
            
            NotificationOption(
                title = "Route Updates",
                description = "Receive updates about route changes",
                isEnabled = true,
                onToggle = { /* TODO */ }
            )
            
            NotificationOption(
                title = "Service Announcements",
                description = "Important service announcements",
                isEnabled = false,
                onToggle = { /* TODO */ }
            )
            
            // Location Section
            Text(
                text = "Location",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            
            ProfileOption(
                icon = Icons.Default.LocationOn,
                title = "Location Settings",
                onClick = { /* TODO */ }
            )
            
            ProfileOption(
                icon = Icons.Default.GpsFixed,
                title = "Search Radius",
                onClick = { /* TODO */ }
            )
            
            // App Section
            Text(
                text = "App",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            
            ProfileOption(
                icon = Icons.Default.Help,
                title = "Help & Support",
                onClick = { /* TODO */ }
            )
            
            ProfileOption(
                icon = Icons.Default.Info,
                title = "About",
                onClick = { /* TODO */ }
            )
            
            ProfileOption(
                icon = Icons.Default.Logout,
                title = "Sign Out",
                onClick = { /* TODO */ }
            )
        }
    }
}

@Composable
private fun ProfileOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
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
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navigate",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun NotificationOption(
    title: String,
    description: String,
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Switch(
                checked = isEnabled,
                onCheckedChange = onToggle
            )
        }
    }
}

