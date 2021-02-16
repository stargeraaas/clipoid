package dev.sukharev.clipangel.data.local.repository

import kotlinx.coroutines.flow.Flow


interface ChannelRepository {
    fun create(channel: Channel)
    fun delete(channel: Channel)
    fun get(id: String): Flow<List<Channel>>
    fun getAll(): Flow<List<Channel>>
}