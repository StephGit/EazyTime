package ch.bfh.mad.eazytime.calendar.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import ch.bfh.mad.eazytime.di.Injector
import org.joda.time.format.DateTimeFormat
import java.util.*
import javax.inject.Inject

class CalendarDetailActivity: AppCompatActivity() {

    private lateinit var calendarDetailViewModel: CalendarDetailViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Injector.appComponent.inject(this)
        calendarDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(CalendarDetailViewModel::class.java)

        val workdayId = intent.extras?.getLong(INTENT_EXTRA_KEY_WORKDAY_ID)
        workdayId?.let {
            val moo = calendarDetailViewModel.getCalendarDetail(it)
            moo.first().workDayDate?.let {date ->
                val pattern = DateTimeFormat.forPattern("EEEE, dd. MM. yyyy").withLocale(Locale.GERMAN)
                title = pattern.print(date)
            }

            print(moo)
        }

    }

    companion object {

        private const val INTENT_EXTRA_KEY_WORKDAY_ID = "Constants"

        fun getIntent(ctx: Context, workDayId: Long): Intent {
            return Intent(ctx, CalendarDetailActivity::class.java).also { intent ->
                intent.putExtra(INTENT_EXTRA_KEY_WORKDAY_ID, workDayId)
            }
        }
    }
}