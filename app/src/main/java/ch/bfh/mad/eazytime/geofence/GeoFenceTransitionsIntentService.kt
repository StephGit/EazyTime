//package ch.bfh.mad.eazytime.geofence
//
//import android.app.IntentService
//import android.content.Intent
//import android.util.Log
//import ch.bfh.mad.eazytime.TAG
//import ch.bfh.mad.eazytime.util.NotificationHandler
//import com.google.android.gms.location.Geofence
//import com.google.android.gms.location.GeofencingEvent
//
///**
// * Handles transition events when entering or leaving a geofence
// */
//class GeoFenceTransitionsIntentService : IntentService("GeoFenceTransitionIntentService") {
//
//    companion object;
//
//    private var provideNotificationHandler: NotificationHandler = NotificationHandler()
//
//    override fun onHandleIntent(intent: Intent?) {
//        val geofencingEvent = GeofencingEvent.fromIntent(intent)
//        if (geofencingEvent.hasError()) {
//            val errorMessage = GeofenceErrorMessages.getErrorString(
//                this,
//                geofencingEvent.errorCode
//            )
//            Log.e(TAG, errorMessage)
//            return
//        }
//        handleEvent(geofencingEvent)
//    }
//
//    private fun handleEvent(event: GeofencingEvent) {
//        if ((event.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) ||
//            (event.geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT)
//        ) {
//            // TODO handle it
//            Log.i(TAG, "FancyFenci")
//            provideNotificationHandler.sendNotification(this, "FancyFenci")
//            // timerService.startDefault/stopDefault
//        }
//    }
//}