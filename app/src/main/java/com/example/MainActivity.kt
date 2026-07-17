package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.data.CoupleSpaceDatabase
import com.example.data.CoupleSpaceRepository
import com.example.ui.CoupleSpaceViewModel
import com.example.ui.screens.AgendaScreen
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private lateinit var database: CoupleSpaceDatabase
    private lateinit var repository: CoupleSpaceRepository
    private lateinit var viewModel: CoupleSpaceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Initialize Room Database natively using expert local practices
        database = Room.databaseBuilder(
            applicationContext,
            CoupleSpaceDatabase::class.java,
            "couplespace_database"
        ).fallbackToDestructiveMigration() // Prevent crashes if structures evolve
         .build()

        // 2. Initialize Repository and ViewModel
        repository = CoupleSpaceRepository(database.dao())
        viewModel = CoupleSpaceViewModel(repository)

        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                val isLoggedIn by viewModel.isLoggedIn.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = androidx.compose.ui.graphics.Color(0xFF10131F)
                ) { innerPadding ->
                    // Standard M3 navigation Safe-Drawing host
                    NavHost(
                        navController = navController,
                        startDestination = if (isLoggedIn) "home" else "login"
                    ) {
                        // Portal Entrance (Login)
                        composable("login") {
                            LoginScreen(
                                viewModel = viewModel,
                                onLoginSuccess = {
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            )
                        }

                        // Hub cozy corner (Home)
                        composable("home") {
                            HomeScreen(
                                viewModel = viewModel,
                                onNavigateToAgenda = { navController.navigate("agenda") },
                                onNavigateToChat = { navController.navigate("chat") }
                            )
                        }

                        // Agenda affective milestones (Schedule)
                        composable("agenda") {
                            AgendaScreen(
                                viewModel = viewModel,
                                onNavigateToHome = { navController.popBackStack() },
                                onNavigateToChat = {
                                    navController.navigate("chat") {
                                        popUpTo("home")
                                    }
                                }
                            )
                        }

                        // Couple's chat room (Messages)
                        composable("chat") {
                            ChatScreen(
                                viewModel = viewModel,
                                onNavigateToHome = { navController.popBackStack() },
                                onNavigateToAgenda = {
                                    navController.navigate("agenda") {
                                        popUpTo("home")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
