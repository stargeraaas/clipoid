package dev.sukharev.clipangel.data.remote.repository.channel

import dev.sukharev.clipangel.domain.channel.models.Channel

interface ChannelRemoteRepository {
    fun createChannel(channel: Channel)
    fun getChannelData(channelId: String)
    fun getClipData(channelId: String)
}