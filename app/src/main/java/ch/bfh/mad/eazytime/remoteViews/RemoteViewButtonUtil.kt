package ch.bfh.mad.eazytime.remoteViews

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import androidx.core.content.ContextCompat
import ch.bfh.mad.eazytime.R
import ch.bfh.mad.eazytime.di.Injector
import ch.bfh.mad.eazytime.projects.ProjectListItem
import ch.bfh.mad.eazytime.util.EazyTimeColorUtil
import javax.inject.Inject

class RemoteViewButtonUtil {

    init {
        Injector.appComponent.inject(this)
    }

    @Inject
    lateinit var eazyTimeColorUtil: EazyTimeColorUtil

    private var addUpdateNotificationExtra: Boolean = false

    fun updateButtons(context: Context, remoteViews: RemoteViews, buttonsToDisplay: Int, projects: List<ProjectListItem>, updateNotification: Boolean) {

        addUpdateNotificationExtra = updateNotification

        when (buttonsToDisplay) {
            0 -> showNoProjectInformation(context, remoteViews, projects)
            1 -> showButtonOne(context, remoteViews, projects)
            2 -> showButtonOneTwo(context, remoteViews, projects)
            3 -> showButtonOneTwoThree(context, remoteViews, projects)
            4 -> showButtonOneTwoThreeFour(context, remoteViews, projects)
            else -> showAllButtons(context, remoteViews, projects)
        }
    }


    private fun showAllButtons(context: Context, remoteViews: RemoteViews, projects: List<ProjectListItem>) {
        initButton(context, remoteViews, projects[0], R.id.bu_widget_one, context.getString(R.string.action_widget_button_one))
        initButton(context, remoteViews, projects[1], R.id.bu_widget_two, context.getString(R.string.action_widget_button_two))
        initButton(context, remoteViews, projects[2], R.id.bu_widget_three, context.getString(R.string.action_widget_button_three))
        initButton(context, remoteViews, projects[3], R.id.bu_widget_four, context.getString(R.string.action_widget_button_four))
        initButton(context, remoteViews, projects[4], R.id.bu_widget_five, context.getString(R.string.action_widget_button_five))
        disableElement(remoteViews, R.id.tv_widget_no_project)
    }

    private fun showButtonOneTwoThreeFour(context: Context, remoteViews: RemoteViews, projects: List<ProjectListItem>) {
        initButton(context, remoteViews, projects[0], R.id.bu_widget_one, context.getString(R.string.action_widget_button_one))
        initButton(context, remoteViews, projects[1], R.id.bu_widget_two, context.getString(R.string.action_widget_button_two))
        initButton(context, remoteViews, projects[2], R.id.bu_widget_three, context.getString(R.string.action_widget_button_three))
        initButton(context, remoteViews, projects[3], R.id.bu_widget_four, context.getString(R.string.action_widget_button_four))
        disableElement(remoteViews, R.id.bu_widget_five)
        disableElement(remoteViews, R.id.tv_widget_no_project)
    }

    private fun showButtonOneTwoThree(context: Context, remoteViews: RemoteViews, projects: List<ProjectListItem>) {
        initButton(context, remoteViews, projects[0], R.id.bu_widget_one, context.getString(R.string.action_widget_button_one))
        initButton(context, remoteViews, projects[1], R.id.bu_widget_two, context.getString(R.string.action_widget_button_two))
        initButton(context, remoteViews, projects[2], R.id.bu_widget_three, context.getString(R.string.action_widget_button_three))
        disableElement(remoteViews, R.id.bu_widget_four)
        disableElement(remoteViews, R.id.bu_widget_five)
        disableElement(remoteViews, R.id.tv_widget_no_project)
    }

    private fun showButtonOneTwo(context: Context, remoteViews: RemoteViews, projects: List<ProjectListItem>) {
        initButton(context, remoteViews, projects[0], R.id.bu_widget_one, context.getString(R.string.action_widget_button_one))
        initButton(context, remoteViews, projects[1], R.id.bu_widget_two, context.getString(R.string.action_widget_button_two))
        disableElement(remoteViews, R.id.bu_widget_three)
        disableElement(remoteViews, R.id.bu_widget_four)
        disableElement(remoteViews, R.id.bu_widget_five)
        disableElement(remoteViews, R.id.tv_widget_no_project)
    }

    private fun showButtonOne(context: Context, remoteViews: RemoteViews, projects: List<ProjectListItem>) {
        initButton(context, remoteViews, projects[0], R.id.bu_widget_one, context.getString(R.string.action_widget_button_one))
        disableElement(remoteViews, R.id.bu_widget_two)
        disableElement(remoteViews, R.id.bu_widget_three)
        disableElement(remoteViews, R.id.bu_widget_four)
        disableElement(remoteViews, R.id.bu_widget_five)
        disableElement(remoteViews, R.id.tv_widget_no_project)
    }

    private fun showNoProjectInformation(context: Context, remoteViews: RemoteViews, projects: List<ProjectListItem>) {
        disableElement(remoteViews, R.id.bu_widget_one)
        disableElement(remoteViews, R.id.bu_widget_two)
        disableElement(remoteViews, R.id.bu_widget_three)
        disableElement(remoteViews, R.id.bu_widget_four)
        disableElement(remoteViews, R.id.bu_widget_five)
        enableInformation(remoteViews, R.id.tv_widget_no_project)
    }

    private fun enableInformation(remoteViews: RemoteViews, buttonId: Int) {
        remoteViews.setViewVisibility(buttonId, View.VISIBLE)
    }

    private fun disableElement(remoteViews: RemoteViews, buttonId: Int) {
        remoteViews.setViewVisibility(buttonId, View.GONE)
    }

    private fun initButton(context: Context, remoteViews: RemoteViews, project: ProjectListItem, buttonId: Int, action: String) {
        val requestCode = project.id?.toInt() ?: 0
        val intent = createBroadcastIntentForProjectWithId(context, project, action)
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        remoteViews.setOnClickPendingIntent(buttonId, pendingIntent)
        remoteViews.setViewVisibility(buttonId, View.VISIBLE)
        remoteViews.setTextViewText(buttonId, project.shortCode)
        if (project.active) {
            remoteViews.setTextColor(buttonId, ContextCompat.getColor(context, R.color.eazyTime_colorBlack))
        } else {
            remoteViews.setTextColor(buttonId, ContextCompat.getColor(context, R.color.eazyTime_colorWhite))
        }
        val colorArrayId = eazyTimeColorUtil.getColorId(project.color!!)
        remoteViews.setInt(buttonId, "setBackgroundColor", ContextCompat.getColor(context, colorArrayId))
    }

    private fun createBroadcastIntentForProjectWithId(context: Context, project: ProjectListItem, action: String): Intent {
        return Intent(context, RemoteViewBroadCastReceiver::class.java).also { intent ->
            intent.action = action
            intent.putExtra(context.getString(R.string.ExtraKeyProjectId), project.id)
            intent.putExtra(context.getString(R.string.ExtraKeyProjectName), project.name)
            intent.putExtra(context.getString(R.string.ExtraKeyProjectShortCode), project.shortCode)
            intent.putExtra(context.getString(R.string.ExtraKeyUpdateNotification), addUpdateNotificationExtra)
        }
    }

    companion object {
        const val MAX_AMOUNT_OF_BUTTONS_ON_WIDGET = 5
    }
}