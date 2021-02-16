package idv.markkuo.cscblebridge.service

import android.content.Context
import android.os.ParcelUuid
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult
import idv.markkuo.cscblebridge.service.ant.*
import idv.markkuo.cscblebridge.service.ble.BleServer
import idv.markkuo.cscblebridge.service.ble.BleServiceType
import java.util.ArrayList

class AntToBleBridge {

    private val antConnectors = ArrayList<AntDeviceConnector<*, *>>()
    private var bleServer: BleServer? = null

    fun startup(service: Context) {
        stop()
        bleServer = BleServer().apply {
            startServer(service)
            BleServiceType.serviceTypes.forEach { subClass ->
                createService(subClass)
            }
        }
        antConnectors.add(BsdConnector(service, object: AntDeviceConnector.DeviceManagerListener<AntDevice.BsdDevice> {
            override fun onDeviceStateChanged(result: RequestAccessResult, deviceState: DeviceState) {
            }

            override fun onDataUpdated(data: AntDevice.BsdDevice) {
                bleServer?.updateData(BleServiceType.CscService, data)
            }
        }))

        antConnectors.add(HRConnector(service, object: AntDeviceConnector.DeviceManagerListener<AntDevice.HRDevice> {
            override fun onDeviceStateChanged(result: RequestAccessResult, deviceState: DeviceState) {
            }

            override fun onDataUpdated(data: AntDevice.HRDevice) {
                bleServer?.updateData(BleServiceType.HrService, data)
            }
        }))

        antConnectors.add(SSConnector(service, object: AntDeviceConnector.DeviceManagerListener<AntDevice.SSDevice> {
            override fun onDeviceStateChanged(result: RequestAccessResult, deviceState: DeviceState) {
            }

            override fun onDataUpdated(data: AntDevice.SSDevice) {
                bleServer?.updateData(BleServiceType.RscService, data)
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