package ch.bfh.mad.eazytime.geofence.detail

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.EazyTimeActivity
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng


class GeoFenceDetailActivity : AppCompatActivity(), GeoFenceFlow, OnMapReadyCallback {
    private val defaultZoom: Float = 15F

    private lateinit var map: GoogleMap

    companion object{
        fun newIntent(ctx: Context)= Intent(ctx, GeoFenceDetailActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geofence_detail)

        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // TODO how to navigate directly to markerFragement?
        replaceFragment(GeoFenceDetailFragment.newFragment())
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
        moveCamera(LatLng(47.031182, 7.854871), defaultZoom)
        showGeoFenceOnMap()
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

    override fun goToMarker() {
        replaceFragment(GeoFenceMarkerFragment.newFragment())
    }

    override fun goToRadius() {
        replaceFragment(GeoFenceRadiusFragment.newFragment())
    }

    override fun goToDetail() {
        replaceFragment(GeoFenceDetailFragment.newFragment())
    }

    override fun leaveGeoFenceDetail() {
        startActivity(EazyTimeActivity.newIntent(this))
    }


}
