package dev.sukharev.clipangel.domain.channel

import dev.sukharev.clipangel.data.local.repository.channel.ChannelRepository
import dev.sukharev.clipangel.data.local.repository.credentials.Credentials
import dev.sukharev.clipangel.data.remote.repository.channel.ChannelRemoteRepository
import dev.sukharev.clipangel.domain.channel.models.Channel
import dev.sukharev.clipangel.domain.channel.models.ChannelCredentials
import dev.sukharev.clipangel.domain.models.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

class ChannelInteractorImpl(val channelRepository: ChannelRepository,
                            val channelRemoteRepository: ChannelRemoteRepository,
                            val credentialsRepository: Credentials) : ChannelInteractor {


    @FlowPreview
    @ExperimentalCoroutinesApi
    override suspend fun createChannel(credentials: ChannelCredentials): Flow<List<Channel>> =
            channelRemoteRepository.createChannel(credentials,
                    credentialsRepository.getFirebaseToken() ?: "")
                    .flatMapMerge { channelRepository.create(it) }


    @ExperimentalCoroutinesApi
    override suspend fun deleteChannel(id: String): Flow<Result<List<Channel>>> = callbackFlow {
        channelRemoteRepository.deleteChannel(id).collect {
            if (it.isSuccess()) {
                channelRepository.delete(it.asSuccess().value).collect {
                    offer(Result.Success.Value(it))
                }
            } else {
                offer(it.asFailure())
            }
        }
    }

    override suspend fun getAllChannels(): Flow<Result<List<Channel>>> = channelRepository.getAll().map {
        Result.Success.Value(it)
    }.catch { Result.Failure.Error(it) }

    @ExperimentalCoroutinesApi
    override suspend fun updateToken(channelId: String, token: String):
            Flow<Result<EmptyResult>> = callbackFlow {
    }
}