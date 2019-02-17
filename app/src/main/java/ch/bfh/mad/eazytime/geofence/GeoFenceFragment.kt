package ch.bfh.mad.eazytime.geofence


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.ProgressBar
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.geofence.detail.GeoFenceDetailActivity
import ch.bfh.mad.eazytime.util.ViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class GeoFenceFragment : GeoFenceBaseFragment() {

    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar

    companion object {
        fun newFragment(): androidx.fragment.app.Fragment = GeoFenceFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_geofence, container, false)
        activity!!.title = getString(R.string.geofence_fragment_title)

        view.findViewById<FloatingActionButton>(R.id.btn_addGeofence).setOnClickListener { super.addGeofence() }

        listView = view.findViewById(R.id.lv_geofences)
        progressBar = view.findViewById(R.id.progressBar)

        getListItems(view)

        if (listView.count == 0) showEmptyGeofenceFragment() else showList()

        listView.setOnItemClickListener { parent, view, position, id ->
            onListItemClick(parent as ListView, position, id)
        }

        return view
    }

    private fun onListItemClick(parent: ListView, position: Int, id: Long) {
        val item = parent.getItemAtPosition(position)
        showGeoFenceDetail(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.checkPermission(this)
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

    private fun showEmptyGeofenceFragment() {
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.frame_content, GeoFenceEmptyFragment.newFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun showList() {
        progressBar.visibility = View.GONE
        listView.visibility = View.VISIBLE

    }


    private fun showGeoFenceDetail(item: Any) {
        var intent = Intent(activity?.baseContext, GeoFenceDetailActivity::class.java)
//        intent.putExtra("GEOFENCE_ITEM", item)
        startActivity(intent)
    }

}
