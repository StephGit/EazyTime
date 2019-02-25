package ch.bfh.mad.eazytime.projects

import android.content.Context
import android.graphics.Color
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ch.bfh.mad.R

class ProjectsListAdapter(context: Context, @LayoutRes itemLayoutRes: Int, items: List<ProjectListItem>) :
    ArrayAdapter<ProjectListItem>(context, itemLayoutRes, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_project, parent, false)
        getItem(position)?.let { project ->
            view.findViewById<View>(R.id.project_list_item_color_tag).setBackgroundColor(Color.parseColor(project.color))
            view.findViewById<TextView>(R.id.project_list_item_project_name).text = project.name
            view.findViewById<TextView>(R.id.project_list_item_color_tag).text = project.shortCode
            view.findViewById<TextView>(R.id.project_list_item_project_time).text = project.currentTime
            view.findViewById<TextView>(R.id.project_list_item_project_isDefault).text = getMarkerForDefaultProject(context, project)
        }
        return view
    }

    private fun getMarkerForDefaultProject(context: Context, project: ProjectListItem): CharSequence? {
        return if (project.default == true){
            context.getString(R.string.is_default_project)
        } else{
            ""
        }
    }

}