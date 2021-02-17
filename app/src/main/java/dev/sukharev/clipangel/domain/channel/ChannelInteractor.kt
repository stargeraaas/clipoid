package dev.sukharev.clipangel.domain.channel

import dev.sukharev.clipangel.domain.channel.models.ChannelCredentials

interface ChannelInteractor {
    fun createChannel(credentials: ChannelCredentials)
}

