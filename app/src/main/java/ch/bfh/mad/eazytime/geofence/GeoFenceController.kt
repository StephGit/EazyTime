package ch.bfh.mad.eazytime.geofence

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import ch.bfh.mad.eazytime.data.entity.GeoFence
import ch.bfh.mad.eazytime.service.GeoFenceReceiver
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import javax.inject.Inject


class GeoFenceController @Inject constructor(private val context: Context) {

    private val geofencingClient: GeofencingClient = LocationServices.getGeofencingClient(context)

    /**
     * Creates a GoogleGeofence
     */
    private fun buildGeofence(id: String, latitude: Double, longitude: Double, radius: Double): Geofence? {
        return Geofence.Builder()
            // string id to identify
            .setRequestId(id)
            .setCircularRegion(
                latitude,
                longitude,
                radius.toFloat()
            )
            // Alerts are generated for these transistions
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
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
        val gf = buildGeofence(geoFence.gfId, geoFence.latitude, geoFence.longitude, geoFence.radius)
        if (gf != null
            && ContextCompat.checkSelfPermission(
                this.context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            geofencingClient
                .addGeofences(buildGeofencingRequest(gf), geofencePendingIntent)
                .addOnSuccessListener {
                    success()
                }
                .addOnFailureListener {
                    failure(GeofenceErrorMessages.getErrorString(context, it))
                }
        }
    }

    fun remove(
        geoFence: GeoFence,
        success: () -> Unit,
        failure: (error: String) -> Unit
    ) {
        geofencingClient
            .removeGeofences(listOf(geoFence.gfId))
            .addOnSuccessListener {
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
            .addGeofences(listOf(geofence))
            .build()
    }

    /**
     * PendingIntent to trigger TransitionsIntentService
     */
    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeoFenceReceiver::class.java)
        PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}