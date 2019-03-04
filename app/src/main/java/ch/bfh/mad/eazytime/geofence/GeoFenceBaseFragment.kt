package ch.bfh.mad.eazytime.geofence

import android.Manifest
import android.annotation.SuppressLint
import ch.bfh.mad.eazytime.util.PermissionHandler

open class GeoFenceBaseFragment : androidx.fragment.app.Fragment() {

    private val permissionFineLocation = Manifest.permission.ACCESS_FINE_LOCATION
    private var permissionFineLocationGranted: Boolean = false
    private lateinit var permissionHandler: PermissionHandler


    protected fun addGeofence() {
        // TODO replace with addFragment
        if (permissionFineLocationGranted) {
            //startActivity(GeoFenceDetailActivity.newIntent(requireContext()))
        }
    }

    protected fun checkPermission(fragment: androidx.fragment.app.Fragment) {
        permissionHandler = PermissionHandler(fragment, permissionFineLocation)
        permissionFineLocationGranted = permissionHandler.checkPermission()
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionFineLocationGranted = permissionHandler.permissionGranted
    }
}
