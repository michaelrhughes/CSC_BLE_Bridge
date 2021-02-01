package idv.markkuo.cscblebridge.service.ble

import java.util.*

sealed class BleServiceType(val serviceId: UUID, val measurement: UUID, val feature: UUID?) {
    object CscService: BleServiceType(
            // https://www.bluetooth.com/specifications/gatt/services/
            /** Cycling Speed and Cadence */
            UUID.fromString("00001816-0000-1000-8000-00805f9b34fb"),
            // https://www.bluetooth.com/specifications/gatt/characteristics/
            /** Mandatory Characteristic: CSC Measurement */
            UUID.fromString("00002a5b-0000-1000-8000-00805f9b34fb"),
            /** Mandatory Characteristic: CSC Feature */
            UUID.fromString("00002a5c-0000-1000-8000-00805f9b34fb")
    )
    object HrService: BleServiceType(
            UUID.fromString("0000180D-0000-1000-8000-00805f9b34fb"),
            UUID.fromString("00002A37-0000-1000-8000-00805f9b34fb"),
            null
    )
    object RscService: BleServiceType(
            UUID.fromString("00001814-0000-1000-8000-00805f9b34fb"),
            UUID.fromString("00002A53-0000-1000-8000-00805f9b34fb"),
            UUID.fromString("00002A54-0000-1000-8000-00805f9b34fb")
    )
}