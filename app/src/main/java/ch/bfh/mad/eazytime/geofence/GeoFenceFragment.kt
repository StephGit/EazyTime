package ch.bfh.mad.eazytime.geofence


import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.ProgressBar
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.projects.PermissionHandler
import ch.bfh.mad.eazytime.util.ViewModelFactory

class GeoFenceFragment : Fragment() {


    private val permissionFineLocation = Manifest.permission.ACCESS_FINE_LOCATION
    private var permissionFineLocationGranted: Boolean = false
    private val permissionHandler = PermissionHandler(this, permissionFineLocation)

    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar

    companion object {
        fun newFragment(): Fragment = GeoFenceFragment()
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_geofence, container, false)
        activity!!.title = getString(R.string.geofence_fragment_title)

        view.findViewById<FloatingActionButton>(R.id.btn_addGeofence).setOnClickListener { addGeofence() }

        listView = view.findViewById(R.id.lv_geofences)
        progressBar = view.findViewById(R.id.progressBar)

        getListItems(view)

        if (listView.count == 0) showEmptyGeofenceFragment() else showList()

        return view
    }

    private fun getListItems(view: View) {
        val factory = ViewModelFactory()
        val viewModel: GeoFenceViewModel = ViewModelProviders.of(this, factory).get(GeoFenceViewModel::class.java)

        viewModel.geoFenceItems.observe(this, Observer {
            val lvGeofences = view.findViewById<ListView>(R.id.lv_geofences)
            val customAdapter = GeoFenceAdapter(requireContext(), 0, it!!)
            lvGeofences.adapter = customAdapter
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }

    private fun addGeofence() {
        // TODO replace with addFragment
        if (permissionFineLocationGranted) {
            startActivity(GeoFenceDetailActivity.newIntent(requireContext()))
        }
    }
    private fun checkPermission() {
        permissionFineLocationGranted = permissionHandler.checkPermission()
    }

    private fun showEmptyGeofenceFragment() {
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.frame_content, GeoFenceFragmentEmpty.newFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun showList() {
        progressBar.visibility = View.GONE
        listView.visibility = View.VISIBLE
    }


    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionFineLocationGranted = permissionHandler.permissionGranted
    }
}
