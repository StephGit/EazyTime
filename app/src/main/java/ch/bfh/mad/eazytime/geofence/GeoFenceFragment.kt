package ch.bfh.mad.eazytime.geofence


import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.util.ViewModelFactory

class GeoFenceFragment : Fragment() {

    private val permissionFineLocation = Manifest.permission.ACCESS_FINE_LOCATION
    private var permissionFineLocationGranted: Boolean = false
    private val permissionRequestCode: Int = 302

    private lateinit var listView: ListView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_geofence, container, false)
        activity!!.title = getString(R.string.geofence_fragment_title)

        listView = view.findViewById<ListView>(R.id.lv_geofences)

        view.findViewById<FloatingActionButton>(R.id.btn_addGeofence).setOnClickListener {
            addGeofence()
        }

        val factory = ViewModelFactory()
        val viewModel: GeoFenceViewModel = ViewModelProviders.of(this, factory).get(GeoFenceViewModel::class.java)

        viewModel.geoFenceItems.observe(this, Observer {
            val lvGeofences = view.findViewById<ListView>(R.id.lv_geofences)
            val customAdapter = GeoFenceAdapter(requireContext(), 0, it!!)
            lvGeofences.adapter = customAdapter
        })

        if (listView.count == 0) showEmptyGeofenceFragment()

        return view
    }

    private fun showEmptyGeofenceFragment() {
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.frame_content, GeoFenceFragmentEmpty())
            .addToBackStack(null)
            .commit()
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
