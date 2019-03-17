package ch.bfh.mad.eazytime.util

import android.app.Service
import android.content.Intent
import android.os.IBinder
import ch.bfh.mad.eazytime.data.repo.TimeSlotRepo
import ch.bfh.mad.eazytime.di.Injector
import javax.inject.Inject
import android.util.Log
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.TAG
import kotlinx.coroutines.runBlocking
import org.joda.time.LocalDateTime
import org.joda.time.Period
import kotlin.concurrent.fixedRateTimer
import android.app.NotificationManager
import android.app.PendingIntent

import android.content.Context
import androidx.core.app.NotificationCompat
import ch.bfh.mad.eazytime.EazyTimeActivity
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import java.util.*


class BurnoutProtectorService: Service() {

    @Inject
    lateinit var timeSlotRepo: TimeSlotRepo

    private var timer: Timer? = null
    private var lastExecution: LocalDateTime? = LocalDate().toLocalDateTime(LocalTime.MIDNIGHT)

    private val maxWorkHOurs: Int = R.integer.eazyTime_burnoutprotector_max_workhours

    init {
        Injector.appComponent.inject(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "Start BurnoutProtectorService")

        timer?.cancel()
        timer = fixedRateTimer("BurnoutProtector", true, initialDelay = 0, period = 15 * 60 * 1000) {
            Log.i(TAG, "BurnoutProtectorTimer executing")
            if(lastExecution?.plusMinutes(15)?.isBefore(LocalDateTime()) == true) {
                runBlocking {
                    //calculate only if currentTimeSlots exist
                    if(timeSlotRepo.getCurrentTimeSlots().isNotEmpty()) {
                        timeSlotRepo.todaysTimeSlotsList()
                        .map {timeSlot ->
                            timeSlot.endDate?.let {
                                Period(timeSlot.startDate, it)
                            } ?: Period(timeSlot.startDate, LocalDateTime())
                        }
                        .fold(Period(0L)) { acc, period -> acc.plus(period)}
                        ?.let {
                            if(it.toStandardHours().hours >= maxWorkHOurs) {
                                Log.i(TAG , "Showing BurnoutProtector notification")
                                showNotification()
                            }
                        }
                    }
                }
                lastExecution = LocalDateTime()
            }
        }
        return START_STICKY
    }


    private fun showNotification() {
        val builder = NotificationCompat.Builder(this, "burnoutProtectorChannel")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(getString(R.string.burnout_protector_notification_title))
            .setStyle(NotificationCompat.BigTextStyle().bigText(getString(R.string.burnout_protector_notification_content)))
        val notificationId = 789557

        val targetIntent = Intent(this, EazyTimeActivity::class.java)
        val contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        builder.setContentIntent(contentIntent)
        val nManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nManager.notify(notificationId, builder.build())
    }
}