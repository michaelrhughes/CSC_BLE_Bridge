package idv.markkuo.cscblebridge

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import idv.markkuo.cscblebridge.antrecyclerview.AntDeviceRecyclerViewAdapter
import idv.markkuo.cscblebridge.service.MainService
import idv.markkuo.cscblebridge.service.ant.AntDevice
import java.util.*
import kotlin.collections.HashMap

class MainFragment: Fragment() {

    interface ServiceStarter {
        fun startService()
        fun stopService()
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
        antDeviceRecyclerViewAdapter = AntDeviceRecyclerViewAdapter()
        recyclerView.adapter = antDeviceRecyclerViewAdapter
        return view
    }

    fun setDevices(devices: List<AntDevice>) {
        activity?.runOnUiThread {
            antDeviceRecyclerViewAdapter?.updateDevices(devices)
        }
    }
}