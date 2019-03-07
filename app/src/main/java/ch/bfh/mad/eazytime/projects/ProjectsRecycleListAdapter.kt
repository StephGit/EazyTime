package ch.bfh.mad.eazytime.projects

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.data.repo.ProjectRepo
import ch.bfh.mad.eazytime.util.EazyTimeDateUtil
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.runBlocking
import java.util.*

class ProjectsRecycleListAdapter(var context: Context, var projectRepo: ProjectRepo) : ListAdapter<ProjectListItem, ProjectsRecycleListAdapter.ViewHolder>(DiffCallBack()) {

    private val timers: MutableList<MyUpdateTimer> = mutableListOf()

    var onItemClick: ((ProjectListItem) -> Unit)? = null
    var onItemLongClick: ((ProjectListItem) -> Boolean)? = null

    private lateinit var itemView: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_project, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val projectListItem = getItem(position)
        holder.apply {
            bind(projectListItem)
            projectListItem?.let { projectItem ->
                formatColorTag(tagTV, projectItem)
                formatProjectName(nameTV, projectItem)
                formatDefaultProject(isDefaultTV, projectItem)
                formatTime(timeTV, projectItem)
            }
        }
    }

    private fun formatTime(timeTV: TextView, projectItem: ProjectListItem) {
        projectItem.currentTime?.let {
            timeTV.text = EazyTimeDateUtil.fromSecondsToHHmmSS(it)
        }
        if (projectItem.active) {
            timeTV.setTypeface(null, Typeface.BOLD)
            timeTV.setTextColor(ContextCompat.getColor(context, R.color.eazyTime_colorBlack))
            val myUpdateTimer = MyUpdateTimer(timeTV, projectItem.currentTime)
            timers.add(myUpdateTimer)
            Timer().scheduleAtFixedRate(myUpdateTimer, 0, 1000)
        }

    }

    private fun formatDefaultProject(defaultTV: TextView, projectItem: ProjectListItem) {
        if (projectItem.default == true) {
            defaultTV.text = context.getString(R.string.is_default_project)
        } else {
            defaultTV.text = ""
        }
    }

    private fun formatProjectName(nameTV: TextView, projectItem: ProjectListItem) {
        nameTV.text = projectItem.name
        if (projectItem.active) {
            nameTV.setTypeface(null, Typeface.BOLD)
            nameTV.setTextColor(ContextCompat.getColor(context, R.color.eazyTime_colorBlack))
        }
    }

    private fun formatColorTag(colorTag: TextView, projectItem: ProjectListItem) {
        colorTag.setBackgroundColor(Color.parseColor(projectItem.color))
        colorTag.text = projectItem.shortCode?.toUpperCase()
        if (projectItem.active) {
                colorTag.setTypeface(null, Typeface.BOLD)
                colorTag.setTextColor(ContextCompat.getColor(context, R.color.eazyTime_colorBlack))
        }
    }

    fun clearTimers() {
        timers.forEach {
            it.cancel()
        }
        timers.clear()
    }

    fun removeItem(viewHolder: RecyclerView.ViewHolder) {
        val removedPosition = viewHolder.adapterPosition
        val projectItem = getItem(removedPosition)
        deleteProject(projectItem)
        if (projectItem.active){
            Snackbar.make(viewHolder.itemView, context.getString(R.string.active_projects_cant_be_deleted), Snackbar.LENGTH_LONG).show()
            rollbackDeleteProject(projectItem)
        }else{
            Snackbar.make(viewHolder.itemView, context.getString(R.string.priject_is_deleted), Snackbar.LENGTH_LONG)
            .setAction(context.getString(R.string.rollback)) {
                notifyItemInserted(removedPosition)
                rollbackDeleteProject(projectItem)
            }.show()
        }
    }

    private fun deleteProject(projectItem: ProjectListItem) = runBlocking{
            projectRepo.deleteProject(projectItem.id!!)
    }

    private fun rollbackDeleteProject(projectItem: ProjectListItem) = runBlocking {
        projectRepo.rollbackDeleteProject(projectItem.id!!)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tagTV = itemView.findViewById<TextView>(R.id.project_list_item_color_tag)!!
        val nameTV = itemView.findViewById<TextView>(R.id.project_list_item_project_name)!!
        val timeTV = itemView.findViewById<TextView>(R.id.project_list_item_project_time)!!
        val isDefaultTV = itemView.findViewById<TextView>(R.id.project_list_item_project_isDefault)!!

        fun bind(projectListItem: ProjectListItem) {
            itemView.setOnClickListener {
                onItemClick?.invoke(projectListItem)
            }
            itemView.setOnLongClickListener {
                onItemLongClick?.invoke(projectListItem)?: false
            }
        }
    }
}

class MyUpdateTimer(val timeTV: TextView?, startTime: Int? = 0) : TimerTask() {
    private var counter = 0

    init {
        startTime?.let { counter = it }
    }

    override fun run() {
        counter++
        timeTV?.text = EazyTimeDateUtil.fromSecondsToHHmmSS(counter)
    }
}

private class DiffCallBack : DiffUtil.ItemCallback<ProjectListItem>() {
    override fun areItemsTheSame(oldItem: ProjectListItem, newItem: ProjectListItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProjectListItem, newItem: ProjectListItem): Boolean {
        return oldItem.id == newItem.id &&
                oldItem.active == newItem.active &&
                oldItem.default == newItem.default &&
                oldItem.color == newItem.color &&
                oldItem.currentTime == newItem.currentTime &&
                oldItem.shortCode == newItem.shortCode &&
                oldItem.name == newItem.name &&
                oldItem.isDeleted == newItem.isDeleted
    }
}