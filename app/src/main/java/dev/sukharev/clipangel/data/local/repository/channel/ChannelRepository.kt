package dev.sukharev.clipangel.data.local.repository.channel

import dev.sukharev.clipangel.domain.channel.models.Channel
import kotlinx.coroutines.flow.Flow


interface ChannelRepository {
    suspend fun create(channel: Channel)
    fun delete(channel: Channel)
    fun get(id: String): Flow<List<Channel>>
    fun getAll(): Flow<List<Channel>>
}