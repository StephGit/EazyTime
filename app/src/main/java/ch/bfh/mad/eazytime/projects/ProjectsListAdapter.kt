package ch.bfh.mad.eazytime.projects

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.util.EazyTimeDateUtil
import java.util.*

class ProjectsListAdapter(context: Context, @LayoutRes itemLayoutRes: Int) :
    ArrayAdapter<ProjectListItem>(context, itemLayoutRes) {

    val timers:MutableList<MyUpdateTimer> = mutableListOf()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_project, parent, false)
        getItem(position)?.let { project ->
            view.findViewById<View>(R.id.project_list_item_color_tag).setBackgroundColor(Color.parseColor(project.color))
            val nameTV = view.findViewById<TextView>(R.id.project_list_item_project_name)
            nameTV.text = project.name
            nameTV.setTypeface(null, Typeface.NORMAL)
            val timeTV = view.findViewById<TextView>(R.id.project_list_item_project_time)
            timeTV.text = getCurrentTime(project)
            timeTV.setTypeface(null, Typeface.NORMAL)
            view.findViewById<TextView>(R.id.project_list_item_color_tag).text = project.shortCode
            view.findViewById<TextView>(R.id.project_list_item_project_isDefault).text = getMarkerForDefaultProject(context, project)
            if (project.active){
                nameTV.setTypeface(null, Typeface.BOLD)
                timeTV.setTypeface(null, Typeface.BOLD)
                val newTimer = MyUpdateTimer(view, project.currentTime)
                timers.forEach {
                    it.cancel()
                }
                timers.clear()
                timers.add(newTimer)
                Timer().scheduleAtFixedRate(newTimer, 0 ,1000)
            }
        }
        return view
    }

    private fun getCurrentTime(project: ProjectListItem): CharSequence? {
        return if (project.active){
            "--:--:--"
        } else if (project.currentTime != null) {
            EazyTimeDateUtil.fromSecondsToHHmmSS(project.currentTime)
        } else {
            "??"
        }
    }

    private fun getMarkerForDefaultProject(context: Context, project: ProjectListItem): CharSequence? {
        return if (project.default == true){
            context.getString(R.string.is_default_project)
        } else{
            ""
        }
    }

    class MyUpdateTimer(val view: View, val startTime: Int? = 0):TimerTask(){
        var counter = 0

        init {
            startTime?.let {
                counter = it
            }
        }
        override fun run() {
            counter++
            view.findViewById<TextView>(R.id.project_list_item_project_time).text = EazyTimeDateUtil.fromSecondsToHHmmSS(counter)
        }

    }

}