package ch.bfh.mad.eazytime.geofence


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.data.entity.GeoFence
import ch.bfh.mad.eazytime.di.Injector
import ch.bfh.mad.eazytime.geofence.detail.GeoFenceDetailActivity
import javax.inject.Inject
import ch.bfh.mad.eazytime.util.ViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class GeoFenceFragment : GeoFenceBaseFragment() {

    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var viewModel: GeoFenceViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        fun newFragment(): androidx.fragment.app.Fragment = GeoFenceFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_geofence, container, false)
        Injector.appComponent.inject(this)
        activity!!.title = getString(R.string.geofence_fragment_title)

        view.findViewById<FloatingActionButton>(R.id.btn_addGeofence).setOnClickListener { super.addGeoFence() }
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GeoFenceViewModel::class.java)

        if (viewModel.hasGeoFenceInDatabase) subscribeViewModel(recyclerAdapter) else showEmptyGeoFenceFragment()
        listView.isClickable = true

        initSwipe()

        listView.setOnItemClickListener { parent, view, position, id ->
            onListItemClick(parent as ListView, position, id)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.checkPermission(this)
    }

    private fun getListItems(view: View) {
        viewModel.geoFenceItems.observe(this, Observer {
            val listViewGeoFence = view.findViewById<ListView>(R.id.lv_geofences)
            val customAdapter = GeoFenceAdapter(requireContext(), 0, it!!)
            listViewGeoFence.adapter = customAdapter
            listViewGeoFence.setOnItemClickListener { parent, _, position, id ->
                onListItemClick(parent as ListView, position, id)
            }
            showList()
        })
    }

    private fun onListItemClick(parent: ListView, position: Int, id: Long) {
        val item: GeoFence = parent.getItemAtPosition(position) as GeoFence
        showGeoFenceDetail(item)
    }

    private fun showEmptyGeoFenceFragment() {
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.frame_content, GeoFenceEmptyFragment.newFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun showList() {
        progressBar.visibility = View.GONE
        listView.visibility = View.VISIBLE
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
        startActivity(intent)
    }
}
