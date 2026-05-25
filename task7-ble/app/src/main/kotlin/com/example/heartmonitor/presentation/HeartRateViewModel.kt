package com.example.heartmonitor.presentation

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.heartmonitor.data.BleManager
import kotlinx.coroutines.flow.StateFlow

class HeartRateViewModel(app: Application) : AndroidViewModel(app) {
    private val bleManager = BleManager(app)

    val devices = bleManager.devices
    val isScanning = bleManager.isScanning
    val connectionStatus = bleManager.connectionStatus
    val heartRate = bleManager.heartRate

    fun startScan() = bleManager.startScan()
    fun stopScan() = bleManager.stopScan()

    fun connect(context: Context, address: String) = bleManager.connect(context, address)
    fun disconnect() = bleManager.disconnect()

    override fun onCleared() {
        super.onCleared()
        bleManager.disconnect()
        bleManager.stopScan()
    }
}
