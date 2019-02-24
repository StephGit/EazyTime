package ch.bfh.mad.eazytime.geofence

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ch.bfh.mad.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class GeoFenceEmptyFragment : GeoFenceBaseFragment() {

    companion object {
        fun newFragment(): androidx.fragment.app.Fragment = GeoFenceEmptyFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_geofence_empty, container, false)
        activity!!.title = getString(R.string.geofence_fragment_title)

        view.findViewById<FloatingActionButton>(R.id.btn_addGeofence).setOnClickListener { super.addGeoFence() }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.checkPermission(this)
    }

    override fun onResume() {
        super.onResume()
        if (super.hasResult) showGeofenceFragment()
    }

    private fun showGeofenceFragment() {
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.frame_content, GeoFenceFragment.newFragment())
            .addToBackStack(null)
            .commit()
    }
}