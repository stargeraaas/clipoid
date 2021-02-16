package dev.sukharev.clipangel.data.local.repository

import dev.sukharev.clipangel.data.local.database.dao.ChannelDao
import dev.sukharev.clipangel.data.local.database.model.ChannelEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class ChannelRepositoryImpl(private val channelDao: ChannelDao): ChannelRepository {


    override suspend fun create(channel: Channel) {
        channelDao.create(ChannelEntity(channel.id, channel.secureKey, channel.createTime))
    }

    override fun delete(channel: Channel) {

    }

    override fun get(id: String): Flow<List<Channel>> = flow {

    }

    override fun getAll(): Flow<List<Channel>> = flow {

    }
}