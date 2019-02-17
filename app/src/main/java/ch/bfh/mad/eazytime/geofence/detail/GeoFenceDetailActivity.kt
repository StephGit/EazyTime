package ch.bfh.mad.eazytime.geofence.detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.EazyTimeActivity
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.data.entity.GeoFence
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task


class GeoFenceDetailActivity : AppCompatActivity(),
    GeoFenceFlow,
    OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, GoogleMap.OnMapLongClickListener {

    private val defaultZoom: Float = 15F

    private var hasTarget: Boolean = false
    private var hasGeoFence: Boolean = false
    private lateinit var geoFence: GeoFence

    private lateinit var map: GoogleMap
    private lateinit var locationProviderClient: FusedLocationProviderClient
    private lateinit var geofencingClient: GeofencingClient

    companion object{
        fun newIntent(ctx: Context)= Intent(ctx, GeoFenceDetailActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geofence_detail)

        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        geofencingClient = LocationServices.getGeofencingClient(this)

        // TODO navigate directly to markerFragement if no item in bundle
        if (intent.hasExtra("GEOFENCE_ITEM")) {
            this.geoFence = intent.getSerializableExtra("GEOFENCE_ITEM") as (GeoFence)
            replaceFragment(GeoFenceDetailFragment.newFragment())
        } else {
            replaceFragment(GeoFenceMarkerFragment.newFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.geofenceFlowContainer, fragment)
            .addToBackStack(null) // TODO check back navigation on detailActivity
            .commit()
    }

    /*
   MapStuff
    */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isMyLocationButtonEnabled = true
        map.uiSettings.isMapToolbarEnabled = true
        map.mapType = GoogleMap.MAP_TYPE_HYBRID

        enableLocation()
        showGeoFenceOnMap()

        with(map) {
            setOnMapLongClickListener(this@GeoFenceDetailActivity)
        }
    }

    override fun onMyLocationClick(p0: Location) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onMyLocationButtonClick(): Boolean {
        showCurrentLocation()
        return true
    }

    override fun onMapLongClick(p0: LatLng?) {
        map.addMarker(
            MarkerOptions()
                .position(LatLng(p0!!.latitude, p0!!.longitude))
        )
    }

    private fun showGeoFenceOnMap() {
//        private fun showGeoFenceOnMap(location: Location) {
        var circleOptions: CircleOptions = CircleOptions()
            .center( LatLng(47.031182, 7.854871)) // TODO remove, only for testing
//            .center( LatLng(location.latitude, location.longitude))
            .radius(50.0)
            .fillColor(0x40ff0000)
            .strokeColor(Color.TRANSPARENT)
            .strokeWidth(2F)
            .clickable(true)

        map.addCircle(circleOptions)
        map.setOnCircleClickListener {
            // TODO edit Geofence
            Toast.makeText(applicationContext, "circle clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun moveCamera(latLng: LatLng, zoom: Float) {
        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(latLng, zoom)
        )
    }


    /** Location
     *
     */

    @SuppressLint("MissingPermission")
    private fun enableLocation() {
        with(map) {

            isMyLocationEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
            setOnMyLocationButtonClickListener(this@GeoFenceDetailActivity)
            setOnMyLocationClickListener(this@GeoFenceDetailActivity)
        }
    }

    private fun showCurrentLocation() {
        try {

            var locationResult: Task<Location> = locationProviderClient.lastLocation

            locationResult.addOnCompleteListener {
                if (it.isComplete && it.isSuccessful) {
                    val currentLocation = it.result
                    currentLocation?.let { loc ->
                        moveCamera(LatLng(loc.latitude, loc.longitude), defaultZoom)
                    }
                }
            }

        } catch (e: SecurityException) {
            Log.e(TAG, "Exception: " + e.message)
        }
    }



    override fun goToMarker() {
        replaceFragment(GeoFenceMarkerFragment.newFragment())
    }

    override fun goToRadius() {
        replaceFragment(GeoFenceRadiusFragment.newFragment())
    }

    override fun goToDetail() {
        replaceFragment(GeoFenceDetailFragment.newFragment())
    }

    override fun deleteGeoFence() {
        // TODO implement delete
        leaveGeoFenceDetail()
    }

    override fun saveGeoFence() {
        // TODO implement save
        leaveGeoFenceDetail()
    }

    override fun leaveGeoFenceDetail() {
        startActivity(EazyTimeActivity.newIntent(this))
    }


}
