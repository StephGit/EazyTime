package ch.bfh.mad.eazytime.geofence


import android.content.Intent
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

// TODO all items delete show EmptyFragment

class GeoFenceFragment : GeoFenceBaseFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var viewModel: GeoFenceViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var swipeBackgroundColor: ColorDrawable
    private lateinit var deleteIcon: Drawable

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var recyclerAdapter: GeoFenceRecyclerAdapter

    companion object {
        fun newFragment(): androidx.fragment.app.Fragment = GeoFenceFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_geofence, container, false)
        Injector.appComponent.inject(this)
        activity!!.title = getString(R.string.geofence_fragment_title)

        recyclerView = view.findViewById(R.id.lv_geofences)
        progressBar = view.findViewById(R.id.progressBar)

        view.findViewById<FloatingActionButton>(R.id.btn_addGeofence).setOnClickListener {
            super.addGeoFence()
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
        super.bind(viewModel)
        if (viewModel.hasGeoFence) subscribeViewModel(recyclerAdapter) else showEmptyGeoFenceFragment()

        initSwipe()

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.checkPermission(this)
    }

    private fun subscribeViewModel(recyclerAdapter: GeoFenceRecyclerAdapter) {
        viewModel.geoFenceItems.observe(this, Observer {
            recyclerAdapter.submitList(it)
            showList()
        })
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
                viewModel.delete(removedGeoFence)
                Snackbar.make(viewHolder.itemView, "${removedGeoFence.name} gelöscht.", Snackbar.LENGTH_LONG)
                    .setAction("Rückgängig") {
                        viewModel.insert(removedGeoFence)
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

    private fun showEmptyGeoFenceFragment() {
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.frame_content, GeoFenceEmptyFragment.newFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun showList() {
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    private fun showGeoFenceDetail(item: GeoFence) {
        val intent = Intent(activity?.baseContext, GeoFenceDetailActivity::class.java)
        intent.putExtra("GEOFENCE_NAME", item.name)
        intent.putExtra("GEOFENCE_ACTIVE", item.active)
        intent.putExtra("GEOFENCE_GFID", item.gfId)
        intent.putExtra("GEOFENCE_RADIUS", item.radius)
        intent.putExtra("GEOFENCE_LAT", item.latitude)
        intent.putExtra("GEOFENCE_LONG", item.longitude)
        intent.putExtra("GEOFENCE_ID", item.id)
        startActivityForResult(intent, super.geoFenceUpdateRequest)
    }
}
