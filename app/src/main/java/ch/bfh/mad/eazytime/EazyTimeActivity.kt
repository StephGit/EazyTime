package ch.bfh.mad.eazytime

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.calendar.CalendarFragment
import ch.bfh.mad.eazytime.geofence.GeoFenceFragment
import ch.bfh.mad.eazytime.projects.ProjectFragment

class EazyTimeActivity : AppCompatActivity() {

    private lateinit var navigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eazy_time)
        navigation = findViewById(R.id.eazy_time_navigation)
        navigation.setOnNavigationItemSelectedListener { clickedMenuItem -> selectMenuItem(clickedMenuItem) }

        if (savedInstanceState == null) {
            showProjectFragment()
        }
    }

    private fun selectMenuItem(clickedMenuItem: MenuItem): Boolean {
        Log.i(TAG, "selectMenuItem: ${clickedMenuItem.title}")

        when (clickedMenuItem.itemId) {
            R.id.bottom_navigation_project -> showProjectFragment()
            R.id.bottom_navigation_calendar -> showCalendarFragment()
            R.id.bottom_navigation_geofence -> showGeofenceFragment()
            else -> throw IllegalArgumentException("Unknown clickedMenuItem.itemId: ${clickedMenuItem.itemId}")
        }
        return true
    }

    private fun showProjectFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_content, ProjectFragment())
            .commit()
    }

    private fun showCalendarFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_content, CalendarFragment())
            .commit()
    }

    private fun showGeofenceFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_content, GeoFenceFragment())
            .commit()
    }
}
