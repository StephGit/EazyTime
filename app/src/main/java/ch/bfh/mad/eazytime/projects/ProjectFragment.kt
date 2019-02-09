package ch.bfh.mad.eazytime.projects


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView

import ch.bfh.mad.R


class ProjectFragment : Fragment() {

    private lateinit var projectListView: ListView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_project, container, false)
        projectListView = view.findViewById(R.id.lv_projects)
        activity!!.title = getString(R.string.project_fragment_title)
        val projects = getProjectsFromInjectedSuperService(requireContext())
        val projectsListAdapter = ProjectsListAdapter(requireContext(), android.R.layout.simple_list_item_1, projects)
        projectListView.adapter = projectsListAdapter
        return view
    }


    private fun getProjectsFromInjectedSuperService(context: Context): List<MySuperProject> {
        return arrayListOf(
            MySuperProject("Admin", "Adm", 34, ContextCompat.getColor(context, R.color.eazyTime_colorProject1)),
            MySuperProject("DeX", "DeX", 789, ContextCompat.getColor(context, R.color.eazyTime_colorProject2)),
            MySuperProject("HassProjekt", "Wäh", 456, ContextCompat.getColor(context, R.color.eazyTime_colorProject3)),
            MySuperProject("Müllentsorgen", "Müll", 999,ContextCompat.getColor(context, R.color.eazyTime_colorProject5)),
            MySuperProject("Admin", "Adm", 34, ContextCompat.getColor(context, R.color.eazyTime_colorProject1)),
            MySuperProject("Müllentsorgen", "Müll", 999,ContextCompat.getColor(context, R.color.eazyTime_colorProject5)),
            MySuperProject("SchoggiProjekt", ";-)", 854, ContextCompat.getColor(context, R.color.eazyTime_colorProject4)),
            MySuperProject("HassProjekt", "Wäh", 456, ContextCompat.getColor(context, R.color.eazyTime_colorProject3)),
            MySuperProject("DeX", "DeX", 789, ContextCompat.getColor(context, R.color.eazyTime_colorProject2)),
            MySuperProject("SchoggiProjekt", ";-)", 854, ContextCompat.getColor(context, R.color.eazyTime_colorProject4)),
            MySuperProject("Müllentsorgen", "Müll", 999,ContextCompat.getColor(context, R.color.eazyTime_colorProject5))
        )
    }

    data class MySuperProject(
        val name: String,
        val shortCode: String,
        val id: Long,
        val color: Int
    )

}
