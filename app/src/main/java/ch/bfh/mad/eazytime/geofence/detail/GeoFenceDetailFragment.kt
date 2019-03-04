package ch.bfh.mad.eazytime.geofence.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import ch.bfh.mad.R

class GeoFenceDetailFragment : androidx.fragment.app.Fragment() {

    private lateinit var callback: GeoFenceFlow

    companion object {
        fun newFragment(): androidx.fragment.app.Fragment = GeoFenceDetailFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = context as? GeoFenceFlow ?: throw RuntimeException("Missing GeoFenceFlow implementation")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_geofence_detail, container, false)
        callback.setStep(GeoFenceFlow.Step.DETAIL)
        val backButton: Button = view.findViewById(R.id.btn_geoFenceDetailBack)
        val editButton: Button = view.findViewById(R.id.btn_geoFenceEdit)

        backButton.setOnClickListener { callback.leaveGeoFenceDetail() }
        editButton.setOnClickListener { callback.goToEdit() }

        return view
    }
}