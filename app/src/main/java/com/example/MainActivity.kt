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
import com.example.data.CoupleSpaceRepository
import com.example.data.SupabaseClientProvider
import com.example.ui.CoupleSpaceViewModel
import com.example.ui.screens.AgendaScreen
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private lateinit var repository: CoupleSpaceRepository
    private lateinit var viewModel: CoupleSpaceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val supabaseClient = SupabaseClientProvider.getInstance(
            supabaseUrl = com.example.BuildConfig.SUPABASE_URL,
            supabaseKey = com.example.BuildConfig.SUPABASE_ANON_KEY
        )

        repository = CoupleSpaceRepository(supabaseClient)
        viewModel = CoupleSpaceViewModel(repository, application)

        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                val isLoggedIn by viewModel.isLoggedIn.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = androidx.compose.ui.graphics.Color(0xFF10131F)
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = if (isLoggedIn) "home" else "login"
                    ) {
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

                        composable("home") {
                            HomeScreen(
                                viewModel = viewModel,
                                onNavigateToAgenda = { navController.navigate("agenda") },
                                onNavigateToChat = { navController.navigate("chat") }
                            )
                        }

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
