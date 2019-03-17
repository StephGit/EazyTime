package ch.bfh.mad.eazytime.remoteViews.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.di.Injector
import javax.inject.Inject

class ScreenActionReceiver : BroadcastReceiver() {

    init {
        Injector.appComponent.inject(this)
    }

    @Inject
    lateinit var notificationHandler: NotificationHandler

    override fun onReceive(context: Context, intent: Intent) {

        val action = intent.action

        when (action) {
            Intent.ACTION_SCREEN_ON ->  Log.d(TAG, "screen is on...")
            Intent.ACTION_SCREEN_OFF -> createNotification()
            Intent.ACTION_USER_PRESENT -> Log.d(TAG, "screen is unlock...")
        }
    }

    private fun createNotification() {
        Log.d(TAG, "screen is of...")
        notificationHandler.createEazyTimeNotification()
    }

    fun getFilter(): IntentFilter {
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        filter.addAction(Intent.ACTION_SCREEN_ON)
        return filter
    }
}