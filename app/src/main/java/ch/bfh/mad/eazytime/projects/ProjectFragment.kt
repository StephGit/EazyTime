package ch.bfh.mad.eazytime.projects


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.EazyTimeNavigator
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.data.dao.ProjectDao
import ch.bfh.mad.eazytime.di.Injector
import ch.bfh.mad.eazytime.util.ProjectProviderService
import ch.bfh.mad.eazytime.util.TimerService
import javax.inject.Inject


class ProjectFragment : Fragment() {

    @Inject
    lateinit var projectProviderService: ProjectProviderService

    @Inject
    lateinit var projectDao: ProjectDao

    @Inject
    lateinit var timerService: TimerService

    private lateinit var projectListViewModel: ProjectListViewModel
    private lateinit var projectListView: ListView
    private lateinit var createNewProjectButton: FloatingActionButton
    private lateinit var navigator: EazyTimeNavigator


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_project, container, false)
        navigator = requireContext() as? EazyTimeNavigator ?: throw IllegalStateException("Context of ProjectFragment is not an Instance of EazyTimeNavigator")
        activity!!.title = getString(R.string.project_fragment_title)
        projectListView = view.findViewById(R.id.lv_projects)
        createNewProjectButton = view.findViewById(R.id.fab_projects)
        createNewProjectButton.setOnClickListener{ openAddNewProjectActivity() }

        Injector.appComponent.inject(this)
        projectListViewModel = ViewModelProviders.of(this, ProjectModelFactory(projectProviderService, projectDao)).get(ProjectListViewModel::class.java)

        val projectsListAdapter = ProjectsListAdapter(requireContext(), android.R.layout.simple_list_item_1)
        projectListView.adapter = projectsListAdapter
        projectListView.setOnItemLongClickListener{parent, view, position, id ->
            openUpdateNewProjectActivity(projectsListAdapter.getItem(position))
        }
        projectListView.setOnItemClickListener { parent, view, position, id ->
            activateOrChangeProject(projectsListAdapter.getItem(position))
        }

        projectListViewModel.projects.observe(this, Observer { projects ->
            projects?.let {
                projectsListAdapter.clear()
                projectsListAdapter.addAll(projects)
            }
        })

        return view
    }

    private fun activateOrChangeProject(projectLostItem: ProjectListItem?) {
        projectLostItem?.id?.let { projectId ->
            timerService.changeAndStartProject(projectId)
        }
    }


    private fun openUpdateNewProjectActivity(projectListItem: ProjectListItem?): Boolean {
        projectListItem?.let {listItem ->
            if (listItem.id != null) {
                Log.i(TAG, "Start UpdateProjectActivity for $listItem")
                navigator.openUpdateProjectActivity(listItem.id)
            }else{
                Log.wtf(TAG, "invalid projectListItem: $listItem")
            }
        }
        return true
    }

    private fun openAddNewProjectActivity() {
        navigator.openAddProjectActivity()
    }
}
