package ch.bfh.mad.eazytime.geofence

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import ch.bfh.mad.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng


class GeoFenceDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private val DEFAULT_ZOOM: Float = 15F

    private lateinit var map: GoogleMap

    companion object{
        fun newIntent(ctx: Context)= Intent(ctx,GeoFenceDetailActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_geofence_detail)

        // TODO move to geofence main activity  -> fragment vs. activity


        val mapFragment: MapFragment = fragmentManager.findFragmentById(R.id.map) as MapFragment
        mapFragment.getMapAsync(this)


    }


    private fun replaceFragment(fragment: Fragment) {

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.geofenceDetail, fragment)
            .commit()
    }

    /*
   MapStuff
    */

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        moveCamera(LatLng(47.031182, 7.854871), DEFAULT_ZOOM)
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


}
