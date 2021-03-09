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
import dev.sukharev.clipangel.data.remote.repository.clip.NoClipDataInDatabaseException
import dev.sukharev.clipangel.domain.clip.create.CreateClipCase
import dev.sukharev.clipangel.domain.models.asSuccess
import dev.sukharev.clipangel.domain.models.isSuccess
import dev.sukharev.clipangel.services.ClipboardCopyBroadcast
import dev.sukharev.clipangel.services.NotificationFactory
import dev.sukharev.clipangel.services.notification.ErrorNotification
import dev.sukharev.clipangel.services.notification.NewClipNotification
import dev.sukharev.clipangel.services.notification.Notification
import dev.sukharev.clipangel.utils.aes.DecryptException
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
            createClipCase.create(remoteMessage.data["channel"].toString())
                    .catch { e ->
                        handleException(e)
                    }
                    .collect {
                        NewClipNotification(App.app, it.data).show()
                    }
        }
    }

    private fun handleException(e: Throwable) {
        val notification: Notification = when(e) {
            is NoClipDataInDatabaseException -> {
                ErrorNotification(App.app, "Клип не найден в БД",
                        "Ошибка получения клипа")
            }
            is DecryptException -> {
                ErrorNotification(App.app, "Произошла ошибка при дешифровании клипа",
                        "Ошибка дешифрования клипа")
            }
            else -> ErrorNotification(App.app, e.message.toString(), "Ошибка получения клипа")
        }

        notification.show()
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        credentials.saveFirebaseToken(s)
    }
}