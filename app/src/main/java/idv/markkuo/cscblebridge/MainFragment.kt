package idv.markkuo.cscblebridge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import idv.markkuo.cscblebridge.antrecyclerview.AntDeviceRecyclerViewAdapter
import idv.markkuo.cscblebridge.service.ant.AntDevice
import idv.markkuo.cscblebridge.service.ble.BleServiceType

class MainFragment: Fragment() {

    interface ServiceStarter {
        fun startService()
        fun stopService()
        fun deviceSelected(antDevice: AntDevice)
    }

    private var isSearching = false
    private var antDeviceRecyclerViewAdapter: AntDeviceRecyclerViewAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_main, container)

        val searchButton = view.findViewById<Button>(R.id.searchButton)
        searchButton.setOnClickListener {
            isSearching = !isSearching
            searchButton.text = if (isSearching) getString(R.string.stop_service) else getString(R.string.start_service)
            if (isSearching) {
                (activity as ServiceStarter).startService()
            } else {
                (activity as ServiceStarter).stopService()
            }
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.main_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        antDeviceRecyclerViewAdapter = AntDeviceRecyclerViewAdapter {
            (activity as ServiceStarter).deviceSelected(it)
        }
        recyclerView.adapter = antDeviceRecyclerViewAdapter
        return view
    }

    fun setDevices(devices: List<AntDevice>, selectedDevices: HashMap<BleServiceType, Int>) {
        activity?.runOnUiThread {
            antDeviceRecyclerViewAdapter?.updateDevices(devices, selectedDevices)
        }
    }
}