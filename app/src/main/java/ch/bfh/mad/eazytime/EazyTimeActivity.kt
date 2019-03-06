package ch.bfh.mad.eazytime

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.calendar.CalendarFragment
import ch.bfh.mad.eazytime.di.Injector
import ch.bfh.mad.eazytime.geofence.GeoFenceFragment
import ch.bfh.mad.eazytime.projects.ProjectFragment
import ch.bfh.mad.eazytime.projects.addProject.AddProjectActivity
import ch.bfh.mad.eazytime.remoteViews.notification.ScreenActionService
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*


class EazyTimeActivity : AppCompatActivity(), EazyTimeNavigator {

    init {
        Injector.appComponent.inject(this)
    }

    private lateinit var navigation: BottomNavigationView

    companion object {
        fun newIntent(ctx: Context) = Intent(ctx, EazyTimeActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eazy_time)
        navigation = findViewById(R.id.eazy_time_navigation)
        navigation.setOnNavigationItemSelectedListener { clickedMenuItem -> selectMenuItem(clickedMenuItem) }

        if (savedInstanceState == null) {
            replaceFragment(ProjectFragment())
        }

        Timer().scheduleAtFixedRate(ScreenServiceKeepAliveTask(), 0, 1000*60)
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
            if (startService(Intent(application, ScreenActionService::class.java)) != null) {
                Log.i(TAG, "ScreenActionService started via Intent")
            } else {
                Log.i(TAG, "ScreenActionService already running")
            }
        }
    }
}
