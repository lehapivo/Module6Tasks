package com.example.heartmonitor.data

import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Build
import com.example.heartmonitor.domain.BleDevice
import com.example.heartmonitor.domain.BleUuids
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@SuppressLint("MissingPermission")
class BleManager(context: Context) {
    private val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val adapter = bluetoothManager.adapter
    private val scanner = adapter.bluetoothLeScanner
    private var gatt: BluetoothGatt? = null

    private val _devices = MutableStateFlow<List<BleDevice>>(emptyList())
    val devices: StateFlow<List<BleDevice>> = _devices

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning

    private val _connectionStatus = MutableStateFlow("Отключено")
    val connectionStatus: StateFlow<String> = _connectionStatus

    private val _heartRate = MutableStateFlow<Int?>(null)
    val heartRate: StateFlow<Int?> = _heartRate

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device = result.device
            val name = device.name ?: return // skip unnamed devices
            val bleDevice = BleDevice(name = name, address = device.address)
            val current = _devices.value.toMutableList()
            if (current.none { it.address == bleDevice.address }) {
                current.add(bleDevice)
                _devices.value = current
            }
        }
    }

    fun startScan() {
        _devices.value = emptyList()
        _isScanning.value = true
        scanner.startScan(scanCallback)
    }

    fun stopScan() {
        _isScanning.value = false
        scanner.stopScan(scanCallback)
    }

    fun connect(context: Context, address: String) {
        stopScan()
        _connectionStatus.value = "Подключение..."
        val device = adapter.getRemoteDevice(address)
        gatt = device.connectGatt(context, false, gattCallback)
    }

    fun disconnect() {
        gatt?.disconnect()
        gatt?.close()
        gatt = null
        _connectionStatus.value = "Отключено"
        _heartRate.value = null
    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    _connectionStatus.value = "Подключено"
                    gatt.discoverServices()
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    _connectionStatus.value = "Отключено"
                    _heartRate.value = null
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            val service = gatt.getService(BleUuids.HEART_RATE_SERVICE) ?: return
            val characteristic = service.getCharacteristic(BleUuids.HEART_RATE_MEASUREMENT) ?: return

            // Enable local notifications
            gatt.setCharacteristicNotification(characteristic, true)

            // Write to CCC descriptor to enable server-side notifications
            val descriptor = characteristic.getDescriptor(BleUuids.CCC_DESCRIPTOR)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                gatt.writeDescriptor(descriptor, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
            } else {
                @Suppress("DEPRECATION")
                descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                @Suppress("DEPRECATION")
                gatt.writeDescriptor(descriptor)
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray
        ) {
            if (characteristic.uuid == BleUuids.HEART_RATE_MEASUREMENT) {
                _heartRate.value = parseHeartRate(value)
            }
        }

        // For older API levels
        @Suppress("DEPRECATION")
        @Deprecated("Deprecated in API 33")
        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            if (characteristic.uuid == BleUuids.HEART_RATE_MEASUREMENT) {
                _heartRate.value = parseHeartRate(characteristic.value)
            }
        }
    }

    /**
     * Heart Rate Measurement format (Bluetooth spec):
     * Byte 0 - Flags:
     *   Bit 0: 0 = HR value is UINT8, 1 = HR value is UINT16
     * Byte 1 (or 1-2): Heart Rate value
     */
    private fun parseHeartRate(data: ByteArray): Int? {
        if (data.isEmpty()) return null
        val flags = data[0].toInt()
        val isUint16 = flags and 0x01 != 0
        return if (isUint16 && data.size >= 3) {
            (data[1].toInt() and 0xFF) or ((data[2].toInt() and 0xFF) shl 8)
        } else if (!isUint16 && data.size >= 2) {
            data[1].toInt() and 0xFF
        } else null
    }
}
