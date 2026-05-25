package com.example.heartmonitor.domain

import java.util.UUID

object BleUuids {
    // Heart Rate Service (standard Bluetooth SIG)
    val HEART_RATE_SERVICE: UUID = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb")

    // Heart Rate Measurement Characteristic
    val HEART_RATE_MEASUREMENT: UUID = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb")

    // Client Characteristic Configuration Descriptor (enables notifications)
    val CCC_DESCRIPTOR: UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
}
