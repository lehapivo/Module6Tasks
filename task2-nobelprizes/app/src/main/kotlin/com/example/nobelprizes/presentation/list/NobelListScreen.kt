package com.example.nobelprizes.presentation.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nobelprizes.domain.model.NobelPrize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NobelListScreen(
    onPrizeClick: (NobelPrize) -> Unit,
    vm: NobelListViewModel = viewModel()
) {
    val state by vm.state.collectAsState()
    val year by vm.selectedYear.collectAsState()
    val category by vm.selectedCategory.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    Scaffold(topBar = { TopAppBar(title = { Text("Нобелевские лауреаты") }) }) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Filters
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = year,
                    onValueChange = { vm.selectedYear.value = it },
                    label = { Text("Год") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = category.ifBlank { "Все" },
                        onValueChange = {},
                        label = { Text("Категория") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        CATEGORIES.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.ifBlank { "Все" }) },
                                onClick = { vm.selectedCategory.value = cat; expanded = false }
                            )
                        }
                    }
                    Spacer(modifier = Modifier
                        .matchParentSize()
                        .clickable { expanded = true })
                }
            }
            Button(
                onClick = { vm.load() },
                modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()
            ) { Text("Найти") }

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                when (val s = state) {
                    is PrizeListState.Loading -> CircularProgressIndicator()
                    is PrizeListState.Error -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(s.message, color = MaterialTheme.colorScheme.error)
                        Button(onClick = { vm.load() }) { Text("Повторить") }
                    }
                    is PrizeListState.Success -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(s.prizes) { prize ->
                            prize.laureates.forEach { laureate ->
                                ListItem(
                                    headlineContent = { Text(laureate.fullName) },
                                    supportingContent = {
                                        Text("${prize.awardYear} · ${prize.category}\n${laureate.motivation?.take(100) ?: ""}")
                                    },
                                    modifier = Modifier.clickable { onPrizeClick(prize) }
                                )
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }
        }
    }
}
