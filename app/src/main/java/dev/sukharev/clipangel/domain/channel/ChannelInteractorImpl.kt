package dev.sukharev.clipangel.domain.channel

import dev.sukharev.clipangel.data.local.repository.channel.ChannelRepository
import dev.sukharev.clipangel.data.local.repository.channel.ChannelRepositoryImpl
import dev.sukharev.clipangel.domain.channel.models.ChannelCredentials

class ChannelInteractorImpl(val channelRepository: ChannelRepository,
                            val channelRepositoryImpl: ChannelRepository): ChannelInteractor {


    override fun createChannel(credentials: ChannelCredentials) {
        // TODO: create
    }
}