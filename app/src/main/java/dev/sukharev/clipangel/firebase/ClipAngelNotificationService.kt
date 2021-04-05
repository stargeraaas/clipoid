package dev.sukharev.clipangel.firebase

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dev.sukharev.clipangel.core.App
import dev.sukharev.clipangel.data.local.repository.channel.ChannelRepository
import dev.sukharev.clipangel.data.local.repository.credentials.Credentials
import dev.sukharev.clipangel.data.remote.repository.clip.NoClipDataInDatabaseException
import dev.sukharev.clipangel.domain.clip.create.CreateClipInteractor
import dev.sukharev.clipangel.services.notification.ErrorNotification
import dev.sukharev.clipangel.services.notification.NewClipNotification
import dev.sukharev.clipangel.services.notification.Notification
import dev.sukharev.clipangel.utils.aes.DecryptException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class ClipAngelNotificationService : FirebaseMessagingService() {

    private val credentials: Credentials by inject()

    private val createClipInteractor: CreateClipInteractor by inject()
    private val channelRepository: ChannelRepository by inject()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        GlobalScope.launch {
            createClipInteractor.create(remoteMessage.data["channel"].toString())
                    .zip(channelRepository.get(remoteMessage.data["channel"].toString())) { clip, channel ->
                        NewClipNotification(App.app, clip.id, channel.name, clip.data.trim()).show()
                    }
                    .catch { e ->
                        handleException(e)
                    }
                    .collect()
        }
    }

    private fun handleException(e: Throwable) {
        val notification: Notification = when (e) {
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