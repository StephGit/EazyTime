package ch.bfh.mad.eazytime.geofence.detail

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import ch.bfh.mad.R

class GeoFenceRadiusFragment : Fragment() {

    private lateinit var callback: GeoFenceFlow

    companion object {
        fun newFragment(): Fragment = GeoFenceRadiusFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = context as? GeoFenceFlow ?: throw RuntimeException("Missing GeoFenceFlow implementation")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_geofence_radius, container, false)

        val proceedButton: Button = view.findViewById(R.id.btn_geoFenceRadiusProceed)
        val backButton: Button = view.findViewById(R.id.btn_geoFenceRadiusBack)

        proceedButton.setOnClickListener { callback.goToDetail() }
        backButton.setOnClickListener { callback.goToMarker() }

        return view
    }
}