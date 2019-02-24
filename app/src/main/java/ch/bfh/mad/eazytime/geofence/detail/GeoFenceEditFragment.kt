package ch.bfh.mad.eazytime.geofence.detail

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import ch.bfh.mad.R

class GeoFenceEditFragment : Fragment() {

    private lateinit var callback: GeoFenceFlow

    companion object {
        fun newFragment(): Fragment = GeoFenceEditFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = context as? GeoFenceFlow ?: throw RuntimeException("Missing GeoFenceFlow implementation")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_geofence_edit, container, false)
        callback.setStep(GeoFenceFlow.Step.DETAIL)
        val editButton: Button = view.findViewById(R.id.btn_geoFenceChange)
        val saveButton: Button = view.findViewById(R.id.btn_geoFenceSave)

        editButton.setOnClickListener { callback.goToMarker() }
        saveButton.setOnClickListener { callback.saveGeoFence() }

        return view
    }
}