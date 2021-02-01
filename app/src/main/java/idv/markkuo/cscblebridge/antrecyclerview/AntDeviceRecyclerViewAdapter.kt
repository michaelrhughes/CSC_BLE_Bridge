package idv.markkuo.cscblebridge.antrecyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import idv.markkuo.cscblebridge.service.ant.AntDevice

class AntDeviceRecyclerViewAdapter: RecyclerView.Adapter<AntDeviceViewHolder>() {
    private val deviceList = ArrayList<AntDevice>()

    fun addDevice(antDevice: AntDevice) {
        deviceList.add(antDevice)
        notifyItemInserted(deviceList.size - 1)
    }

    fun removeDevice(antDevice: AntDevice) {
        val index = deviceList.indexOf(antDevice)
        deviceList.remove(antDevice)
        notifyItemRemoved(index)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AntDeviceViewHolder {
        val view = AntDeviceView(parent.context).doInflation(parent)
        return AntDeviceViewHolder(view)
    }

    override fun getItemCount(): Int = deviceList.size

    override fun onBindViewHolder(holder: AntDeviceViewHolder, position: Int) {
        holder.view.bind(deviceList[position])
    }
}