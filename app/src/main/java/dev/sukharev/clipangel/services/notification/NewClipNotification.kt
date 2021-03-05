package dev.sukharev.clipangel.services.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.services.ClipboardCopyBroadcast.*
import dev.sukharev.clipangel.services.ClipboardCopyBroadcast.Companion.ACTION_UPDATE_NOTIFICATION
import dev.sukharev.clipangel.services.ClipboardCopyBroadcast.Companion.CLIP_DATA_EXTRA
import dev.sukharev.clipangel.services.ClipboardCopyBroadcast.Companion.NOTIFICATION_ID_EXTRA


class NewClipNotification(context: Context, private val data: String) : BaseNotification(context) {

    private val NOTIFICATION_ID = 100

    override fun show(): Int {
        val notificationBuilder = NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setContentTitle("Получен новый клип!")
                .setContentText(getDataAsSubSequence(data))
                .setSmallIcon(R.mipmap.ic_launcher_clipangel_round)
                .addAction(R.drawable.ic_copy, context.getString(R.string.copy), getPendingIntent())

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())

        return NOTIFICATION_ID
    }

    private fun getDataAsSubSequence(data: String): String =
            if (data.length < 50) data else data.subSequence(0, 50).toString()

    private fun getCopyClipIntent() = Intent(ACTION_UPDATE_NOTIFICATION).apply {
        putExtra(CLIP_DATA_EXTRA, this@NewClipNotification.data)
        putExtra(NOTIFICATION_ID_EXTRA, NOTIFICATION_ID)
    }

    private fun getPendingIntent() = PendingIntent.getBroadcast(context, NOTIFICATION_ID,
            getCopyClipIntent(), PendingIntent.FLAG_ONE_SHOT)
}