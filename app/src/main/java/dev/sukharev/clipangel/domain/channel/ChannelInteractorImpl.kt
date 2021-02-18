package dev.sukharev.clipangel.domain.channel

import com.google.firebase.messaging.FirebaseMessaging
import dev.sukharev.clipangel.data.local.repository.channel.ChannelRepository
import dev.sukharev.clipangel.data.remote.repository.channel.ChannelRemoteRepository
import dev.sukharev.clipangel.domain.channel.models.Channel
import dev.sukharev.clipangel.domain.channel.models.ChannelCredentials
import dev.sukharev.clipangel.domain.models.EmptyResult
import dev.sukharev.clipangel.domain.models.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

class ChannelInteractorImpl(val channelRepository: ChannelRepository,
                            val channelRemoteRepository: ChannelRemoteRepository) : ChannelInteractor {


    @ExperimentalCoroutinesApi
    override suspend fun createChannel(credentials: ChannelCredentials): Flow<Result<EmptyResult>> = callbackFlow {
        val token = FirebaseMessaging.getInstance().token.addOnCompleteListener {

            channelRemoteRepository.createChannel(Channel(credentials.id, credentials.secret,
                    System.currentTimeMillis()), it.result ?: "")
            offer(Result.Success.Empty)
            close()
        }.addOnFailureListener {
            offer(Result.Failure.Error(it))
            close()
        }

        awaitClose()
    }

    @ExperimentalCoroutinesApi
    override suspend fun deleteChannel(id: String): Flow<Result<EmptyResult>> = callbackFlow {

    }

    @ExperimentalCoroutinesApi
    override suspend fun updateToken(channelId: String, token: String):
            Flow<Result<EmptyResult>> = callbackFlow {

    }
}