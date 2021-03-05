package dev.sukharev.clipangel.firebase

import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.core.App
import dev.sukharev.clipangel.data.local.repository.credentials.Credentials
import dev.sukharev.clipangel.domain.clip.create.CreateClipCase
import dev.sukharev.clipangel.domain.models.asSuccess
import dev.sukharev.clipangel.domain.models.isSuccess
import dev.sukharev.clipangel.services.ClipboardCopyBroadcast
import dev.sukharev.clipangel.services.NotificationFactory
import dev.sukharev.clipangel.services.notification.NewClipNotification
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onErrorCollect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

class ClipAngelNotificationService : FirebaseMessagingService() {

    private val credentials: Credentials by inject()

    private val createClipCase: CreateClipCase by inject()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        GlobalScope.launch {
            createClipCase.create(remoteMessage.data.get("channel").toString()).collect {
                if (it.isSuccess())
                    NewClipNotification(App.app, it.asSuccess().value.data).show()
                else
                    NewClipNotification(App.app, "").show()
            }
        }
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        credentials.saveFirebaseToken(s)
    }
}