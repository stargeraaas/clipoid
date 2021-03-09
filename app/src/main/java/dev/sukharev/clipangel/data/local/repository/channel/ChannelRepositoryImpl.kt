package dev.sukharev.clipangel.data.local.repository.channel

import dev.sukharev.clipangel.data.local.database.dao.ChannelDao
import dev.sukharev.clipangel.data.local.database.dao.ClipDao
import dev.sukharev.clipangel.data.local.database.model.ChannelEntity
import dev.sukharev.clipangel.data.local.database.model.mapToDomain
import dev.sukharev.clipangel.domain.channel.models.Channel
import dev.sukharev.clipangel.domain.clip.Clip
import dev.sukharev.clipangel.domain.models.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception
import java.util.*


class ChannelRepositoryImpl(private val channelDao: ChannelDao,
                            private val clipDao: ClipDao) : ChannelRepository {

    override suspend fun create(channel: Channel): Flow<List<Channel>> = flow {
        channelDao.create(ChannelEntity(channel.id, channel.name, channel.secureKey,
                channel.createTime))

        getAll().collect {
            emit(it)
        }
    }

    override suspend fun delete(id: String): Flow<List<Channel>> = flow {
        channelDao.delete(id)
        getAll().collect {
            emit(it)
        }
    }

    override suspend fun get(id: String): Flow<Channel> = flow {
        emit(channelDao.getAll().map { Channel(it.id, it.name, it.secret, it.registerTime) }.find { it.id == id }!!)
    }

    override suspend fun getAll(): Flow<List<Channel>> = flow {
        try {
            val clipList = clipDao.getAll().map { it.mapToDomain() }
            emit(channelDao.getAll().map { Channel(it.id, it.name, it.secret, it.registerTime,
                    clipCount = getClipCount(clipList, it.id)) })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.flowOn(Dispatchers.IO)

    private fun getClipCount(clipList: List<Clip>, channelId: String): Int {
        return clipList.filter { it.channelId == channelId }.size
    }
}