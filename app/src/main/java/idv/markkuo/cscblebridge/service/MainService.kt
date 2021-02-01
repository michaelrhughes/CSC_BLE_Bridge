package idv.markkuo.cscblebridge.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import idv.markkuo.cscblebridge.MainActivity
import idv.markkuo.cscblebridge.R

class MainService : Service() {

    companion object {
        private const val CHANNEL_DEFAULT_IMPORTANCE = "csc_ble_channel"
        private const val MAIN_CHANNEL_NAME = "CscService"
        private const val ONGOING_NOTIFICATION_ID = 9999
    }

    private val bridge = AntToBleBridge()

    override fun onCreate() {
        super.onCreate()
        startServiceInForeground()
        bridge.startupAnt(this)
    }

    private val binder: IBinder = LocalBinder()

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    /**
     * Get the services for communicating with it
     */
    inner class LocalBinder : Binder() {
        val service: MainService
            get() = this@MainService
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String) {
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
        channel.lightColor = Color.BLUE
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    private fun startServiceInForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(CHANNEL_DEFAULT_IMPORTANCE, MAIN_CHANNEL_NAME)

            // Create the PendingIntent
            val notifyPendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this.applicationContext, MainActivity::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT)

            // build a notification
            val notification: Notification = Notification.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
                    .setContentTitle(getText(R.string.app_name))
                    .setContentText("Active")
                    .setSmallIcon(R.drawable.ic_notification_icon)
                    .setAutoCancel(false)
                    .setContentIntent(notifyPendingIntent)
                    .setTicker(getText(R.string.app_name))
                    .build()
            startForeground(ONGOING_NOTIFICATION_ID, notification)
        } else {
            val notification: Notification = NotificationCompat.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Active")
                    .setSmallIcon(R.drawable.ic_notification_icon)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(false)
                    .build()
            startForeground(ONGOING_NOTIFICATION_ID, notification)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        bridge.stopAnt()
    }
}