package dev.sukharev.clipangel.services

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.core.App
import dev.sukharev.clipangel.utils.copyInClipboardWithToast

class ClipboardCopyBroadcast : BroadcastReceiver() {

    private val context = App.app.applicationContext

    private val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        val ACTION_UPDATE_NOTIFICATION = "com.clipangel.clip.ACTION_COPY_CLIP"
        val NOTIFICATION_ID_EXTRA = "NOTIFICATION_ID"
        val CLIP_DATA_EXTRA = "CLIP_DATA_EXTRA"
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        intent?.extras?.getString(CLIP_DATA_EXTRA)?.apply {
            copyInClipboardWithToast(context?.getString(R.string.copied_alert)!!)
        }

        val notificationId = intent?.extras?.getInt(NOTIFICATION_ID_EXTRA, -1) ?: -1

        if (notificationId != -1)
            notificationManager.cancel(notificationId)
    }
}