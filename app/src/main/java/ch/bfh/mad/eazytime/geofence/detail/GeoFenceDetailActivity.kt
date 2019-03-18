package ch.bfh.mad.eazytime.geofence.detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.bfh.mad.eazytime.R
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.data.entity.GeoFence
import ch.bfh.mad.eazytime.data.repo.GeoFenceRepo
import ch.bfh.mad.eazytime.di.Injector
import ch.bfh.mad.eazytime.geofence.GeoFenceService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.maps.android.SphericalUtil
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class GeoFenceDetailActivity : AppCompatActivity(),
    GeoFenceFlow,
    OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMapClickListener,
    GoogleMap.OnMapLongClickListener,
    ScaleGestureDetector.OnScaleGestureListener {

    private val defaultZoom: Float = 18F
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
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var searchItem: MenuItem

    @Inject
    lateinit var geoFenceRepo: GeoFenceRepo

    @Inject
    lateinit var geoFenceService: GeoFenceService

    companion object{
        fun newIntent(ctx: Context)= Intent(ctx, GeoFenceDetailActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geofence_detail)
        title = getString(R.string.geofence_edit_fragment_title)

        Injector.appComponent.inject(this)

        mapFragment = supportFragmentManager.findFragmentById(R.id.map_geoFence) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        scaleGestureDetector = ScaleGestureDetector(applicationContext, this)

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.google_api_key))
        }

        val extras = intent.extras
        val geoFenceId = extras?.get("GEOFENCE_ID") as Long?
        geoFenceId?.let {
            getGeoFenceFromRepo(geoFenceId)
        }
    }

    private fun initFlowFragment() {
        if (::geoFence.isInitialized) {
            setSearchBar(false)
            replaceFragment(GeoFenceEditFragment.newFragment())
        } else {
            replaceFragment(GeoFenceMarkerFragment.newFragment())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.eazy_time_search, menu)
        searchItem = menu!!.findItem(R.id.menu_search)
        searchItem.setOnMenuItemClickListener {
            val fields: MutableList<Place.Field> = ArrayList()
            fields.add(Place.Field.ID)
            fields.add(Place.Field.NAME)
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(this)
            startActivityForResult(intent, 121)
            true
        }
        initFlowFragment()
        return super.onCreateOptionsMenu(menu)
    }

    private fun initGeoFence() {
        this.geoFence = GeoFence(null, false, null, null, null, null)
    }

    private fun getGeoFenceFromRepo(geoFenceId: Long) {
        geoFenceRepo.getGeoFence(geoFenceId)?.let {
            this.geoFence = it
        }
    }

    private fun replaceFragment(fragment: androidx.fragment.app.Fragment) {
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
        setMapInteractions(mapActions = true)
        enableLocation()

        with(map) {
            val locationButton =
                (mapFragment.view!!.findViewById<View>(Integer.parseInt("1")).parent as View).findViewById<View>(
                    Integer.parseInt("2")
                )
            val rlp = locationButton.layoutParams as (RelativeLayout.LayoutParams)
            // position on right bottom
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            rlp.setMargins(0, 0, 30, 30)

            setOnMapClickListener(this@GeoFenceDetailActivity)
            setOnMapLongClickListener(this@GeoFenceDetailActivity)
        }
        if (::geoFence.isInitialized) {
            showGeoFenceOnMap(LatLng(geoFence.latitude!!, geoFence.longitude!!), geoFence.radius!!)
        } else {
            showCurrentLocation()
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        showCurrentLocation()
        return true
    }

    override fun onMapClick(p0: LatLng?) {
        if (step == GeoFenceFlow.Step.MARKER) {
            p0?.let {
                showMarker(it)
            }
        }
    }

    override fun onMapLongClick(p0: LatLng?) {
        onMapClick(p0)
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

    private fun setMapInteractions(mapActions: Boolean) {
        with(map.uiSettings) {
            isMyLocationButtonEnabled = mapActions
            isMapToolbarEnabled = mapActions
            setAllGesturesEnabled(mapActions)
        }

    }

    private fun setSearchBar(active: Boolean) {
        if (::searchItem.isInitialized) {
            searchItem.isVisible = active
        } else searchItem.isVisible = false
    }

    private fun showMarker(position: LatLng) {
        if (showingMarker()) {
            this.marker.position = position
        } else {
            this.marker = map.addMarker(
                MarkerOptions()
                    .position(LatLng(position.latitude, position.longitude))
                    .icon(BitmapDescriptorFactory.defaultMarker(205F))
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
            .visible(true)
        this.circle = map.addCircle(circleOptions)
    }

    private fun removeCircle() {
        if (showingCircle()) {
            circle.remove()
            circle.isVisible = false
        }
    }

    private fun showingCircle(): Boolean = ::circle.isInitialized && circle.isVisible

    private fun showGeoFenceOnMap(position: LatLng, radius: Double) {
        showMarker(position)
        showCircle(position, radius)
        moveCameraToBounds(calcLatLngBounds(position, radius))
    }

    private fun moveCamera(latLng: LatLng, zoom: Float) {
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(latLng, zoom)
        )
    }

    /**
     * Moves camera to specified bounds.
     * Width and Height are calculated based on displayMetrics to avoid map with 0 size - error.
     */
    private fun moveCameraToBounds(bounds: LatLngBounds) {
        val mapHeight =
            resources.displayMetrics.heightPixels / 100 * resources.getInteger(R.integer.eazyTime_layout_weight_geofence_map_fragment)
        val mapWidth = resources.displayMetrics.widthPixels
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, mapWidth, mapHeight, 25))
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
     * Calculates two corner points from center of the circle and radius to get LatLngBounds
     */
    private fun calcLatLngBounds(center: LatLng, radius: Double): LatLngBounds {
        val distanceCenterToCorner = radius * Math.sqrt(2.0)
        val swCorner = SphericalUtil.computeOffset(center, distanceCenterToCorner, 225.0)
        val neCorner = SphericalUtil.computeOffset(center, distanceCenterToCorner, 45.0)
        return LatLngBounds(swCorner, neCorner)
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
    override fun getFenceName(): String {
        if (::geoFence.isInitialized && this.geoFence.name != null) return this.geoFence.name!!
        return ""
    }

    override fun goToMarker() {
        setMapInteractions(true)
        setSearchBar(true)
        removeCircle()
        replaceFragment(GeoFenceMarkerFragment.newFragment())
    }

    override fun goToRadius() {
        if (showingMarker()) {
            moveCamera(this.marker.position, this.map.cameraPosition.zoom)
            setMapInteractions(false)
            setSearchBar(false)
            if (!::geoFence.isInitialized) initGeoFence()
            this.geoFence.latitude = this.marker.position.latitude
            this.geoFence.longitude = this.marker.position.longitude
            showCircle(this.marker.position, calcRadiusForZoomLevel())
            replaceFragment(GeoFenceRadiusFragment.newFragment())
        } else {
            Toast.makeText(this, getString(R.string.geofence_detail_activity_toast_marker_error), Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun goToEdit() {
        setMapInteractions(true)
        setSearchBar(false)
        if (!showingCircle()) {
            showCircle(this.marker.position, calcRadiusForZoomLevel())
        }
        this.geoFence.radius = this.circle.radius
        replaceFragment(GeoFenceEditFragment.newFragment())
    }

    override fun saveOrUpdate(geoFenceName: String) {
        geoFence.name = geoFenceName
        geoFence.id?.let {
            geoFenceRepo.update(geoFence)
            if (geoFence.active) {
                geoFenceService.addOrUpdate(geoFence)
            }
        } ?: run {
            geoFence.requestId = geoFenceName + UUID.randomUUID()
            geoFenceRepo.insert(geoFence)
        }
        leaveGeoFenceDetail()
        Toast.makeText(this, getString(R.string.geofence_detail_activity_toast_saved_geofence), Toast.LENGTH_SHORT)
            .show()
    }

    override fun setStep(step: GeoFenceFlow.Step) {
        this.step = step
    }

    override fun leaveGeoFenceDetail() {
        finish()
    }

    override fun goBack() {
        if (::geoFence.isInitialized) goToEdit() else finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 121) {
            when (resultCode) {
                RESULT_OK -> {
                    val place: Place = Autocomplete.getPlaceFromIntent(data!!)
                    val asList =
                        Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
                    val request = FetchPlaceRequest.builder(place.id!!, asList).build()

                    Places.createClient(application).fetchPlace(request).addOnSuccessListener { response ->
                        val selectedPlace = response.place
                        Log.d(TAG, "Place selected: $selectedPlace")
                        selectedPlace.latLng?.let { latLng ->
                            moveCamera(latLng, defaultZoom)
                        }
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    val status = Autocomplete.getStatusFromIntent(data!!)
                    Log.d(TAG, status.statusMessage)
                }
            }
        }
    }
}
