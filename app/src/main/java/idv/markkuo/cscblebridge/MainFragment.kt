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

class MainFragment: Fragment() {

    private var isSearching = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_main, container)

        val searchButton = view.findViewById<Button>(R.id.searchButton)
        searchButton.setOnClickListener {
            isSearching = !isSearching
            searchButton.text = if (isSearching) getString(R.string.stop_service) else getString(R.string.start_service)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}