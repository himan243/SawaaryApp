package com.adtu.sawaary.ui.screens.driver

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.adtu.sawaary.auth.AuthHelper
import com.adtu.sawaary.data.model.User
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverProfileScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val authHelper = remember { AuthHelper(context) }
    val coroutineScope = rememberCoroutineScope()
    
    var currentUser by remember { mutableStateOf<User?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    
    // Load user data
    LaunchedEffect(Unit) {
        authHelper.getCurrentUser()?.let { firebaseUser ->
            try {
                val user = authHelper.getUserData(firebaseUser.uid)
                currentUser = user
            } catch (e: Exception) {
                // Handle error
            }
        }
        isLoading = false
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Driver Profile") },
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
            // Profile Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        modifier = Modifier.size(64.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    if (isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Text(
                            text = currentUser?.name ?: "Driver",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Text(
                            text = currentUser?.email ?: "No email",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        Text(
                            text = "Phone: ${currentUser?.phoneNumber ?: "Not provided"}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            // Profile Options
            ProfileOption(
                icon = Icons.Default.Edit,
                title = "Edit Profile",
                onClick = { /* TODO */ }
            )
            
            ProfileOption(
                icon = Icons.Default.Security,
                title = "Change Password",
                onClick = { /* TODO */ }
            )
            
            ProfileOption(
                icon = Icons.Default.Notifications,
                title = "Notifications",
                onClick = { /* TODO */ }
            )
            
            ProfileOption(
                icon = Icons.Default.Help,
                title = "Help & Support",
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
