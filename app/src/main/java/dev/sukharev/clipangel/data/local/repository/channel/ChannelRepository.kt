package dev.sukharev.clipangel.data.local.repository.channel

import dev.sukharev.clipangel.domain.channel.models.Channel
import kotlinx.coroutines.flow.Flow


interface ChannelRepository {
    suspend fun create(channel: Channel): Flow<List<Channel>>
    suspend fun delete(id: String): Flow<List<Channel>>
    suspend fun get(id: String): Flow<Channel>
    suspend fun getAll(): Flow<List<Channel>>
}