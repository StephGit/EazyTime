package ch.bfh.mad.eazytime.remoteViews

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import ch.bfh.mad.eazytime.R
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.di.Injector
import ch.bfh.mad.eazytime.remoteViews.homeScreenWidget.WidgetProvider
import ch.bfh.mad.eazytime.remoteViews.notification.NotificationHandler
import ch.bfh.mad.eazytime.util.TimerService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject


class RemoteViewBroadCastReceiver : BroadcastReceiver() {

    init {
        Injector.appComponent.inject(this)
    }

    @Inject
    lateinit var timerService: TimerService

    @Inject
    lateinit var notificationHandler: NotificationHandler

    override fun onReceive(context: Context, intent: Intent) {
        val projectIdExtraKey = context.getString(R.string.ExtraKeyProjectId)
        val updateNotificationKey = context.getString(R.string.ExtraKeyUpdateNotification)
        val projectId = intent.getLongExtra(projectIdExtraKey, -1)
        val updateNotification = intent.getBooleanExtra(updateNotificationKey, false)
        Log.i(TAG, "RemoteViewBroadCastReceiver received: ${intent.action} for projectId: $projectId}")
        changeAndStartProject(projectId, context, updateNotification)
    }

    private fun changeAndStartProject(projectId: Long, ctx: Context, updateNotification: Boolean) = GlobalScope.launch(Dispatchers.IO) {
        timerService.changeAndStartProject(projectId)
        ctx.sendBroadcast(WidgetProvider.getUpdateAppWidgetsIntent(ctx))
        if (updateNotification) {
            notificationHandler.createEazyTimeNotification()
        }
    }
}