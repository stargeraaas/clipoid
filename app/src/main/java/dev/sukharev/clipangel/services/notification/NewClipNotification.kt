package dev.sukharev.clipangel.services.notification

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.services.ClipboardCopyBroadcast.*
import dev.sukharev.clipangel.services.ClipboardCopyBroadcast.Companion.ACTION_UPDATE_NOTIFICATION
import dev.sukharev.clipangel.services.ClipboardCopyBroadcast.Companion.CLIP_DATA_EXTRA
import dev.sukharev.clipangel.services.ClipboardCopyBroadcast.Companion.NOTIFICATION_ID_EXTRA


class NewClipNotification(context: Context, private val clipId: String, private val senderName: String,
                          private val data: String) : BaseNotification(context) {

    private val NOTIFICATION_ID = 100

    override fun show(): Int {
        val notificationBuilder = NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setContentTitle(context.getString(R.string.new_clip_received).format(senderName))
                .setContentText(data)
                .setLargeIcon(context.resources.getDrawable(R.mipmap.ic_launcher_clipangel, null).toBitmap(200, 200))
                .setSmallIcon(R.mipmap.ic_launcher_clipangel_foreground)
                .addAction(R.drawable.ic_copy, context.getString(R.string.copy), getPendingIntent())
                .setAutoCancel(true)
                .setContentIntent(getDeepLinkIntent(clipId))

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())

        return NOTIFICATION_ID
    }

    private fun getDataAsSubSequence(data: String): String =
            if (data.length < 150) data else data.subSequence(0, 150).toString()

    private fun getCopyClipIntent() = Intent(ACTION_UPDATE_NOTIFICATION).apply {
        putExtra(CLIP_DATA_EXTRA, this@NewClipNotification.data)
        putExtra(NOTIFICATION_ID_EXTRA, NOTIFICATION_ID)
    }

    private fun getPendingIntent() = PendingIntent.getBroadcast(context, NOTIFICATION_ID,
            getCopyClipIntent(), PendingIntent.FLAG_ONE_SHOT)

    private fun getDeepLinkIntent(clipId: String): PendingIntent {
        return PendingIntent.getActivity(context, 1599, Intent("com.clipoid.OPEN_DETAILED_CLIP").apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            putExtra("CLIP_ID", clipId)
        }, PendingIntent.FLAG_ONE_SHOT)
    }
}