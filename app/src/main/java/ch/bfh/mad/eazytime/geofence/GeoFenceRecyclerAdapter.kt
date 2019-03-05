package ch.bfh.mad.eazytime.geofence

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.data.entity.GeoFence
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class GeoFenceRecyclerAdapter @Inject constructor(private var geoFenceService: GeoFenceService) :
    ListAdapter<GeoFence, GeoFenceRecyclerAdapter.ViewHolder>(GeoFenceDiffCallback()) {

    var onItemClick: ((GeoFence) -> Unit)? = null
    var onSwitch: ((GeoFence) -> Unit)? = null
    private lateinit var itemView: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.listitem_geofence, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentGeoFence = getItem(position)
        holder.apply {
            bind(currentGeoFence)
            itemText.text = currentGeoFence.name
            switch.isChecked = currentGeoFence.active

            if (switch.isChecked) {
                addGeoFence(currentGeoFence)
                Toast.makeText(this.itemText.context, this.itemText.context.getString(R.string.geofence_fragment_toast_activated_geofence), Toast.LENGTH_SHORT)
                    .show()
            } else removeGeoFence(currentGeoFence)
        }
    }

    private fun onSwitchChange(geoFence: GeoFence) {
        if (geoFence.active) {
            addGeoFence(geoFence)
        } else {
            removeGeoFence(geoFence)
        }
    }

    private fun removeGeoFence(it: GeoFence) {
        geoFenceService.remove(
            it,
            success = { Log.d(TAG, "GeoFence " + it.name + " removed successfully") },
            failure = { err ->
                Snackbar.make(itemView, err, Snackbar.LENGTH_LONG).show()
            })
    }

    private fun addGeoFence(it: GeoFence) {
        geoFenceService.add(
            it,
            success = { Log.d(TAG, "GeoFence " + it.name + " added successfully") },
            failure = { err ->
                Snackbar.make(itemView, err, Snackbar.LENGTH_LONG).show()
            })
    }

    fun removeItem(viewHolder: RecyclerView.ViewHolder): GeoFence {
        val removedPosition = viewHolder.adapterPosition
        val removedGeoFence = getItem(removedPosition)
        removeGeoFence(removedGeoFence)
        notifyItemRemoved(removedPosition)
        return removedGeoFence
    }

    fun insertItem(position: Int) {
        notifyItemInserted(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemText: TextView = itemView.findViewById(R.id.tv_geoFenceItemText)
        val switch: Switch = itemView.findViewById(R.id.sw_geoFenceActive)

        fun bind(geoFence: GeoFence) {
            itemView.setOnClickListener {
                onItemClick?.invoke(geoFence)
            }
            switch.setOnCheckedChangeListener { _, isChecked ->
                geoFence.active = isChecked
                onSwitchChange(geoFence)
                onSwitch?.invoke(geoFence)
            }
        }
    }
}

private class GeoFenceDiffCallback : DiffUtil.ItemCallback<GeoFence>() {
    override fun areItemsTheSame(oldItem: GeoFence, newItem: GeoFence): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GeoFence, newItem: GeoFence): Boolean {
        return oldItem.active == newItem.active &&
                oldItem.gfId == newItem.gfId &&
                oldItem.name == newItem.name &&
                oldItem.latitude == newItem.latitude &&
                oldItem.longitude == newItem.longitude &&
                oldItem.radius == newItem.radius &&
                oldItem.id == newItem.id
    }
}