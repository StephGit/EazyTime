package ch.bfh.mad.eazytime.util

import android.app.Service
import android.content.Intent
import android.os.IBinder
import ch.bfh.mad.eazytime.data.repo.TimeSlotRepo
import ch.bfh.mad.eazytime.di.Injector
import javax.inject.Inject
import android.util.Log
import ch.bfh.mad.eazytime.R
import ch.bfh.mad.eazytime.TAG
import kotlinx.coroutines.runBlocking
import org.joda.time.LocalDateTime
import org.joda.time.Period
import kotlin.concurrent.fixedRateTimer

import ch.bfh.mad.eazytime.remoteViews.notification.NotificationHandler
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import java.util.*


class BurnoutProtectorService: Service() {

    @Inject
    lateinit var timeSlotRepo: TimeSlotRepo

    @Inject
    lateinit var notificationHandler: NotificationHandler

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
        val fifteenMinutes = 1000L * 60 * 15
        timer = fixedRateTimer("BurnoutProtector", true, initialDelay = 0, period = fifteenMinutes) {
            // track last execution to handle service restarts
            if(lastExecution?.plusMinutes(15)?.isBefore(LocalDateTime()) == true) {
                Log.i(TAG, "BurnoutProtectorTimer executing")
                runBlocking {
                    // calculate only if currentTimeSlots exist
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
                                notificationHandler.createBurnotProtectorNotification()
                            }
                        }
                    }
                }
                lastExecution = LocalDateTime()
            }
        }
        return START_STICKY
    }

}