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

class MainFragment: Fragment() {

    private var isSearching = false
    private var serviceIntent: Intent? = null

    private var mService: MainService? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_main, container)

        val searchButton = view.findViewById<Button>(R.id.searchButton)
        searchButton.setOnClickListener {
            isSearching = !isSearching
            searchButton.text = if (isSearching) getString(R.string.stop_service) else getString(R.string.start_service)
            if (isSearching) {
                serviceIntent = Intent(context, MainService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    requireContext().startForegroundService(serviceIntent)
                } else {
                    requireContext().startService(serviceIntent)
                }
                if (!requireContext().bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)) {
                    // TODO
                }
            } else {
                requireContext().unbindService(connection)
                requireContext().stopService(serviceIntent)
            }
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.main_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        val antDeviceRecyclerViewAdapter = AntDeviceRecyclerViewAdapter()
        recyclerView.adapter = antDeviceRecyclerViewAdapter

//        antDeviceRecyclerViewAdapter.addDevice(AntDevice("Test1"))
//        antDeviceRecyclerViewAdapter.addDevice(AntDevice("Test2"))
//        antDeviceRecyclerViewAdapter.addDevice(AntDevice("Test3"))
        return view
    }

    private val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as MainService.LocalBinder
            mService = binder.service
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mService = null
        }
    }

}