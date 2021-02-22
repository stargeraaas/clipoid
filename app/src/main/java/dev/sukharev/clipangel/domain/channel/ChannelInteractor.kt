package dev.sukharev.clipangel.domain.channel

import dev.sukharev.clipangel.domain.channel.models.Channel
import dev.sukharev.clipangel.domain.channel.models.ChannelCredentials
import dev.sukharev.clipangel.domain.models.EmptyResult
import dev.sukharev.clipangel.domain.models.Result
import kotlinx.coroutines.flow.Flow

interface ChannelInteractor {
    suspend fun createChannel(credentials: ChannelCredentials): Flow<Result<List<Channel>>>
    suspend fun deleteChannel(id: String): Flow<Result<EmptyResult>>

    suspend fun getAllChannels(): Flow<Result<List<Channel>>>

    suspend fun updateToken(channelId: String, token: String): Flow<Result<EmptyResult>>
}

