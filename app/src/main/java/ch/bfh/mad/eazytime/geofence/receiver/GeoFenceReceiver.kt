package ch.bfh.mad.eazytime.geofence.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Receives geofence transitions with an Intent and the transition type
 * Creates a JobIntentService to handle the intent in the background
 */
class GeoFenceReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val service = GeoFenceTransitionsJobIntentService()
        service.enqueueWork(context, intent)
    }
}