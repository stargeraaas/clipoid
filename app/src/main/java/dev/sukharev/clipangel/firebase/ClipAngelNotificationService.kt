package dev.sukharev.clipangel.firebase

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dev.sukharev.clipangel.data.local.repository.credentials.Credentials
import org.koin.android.ext.android.inject

class ClipAngelNotificationService : FirebaseMessagingService() {


    private val credentials: Credentials by inject()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        credentials.saveFirebaseToken(s)
    }
}