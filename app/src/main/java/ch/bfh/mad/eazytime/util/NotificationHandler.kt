package ch.bfh.mad.eazytime.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.os.Build
import android.support.v4.app.NotificationCompat
import ch.bfh.mad.BuildConfig
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.EazyTimeActivity
import java.util.*

class NotificationHandler {

    private val notificationChannelId = BuildConfig.APPLICATION_ID + ".channel"

    fun sendNotification(context: Context, message: String) {
        val notificationManager = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
            && notificationManager.getNotificationChannel(notificationChannelId) == null
        ) {
            val name = context.getString(R.string.app_name)
            val channel = NotificationChannel(
                notificationChannelId,
                name,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

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
}