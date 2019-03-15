package ch.bfh.mad.eazytime.geofence

import android.Manifest
import android.annotation.SuppressLint
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

//    private val geoFenceEntities: MutableList<GeoFence> = ArrayList()

    private val geofences: MutableMap<Long, String> = HashMap()
    private val geofencingClient: GeofencingClient = LocationServices.getGeofencingClient(context)
    private var localGeofencePendingIntent: PendingIntent? = null

    /**
     * Add Geofence to GeofencingClient
     */
    private fun add(geoFence: GeoFence) {

        if (isAlreadyAdded(geoFence)) return

        val geofence = buildGeofence(geoFence.requestId!!, geoFence.latitude!!, geoFence.longitude!!, geoFence.radius!!)
        if (geofence != null && hasPermissions()) {
            geofences[geoFence.id!!] = geoFence.requestId!!
            addToGeofencingClient(geofence)
        }
    }

    /**
     * Updates an existing geofence if already in map, otherwise adds it to map.
     * If an existing geofence with the same request ID is already registered, the old geofence is replaced by geofencingClient with the new one
     */
    fun addOrUpdate(geoFence: GeoFence) {
        if (isAlreadyAdded(geoFence)) {
            // if requestId changes old geofence has to be removed
            if (geoFence.requestId != this.geofences[geoFence.id]) {
                remove(geoFence)
                add(geoFence)
            } else {
                val geofence =
                    buildGeofence(geoFence.requestId!!, geoFence.latitude!!, geoFence.longitude!!, geoFence.radius!!)
                if (geofence != null && hasPermissions()) {
                    addToGeofencingClient(geofence)
                }
            }
        } else {
            add(geoFence)
        }
    }

    @SuppressLint("MissingPermission")
    private fun addToGeofencingClient(geofence: Geofence) {
        geofencingClient
            .addGeofences(buildGeofencingRequest(geofence), geofencePendingIntent)
            .addOnCompleteListener(this@GeoFenceService)
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
     *  Setting the value to 0 indicates that you don’t want to trigger a GEOFENCE_TRANSITION_ENTER event if the device is already inside the geofence
     *  Adds the list of geofences to be monitored
     */
    private fun buildGeofencingRequest(geofence: Geofence): GeofencingRequest {
        return GeofencingRequest.Builder()
            .setInitialTrigger(0)
            .addGeofence(geofence)
            .build()
    }

    private fun hasPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this.context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Used to initialize geofences after reboot or on app-start
     */
    fun initGeoFences(): Boolean {
        val tmpList: MutableList<GeoFence> = ArrayList()
        tmpList.addAll(geoFenceRepo.getActiveGeoFences())
        tmpList.forEach {
            add(it)
        }
        return (geofences.isNotEmpty())
    }

    /**
     * Check if geoFence-Entity-Id is in map
     */
    private fun isAlreadyAdded(geoFence: GeoFence): Boolean {
        return (this.geofences.containsKey(geoFence.id))
    }

    override fun onComplete(task: Task<Void>) {
        if (task.isSuccessful) {
            Log.d(TAG, "GeoFence Task successfully")
        } else {
            Log.d(TAG, "Adding failed for GeoFence, Error: " + geofenceErrorMessages.getErrorString(task.exception!!))
        }
    }

    fun remove(geoFence: GeoFence) {
        if (!isAlreadyAdded(geoFence)) return
        val requestToRemove = this.geofences[geoFence.id]
        this.geofences.remove(geoFence.id)
        geofencingClient
            .removeGeofences(listOf(requestToRemove))
            .addOnCompleteListener(this@GeoFenceService)
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