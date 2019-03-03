package ch.bfh.mad.eazytime.geofence

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.LayoutRes
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.data.GeoFenceRepository
import ch.bfh.mad.eazytime.data.entity.GeoFence
import ch.bfh.mad.eazytime.di.Injector
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class GeoFenceAdapter (context: Context, @LayoutRes itemLayoutRes: Int, items: List<GeoFence>):
    ArrayAdapter<GeoFence>(context, itemLayoutRes, items) {

    @Inject
    lateinit var geoFenceRepository: GeoFenceRepository

    @Inject
    lateinit var geoFenceController: GeoFenceController

    init {
        Injector.appComponent.inject(this)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.listitem_geofence, parent, false)
        val item = getItem(position)
        item?.let {
            view.findViewById<TextView>(R.id.tv_geoFenceItemText).text = it.name
            val switch = view.findViewById<Switch>(R.id.sw_geoFenceActive)
            switch.isChecked = it.active
            if (it.active) {
                switch.text = context.resources.getString(R.string.geofence_listitem_switch_text)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    switch.setTextColor(context.getColor(R.color.eazyTime_colorBrand))
                }
                addGeoFence(it, view)
            } else {
                removeGeoFence(it, view)
            }

            switch.setOnCheckedChangeListener { _, isChecked ->
                onCheckChange(it, isChecked, view)
            }
        }
        return view
    }

    private fun onCheckChange(
        it: GeoFence,
        isChecked: Boolean,
        view: View
    ) {
        it.active = isChecked
        this.geoFenceRepository.updateGeoFence(it)
        if (it.active) {
            addGeoFence(it, view)
        } else {
            view.findViewById<Switch>(R.id.sw_geoFenceActive).textOff
            removeGeoFence(it, view)
        }
    }

    private fun removeGeoFence(it: GeoFence, view: View) {
        geoFenceController.remove(
            it,
            success = { Log.d(TAG, "GeoFence " + it.name + " removed successfully") },
            failure = { err ->
                Snackbar.make(view, err, Snackbar.LENGTH_LONG).show()
            })
    }

    private fun addGeoFence(it: GeoFence, view: View) {
        geoFenceController.add(
            it,
            success = { Log.d(TAG, "GeoFence " + it.name + " added successfully") },
            failure = { err ->
                Snackbar.make(view, err, Snackbar.LENGTH_LONG).show()
            })
    }
}