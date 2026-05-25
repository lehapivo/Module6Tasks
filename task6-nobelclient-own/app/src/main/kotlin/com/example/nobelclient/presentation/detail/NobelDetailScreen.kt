package com.example.nobelclient.presentation.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.nobelclient.domain.model.NobelPrize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NobelDetailScreen(prize: NobelPrize, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${prize.category} ${prize.awardYear}") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
            items(prize.laureates) { laureate ->
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        if (laureate.portraitUrl != null) {
                            AsyncImage(
                                model = laureate.portraitUrl,
                                contentDescription = laureate.fullName,
                                modifier = Modifier.fillMaxWidth().height(200.dp)
                            )
                            Spacer(Modifier.height(8.dp))
                        }
                        Text(laureate.fullName, style = MaterialTheme.typography.titleMedium)
                        laureate.birthCountry?.let {
                            Text("Страна: $it", style = MaterialTheme.typography.bodySmall)
                        }
                        laureate.portion?.let {
                            Text("Доля: $it", style = MaterialTheme.typography.bodySmall)
                        }
                        laureate.motivation?.let {
                            Spacer(Modifier.height(8.dp))
                            Text(it, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
