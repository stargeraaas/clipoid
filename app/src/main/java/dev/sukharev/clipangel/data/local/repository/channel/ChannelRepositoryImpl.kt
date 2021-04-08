package dev.sukharev.clipangel.data.local.repository.channel

import dev.sukharev.clipangel.data.local.database.dao.ChannelDao
import dev.sukharev.clipangel.data.local.database.dao.ClipDao
import dev.sukharev.clipangel.data.local.database.model.ChannelEntity
import dev.sukharev.clipangel.data.local.database.model.ChannelWithClips
import dev.sukharev.clipangel.domain.channel.models.Channel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ChannelRepositoryImpl(private val channelDao: ChannelDao,
                            private val clipDao: ClipDao) : ChannelRepository {

    private val channels = mutableListOf<Channel>()

    override suspend fun create(channel: Channel): Flow<List<Channel>> = flow {
        val entity = ChannelEntity(channel.id, channel.name, channel.secureKey, channel.createTime)
        channelDao.create(entity)
        updateChannelCache()
        emit(channels)
    }

    override suspend fun delete(id: String): Flow<List<Channel>> = flow {
        channels.find { it.id == id }?.let { channel ->
            channelDao.delete(channel.id)
            updateChannelCache()
            emit(channels)
        }
    }

    override suspend fun get(id: String): Flow<Channel> = flow {
        if (channels.isEmpty())
            updateChannelCache()

        val channel = channels.find { it.id == id }
                ?: throw IllegalStateException("Channel not found")
        emit(channel)
    }

    override suspend fun getAll(): Flow<List<Channel>> = flow {
        if (channels.isEmpty())
            updateChannelCache()
        emit(channels)
    }.flowOn(Dispatchers.IO)

    private fun updateChannelCache() {
        val updatedChannels = channelDao.getChannelWithClips().map { it.mapToChannel() }
        channels.clear()
        channels.addAll(updatedChannels)
    }

    override fun getFromCache(id: String): Channel = channels.find { it.id == id }
            ?: throw IllegalStateException()

    private fun ChannelWithClips.mapToChannel(): Channel {
        return Channel(this.channelEntity.id,
                this.channelEntity.name,
                this.channelEntity.secret,
                this.channelEntity.registerTime,
                this.channelEntity.isDeleted,
                clips.size
        )
    }

}