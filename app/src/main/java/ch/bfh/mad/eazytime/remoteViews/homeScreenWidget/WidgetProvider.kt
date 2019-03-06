package ch.bfh.mad.eazytime.remoteViews.homeScreenWidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidx.core.content.ContextCompat
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.data.dao.ProjectDao
import ch.bfh.mad.eazytime.di.Injector
import ch.bfh.mad.eazytime.projects.ProjectListItem
import ch.bfh.mad.eazytime.remoteViews.RemoteViewBroadCastReceiver
import ch.bfh.mad.eazytime.remoteViews.RemoteViewButtonUtil
import ch.bfh.mad.eazytime.util.EazyTimeColorUtil
import ch.bfh.mad.eazytime.util.ProjectProviderService
import ch.bfh.mad.eazytime.util.WidgetProviderUtils
import javax.inject.Inject

class WidgetProvider : AppWidgetProvider() {

    init {
        Injector.appComponent.inject(this)
    }

    @Inject
    lateinit var widgetProviderUtils: WidgetProviderUtils

    @Inject
    lateinit var projectProviderService: ProjectProviderService

    @Inject
    lateinit var remoteViewButtonUtil: RemoteViewButtonUtil

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        val amountOfWidgets: Int = appWidgetIds?.size ?: 0
        Log.i(TAG, "onUpdate amountOfWidgets: $amountOfWidgets")

        for (i in 0 until amountOfWidgets) {
            val appWidgetId = appWidgetIds!![i]
            updateButtonsOnWidget(context!!, appWidgetManager!!, appWidgetId)
        }
    }

    override fun onAppWidgetOptionsChanged(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, newOptions: Bundle?) {
        Log.i(TAG, "onAppWidgetOptionsChanged")
        updateButtonsOnWidget(context, appWidgetManager, appWidgetId)
    }

    private fun updateButtonsOnWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        projectProviderService.getProjectListitems().observeForever {allProjects ->
            allProjects.filter { project -> project.onWidget == true }.let { projectListItems ->
                val buttonsToDisplay = widgetProviderUtils.getAmountOfButtonsToDisplay(projectListItems.size, appWidgetManager, appWidgetId)
                Log.i(TAG, "buttonsToDisplay in HomeScreenWidget: $buttonsToDisplay")
                val remoteViews = RemoteViews(context.packageName, R.layout.homescreen_widget)
                remoteViewButtonUtil.updateButtons(context, remoteViews, buttonsToDisplay, projectListItems, false)
                appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
            }

        }
    }

    companion object {
        fun getUpdateAppWidgetsIntent(ctx: Context): Intent {
            return Intent(ctx, WidgetProvider::class.java).also { intent ->
                intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                val ids = AppWidgetManager.getInstance(ctx.applicationContext)
                    .getAppWidgetIds(ComponentName(ctx,  WidgetProvider::class.java))
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            }
        }
    }
}


