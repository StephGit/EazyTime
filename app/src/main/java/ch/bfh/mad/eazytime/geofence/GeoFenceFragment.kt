package ch.bfh.mad.eazytime.geofence


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import ch.bfh.mad.R

class GeoFenceFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_geofence, container, false)
        val textView = view.findViewById<TextView>(R.id.tvGeofenceDetailItemName)
        textView.text = getString(R.string.placeholder) + " Geofence"
        activity!!.title = getString(R.string.geofence_fragment_title)
        return view
    }
}
