package idv.markkuo.cscblebridge.service

import android.content.Context
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult
import idv.markkuo.cscblebridge.service.ant.*

class AntToBleBridge {

    private val antConnectors = ArrayList<AntDeviceConnector<*, *>>()

    fun startupAnt(service: Context) {
        antConnectors.add(BsdConnector(service, object: AntDeviceConnector.DeviceManagerListener<AntDevice.BsdDevice> {
            override fun onDeviceStateChanged(result: RequestAccessResult, deviceState: DeviceState) {
            }

            override fun onDataUpdated(data: AntDevice.BsdDevice) {
                // TODO broadcast
            }
        }))

        antConnectors.add(HRConnector(service, object: AntDeviceConnector.DeviceManagerListener<AntDevice.HRDevice> {
            override fun onDeviceStateChanged(result: RequestAccessResult, deviceState: DeviceState) {
            }

            override fun onDataUpdated(data: AntDevice.HRDevice) {
                // TODO broadcast
            }
        }))

        antConnectors.add(SSConnector(service, object: AntDeviceConnector.DeviceManagerListener<AntDevice.SSDevice> {
            override fun onDeviceStateChanged(result: RequestAccessResult, deviceState: DeviceState) {
            }

            override fun onDataUpdated(data: AntDevice.SSDevice) {
                // TODO broadcast
            }
        }))

        antConnectors.forEach { connector -> connector.startSearch() }
    }

    fun stopAnt() {
        antConnectors.forEach { connector -> connector.stopSearch() }
    }

}