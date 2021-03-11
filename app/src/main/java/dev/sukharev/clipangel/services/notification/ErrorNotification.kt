package dev.sukharev.clipangel.services.notification

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import dev.sukharev.clipangel.R


class ErrorNotification(context: Context, val title: String, val message: String) : BaseNotification(context) {

    private val NOTIFICATION_ID = 255

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun show(): Int {

        val notificationBuilder = NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setLargeIcon(context.resources.getDrawable(R.drawable.ic_warning, null).toBitmap(100, 100))
                .setSmallIcon(R.mipmap.ic_launcher_clipangel_foreground)

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())

        return NOTIFICATION_ID
    }
}