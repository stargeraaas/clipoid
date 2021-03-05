package dev.sukharev.clipangel.services.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.annotation.RequiresApi

abstract class BaseNotification(val context: Context): Notification {

    protected val PRIMARY_CHANNEL_ID = "clip_notification_channel"

    protected val notificationManager: NotificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    @RequiresApi(Build.VERSION_CODES.O)
    protected val notificationChannel = NotificationChannel(PRIMARY_CHANNEL_ID,
            "Clip notification", NotificationManager.IMPORTANCE_HIGH).apply {
                enableVibration(true)
                description = "Clip description"
                notificationManager.createNotificationChannel(this)
    }

}