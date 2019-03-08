package ch.bfh.mad.eazytime.geofence.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ch.bfh.mad.eazytime.di.Injector
import ch.bfh.mad.eazytime.geofence.GeoFenceService
import javax.inject.Inject

class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var geoFenceService: GeoFenceService

    init {
        Injector.appComponent.inject(this)
    }

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            // geofences are removed on reboot
            geoFenceService.initGeoFences()
        }
    }
}