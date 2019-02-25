package ch.bfh.mad.eazytime.geofence.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.Toast
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.data.GeoFenceRepository
import ch.bfh.mad.eazytime.data.entity.GeoFence
import ch.bfh.mad.eazytime.di.Injector
import ch.bfh.mad.eazytime.geofence.GeoFenceController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task
import java.util.*
import javax.inject.Inject

// TODO wrap text on steps
// TODO disable buttons on steps?
// TODO add Geofence to entity? as one or properties?

class GeoFenceDetailActivity : AppCompatActivity(),
    GeoFenceFlow,
    OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMapLongClickListener,
    ScaleGestureDetector.OnScaleGestureListener {

    @Inject
    lateinit var geoFenceRepository: GeoFenceRepository

    @Inject
    lateinit var geoFenceController: GeoFenceController

    private val defaultZoom: Float = 15F
    private var defaultRadius: Double = 0.0
    private var scaleFactor: Float = 1.0F
    private val minZoom: Float = 0F
    private val maxZoom: Float = 8F

    private lateinit var step: GeoFenceFlow.Step
    private lateinit var geoFence: GeoFence

    private lateinit var map: GoogleMap
    private lateinit var marker: Marker
    private lateinit var circle: Circle

    private lateinit var locationProviderClient: FusedLocationProviderClient
    private lateinit var scaleGestureDetector: ScaleGestureDetector

    companion object{
        fun newIntent(ctx: Context)= Intent(ctx, GeoFenceDetailActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector.appComponent.inject(this)
        setContentView(R.layout.activity_geofence_detail)

        val mapFragment: SupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.map_geoFence) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        scaleGestureDetector = ScaleGestureDetector(applicationContext, this)

        // TODO handling of existing geofence
        if (intent.hasExtra("GEOFENCE_ITEM")) {
            this.geoFence = intent.getSerializableExtra("GEOFENCE_ITEM") as (GeoFence)
            showGeoFenceOnMap(LatLng(geoFence.latitude, geoFence.longitude), geoFence.radius)
            replaceFragment(GeoFenceDetailFragment.newFragment())
        } else {
            showCurrentLocation()
            replaceFragment(GeoFenceMarkerFragment.newFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.geofenceFlowContainer, fragment)
            .commit()
    }

    /*
   MapStuff
    */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.mapType = GoogleMap.MAP_TYPE_HYBRID
        setMapInteractive(true)
        enableLocation()
//        showGeoFenceOnMap()

        with(map) {
            setOnMapLongClickListener(this@GeoFenceDetailActivity)
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        showCurrentLocation()
        return true
    }

    override fun onMapLongClick(p0: LatLng?) {
        if (step == GeoFenceFlow.Step.MARKER) {
            p0?.let {
                showMarker(it)
            }
        }
    }

    /**
     *  prevent dispatching of MotionEvent to handle pinch gesture on map
     */
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val handled = scaleGestureDetector.onTouchEvent(event)
        // If the scale gesture detector didn't handle the event pass it to super.
        if (!handled) {
            return super.onTouchEvent(event)
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
        if (step == GeoFenceFlow.Step.RADIUS) {
            return true
        }
        return false
    }

    override fun onScaleEnd(detector: ScaleGestureDetector?) {}

    override fun onScale(detector: ScaleGestureDetector?): Boolean {
        if (step == GeoFenceFlow.Step.RADIUS) {
            scaleFactor *= detector!!.scaleFactor
            scaleFactor = Math.max(minZoom, Math.min(scaleFactor, maxZoom))
            this.circle.radius = (defaultRadius * scaleFactor)
            return true
        }
        return false
    }

    private fun setMapInteractive(state: Boolean) {
        map.uiSettings.isMyLocationButtonEnabled = state
        map.uiSettings.isMapToolbarEnabled = state
        map.uiSettings.setAllGesturesEnabled(state)
    }

    private fun showMarker(position: LatLng) {
        if (showingMarker()) {
            this.marker.position = position
        } else {
            this.marker = map.addMarker(
                MarkerOptions()
                    .position(LatLng(position.latitude, position.longitude))
                    .icon(
                        BitmapDescriptorFactory
                            .defaultMarker(205F)
                    )
            )
        }
    }

    private fun showingMarker() = ::marker.isInitialized && marker.isVisible

    private fun showCircle(position: LatLng, radius: Double) {
        val circleOptions: CircleOptions = CircleOptions()
            .center(position)
            .radius(radius)
            .fillColor(R.color.eazyTime_colorGeoFenceRadius)
            .strokeColor(Color.TRANSPARENT)
            .strokeWidth(2F)
        this.circle = map.addCircle(circleOptions)
    }

    private fun removeCircle() {
        if (showingCircle()) {
            circle.remove()
        }
    }

    private fun showingCircle(): Boolean = ::circle.isInitialized && circle.isVisible

    private fun showGeoFenceOnMap(position: LatLng, radius: Double) {
        showMarker(position)
        showCircle(position, radius)

//        map.setOnCircleClickListener {
//            // TODO edit Geofence
//            Toast.makeText(applicationContext, "circle clicked", Toast.LENGTH_SHORT).show()
//        }
    }

    private fun moveCamera(latLng: LatLng, zoom: Float) {
        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(latLng, zoom)
        )
    }

    /**
     * Calculates the defaultRadius relative to meter per pixel on the current camera latitude and zoom level
     *  Formula from Chris Broadfoot https://groups.google.com/d/msg/google-maps-js-api-v3/hDRO4oHVSeM/osOYQYXg2oUJ
     */
    private fun calcRadiusForZoomLevel(): Double {
        val level: Double = map.cameraPosition.zoom.toDouble()
        val lat: Double = map.cameraPosition.target.latitude
        val metersPerPixel = 156543.03392 * Math.cos(lat * Math.PI / 180) / Math.pow(2.0, level)
        defaultRadius = 100 * metersPerPixel
        return defaultRadius
    }

    /**
     * Location handling
     */
    @SuppressLint("MissingPermission")
    private fun enableLocation() {
        with(map) {
            isMyLocationEnabled = true
            setOnMyLocationButtonClickListener(this@GeoFenceDetailActivity)
        }
    }

    private fun showCurrentLocation() {
        try {
            val locationResult: Task<Location> = locationProviderClient.lastLocation

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

    /**
     *  Callback methodes from fragments
     **/
    override fun goToMarker() {
        setMapInteractive(true)
        removeCircle()
        replaceFragment(GeoFenceMarkerFragment.newFragment())
    }

    override fun goToRadius() {
        if (showingMarker()) {
            moveCamera(this.marker.position, this.map.cameraPosition.zoom)
            setMapInteractive(false)
            showCircle(this.marker.position, calcRadiusForZoomLevel())
            replaceFragment(GeoFenceRadiusFragment.newFragment())
        } else {
            Toast.makeText(this, getString(R.string.geofence_detail_activity_toast_marker_error), Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun goToEdit() {
        setMapInteractive(false)
        if (showingCircle()) {
            replaceFragment(GeoFenceEditFragment.newFragment())
        }
    }

    override fun goToDetail() {
        setMapInteractive(false)
        replaceFragment(GeoFenceDetailFragment.newFragment())
    }

    override fun deleteGeoFence() {
        // TODO implement delete on list
        leaveGeoFenceDetail()
    }

    override fun saveGeoFence(geoFenceName: String) {
        this.geoFence =
            GeoFence(
                geoFenceName,
                false,
                geoFenceName + UUID.randomUUID(),
                this.circle.radius,
                this.marker.position.latitude,
                this.marker.position.longitude
            )
        geoFenceController.add(geoFence,
            success = {
                Log.d(TAG, "GeoFence added successfully")
                geoFence.active = true // TODO success callback to late for saving
            },
            failure = { err ->
                Snackbar.make(window.decorView.rootView, err, Snackbar.LENGTH_LONG).show()
            })
        geoFenceRepository.saveGeoFence(this.geoFence)
        val returnIntent = Intent()
        returnIntent.putExtra("result", true)
        setResult(Activity.RESULT_OK, returnIntent)
        leaveGeoFenceDetail()
    }

    override fun setStep(step: GeoFenceFlow.Step) {
        this.step = step
    }

    override fun leaveGeoFenceDetail() {
        finish()
    }
}
