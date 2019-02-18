package ch.bfh.mad.eazytime.projects


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.EazyTimeNavigator
import ch.bfh.mad.eazytime.di.Injector
import javax.inject.Inject


class ProjectFragment : Fragment() {

    @Inject
    lateinit var fakeProjectProviderService: FakeProjectProviderService

    @Inject
    lateinit var fakeProjectRepo: FakeProjectRepo

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
        projectListViewModel = ViewModelProviders.of(this, ProjectModelFactory(fakeProjectProviderService, fakeProjectRepo)).get(ProjectListViewModel::class.java)

        projectListViewModel.projects.observe(this, Observer { projects ->
            val projectsListAdapter = ProjectsListAdapter(requireContext(), android.R.layout.simple_list_item_1, projects!!)
            projectListView.adapter = projectsListAdapter
        })

        projectListViewModel.refreshListItems()
        return view
    }

    override fun onResume() {
        super.onResume()
        projectListViewModel.refreshListItems()
    }

    private fun openAddNewProjectActivity() {
        navigator.openAddProjectActivity()
    }
}
