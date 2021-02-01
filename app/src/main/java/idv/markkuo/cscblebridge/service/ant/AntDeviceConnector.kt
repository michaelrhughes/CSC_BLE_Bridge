package idv.markkuo.cscblebridge.service.ant

import android.content.Context
import android.util.Log
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc.IDeviceStateChangeReceiver
import com.dsi.ant.plugins.antplus.pccbase.PccReleaseHandle
import java.util.concurrent.ConcurrentHashMap

abstract class AntDeviceConnector<T: AntPluginPcc, Data: AntDevice>(private val context: Context, internal val listener: DeviceManagerListener<Data>) {

    interface DeviceManagerListener<Data> {
        fun onDeviceStateChanged(result: RequestAccessResult, deviceState: DeviceState)
        fun onDataUpdated(data: Data)
    }

    companion object {
        private const val TAG = "AntDeviceManager"
    }

    private val devices = ConcurrentHashMap<Int, Data>()
    private var pcc: AntPluginPcc? = null
    private var releaseHandle: PccReleaseHandle<T>? = null

    private var deviceStateChangedReceiver: IDeviceStateChangeReceiver = IDeviceStateChangeReceiver {
        Log.d("Test", "Device State Changed ${it.name}")
        if (it == DeviceState.DEAD) {
            pcc = null
        }
    }

    private val resultReceiver = AntPluginPcc.IPluginAccessResultReceiver {
        pcc: T, requestAccessResult: RequestAccessResult, deviceState: DeviceState ->
        when (requestAccessResult) {
            RequestAccessResult.SUCCESS -> {
                this.pcc = pcc
                Log.d(TAG, "${pcc.deviceName}: ${deviceState})")
                subscribeToEvents(pcc)
            }
            RequestAccessResult.USER_CANCELLED -> {
                Log.d(TAG, "Ant Device Closed: $requestAccessResult")
            }
            else -> {
                Log.w(TAG, "Ant Device State changed: $deviceState, resultCode: $requestAccessResult")
            }
        }
        listener.onDeviceStateChanged(requestAccessResult, deviceState)
    }

    abstract fun requestAccess(
            context: Context,
            resultReceiver: AntPluginPcc.IPluginAccessResultReceiver<T>,
            stateChangedReceiver: AntPluginPcc.IDeviceStateChangeReceiver,
            deviceNumber: Int = 0
    ): PccReleaseHandle<T>

    abstract fun subscribeToEvents(pcc: T)
    abstract fun init(deviceNumber: Int, deviceName: String): Data

    fun startSearch() {
        stopSearch()
        releaseHandle = requestAccess(context, resultReceiver, deviceStateChangedReceiver)
    }

    fun stopSearch() {
        releaseHandle?.close()
        devices.clear()
    }

    internal fun getDevice(pcc: T): Data {
        if (!devices.containsKey(pcc.antDeviceNumber)) {
            devices[pcc.antDeviceNumber] = init(pcc.antDeviceNumber, pcc.deviceName)
        }
        return devices[pcc.antDeviceNumber]!!
    }
}