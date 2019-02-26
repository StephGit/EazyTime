package ch.bfh.mad.eazytime.homeScreenWidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.data.dao.ProjectDao
import ch.bfh.mad.eazytime.data.entity.Project
import ch.bfh.mad.eazytime.di.Injector
import ch.bfh.mad.eazytime.util.WidgetProviderUtils
import javax.inject.Inject

class WidgetProvider : AppWidgetProvider() {

    private val widgetProjects: MutableLiveData<List<Project>> = MutableLiveData()

    init {
        Injector.appComponent.inject(this)
    }

    @Inject
    lateinit var widgetProviderUtils: WidgetProviderUtils

    @Inject
    lateinit var projectDao: ProjectDao

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        val amountOfWidgets: Int = appWidgetIds?.size ?: 0
        Log.i(TAG, "onUpdate amountOfWidgets: $amountOfWidgets")

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (i in 0 until amountOfWidgets) {
            val appWidgetId = appWidgetIds!![i]
            updateButtonsOnWidget(context!!, appWidgetManager!!, appWidgetId)
        }
    }

    private fun updateButtons(context: Context, remoteViews: RemoteViews, appWidgetManager: AppWidgetManager, appWidgetId: Int, projects: List<Project>) {
        Log.i(TAG, "Projects for Widget: $projects")
        val buttonsToDisplay = widgetProviderUtils.getAmountOfButtonsToDisplay(projects.size, appWidgetManager, appWidgetId)
        Log.i(TAG, "buttonsToDisplay: $buttonsToDisplay")
        when (buttonsToDisplay) {
            0 -> Log.e(TAG, "Please setup some Projects")
            1 -> showButtonOne(context, remoteViews, projects)
            2 -> showButtonOneTwo(context, remoteViews, projects)
            3 -> showButtonOneTwoThree(context, remoteViews, projects)
            4 -> showButtonOneTwoThreeFour(context, remoteViews, projects)
            else -> showAllButtons(context, remoteViews, projects)
        }
    }

    private fun showAllButtons(context: Context, remoteViews: RemoteViews, projects: List<Project>) {
        initButton(context, remoteViews, projects[0], R.id.bu_widget_one, context.getString(R.string.action_widget_button_one))
        initButton(context, remoteViews, projects[1], R.id.bu_widget_two, context.getString(R.string.action_widget_button_two))
        initButton(context, remoteViews, projects[2], R.id.bu_widget_three, context.getString(R.string.action_widget_button_three))
        initButton(context, remoteViews, projects[3], R.id.bu_widget_four, context.getString(R.string.action_widget_button_four))
        initButton(context, remoteViews, projects[4], R.id.bu_widget_five, context.getString(R.string.action_widget_button_five))
    }

    private fun showButtonOneTwoThreeFour(context: Context, remoteViews: RemoteViews, projects: List<Project>) {
        initButton(context, remoteViews, projects[0], R.id.bu_widget_one, context.getString(R.string.action_widget_button_one))
        initButton(context, remoteViews, projects[1], R.id.bu_widget_two, context.getString(R.string.action_widget_button_two))
        initButton(context, remoteViews, projects[2], R.id.bu_widget_three, context.getString(R.string.action_widget_button_three))
        initButton(context, remoteViews, projects[3], R.id.bu_widget_four, context.getString(R.string.action_widget_button_four))
        disableButton(remoteViews, R.id.bu_widget_five)
    }

    private fun showButtonOneTwoThree(context: Context, remoteViews: RemoteViews, projects: List<Project>) {
        initButton(context, remoteViews, projects[0], R.id.bu_widget_one, context.getString(R.string.action_widget_button_one))
        initButton(context, remoteViews, projects[1], R.id.bu_widget_two, context.getString(R.string.action_widget_button_two))
        initButton(context, remoteViews, projects[2], R.id.bu_widget_three, context.getString(R.string.action_widget_button_three))
        disableButton(remoteViews, R.id.bu_widget_four)
        disableButton(remoteViews, R.id.bu_widget_five)
    }

    private fun showButtonOneTwo(context: Context, remoteViews: RemoteViews, projects: List<Project>) {
        initButton(context, remoteViews, projects[0], R.id.bu_widget_one, context.getString(R.string.action_widget_button_one))
        initButton(context, remoteViews, projects[1], R.id.bu_widget_two, context.getString(R.string.action_widget_button_two))
        disableButton(remoteViews, R.id.bu_widget_three)
        disableButton(remoteViews, R.id.bu_widget_four)
        disableButton(remoteViews, R.id.bu_widget_five)
    }

    private fun showButtonOne(context: Context, remoteViews: RemoteViews, projects: List<Project>) {
        initButton(context, remoteViews, projects[0], R.id.bu_widget_one, context.getString(R.string.action_widget_button_one))
        disableButton(remoteViews, R.id.bu_widget_two)
        disableButton(remoteViews, R.id.bu_widget_three)
        disableButton(remoteViews, R.id.bu_widget_four)
        disableButton(remoteViews, R.id.bu_widget_five)
    }

    private fun disableButton(remoteViews: RemoteViews, buttonId: Int) {
        remoteViews.setViewVisibility(buttonId, View.GONE)
    }

    private fun initButton(context: Context, remoteViews: RemoteViews, project: Project, buttonId: Int, action: String) {
        val intent = createBroadcastIntentForProjectWithId(context, project, action)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        remoteViews.setOnClickPendingIntent(buttonId, pendingIntent)
        remoteViews.setViewVisibility(buttonId, View.VISIBLE)
        remoteViews.setTextViewText(buttonId, project.shortCode)
        remoteViews.setTextColor(buttonId, ContextCompat.getColor(context, R.color.eazyTime_colorWhite))
        remoteViews.setInt(buttonId, "setBackgroundColor", R.color.eazyTime_colorProject1)
    }

    private fun createBroadcastIntentForProjectWithId(context: Context, project: Project, action: String): Intent {
        return Intent(context, WidgetBroadCastReceiver::class.java).also { intent ->
            intent.action = action
            intent.putExtra(context.getString(R.string.ExtraKeyProjectId), project.id)
            intent.putExtra(context.getString(R.string.ExtraKeyProjectName), project.name)
            intent.putExtra(context.getString(R.string.ExtraKeyProjectShortCode), project.shortCode)
        }
    }

    override fun onAppWidgetOptionsChanged(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, newOptions: Bundle?) {
        updateButtonsOnWidget(context, appWidgetManager, appWidgetId)
    }

    private fun updateButtonsOnWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val task = GetProjectListItemsAsyncTask(widgetProjects)
        task.projectDao = projectDao
        task.execute()

        widgetProjects.observeForever { selectedProjects ->
            selectedProjects?.let {
                val remoteViews = RemoteViews(context.packageName, R.layout.homescreen_widget)
                updateButtons(context, remoteViews, appWidgetManager, appWidgetId, selectedProjects)
                appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
            }
        }
    }

    private class GetProjectListItemsAsyncTask internal constructor(private val projectsLiveData: MutableLiveData<List<Project>>) : AsyncTask<Void, Void, Void>() {
        var projectDao: ProjectDao? = null
        override fun doInBackground(vararg params: Void?): Void? {
            projectDao?.geProjectsForWidget()?.let { projects ->
                projectsLiveData.postValue(projects)
            }
            return null
        }
    }
}


