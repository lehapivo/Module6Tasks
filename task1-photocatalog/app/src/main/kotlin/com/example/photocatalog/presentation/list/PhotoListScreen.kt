package com.example.photocatalog.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import com.example.photocatalog.domain.model.Photo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoListScreen(
    onPhotoClick: (Photo) -> Unit,
    viewModel: PhotoListViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Фотокаталог") }) }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (val s = state) {
                is PhotoListState.Loading -> CircularProgressIndicator()
                is PhotoListState.Error -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Ошибка: ${s.message}", color = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { viewModel.loadPhotos() }) { Text("Повторить") }
                }
                is PhotoListState.Success -> LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(s.photos) { photo ->
                        PhotoCard(photo = photo, onClick = { onPhotoClick(photo) })
                    }
                }
            }
        }
    }
}

private val placeholderColors = listOf(
    Color(0xFF5C6BC0), Color(0xFF26A69A), Color(0xFFEF5350),
    Color(0xFFAB47BC), Color(0xFF42A5F5), Color(0xFFFF7043),
    Color(0xFF66BB6A), Color(0xFFEC407A), Color(0xFF8D6E63),
)

@Composable
private fun PhotoCard(photo: Photo, onClick: () -> Unit) {
    val bgColor = placeholderColors[photo.picsumId % placeholderColors.size]

    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            SubcomposeAsyncImage(
                model = photo.thumbnailUrl,
                contentDescription = photo.author,
                modifier = Modifier.fillMaxWidth().height(130.dp),
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        Modifier.fillMaxSize().background(bgColor.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator(Modifier.size(24.dp), strokeWidth = 2.dp) }
                },
                error = {
                    Box(
                        Modifier.fillMaxSize().background(bgColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("📷", style = MaterialTheme.typography.headlineMedium)
                            Text("#${photo.picsumId}", color = Color.White,
                                style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            )
            Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
                Text(
                    text = photo.author,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${photo.width} × ${photo.height}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
