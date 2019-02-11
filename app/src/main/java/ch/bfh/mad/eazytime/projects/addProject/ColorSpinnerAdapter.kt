package ch.bfh.mad.eazytime.projects.addProject

import android.content.Context
import android.graphics.Color
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ch.bfh.mad.R

class ColorSpinnerAdapter(context: Context, @LayoutRes itemLayoutRes: Int, items: List<String>) :
    ArrayAdapter<String>(context, itemLayoutRes, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
        val color = getItem(position)
        color?.let{
            view.findViewById<TextView>(android.R.id.text1).text = color
            view.findViewById<TextView>(android.R.id.text1).setBackgroundColor(Color.parseColor(color))
        }
        return view
    }

}