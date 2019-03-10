package ch.bfh.mad.eazytime.geofence

import android.content.Context
import ch.bfh.mad.R
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.GeofenceStatusCodes
import javax.inject.Inject

class GeofenceErrorMessages @Inject constructor(private var context: Context) {

    fun getErrorString(e: Exception): String {
        return if (e is ApiException) {
            getErrorString(e.statusCode)
        } else {
            context.resources.getString(R.string.geofence_unknown_error)
        }
    }

    fun getErrorString(errorCode: Int): String {
        val resources = context.resources
        return when (errorCode) {
            GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE ->
                resources.getString(R.string.geofence_not_available)

            GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES ->
                resources.getString(R.string.geofence_too_many_geofences)

            GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS ->
                resources.getString(R.string.geofence_too_many_pending_intents)

            else -> resources.getString(R.string.geofence_unknown_error)
        }
    }
}