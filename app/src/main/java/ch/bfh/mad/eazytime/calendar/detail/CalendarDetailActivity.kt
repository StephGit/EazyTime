package ch.bfh.mad.eazytime.calendar.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

class CalendarDetailActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        TODO("DUMMY")
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