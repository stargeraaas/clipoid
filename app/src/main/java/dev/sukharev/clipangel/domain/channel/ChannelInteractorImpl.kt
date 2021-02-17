package dev.sukharev.clipangel.domain.channel

import com.google.firebase.messaging.FirebaseMessaging
import dev.sukharev.clipangel.data.local.repository.channel.ChannelRepository
import dev.sukharev.clipangel.data.remote.repository.channel.ChannelRemoteRepository
import dev.sukharev.clipangel.domain.channel.models.Channel
import dev.sukharev.clipangel.domain.channel.models.ChannelCredentials

class ChannelInteractorImpl(val channelRepository: ChannelRepository,
                            val channelRemoteRepository: ChannelRemoteRepository) : ChannelInteractor {


    override fun createChannel(credentials: ChannelCredentials) {
        val token = FirebaseMessaging.getInstance().token.addOnCompleteListener {
            channelRemoteRepository.createChannel(Channel(credentials.id, credentials.secret,
                    System.currentTimeMillis()), it.result ?: "")
        }.addOnFailureListener {
            println()
        }

    }
}