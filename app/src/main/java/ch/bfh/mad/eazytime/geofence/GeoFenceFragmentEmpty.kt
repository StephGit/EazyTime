package ch.bfh.mad.eazytime.geofence

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ch.bfh.mad.R

class GeoFenceFragmentEmpty : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_geofence_empty, container, false)
        activity!!.title = getString(R.string.geofence_fragment_title)

        view.findViewById<FloatingActionButton>(R.id.btn_addGeofence).setOnClickListener {
            addGeofence()
        }

        return view
    }

    private fun addGeofence() {
        // TODO replace with addFragment
        startActivity(GeoFenceDetailActivity.newIntent(requireContext()))
    }


}