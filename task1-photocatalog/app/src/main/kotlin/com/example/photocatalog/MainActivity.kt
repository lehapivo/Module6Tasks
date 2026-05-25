package com.example.photocatalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.photocatalog.domain.model.Photo
import com.example.photocatalog.presentation.detail.PhotoDetailScreen
import com.example.photocatalog.presentation.list.PhotoListScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                var selectedPhoto: Photo? = null

                NavHost(navController = navController, startDestination = "list") {
                    composable("list") {
                        PhotoListScreen(
                            onPhotoClick = { photo ->
                                selectedPhoto = photo
                                navController.navigate("detail")
                            }
                        )
                    }
                    composable("detail") {
                        selectedPhoto?.let { photo ->
                            PhotoDetailScreen(
                                photo = photo,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
