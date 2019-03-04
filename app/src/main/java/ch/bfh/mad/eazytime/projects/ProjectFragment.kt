package ch.bfh.mad.eazytime.projects


import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.EazyTimeNavigator
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.di.Injector
import ch.bfh.mad.eazytime.homeScreenWidget.WidgetProvider
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
    lateinit var projectsRecycleListAdapter: ProjectsRecycleListAdapter

    private lateinit var projectListViewModel: ProjectListViewModel
    private lateinit var projectListView: RecyclerView
    private lateinit var createNewProjectButton: FloatingActionButton
    private lateinit var navigator: EazyTimeNavigator
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var swipeBackgroundColor: ColorDrawable
    private lateinit var deleteIcon: Drawable


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_project, container, false)
        navigator = requireContext() as? EazyTimeNavigator ?: throw IllegalStateException("Context of ProjectFragment is not an Instance of EazyTimeNavigator")
        activity!!.title = getString(R.string.project_fragment_title)
        projectListView = view.findViewById(R.id.lv_projects)
        createNewProjectButton = view.findViewById(R.id.fab_projects)
        createNewProjectButton.setOnClickListener{ openAddNewProjectActivity() }

        Injector.appComponent.inject(this)
        projectListViewModel = ViewModelProviders.of(this, ProjectModelFactory(projectProviderService)).get(ProjectListViewModel::class.java)

        linearLayoutManager = LinearLayoutManager(context)
        projectListView.layoutManager = linearLayoutManager
        projectListView.setHasFixedSize(true)

        projectListView.adapter = projectsRecycleListAdapter
        projectsRecycleListAdapter.onItemClick = { activateOrChangeProject(it) }
        projectsRecycleListAdapter.onItemLongClick = { openUpdateNewProjectActivity(it) }

        projectListViewModel.projects.observe(this, Observer { projects ->
            projectsRecycleListAdapter.submitList(projects)
        })

        initSwipe()

        return view
    }

    private fun activateOrChangeProject(projectLostItem: ProjectListItem?) = runBlocking {
        projectLostItem?.id?.let { projectId ->
            timerService.changeAndStartProject(projectId)
            val ctx = requireContext()
            ctx.sendBroadcast(WidgetProvider.getUpdateAppWidgetsIntent(ctx))
            projectsRecycleListAdapter.clearTimers()
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

    private fun initSwipe() {
        swipeBackgroundColor = ColorDrawable(ResourcesCompat.getColor(resources, R.color.eazyTime_colorDelete, null))
        deleteIcon = ResourcesCompat.getDrawable(resources, R.drawable.ic_delete, null)!!

        val simpleItemTouchCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
                projectsRecycleListAdapter.removeItem(viewHolder)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                with(itemView) {
                    val iconMargin = (height - deleteIcon.intrinsicHeight) / 2
                    if (dX > 0) {
                        swipeBackgroundColor.setBounds(left, top, dX.toInt(), bottom)
                        deleteIcon.setBounds(
                            left + iconMargin, top + iconMargin, left + iconMargin + deleteIcon.intrinsicWidth,
                            bottom - iconMargin
                        )
                    } else {
                        swipeBackgroundColor.setBounds(right + dX.toInt(), top, right, bottom)
                        deleteIcon.setBounds(
                            right - iconMargin - deleteIcon.intrinsicWidth, top + iconMargin, right - iconMargin,
                            bottom - iconMargin
                        )
                    }

                    swipeBackgroundColor.draw(c)
                    c.save()

                    if (dX > 0)
                        c.clipRect(left, top, dX.toInt(), bottom)
                    else
                        c.clipRect(right + dX.toInt(), top, right, bottom)
                    deleteIcon.draw(c)
                    c.restore()
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(projectListView)
    }

}
