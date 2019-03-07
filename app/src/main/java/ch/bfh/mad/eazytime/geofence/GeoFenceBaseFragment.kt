package ch.bfh.mad.eazytime.geofence

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import ch.bfh.mad.eazytime.data.entity.GeoFence
import ch.bfh.mad.eazytime.geofence.detail.GeoFenceDetailActivity
import ch.bfh.mad.eazytime.util.PermissionHandler

open class GeoFenceBaseFragment : androidx.fragment.app.Fragment() {

    private val permissionFineLocation = Manifest.permission.ACCESS_FINE_LOCATION
    private var permissionFineLocationGranted: Boolean = false
    private lateinit var viewModel: GeoFenceViewModel
    private lateinit var permissionHandler: PermissionHandler
    protected var hasResult: Boolean = false
    private val geoFenceAddRequest = 1
    protected val geoFenceUpdateRequest = 2

    protected fun bind(viewModel: GeoFenceViewModel) {
        this.viewModel = viewModel
    }

    protected fun addGeoFence() {
        permissionFineLocationGranted = permissionHandler.checkPermission()
        if (permissionFineLocationGranted) {
            startActivityForResult(GeoFenceDetailActivity.newIntent(context!!), geoFenceAddRequest)
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if ((requestCode == geoFenceAddRequest || requestCode == geoFenceUpdateRequest) && resultCode == Activity.RESULT_OK) {
            val id = data!!.getLongExtra("GEOFENCE_ID", -1L)
            val geoFence = GeoFence(
                data.getStringExtra("GEOFENCE_NAME"),
                data.getBooleanExtra("GEOFENCE_ACTIVE", false),
                data.getStringExtra("GEOFENCE_GFID"),
                data.getDoubleExtra("GEOFENCE_RADIUS", 0.0),
                data.getDoubleExtra("GEOFENCE_LAT", 0.0),
                data.getDoubleExtra("GEOFENCE_LONG", 0.0),
                if (id > -1L) id else null
            )

            if (requestCode == geoFenceUpdateRequest) {
                viewModel.update(geoFence)
            } else {
                viewModel.insert(geoFence)
            }
            hasResult = true
        }
    }
}