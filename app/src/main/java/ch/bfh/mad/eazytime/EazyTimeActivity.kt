package ch.bfh.mad.eazytime

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.calendar.CalendarFragment
import ch.bfh.mad.eazytime.di.Injector
import ch.bfh.mad.eazytime.geofence.GeoFenceFragment
import ch.bfh.mad.eazytime.geofence.GeoFenceService
import ch.bfh.mad.eazytime.projects.ProjectFragment
import ch.bfh.mad.eazytime.projects.addProject.AddProjectActivity
import ch.bfh.mad.eazytime.remoteViews.notification.ScreenActionService
import ch.bfh.mad.eazytime.util.CheckPowerSafeUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
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

    private inner class ScreenServiceKeepAliveTask : TimerTask() {
        override fun run() {
            // This keeps the ScreenActionReceiver online
            // Based on developer.android.com it will not be started twice: "...if it is running then it remains running."
            startService(Intent(application, ScreenActionService::class.java))
        }
    }
}
// TODO fix error
//E/AndroidRuntime: FATAL EXCEPTION: Timer-0
//Process: ch.bfh.mad, PID: 10555
//java.lang.IllegalStateException: Not allowed to start service Intent { cmp=ch.bfh.mad/.eazytime.remoteViews.notification.ScreenActionService }: app is in background uid UidRecord{7f8d4a7 u0a213 CAC  bg:+1m52s736ms idle procs:1 seq(0,0,0)}
//at android.app.ContextImpl.startServiceCommon(ContextImpl.java:1538)
//at android.app.ContextImpl.startService(ContextImpl.java:1484)
//at android.content.ContextWrapper.startService(ContextWrapper.java:663)
//at ch.bfh.mad.eazytime.EazyTimeActivity$ScreenServiceKeepAliveTask.run(EazyTimeActivity.kt:94)
//at java.util.TimerThread.mainLoop(Timer.java:555)
//at java.util.TimerThread.run(Timer.java:505)