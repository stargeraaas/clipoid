package dev.sukharev.clipangel.data.local.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class ChannelRepositoryImpl: ChannelRepository {



    override fun create(channel: Channel) {
        TODO("Not yet implemented")
    }

    override fun delete(channel: Channel) {
        TODO("Not yet implemented")
    }

    override fun get(id: String): Flow<List<Channel>> = flow {

    }

    override fun getAll(): Flow<List<Channel>> = flow {

    }
}