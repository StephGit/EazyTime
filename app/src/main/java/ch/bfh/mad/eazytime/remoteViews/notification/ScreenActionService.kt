package ch.bfh.mad.eazytime.remoteViews.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.di.Injector
import javax.inject.Inject

@SuppressLint
class ScreenActionService : Service() {

    private val unlockReceiver: ScreenActionReceiver = ScreenActionReceiver()

    init {
        Injector.appComponent.inject(this)
    }

    @Inject
    lateinit var notificationHandler: NotificationHandler

    override fun onCreate() {
        super.onCreate()
        startForeground(1212, Notification())
    }

    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not yet implemented")
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "Start ScreenActionService")
        registerReceiver(unlockReceiver, unlockReceiver.getFilter())
        return Service.START_STICKY
    }

    override fun onDestroy() {
        Log.i(TAG, "Destroy ScreenActionService and unregister ScreenActionReceiver")
        unregisterReceiver(unlockReceiver)
    }

    inner class ScreenActionReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            val action = intent.action

            when (action) {
                Intent.ACTION_SCREEN_ON -> screenOnEvent()
                Intent.ACTION_SCREEN_OFF -> Log.d(TAG, "screen is off...")
                Intent.ACTION_USER_PRESENT -> Log.d(TAG, "screen is unlock...")
            }
        }

        private fun screenOnEvent() {
            Log.d(TAG, "screen is on...")
            notificationHandler.createEazyTimeNotification()
        }

        fun getFilter(): IntentFilter {
            val filter = IntentFilter()
            filter.addAction(Intent.ACTION_SCREEN_OFF)
            filter.addAction(Intent.ACTION_SCREEN_ON)
            return filter
        }
    }
}
