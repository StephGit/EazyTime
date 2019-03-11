package ch.bfh.mad.eazytime.remoteViews.notification

import android.app.*
import android.content.Context
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ch.bfh.mad.BuildConfig
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.EazyTimeActivity
import ch.bfh.mad.eazytime.remoteViews.RemoteViewButtonUtil
import ch.bfh.mad.eazytime.util.ProjectProviderService
import java.util.*

class NotificationHandler(val context: Context, private val remoteViewButtonUtil: RemoteViewButtonUtil, val projectProviderService: ProjectProviderService) {

    private val notificationChannelId = BuildConfig.APPLICATION_ID + ".channel"
    private val notificationId = 789556

    // TODO remove for release
    fun sendNotification(message: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(notificationChannelId)

        val id = Random().nextInt()
        val intent = EazyTimeActivity.newIntent(context.applicationContext)

        val stackBuilder = TaskStackBuilder.create(context)
            .addParentStack(EazyTimeActivity::class.java)
            .addNextIntent(intent)
        val notificationPendingIntent = stackBuilder
            .getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, notificationChannelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(message)
            .setContentIntent(notificationPendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(id, notification)
    }

    fun getNotification(): Notification {
        val id = Random().nextInt()
        val intent = EazyTimeActivity.newIntent(context.applicationContext)
        val stackBuilder = TaskStackBuilder.create(context)
            .addParentStack(EazyTimeActivity::class.java)
            .addNextIntent(intent)
        val notificationPendingIntent = stackBuilder
            .getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT)
        return  NotificationCompat.Builder(context, notificationChannelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("EazyTime")
            .setContentIntent(notificationPendingIntent)
            .setAutoCancel(true)
            .build()
    }

    fun createEazyTimeNotification() {
        createNotificationChannel(notificationChannelId)

        val notificationLayout = RemoteViews(context.packageName, R.layout.notification)
        projectProviderService.getProjectListitems().observeForever { projectListItems ->
            remoteViewButtonUtil.updateButtons(context, notificationLayout, projectListItems.size, projectListItems, true)
            val builder = NotificationCompat.Builder(context, notificationChannelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(null)
                .setVibrate(longArrayOf(0))
                .setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationLayout)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(context)) {
                // reuse same notificationId because we only want to update a existing notification
                notify(notificationId, builder.build())
            }
        }
    }

    private fun createNotificationChannel(name: String) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val descriptionText = context.getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(notificationChannelId, name, importance).apply {
                description = descriptionText
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            // Register the channel with the system
            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}