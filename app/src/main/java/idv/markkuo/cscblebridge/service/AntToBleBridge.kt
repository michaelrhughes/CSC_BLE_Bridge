package idv.markkuo.cscblebridge.service

import android.content.Context
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult
import idv.markkuo.cscblebridge.service.ant.*
import idv.markkuo.cscblebridge.service.ble.BleServer
import idv.markkuo.cscblebridge.service.ble.BleServiceType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.ArrayList

class AntToBleBridge {

    private val antConnectors = ArrayList<AntDeviceConnector<*, *>>()
    private var bleServer: BleServer? = null

    val antDevices = hashMapOf<Int, AntDevice>()
    val selectedDevices = hashMapOf<BleServiceType, Int>()
    var serviceCallback: (() -> Unit)? = null
    var isSearching = false

    fun startup(service: Context, callback: () -> Unit) {
        serviceCallback = callback
        stop()
        isSearching = true
        antDevices.clear()
        bleServer = BleServer().apply {
            startServer(service)
        }

        antConnectors.add(BsdConnector(service, object: AntDeviceConnector.DeviceManagerListener<AntDevice.BsdDevice> {
            override fun onDeviceStateChanged(result: RequestAccessResult, deviceState: DeviceState) {
            }

            override fun onDataUpdated(data: AntDevice.BsdDevice) {
                dataUpdated(data, BleServiceType.CscService, callback) {
                    return@dataUpdated BsdConnector(service, this)
                }
            }
        }))

        antConnectors.add(HRConnector(service, object: AntDeviceConnector.DeviceManagerListener<AntDevice.HRDevice> {
            override fun onDeviceStateChanged(result: RequestAccessResult, deviceState: DeviceState) {
            }

            override fun onDataUpdated(data: AntDevice.HRDevice) {
                dataUpdated(data, BleServiceType.HrService, callback) {
                    return@dataUpdated HRConnector(service, this)
                }
            }
        }))

        antConnectors.add(SSConnector(service, object: AntDeviceConnector.DeviceManagerListener<AntDevice.SSDevice> {
            override fun onDeviceStateChanged(result: RequestAccessResult, deviceState: DeviceState) {
            }

            override fun onDataUpdated(data: AntDevice.SSDevice) {
                dataUpdated(data, BleServiceType.RscService, callback) {
                    return@dataUpdated SSConnector(service, this)
                }
            }
        }))

        antConnectors.forEach { connector -> connector.startSearch() }
    }

    private fun dataUpdated(data: AntDevice, type: BleServiceType, serviceCallback: () -> Unit, createService: () -> AntDeviceConnector<*, *>) {
        val isNew = !antDevices.containsKey(data.deviceId)
        antDevices[data.deviceId] = data
        bleServer?.updateData(type, data)
        if (isNew) {
            val connector = createService()
            antConnectors.add(connector)
            connector.startSearch()
        }
        if (!selectedDevices.containsKey(type)) {
            selectedDevices[type] = data.deviceId
            selectedDevicesUpdated()
        }
        serviceCallback()
    }

    fun deviceSelected(data: AntDevice) {
        selectedDevices[data.bleType] = data.deviceId
        selectedDevicesUpdated()
        serviceCallback?.invoke()
    }

    private fun selectedDevicesUpdated() {
        bleServer?.selectedDevices = selectedDevices
    }

    fun stop() {
        isSearching = false

        runBlocking {
            withContext(Dispatchers.IO) {
                antConnectors.forEach { connector -> connector.stopSearch() }
                antConnectors.clear()
                bleServer?.stopServer()

                serviceCallback = null
            }
        }
    }
}