package ch.bfh.mad.eazytime.geofence


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.data.entity.GeoFence
import ch.bfh.mad.eazytime.di.Injector
import ch.bfh.mad.eazytime.geofence.detail.GeoFenceDetailActivity
import ch.bfh.mad.eazytime.util.CheckPowerSafeUtil
import ch.bfh.mad.eazytime.util.PermissionHandler
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class GeoFenceFragment : androidx.fragment.app.Fragment() {

    private lateinit var noGeoFenceInfo: ConstraintLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: GeoFenceViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var swipeBackgroundColor: ColorDrawable
    private lateinit var deleteIcon: Drawable
    private lateinit var permissionHandler: PermissionHandler
    private lateinit var prefs: SharedPreferences

    private val permissionFineLocation = Manifest.permission.ACCESS_FINE_LOCATION
    private var permissionFineLocationGranted: Boolean = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var recyclerAdapter: GeoFenceRecyclerAdapter

    @Inject
    lateinit var checkPowerSafeUtil: CheckPowerSafeUtil

    @Inject
    lateinit var geoFenceService: GeoFenceService

    companion object {
        fun newFragment(): androidx.fragment.app.Fragment = GeoFenceFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_geofence, container, false)
        Injector.appComponent.inject(this)
        activity!!.title = getString(R.string.geofence_fragment_title)
        prefs = requireActivity().getSharedPreferences("ch.bfh.mad.eazytime", MODE_PRIVATE)
        noGeoFenceInfo = view.findViewById(R.id.geofences_no_geofences_exists)
        recyclerView = view.findViewById(R.id.lv_geofences)

        view.findViewById<FloatingActionButton>(R.id.btn_addGeofence).setOnClickListener {
            addGeoFence()
        }

        linearLayoutManager = LinearLayoutManager(context)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = recyclerAdapter
        }

        recyclerAdapter.apply {
            onItemClick = { showGeoFenceDetail(it) }
            onSwitch = { onSwitch(it) }
        }

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GeoFenceViewModel::class.java)
        subscribeViewModel(recyclerAdapter)

        initSwipe()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission(this)
    }

    override fun onResume() {
        super.onResume()
        if (!prefs.getBoolean("ignorePowerSafe", false)) {
            checkPowerSafeUtil.checkPowerSaveMode(requireFragmentManager())
        }
    }

    private fun subscribeViewModel(recyclerAdapter: GeoFenceRecyclerAdapter) {
        viewModel.geoFenceItems.observe(this, Observer { geofences ->
            recyclerAdapter.submitList(geofences)
            if (!geofences.isEmpty()) {
                noGeoFenceInfo.visibility = View.GONE
            }
        })
    }

    private fun checkPermission(fragment: androidx.fragment.app.Fragment) {
        permissionHandler = PermissionHandler(fragment, permissionFineLocation)
        permissionFineLocationGranted = permissionHandler.checkPermission()
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun initSwipe() {
        swipeBackgroundColor = ColorDrawable(ResourcesCompat.getColor(resources, R.color.eazyTime_colorDelete, null))
        deleteIcon = ResourcesCompat.getDrawable(resources, R.drawable.ic_delete, null)!!

        val simpleItemTouchCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
                val removedGeoFence = recyclerAdapter.removeItem(viewHolder)
                if (recyclerAdapter.itemCount <= 1) {
                    noGeoFenceInfo.visibility = View.VISIBLE
                }
                viewModel.delete(removedGeoFence)
                Snackbar.make(viewHolder.itemView, "${removedGeoFence.name} gelöscht.", Snackbar.LENGTH_LONG)
                    .setAction("Rückgängig") {
                        noGeoFenceInfo.visibility = View.GONE
                        viewModel.insert(removedGeoFence)
                        if (removedGeoFence.active) {
                            geoFenceService.addOrUpdate(removedGeoFence)
                        }
                    }.show()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                with(itemView) {
                    val iconMargin = (height - deleteIcon.intrinsicHeight) / 2
                    if (dX > 0) {
                        swipeBackgroundColor.setBounds(left, top, dX.toInt(), bottom)
                        deleteIcon.setBounds(
                            left + iconMargin, top + iconMargin, left + iconMargin + deleteIcon.intrinsicWidth,
                            bottom - iconMargin
                        )
                    } else {
                        swipeBackgroundColor.setBounds(right + dX.toInt(), top, right, bottom)
                        deleteIcon.setBounds(
                            right - iconMargin - deleteIcon.intrinsicWidth, top + iconMargin, right - iconMargin,
                            bottom - iconMargin
                        )
                    }

                    swipeBackgroundColor.draw(c)
                    c.save()

                    if (dX > 0)
                        c.clipRect(left, top, dX.toInt(), bottom)
                    else
                        c.clipRect(right + dX.toInt(), top, right, bottom)
                    deleteIcon.draw(c)
                    c.restore()
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun onSwitch(geoFence: GeoFence) {
        viewModel.update(geoFence)
    }

    private fun addGeoFence() {
        permissionFineLocationGranted = permissionHandler.checkPermission()
        if (permissionFineLocationGranted) {
            startActivity(GeoFenceDetailActivity.newIntent(context!!))
        }
    }

    private fun showGeoFenceDetail(item: GeoFence) {
        val intent = Intent(activity?.baseContext, GeoFenceDetailActivity::class.java)
        intent.putExtra("GEOFENCE_ID", item.id)
        startActivity(intent)
    }
}
