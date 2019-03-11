package ch.bfh.mad.eazytime

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.calendar.CalendarFragment
import ch.bfh.mad.eazytime.calendar.detail.CalendarDetailActivity
import ch.bfh.mad.eazytime.di.Injector
import ch.bfh.mad.eazytime.geofence.GeoFenceFragment
import ch.bfh.mad.eazytime.geofence.GeoFenceService
import ch.bfh.mad.eazytime.projects.ProjectFragment
import ch.bfh.mad.eazytime.projects.addProject.AddProjectActivity
import ch.bfh.mad.eazytime.remoteViews.notification.ScreenActionService
import ch.bfh.mad.eazytime.util.CheckPowerSafeUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.IllegalStateException
import javax.inject.Inject
import java.util.*


class EazyTimeActivity : AppCompatActivity(), EazyTimeNavigator {

    private lateinit var navigation: BottomNavigationView
    private lateinit var prefs: SharedPreferences

    @Inject
    lateinit var geoFenceService: GeoFenceService

    @Inject
    lateinit var checkPowerSaveUtil: CheckPowerSafeUtil

    companion object {
        fun newIntent(ctx: Context) = Intent(ctx, EazyTimeActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eazy_time)
        navigation = findViewById(R.id.eazy_time_navigation)
        navigation.setOnNavigationItemSelectedListener { clickedMenuItem -> selectMenuItem(clickedMenuItem) }
        Injector.appComponent.inject(this)
        prefs = getSharedPreferences("ch.bfh.mad.eazytime", Context.MODE_PRIVATE)

        if (savedInstanceState == null) {
            replaceFragment(ProjectFragment())
        }

        Timer().scheduleAtFixedRate(ScreenServiceKeepAliveTask(), 0, 1000*60)
    }

    override fun onResume() {
        super.onResume()
        if (geoFenceService.initGeoFences()) {
            if (!prefs.getBoolean("ignorePowerSafe", false)) {
                checkPowerSaveUtil.checkPowerSaveMode(supportFragmentManager)
            }
        }
    }

    private fun selectMenuItem(clickedMenuItem: MenuItem): Boolean {
        Log.i(TAG, "selectMenuItem: ${clickedMenuItem.title}")

        when (clickedMenuItem.itemId) {
            R.id.bottom_navigation_project -> replaceFragment(ProjectFragment())
            R.id.bottom_navigation_calendar -> replaceFragment(CalendarFragment())
            R.id.bottom_navigation_geofence -> replaceFragment(GeoFenceFragment.newFragment())
            else -> throw IllegalArgumentException("Unknown clickedMenuItem.itemId: ${clickedMenuItem.itemId}")
        }
        return true
    }

    private fun replaceFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_content, fragment)
            .commit()
    }

    override fun openAddProjectActivity() {
        startActivity(AddProjectActivity.getAddProjectActivityIntent(this))
    }

    override fun openUpdateProjectActivity(projectId: Long) {
        startActivity(AddProjectActivity.getUpdateProjectActivityIntent(this, projectId))
    }

    override fun openCalendarDetailActivity(workDayId: Long) {
        startActivity(CalendarDetailActivity.getIntent(this, workDayId))
    }

    private inner class ScreenServiceKeepAliveTask : TimerTask() {
        override fun run() {
            // This keeps the ScreenActionReceiver online
            // Based on developer.android.com it will not be started twice: "...if it is running then it remains running."
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(Intent(application, ScreenActionService::class.java))
            } else {
                try {
                    startService(Intent(application, ScreenActionService::class.java))
                } catch (error: IllegalStateException) {
                    Log.d(TAG, error.toString())
                }
            }
        }
    }
}