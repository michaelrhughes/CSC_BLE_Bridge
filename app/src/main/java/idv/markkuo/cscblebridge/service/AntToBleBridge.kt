package idv.markkuo.cscblebridge.service

import android.content.Context
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult
import idv.markkuo.cscblebridge.service.ant.*
import idv.markkuo.cscblebridge.service.ble.BleServer
import idv.markkuo.cscblebridge.service.ble.BleServiceType
import java.util.ArrayList

class AntToBleBridge {

    private val antConnectors = ArrayList<AntDeviceConnector<*, *>>()
    private var bleServer: BleServer? = null

    val antDevices = hashMapOf<Int, AntDevice>()

    fun startup(service: Context, callback: () -> Unit) {
        stop()
        antDevices.clear()
        bleServer = BleServer().apply {
            startServer(service)
        }
        antConnectors.add(BsdConnector(service, object: AntDeviceConnector.DeviceManagerListener<AntDevice.BsdDevice> {
            override fun onDeviceStateChanged(result: RequestAccessResult, deviceState: DeviceState) {
            }

            override fun onDataUpdated(data: AntDevice.BsdDevice) {
                antDevices[data.deviceId] = data
                bleServer?.updateData(BleServiceType.CscService, data)
                callback()
            }
        }))

        antConnectors.add(HRConnector(service, object: AntDeviceConnector.DeviceManagerListener<AntDevice.HRDevice> {
            override fun onDeviceStateChanged(result: RequestAccessResult, deviceState: DeviceState) {
            }

            override fun onDataUpdated(data: AntDevice.HRDevice) {
                antDevices[data.deviceId] = data
                bleServer?.updateData(BleServiceType.HrService, data)
                callback()
            }
        }))

        antConnectors.add(SSConnector(service, object: AntDeviceConnector.DeviceManagerListener<AntDevice.SSDevice> {
            override fun onDeviceStateChanged(result: RequestAccessResult, deviceState: DeviceState) {
            }

            override fun onDataUpdated(data: AntDevice.SSDevice) {
                antDevices[data.deviceId] = data
                bleServer?.updateData(BleServiceType.RscService, data)
                callback()
            }
        }))

        antConnectors.forEach { connector -> connector.startSearch() }
    }

    fun stop() {
        antConnectors.forEach { connector -> connector.stopSearch() }
        antConnectors.clear()
        bleServer?.stopServer()
    }
}