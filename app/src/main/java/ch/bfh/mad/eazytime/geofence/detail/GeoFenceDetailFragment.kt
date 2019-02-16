package ch.bfh.mad.eazytime.geofence.detail

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ch.bfh.mad.R

class GeoFenceDetailFragment : Fragment() {

    private lateinit var callback: GeoFenceFlow

    companion object {
        fun newFragment(): Fragment = GeoFenceDetailFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = context as? GeoFenceFlow ?: throw RuntimeException("Missing GeoFenceFlow implementation")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_geofence_detail, container, false)

        return view
    }

    fun saveGeoFenceDetail() {
        callback.leaveGeoFenceDetail()
    }
}