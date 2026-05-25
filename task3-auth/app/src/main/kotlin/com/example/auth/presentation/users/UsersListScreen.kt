package com.example.auth.presentation.users

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.auth.domain.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersListScreen(
    token: String,
    onUserClick: (User) -> Unit,
    vm: UsersViewModel = viewModel()
) {
    LaunchedEffect(token) { vm.load(token) }
    val state by vm.state.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("Пользователи") }) }) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            when (val s = state) {
                is UsersState.Loading -> CircularProgressIndicator()
                is UsersState.Error -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(s.message, color = MaterialTheme.colorScheme.error)
                    Button(onClick = { vm.load(token) }) { Text("Повторить") }
                }
                is UsersState.Success -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(s.users) { user ->
                        ListItem(
                            headlineContent = { Text("${user.firstName} ${user.lastName}") },
                            supportingContent = { Text("@${user.username} · ${user.email}") },
                            leadingContent = {
                                AsyncImage(
                                    model = user.image,
                                    contentDescription = user.firstName,
                                    modifier = Modifier.size(48.dp).clip(CircleShape)
                                )
                            },
                            modifier = Modifier.clickable { onUserClick(user) }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
