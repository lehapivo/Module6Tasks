package com.example.nobelprizes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nobelprizes.domain.model.NobelPrize
import com.example.nobelprizes.presentation.detail.NobelDetailScreen
import com.example.nobelprizes.presentation.list.NobelListScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                var selectedPrize: NobelPrize? = null

                NavHost(navController, startDestination = "list") {
                    composable("list") {
                        NobelListScreen(onPrizeClick = { prize ->
                            selectedPrize = prize
                            navController.navigate("detail")
                        })
                    }
                    composable("detail") {
                        selectedPrize?.let { prize ->
                            NobelDetailScreen(prize = prize, onBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}
