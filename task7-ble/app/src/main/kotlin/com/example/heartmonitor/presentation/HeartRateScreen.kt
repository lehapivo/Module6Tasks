package com.example.heartmonitor.presentation

import android.Manifest
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HeartRateScreen(vm: HeartRateViewModel = viewModel()) {
    val context = LocalContext.current
    val devices by vm.devices.collectAsState()
    val isScanning by vm.isScanning.collectAsState()
    val connectionStatus by vm.connectionStatus.collectAsState()
    val heartRate by vm.heartRate.collectAsState()

    // Request BLE permissions
    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        listOf(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.ACCESS_FINE_LOCATION)
    } else {
        listOf(Manifest.permission.ACCESS_FINE_LOCATION)
    }
    val permissionState = rememberMultiplePermissionsState(permissions)

    LaunchedEffect(Unit) {
        if (!permissionState.allPermissionsGranted) permissionState.launchMultiplePermissionRequest()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Монитор сердечного ритма") }) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Heart Rate Display
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Пульс", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = if (heartRate != null) "${heartRate} bpm" else "—",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (heartRate != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("Статус: $connectionStatus", style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(Modifier.height(16.dp))

            // Control buttons
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { if (isScanning) vm.stopScan() else vm.startScan() },
                    enabled = permissionState.allPermissionsGranted && connectionStatus == "Отключено",
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (isScanning) "Остановить" else "Сканировать")
                }
                OutlinedButton(
                    onClick = { vm.disconnect() },
                    enabled = connectionStatus != "Отключено",
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Отключиться")
                }
            }

            if (!permissionState.allPermissionsGranted) {
                Spacer(Modifier.height(8.dp))
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                    Text(
                        "Необходимо разрешение на Bluetooth и геолокацию",
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            if (isScanning) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    Spacer(Modifier.width(8.dp))
                    Text("Поиск устройств...")
                }
                Spacer(Modifier.height(8.dp))
            }

            // Device list
            if (devices.isEmpty() && !isScanning) {
                Text("Устройства не найдены. Нажмите «Сканировать»",
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                Text("Найденные устройства (${devices.size}):", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(8.dp))
                LazyColumn {
                    items(devices) { device ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                                .clickable { vm.connect(context, device.address) }
                        ) {
                            ListItem(
                                headlineContent = { Text(device.name, fontWeight = FontWeight.Medium) },
                                supportingContent = { Text(device.address, style = MaterialTheme.typography.bodySmall) }
                            )
                        }
                    }
                }
            }
        }
    }
}
