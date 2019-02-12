package ch.bfh.mad.eazytime.geofence

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.projects.PermissionHandler

class GeoFenceFragmentEmpty : Fragment() {


    private val permissionFineLocation = Manifest.permission.ACCESS_FINE_LOCATION
    private var permissionFineLocationGranted: Boolean = false
    private val permissionHandler = PermissionHandler(this, permissionFineLocation)

    private lateinit var addButton: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_geofence_empty, container, false)
        activity!!.title = getString(R.string.geofence_fragment_title)

        addButton = view.findViewById<FloatingActionButton>(R.id.btn_addGeofence)

        checkPermission()
        enableAddButton()
        return view
    }

    private fun addGeofence() {
        // TODO replace with addFragment
        startActivity(GeoFenceDetailActivity.newIntent(requireContext()))
    }

    private fun checkPermission() {
        permissionFineLocationGranted = permissionHandler.checkPermission()
    }

    private fun enableAddButton() {
        if (permissionFineLocationGranted) {
            addButton.isEnabled = true
            addButton.setOnClickListener { addGeofence() }
        } else {
            addButton.isEnabled = false
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}