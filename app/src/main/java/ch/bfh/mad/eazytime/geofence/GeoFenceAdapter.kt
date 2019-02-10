package ch.bfh.mad.eazytime.geofence

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.data.entity.GeoFence

class GeoFenceAdapter (context: Context, @LayoutRes itemLayoutRes: Int, items: List<GeoFence>):
    ArrayAdapter<GeoFence>(context, itemLayoutRes, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.listitem_geofence, parent, false)
        val item = getItem(position)

        view.findViewById<TextView>(R.id.tvItemText1).text = item?.name
        view.findViewById<TextView>(R.id.tvItemText2).text = item?.name
        view.findViewById<TextView>(R.id.tvItemText3).text = item?.gfId.toString()
        return view
    }
}