package ch.bfh.mad.eazytime.geofence

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import ch.bfh.mad.R

class GeoFenceFragment : Fragment() {

    private lateinit var listView: ListView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_geo_fence, container, false)
        activity!!.title = getString(R.string.geofence_fragment_title)

        listView = view.findViewById<ListView>(R.id.lv_geofences)

        view.findViewById<FloatingActionButton>(R.id.btn_addGeofence).setOnClickListener {
            addGeofence()
        }


        if (listView.count == 0) showEmptyGeofenceFragment()

        return view
    }

    private fun showEmptyGeofenceFragment() {
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.frame_content, GeoFenceFragmentEmpty())
            .addToBackStack(null)
            .commit()
    }

    private fun addGeofence() {
        // TODO replace with addFragment
    }
}
