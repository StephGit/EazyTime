package ch.bfh.mad.eazytime.geofence

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.di.Injector
import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject

class GeoFenceEmptyFragment : GeoFenceBaseFragment() {

    private lateinit var viewModel: GeoFenceViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        fun newFragment(): androidx.fragment.app.Fragment = GeoFenceEmptyFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_geofence_empty, container, false)
        activity!!.title = getString(R.string.geofence_fragment_title)
        Injector.appComponent.inject(this)
        
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GeoFenceViewModel::class.java)
        super.bind(viewModel)

        view.findViewById<FloatingActionButton>(R.id.btn_addGeofence).setOnClickListener { super.addGeoFence() }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.checkPermission(this)
    }

    override fun onResume() {
        super.onResume()
        if (super.hasResult) showGeofenceFragment()
    }

    private fun showGeofenceFragment() {
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.frame_content, GeoFenceFragment.newFragment())
            .addToBackStack(null)
            .commit()
    }
}