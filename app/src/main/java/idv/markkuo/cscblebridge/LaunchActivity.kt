package idv.markkuo.cscblebridge

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import idv.markkuo.cscblebridge.service.MainService
import idv.markkuo.cscblebridge.service.ant.AntDevice

class LaunchActivity: AppCompatActivity(), MainFragment.ServiceStarter, MainService.MainServiceListener {

    private var mService: MainService? = null
    private var serviceIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
    }

    private val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as MainService.LocalBinder
            mService = binder.service

            mService?.addListener(this@LaunchActivity)
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mService?.removeListener(this@LaunchActivity)
            mService = null
        }
    }

    override fun startService() {
        serviceIntent = Intent(this, MainService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
        if (!bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)) {
            // TODO
        }
    }

    override fun stopService() {
        unbindService(connection)
        stopService(serviceIntent)
    }

    override fun onDevicesUpdated(devices: List<AntDevice>) {
        val mainFragment = supportFragmentManager.findFragmentById(R.id.main_fragment) as MainFragment
        mainFragment.setDevices(devices)
    }
}