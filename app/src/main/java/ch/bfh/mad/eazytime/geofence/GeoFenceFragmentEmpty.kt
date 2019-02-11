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

class GeoFenceFragmentEmpty : Fragment() {

    private val permissionFineLocation = Manifest.permission.ACCESS_FINE_LOCATION
    private var permissionFineLocationGranted: Boolean = false
    private val permissionRequestCode: Int = 303

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_geofence_empty, container, false)
        activity!!.title = getString(R.string.geofence_fragment_title)

        view.findViewById<FloatingActionButton>(R.id.btn_addGeofence).setOnClickListener {
            addGeofence()
        }

        return view
    }

    private fun addGeofence() {
        val permissionHandler = PermissionHandler(requireContext(), this)
        permissionFineLocationGranted = permissionHandler.checkForPermissions(permissionFineLocation, permissionRequestCode)

        if (permissionFineLocationGranted) {
            // TODO replace with addFragment
            startActivity(GeoFenceDetailActivity.newIntent(requireContext()))
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequestCode) {
            if (permissions.size == 1 &&
                permissions[0] == permissionFineLocation &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                permissionFineLocationGranted = true
                addGeofence()
            }
        }
    }


}