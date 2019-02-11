package ch.bfh.mad.eazytime.geofence


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
<<<<<<< HEAD
import android.widget.TextView

=======
import android.widget.ListView
>>>>>>> c607a86... added emptyFragment with dimensions
import ch.bfh.mad.R

class GeoFenceFragment : Fragment() {

<<<<<<< HEAD
=======
    private lateinit var listView: ListView

>>>>>>> c607a86... added emptyFragment with dimensions
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_geo_fence, container, false)
        val textView = view.findViewById<TextView>(R.id.tv_geofence_placeholder)
        textView.text = getString(R.string.placeholder) + " Geofence"
        activity!!.title = getString(R.string.geofence_fragment_title)
<<<<<<< HEAD
        return view
    }
=======

        listView = view.findViewById<ListView>(R.id.lv_geofences)

        view.findViewById<FloatingActionButton>(R.id.btn_addGeofence).setOnClickListener {
            addGeofence()
        }

        val factory = ViewModelFactory()
        val viewModel: GeoFenceViewModel = ViewModelProviders.of(this, factory).get(GeoFenceViewModel::class.java)

        viewModel.geoFenceItems.observe(this, Observer {
            val lvGeofences = view.findViewById<ListView>(R.id.lv_geofences)
            val customAdapter = GeoFenceAdapter(requireContext(), 0, it!!)
            lvGeofences.adapter = customAdapter
        })

        if (listView.count == 0) showEmptyGeofenceFragment()

        return view
    }

    private fun showEmptyGeofenceFragment() {
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.frame_content, GeoFenceFragmentEmpty())
            .addToBackStack(null)
            .commit()
    }

    private fun addGeofence() {
        // TODO replace with addFragment
        startActivity(GeoFenceDetailActivity.newIntent(requireContext()))
    }

>>>>>>> c607a86... added emptyFragment with dimensions
}
