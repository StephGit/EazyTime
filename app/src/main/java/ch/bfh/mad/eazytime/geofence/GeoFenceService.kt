package ch.bfh.mad.eazytime.geofence

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.data.GeoFenceRepository
import ch.bfh.mad.eazytime.data.entity.GeoFence
import ch.bfh.mad.eazytime.geofence.receiver.GeoFenceReceiver
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import javax.inject.Inject


class GeoFenceService @Inject constructor(
    private val context: Context,
    private val geoFenceRepository: GeoFenceRepository
) {

    private val geoFences: MutableList<GeoFence> = ArrayList()
    private val geofencingClient: GeofencingClient = LocationServices.getGeofencingClient(context)

    /**
     * Explicit removal of all active geofences and add them again.
     */
    fun initGeoFences() {
        this.geoFences.addAll(geoFenceRepository.getActiveGeoFences())
        geoFences.forEach {
            remove(it,
                success = { Log.d(TAG, "GeoFence " + it.name + " removed successfully") },
                failure = { err -> Log.d(TAG, "Removal failed for GeoFence [" + it.name + "], Error: " + err) })
        }
        geoFences.forEach {
            add(it,
                success = { Log.d(TAG, "GeoFence " + it.name + " added successfully") },
                failure = { err -> Log.d(TAG, "Adding failed for GeoFence [" + it.name + "], Error: " + err) })
        }
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
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            // Interval to check for events, default is 0 - battery improvement
            .setNotificationResponsiveness(30000)
            .build()
    }

    /**
     * Add Geofence to GeofencingClient
     */
    fun add(
        geoFence: GeoFence,
        success: () -> Unit,
        failure: (error: String) -> Unit
    ) {
        if (isInList(geoFence)) {
            return
        }
        val gf = buildGeofence(geoFence.gfId!!, geoFence.latitude!!, geoFence.longitude!!, geoFence.radius!!)
        if (gf != null &&
            ContextCompat.checkSelfPermission(
                this.context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            geofencingClient
                .addGeofences(buildGeofencingRequest(gf), geofencePendingIntent)
                .addOnSuccessListener {
                    this.geoFences.add(geoFence)
                    success()
                }
                .addOnFailureListener {
                    failure(GeofenceErrorMessages.getErrorString(context, it))
                }
        }
    }

    private fun isInList(geoFence: GeoFence): Boolean {
        return (this.geoFences.find { it == geoFence } != null)
    }

    fun remove(
        geoFence: GeoFence,
        success: () -> Unit,
        failure: (error: String) -> Unit
    ) {
        if (!isInList(geoFence)) {
            return
        }
        geofencingClient
            .removeGeofences(listOf(geoFence.gfId))
            .addOnSuccessListener {
                this.geoFences.remove(geoFence)
                success()
            }
            .addOnFailureListener {
                failure(GeofenceErrorMessages.getErrorString(context, it))
            }
    }

    /**
     *  Setting the value to 0 indicates that you don’t want to trigger a GEOFENCE_TRANSITION_ENTER event if the device is already inside the geofence
     */
    private fun buildGeofencingRequest(geofence: Geofence): GeofencingRequest {
        return GeofencingRequest.Builder()
            .setInitialTrigger(0)
            .addGeofence(geofence)
            .build()
    }

    /**
     * PendingIntent to trigger TransitionsIntentService
     */
    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeoFenceReceiver::class.java)
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}