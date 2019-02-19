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
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.Toast
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.data.entity.GeoFence
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task


class GeoFenceDetailActivity : AppCompatActivity(),
    GeoFenceFlow,
    OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMapLongClickListener,
    ScaleGestureDetector.OnScaleGestureListener {

    private val defaultZoom: Float = 15F
    private var scaleFactor: Float = 1.0F
    private val minZoom: Float = 0F
    private val maxZoom: Float = 5F
    private val radius: Float = 100F

    private lateinit var step: GeoFenceFlowStep
    private lateinit var geoFence: GeoFence

    private lateinit var map: GoogleMap
    private lateinit var marker: Marker
    private lateinit var circle: Circle

    private lateinit var locationProviderClient: FusedLocationProviderClient
    private lateinit var geofencingClient: GeofencingClient
    private lateinit var scaleGestureDetector: ScaleGestureDetector


    companion object{
        fun newIntent(ctx: Context)= Intent(ctx, GeoFenceDetailActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geofence_detail)

        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        geofencingClient = LocationServices.getGeofencingClient(this)

        scaleGestureDetector = ScaleGestureDetector(applicationContext, this)

        // TODO handling of existing geofence
        if (intent.hasExtra("GEOFENCE_ITEM")) {
            this.step = GeoFenceFlowStep.DETAIL
            this.geoFence = intent.getSerializableExtra("GEOFENCE_ITEM") as (GeoFence)
            replaceFragment(GeoFenceDetailFragment.newFragment())
        } else {
            this.step = GeoFenceFlowStep.MARKER
            replaceFragment(GeoFenceMarkerFragment.newFragment())
            showCurrentLocation()
        }
    }

    /**
     *  prevent dispatching of MotionEvent to handle pinch gesture on map
     */
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        var handled = scaleGestureDetector.onTouchEvent(event)
        // If the scale gesture detector didn't handle the event pass it to super.
        if (!handled) {
            return super.onTouchEvent(event)
        }
        return super.dispatchTouchEvent(event)
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
        map.uiSettings.isMyLocationButtonEnabled = true
        map.uiSettings.isMapToolbarEnabled = true
        map.mapType = GoogleMap.MAP_TYPE_HYBRID

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
        if (step == GeoFenceFlowStep.MARKER) {
            p0?.let {
                showMarker(it)
            }
        }
    }

    override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
        if (step == GeoFenceFlowStep.RADIUS) {
            return true
        }
        return false
    }

    override fun onScaleEnd(detector: ScaleGestureDetector?) {
    }

    override fun onScale(detector: ScaleGestureDetector?): Boolean {
        if (step == GeoFenceFlowStep.RADIUS) {
            scaleFactor *= detector!!.scaleFactor
            scaleFactor = Math.max(minZoom, Math.min(scaleFactor, maxZoom))
            this.circle.radius = (radius * scaleFactor).toDouble()
            return true
        }
        return false
    }

    private fun showMarker(position: LatLng) {
        if (::marker.isInitialized && marker.isVisible) {
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


    private fun showGeoFenceOnMap(location: Location) {
        var circleOptions: CircleOptions = CircleOptions()
            .center(LatLng(location.latitude, location.longitude))
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

    private fun showCircle(position: LatLng, radius: Double) {
        val circleOptions: CircleOptions = CircleOptions()
            .center(position)
            .radius(radius)
            .fillColor(R.color.eazyTime_colorGeoFenceRadius)
            .strokeColor(Color.TRANSPARENT)
            .strokeWidth(2F)

        this.circle = map.addCircle(circleOptions)
    }

    private fun buildGeofence(location: Location, radius: Double): Geofence? {
        val latitude = location.latitude
        val longitude = location.longitude

        return Geofence.Builder()
            // string id to identify
            .setRequestId(location.time.toString())

            .setCircularRegion(
                latitude,
                longitude,
                radius.toFloat()
            )
            // Alerts are generated for these transistions
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT)

            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .build()
    }

    private fun moveCamera(latLng: LatLng, zoom: Float) {
        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(latLng, zoom)
        )
    }

    /**
     * Location handling
     */
    @SuppressLint("MissingPermission")
    private fun enableLocation() {
        with(map) {
            isMyLocationEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
            setOnMyLocationButtonClickListener(this@GeoFenceDetailActivity)
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

    /**
     *  Callback methodes from fragments
     **/

    override fun goToMarker() {
        map.uiSettings.isZoomGesturesEnabled = true
        if (::circle.isInitialized && circle.isVisible) {
            circle.remove()
        }
        this.step = GeoFenceFlowStep.MARKER
        replaceFragment(GeoFenceMarkerFragment.newFragment())
    }

    override fun goToRadius() {
        if (::marker.isInitialized && marker.isVisible) {
            map.uiSettings.isZoomGesturesEnabled = false
            this.step = GeoFenceFlowStep.RADIUS
            replaceFragment(GeoFenceRadiusFragment.newFragment())
            showCircle(this.marker.position, this.radius.toDouble())
        }
    }

    override fun goToDetail() {
        map.uiSettings.isZoomGesturesEnabled = true
        this.step = GeoFenceFlowStep.DETAIL
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
        finish()
    }
}
