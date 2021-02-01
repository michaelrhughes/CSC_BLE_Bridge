package idv.markkuo.cscblebridge.service.ble

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import idv.markkuo.cscblebridge.CSCProfile

class BleService(val type: BleServiceType) {
    private var service: BluetoothGattService? = null

    fun startService() {
        val s = BluetoothGattService(type.serviceId, BluetoothGattService.SERVICE_TYPE_PRIMARY)

        val cscMeasurement = BluetoothGattCharacteristic(type.measurement,
                BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                BluetoothGattCharacteristic.PERMISSION_READ)
        val configDescriptor = BluetoothGattDescriptor(CSCProfile.CLIENT_CONFIG,
                BluetoothGattDescriptor.PERMISSION_READ or BluetoothGattDescriptor.PERMISSION_WRITE)
        cscMeasurement.addDescriptor(configDescriptor)
        s.addCharacteristic(cscMeasurement)

        if (type.feature != null) {
            val cscFeature = BluetoothGattCharacteristic(type.feature,
                    BluetoothGattCharacteristic.PROPERTY_READ,
                    BluetoothGattCharacteristic.PERMISSION_READ)
            s.addCharacteristic(cscFeature)
        }
    }
}