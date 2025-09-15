package com.adtu.sawaary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.adtu.sawaary.firebase.FirebaseManager
import com.adtu.sawaary.navigation.SawaaryNavigation
import com.adtu.sawaary.ui.theme.SawaaryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Firebase
        FirebaseManager.initialize(this)
        
        enableEdgeToEdge()
        setContent {
            SawaaryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SawaaryApp()
                }
            }
        }
    }
}

@Composable
fun SawaaryApp() {
    val navController = rememberNavController()
    
    SawaaryNavigation(
        navController = navController,
        startDestination = "welcome"
    )
}