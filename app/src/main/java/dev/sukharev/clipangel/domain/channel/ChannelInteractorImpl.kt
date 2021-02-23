package dev.sukharev.clipangel.domain.channel

import com.google.firebase.messaging.FirebaseMessaging
import dev.sukharev.clipangel.data.local.repository.channel.ChannelRepository
import dev.sukharev.clipangel.data.local.repository.credentials.Credentials
import dev.sukharev.clipangel.data.remote.repository.channel.ChannelRemoteRepository
import dev.sukharev.clipangel.domain.channel.models.Channel
import dev.sukharev.clipangel.domain.channel.models.ChannelCredentials
import dev.sukharev.clipangel.domain.models.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import java.lang.Exception

class ChannelInteractorImpl(val channelRepository: ChannelRepository,
                            val channelRemoteRepository: ChannelRemoteRepository,
                            val credentialsRepository: Credentials) : ChannelInteractor {


    @ExperimentalCoroutinesApi
    override suspend fun createChannel(credentials: ChannelCredentials): Flow<Result<List<Channel>>> = callbackFlow {

        channelRemoteRepository.createChannel(credentials,
                credentialsRepository.getFirebaseToken() ?: "").collect {
            if (it.isSuccess()) {
                channelRepository.create(it.asSuccess().value).collect {
                    offer(Result.Success.Value(it))
                }
            } else if (it.isFailure()) {
                offer(Result.Failure.Error(it.asFailure().error))
            }
        }

        awaitClose()
    }

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