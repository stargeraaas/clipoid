package dev.sukharev.clipangel.data.local.repository.channel

import dev.sukharev.clipangel.data.local.database.dao.ChannelDao
import dev.sukharev.clipangel.data.local.database.model.ChannelEntity
import dev.sukharev.clipangel.domain.channel.models.Channel
import dev.sukharev.clipangel.domain.models.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class ChannelRepositoryImpl(private val channelDao: ChannelDao) : ChannelRepository {

    private val cachedResult: MutableList<Channel> = mutableListOf()

    override suspend fun create(channel: Channel) {
        channelDao.create(ChannelEntity(channel.id, channel.name ,channel.secureKey, 1, channel.createTime))
    }

    override fun delete(channel: Channel) {

    }

    override fun get(id: String): Flow<Channel> = flow {
        emit(channelDao.getAll().map { Channel(it.id, it.name, it.secret, it.createTime) }.find { it.id == id }!!)
    }

    override fun getAll(): Flow<List<Channel>> = flow {
        emit(channelDao.getAll().map { Channel(it.id, it.name, it.secret, it.createTime) })
    }
}