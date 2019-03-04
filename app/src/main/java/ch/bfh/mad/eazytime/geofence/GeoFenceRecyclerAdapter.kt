package ch.bfh.mad.eazytime.geofence

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.data.GeoFenceRepository
import ch.bfh.mad.eazytime.data.entity.GeoFence
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class  GeoFenceRecyclerAdapter @Inject constructor(private var geoFenceRepository: GeoFenceRepository, private var geoFenceService: GeoFenceService) :
    ListAdapter<GeoFence, GeoFenceRecyclerAdapter.ViewHolder>(GeoFenceDiffCallback()) {

    var onItemClick: ((GeoFence) -> Unit)? = null
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
                switch.text = itemView.context.resources.getString(R.string.geofence_listitem_switch_text)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    switch.setTextColor(itemView.context.getColor(R.color.eazyTime_colorBrand))
                }
                addGeoFence(currentGeoFence)
            } else removeGeoFence(currentGeoFence)

            switch.setOnCheckedChangeListener { _, isChecked ->
                onCheckChange(currentGeoFence, isChecked)
            }
        }
    }

    private fun onCheckChange(
        it: GeoFence,
        isChecked: Boolean
    ) {
        it.active = isChecked
        this.geoFenceRepository.update(it)
        if (it.active) {
            addGeoFence(it)
        } else {
            removeGeoFence(it)
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

    fun removeItem(viewHolder: RecyclerView.ViewHolder) {
        val removedPosition = viewHolder.adapterPosition
        val removedGeoFence = getItem(removedPosition)
        removeGeoFence(removedGeoFence)
        this.geoFenceRepository.delete(removedGeoFence)
        notifyItemRemoved(removedPosition)
        Snackbar.make(viewHolder.itemView, "${removedGeoFence.name} gelöscht.", Snackbar.LENGTH_LONG)
            .setAction("Rückgängig") {
                notifyItemInserted(removedPosition)
                addGeoFence(removedGeoFence)
                this.geoFenceRepository.insert(removedGeoFence)
            }.show()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemText: TextView = itemView.findViewById(R.id.tv_geoFenceItemText)
        val switch: Switch = itemView.findViewById(R.id.sw_geoFenceActive)

        fun bind(geoFence: GeoFence) {
            itemView.setOnClickListener {
                onItemClick?.invoke(geoFence)
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