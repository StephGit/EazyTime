package ch.bfh.mad.eazytime.geofence.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import ch.bfh.mad.R

class GeoFenceEditFragment : androidx.fragment.app.Fragment() {

    private lateinit var callback: GeoFenceFlow
    private lateinit var geoFenceName: EditText

    companion object {
        fun newFragment(): androidx.fragment.app.Fragment = GeoFenceEditFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = context as? GeoFenceFlow ?: throw RuntimeException("Missing GeoFenceFlow implementation")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_geofence_edit, container, false)
        callback.setStep(GeoFenceFlow.Step.EDIT)
        val editButton: Button = view.findViewById(R.id.btn_geoFenceChange)
        val saveButton: Button = view.findViewById(R.id.btn_geoFenceSave)

        geoFenceName = view.findViewById(R.id.tv_geoFenceEditName)

        geoFenceName.setText(callback.getFenceName(), TextView.BufferType.EDITABLE)

        editButton.setOnClickListener { callback.goToMarker() }
        saveButton.setOnClickListener { onSave() }

        return view
    }

    private fun isValid(): Boolean {
        return when {
            geoFenceName.text.trim().isBlank() -> {
                geoFenceName.error = getString(R.string.geofence_edit_error_no_name)
                false
            }
            else -> true
        }
    }

    private fun onSave() {
        var name = geoFenceName.text.toString().trim()
        if (isValid()) callback.saveGeoFence(name)
    }
}