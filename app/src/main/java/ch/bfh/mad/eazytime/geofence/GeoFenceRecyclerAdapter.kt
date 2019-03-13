package ch.bfh.mad.eazytime.geofence

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.data.entity.GeoFence
import javax.inject.Inject

class GeoFenceRecyclerAdapter @Inject constructor(private var geoFenceService: GeoFenceService) :
    ListAdapter<GeoFence, GeoFenceRecyclerAdapter.ViewHolder>(GeoFenceDiffCallback()) {

    var onItemClick: ((GeoFence) -> Unit)? = null
    var onSwitch: ((GeoFence) -> Unit)? = null
    private lateinit var itemView: View
    private lateinit var parentView: ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        parentView = parent
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
        }
    }

    private fun onSwitchChange(geoFence: GeoFence) {
        if (geoFence.active) {
            geoFenceService.add(geoFence)
        } else {
            geoFenceService.remove(geoFence)
        }
    }

    fun removeItem(viewHolder: RecyclerView.ViewHolder): GeoFence {
        val removedGeoFence = getItem(viewHolder.adapterPosition)
        geoFenceService.remove(removedGeoFence)
        return removedGeoFence
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