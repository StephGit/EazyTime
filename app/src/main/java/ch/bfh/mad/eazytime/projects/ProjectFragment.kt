package ch.bfh.mad.eazytime.projects

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.EazyTimeNavigator
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.di.Injector
import ch.bfh.mad.eazytime.homeScreenWidget.WidgetProvider
import ch.bfh.mad.eazytime.util.NotificationHandler
import ch.bfh.mad.eazytime.util.ProjectProviderService
import ch.bfh.mad.eazytime.util.TimerService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class ProjectFragment : androidx.fragment.app.Fragment() {

    @Inject
    lateinit var projectProviderService: ProjectProviderService

    @Inject
    lateinit var timerService: TimerService

    @Inject
    lateinit var notificationHandler: NotificationHandler

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
        projectListViewModel = ViewModelProviders.of(this, ProjectModelFactory(projectProviderService)).get(ProjectListViewModel::class.java)

        val projectsListAdapter = ProjectsListAdapter(requireContext(), android.R.layout.simple_list_item_1)
        projectListView.adapter = projectsListAdapter
        projectListView.setOnItemLongClickListener{parent, view, position, id ->
            openUpdateNewProjectActivity(projectsListAdapter.getItem(position))
        }
        projectListView.setOnItemClickListener { parent, view, position, id ->
            projectsListAdapter.clearTimers()
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

    private fun activateOrChangeProject(projectLostItem: ProjectListItem?) = runBlocking {
        projectLostItem?.id?.let { projectId ->
            timerService.changeAndStartProject(projectId)
            val ctx = requireContext()
            ctx.sendBroadcast(WidgetProvider.getUpdateAppWidgetsIntent(ctx))
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
        notificationHandler.createEazyTimeNotification()
        navigator.openAddProjectActivity()
    }

}
