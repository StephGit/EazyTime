package ch.bfh.mad.eazytime.remoteViews.notification

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.di.Injector
import javax.inject.Inject

class ScreenActionService : Service() {

    @Inject
    lateinit var unlockReceiver: ScreenActionReceiver

    @Inject
    lateinit var notificationHandler: NotificationHandler

    init {
        Injector.appComponent.inject(this)
    }

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
}
