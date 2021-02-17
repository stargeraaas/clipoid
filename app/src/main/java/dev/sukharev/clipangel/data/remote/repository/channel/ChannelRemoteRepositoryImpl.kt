package dev.sukharev.clipangel.data.remote.repository.channel

import com.google.firebase.database.DatabaseReference
import dev.sukharev.clipangel.domain.channel.models.Channel

class ChannelRemoteRepositoryImpl(private var firebaseDb: DatabaseReference): ChannelRemoteRepository {

    override fun createChannel(channel: Channel) {
        println()
    }

    override fun getChannelData(channelId: String) {
        TODO("Not yet implemented")
    }

    override fun getClipData(channelId: String) {
        TODO("Not yet implemented")
    }
}