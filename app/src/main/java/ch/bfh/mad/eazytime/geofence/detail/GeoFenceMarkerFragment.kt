package ch.bfh.mad.eazytime.geofence.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import ch.bfh.mad.eazytime.R

class GeoFenceMarkerFragment : androidx.fragment.app.Fragment() {

    private lateinit var callback: GeoFenceFlow

    companion object {
        fun newFragment(): androidx.fragment.app.Fragment = GeoFenceMarkerFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = context as? GeoFenceFlow ?: throw RuntimeException("Missing GeoFenceFlow implementation")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_geofence_marker, container, false)
        callback.setStep(GeoFenceFlow.Step.MARKER)
        val proceedButton: Button = view.findViewById(R.id.btn_geoFenceMarkerProceed)
        val backButton: Button = view.findViewById(R.id.btn_geoFenceMarkerBack)
        proceedButton.setOnClickListener { callback.goToRadius() }
        backButton.setOnClickListener { callback.goBack() }

        return view
    }
}