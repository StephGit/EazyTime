package ch.bfh.mad.eazytime.homeScreenWidget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.di.Injector

class WidgetBroadCastReceiver : BroadcastReceiver() {

    init {
        Injector.appComponent.inject(this)
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "WidgetBroadCastReceiver received: ${intent.action}")

        Toast.makeText(context, "HomeScreen", Toast.LENGTH_SHORT).show()
    }

}