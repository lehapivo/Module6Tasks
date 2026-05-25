package com.example.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.auth.data.repository.TokenStorage
import com.example.auth.domain.model.User
import com.example.auth.presentation.detail.UserDetailScreen
import com.example.auth.presentation.login.LoginScreen
import com.example.auth.presentation.users.UsersListScreen
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tokenStorage = TokenStorage(applicationContext)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                var token by remember { mutableStateOf<String?>(null) }
                var selectedUser by remember { mutableStateOf<User?>(null) }
                val scope = rememberCoroutineScope()

                LaunchedEffect(Unit) {
                    val saved = tokenStorage.tokenFlow.firstOrNull()
                    if (!saved.isNullOrBlank()) {
                        token = saved
                        navController.navigate("users") { popUpTo("login") { inclusive = true } }
                    }
                }

                NavHost(navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(onSuccess = { t ->
                            token = t
                            navController.navigate("users") { popUpTo("login") { inclusive = true } }
                        })
                    }
                    composable("users") {
                        UsersListScreen(
                            token = token ?: "",
                            onUserClick = { user ->
                                selectedUser = user
                                navController.navigate("detail")
                            }
                        )
                    }
                    composable("detail") {
                        selectedUser?.let { user ->
                            UserDetailScreen(
                                user = user,
                                onBack = { navController.popBackStack() },
                                onLogout = {
                                    scope.launch {
                                        tokenStorage.clearToken()
                                        token = null
                                        navController.navigate("login") { popUpTo("users") { inclusive = true } }
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
