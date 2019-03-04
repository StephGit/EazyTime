package ch.bfh.mad.eazytime.homeScreenWidget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.TAG
import ch.bfh.mad.eazytime.di.Injector
import ch.bfh.mad.eazytime.util.TimerService
import java.lang.ref.WeakReference
import javax.inject.Inject


class WidgetBroadCastReceiver : BroadcastReceiver() {

    init {
        Injector.appComponent.inject(this)
    }

    @Inject
    lateinit var timerService: TimerService

    override fun onReceive(context: Context, intent: Intent) {
        val projectIdExtraKey = context.getString(R.string.ExtraKeyProjectId)
        val projectId = intent.getLongExtra(projectIdExtraKey, -1)
        Log.i(TAG, "WidgetBroadCastReceiver received: ${intent.action} for projectId: $projectId}")
        val changeAndStartProjectAsyncTask = ChangeAndStartProjectAsyncTask(timerService)
        changeAndStartProjectAsyncTask.currentContext = WeakReference(context)
        changeAndStartProjectAsyncTask.execute(projectId)
    }

    private class ChangeAndStartProjectAsyncTask internal constructor(private val timerService: TimerService) : AsyncTask<Long, Void, Void>() {
        var currentContext: WeakReference<Context>? = null
        override fun doInBackground(vararg params: Long?): Void? {
            params[0]?.let { projectId ->
                timerService.changeAndStartProject(projectId)
            }
            return null
        }

        override fun onPostExecute(result: Void?) {

            currentContext?.get()?.let { ctx ->
                ctx.sendBroadcast(WidgetProvider.getUpdateAppWidgetsIntent(ctx))
            }
        }
    }

}