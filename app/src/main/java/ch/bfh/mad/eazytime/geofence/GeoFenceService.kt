package ch.bfh.mad.eazytime.geofence

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.data.entity.GeoFence
import ch.bfh.mad.eazytime.data.repo.GeoFenceRepo
import ch.bfh.mad.eazytime.geofence.receiver.GeoFenceBroadcastReceiver
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import javax.inject.Inject


class GeoFenceService @Inject constructor(
    private val context: Context,
    private val geoFenceRepo: GeoFenceRepo,
    private val geofenceErrorMessages: GeofenceErrorMessages
) : OnCompleteListener<Void> {

    private val geoFenceEntities: MutableList<GeoFence> = ArrayList()
    private val geofences: MutableList<Geofence> = ArrayList()
    private val geofencingClient: GeofencingClient = LocationServices.getGeofencingClient(context)
    private var localGeofencePendingIntent: PendingIntent? = null

    /**
     * Explicit removal of all active geofences and add them again.
     */
    fun initGeoFences() : Boolean {

        geoFenceEntities.addAll(geoFenceRepo.getActiveGeoFences())
        geoFenceEntities.forEach {
            remove(it)
            add(it)
        }
        return (geofences.size > 0)
    }

    /**
     * Creates a GoogleGeofence
     */
    private fun buildGeofence(id: String, latitude: Double, longitude: Double, radius: Double): Geofence? {
        return Geofence.Builder()
            // string id to identify
            .setRequestId(id)
            .setCircularRegion(latitude, longitude, radius.toFloat())
            // Alerts are generated for these transistions
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            // Interval to check for events, default is 0 - battery improvement
            .setNotificationResponsiveness(0)
            .build()
    }

    /**
     * Add Geofence to GeofencingClient
     */
    fun add(geoFence: GeoFence) {

        if (!isInList(geoFence)) this.geoFenceEntities.add(geoFence) else return

        val geofence = buildGeofence(geoFence.gfId!!, geoFence.latitude!!, geoFence.longitude!!, geoFence.radius!!)
        if (geofence != null &&
            ContextCompat.checkSelfPermission(
                this.context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            geofences.add(geofence)
            geofencingClient
                .addGeofences(buildGeofencingRequest(geofences), geofencePendingIntent)
                .addOnCompleteListener(this@GeoFenceService)
        }
    }

    fun remove(geoFence: GeoFence) {
        if (isInList(geoFence)) this.geoFenceEntities.remove(geoFence) else return

        geofencingClient
            .removeGeofences(listOf(geoFence.gfId))
            .addOnCompleteListener(this@GeoFenceService)
    }

    private fun isInList(geoFence: GeoFence): Boolean {
        return (this.geoFenceEntities.find { it == geoFence } != null)
    }

    override fun onComplete(task: Task<Void>) {
        if (task.isSuccessful) {
            Log.d(TAG, "GeoFence added successfully")
        } else {
            Log.d(TAG, "Adding failed for GeoFence, Error: " + geofenceErrorMessages.getErrorString(task.exception!!))
        }
    }

    /**
     *  Setting the value to 0 indicates that you don’t want to trigger a GEOFENCE_TRANSITION_ENTER event if the device is already inside the geofence
     *  Adds the list of geofences to be monitored
     */
    private fun buildGeofencingRequest(geofences: MutableList<Geofence>): GeofencingRequest {
        return GeofencingRequest.Builder()
            .setInitialTrigger(0)
            .addGeofences(geofences)
            .build()
    }

    /**
     * Returns a PendingIntent with the request to trigger GeoFenceBroadcastReceiver.
     * Location Service addresses the intent inside this PendingIntent.
     * With usage of FLAG_UPDATE_CURRENT we reuse the same pending intent when calling this function
     */
    private val geofencePendingIntent: PendingIntent? by lazy {
        if (localGeofencePendingIntent != null) return@lazy localGeofencePendingIntent

        val intent = Intent(context, GeoFenceBroadcastReceiver::class.java)
        localGeofencePendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        return@lazy localGeofencePendingIntent
    }
}