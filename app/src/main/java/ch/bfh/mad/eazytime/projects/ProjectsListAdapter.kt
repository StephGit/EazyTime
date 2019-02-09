package ch.bfh.mad.eazytime.projects

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ch.bfh.mad.R

class ProjectsListAdapter(context: Context, @LayoutRes itemLayoutRes: Int, items: List<ProjectFragment.MySuperProject>) :
    ArrayAdapter<ProjectFragment.MySuperProject>(context, itemLayoutRes, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_project, parent, false)
        val project = getItem(position)
        project.let {
            view.findViewById<View>(R.id.project_list_item_color_tag).setBackgroundColor(project.color)
            view.findViewById<TextView>(R.id.project_list_item_project_name).text = project.name
            view.findViewById<TextView>(R.id.project_list_item_project_short_code).text = project.shortCode
            view.findViewById<TextView>(R.id.project_list_item_project_time).text = "08:45:12"
        }
        return view
    }

}