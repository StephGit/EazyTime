package ch.bfh.mad.eazytime.geofence


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.util.ViewModelFactory

class GeoFenceFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var iconNoGeofence: ImageView
    private lateinit var textNoGeofence: TextView
    private lateinit var textAddGeofence: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_geofence, container, false)
        activity!!.title = getString(R.string.geofence_fragment_title)

        listView = view.findViewById(R.id.lv_geofences)
        iconNoGeofence = view.findViewById(R.id.iv_noGeofence)
        textNoGeofence = view.findViewById(R.id.tv_noGeofence)
        textAddGeofence = view.findViewById(R.id.tv_noGeofenceHint)

        view.findViewById<FloatingActionButton>(R.id.btn_addGeofence).setOnClickListener {
            addGeofence()
        }

        val factory = ViewModelFactory()
        val viewModel: GeoFenceViewModel = ViewModelProviders.of(this, factory).get(GeoFenceViewModel::class.java)

        viewModel.geoFenceItems.observe(this, Observer {
            if (it!!.isNotEmpty()) showListView() else hideListView()
            val lvGeofences = view.findViewById<ListView>(R.id.lv_geofences)
            val customAdapter = GeoFenceAdapter(requireContext(), 0, it!!)
            lvGeofences.adapter = customAdapter
        })

        return view
    }

    private fun addGeofence() {
        // TODO replace with addFragment
        startActivity(GeoFenceDetailActivity.newIntent(requireContext()))
    }


    // TODO fragment show/hide -> Fragment vs. Activity
    private fun hideListView() {
        iconNoGeofence.visibility = View.VISIBLE
        textAddGeofence.visibility = View.VISIBLE
        textNoGeofence.visibility = View.VISIBLE
        listView.visibility = View.GONE
    }

    private fun showListView() {
        iconNoGeofence.visibility = View.GONE
        textAddGeofence.visibility = View.GONE
        textNoGeofence.visibility = View.GONE
        listView.visibility = View.VISIBLE
    }
}
