package dev.sukharev.clipangel.data.remote.repository.channel

import android.os.Build
import com.google.firebase.database.FirebaseDatabase
import dev.sukharev.clipangel.domain.channel.models.Channel
import dev.sukharev.clipangel.domain.channel.models.ChannelCredentials
import dev.sukharev.clipangel.domain.models.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception

class ChannelRemoteRepositoryImpl(private var firebaseDb: FirebaseDatabase): ChannelRemoteRepository {

    @ExperimentalCoroutinesApi
    override suspend fun createChannel(channel: ChannelCredentials, deviceToken: String) = callbackFlow {
        firebaseDb.goOnline()
        val channelDb = firebaseDb.getReference(channel.id).get().addOnSuccessListener {
            println()
        }.addOnFailureListener {
            offer(Result.Failure.Error(it))
//            cancel("DB", it)
        }
        val recipient = firebaseDb.getReference(channel.id).child("recipients")

        firebaseDb.getReference(channel.id).get().addOnSuccessListener { snapshot ->
            recipient.updateChildren(hashMapOf<String, Any>("${Build.BRAND} ${Build.DEVICE}" to deviceToken)).addOnCompleteListener {
                offer(Result.Success.Value(Channel(channel.id,
                        snapshot.child("senderName").value.toString(),
                        channel.secret, snapshot.child("dataDate").value.toString())))
            }.addOnFailureListener {
                offer(Result.Failure.Error(it))
            }
        }

        awaitClose()
    }

    @ExperimentalCoroutinesApi
    override suspend fun deleteChannel(id: String): Flow<Result<Channel>> = callbackFlow {
        val channelDb = firebaseDb.getReference(id)
        try {
            val recipient = channelDb.child("recipients")
            channelDb.get().addOnSuccessListener { snapshot ->

            }.addOnFailureListener {
                offer(Result.Failure.Error(it))
                close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
//        sendBlocking(Result.Failure.Error(Exception("Database Exception")))
        awaitClose {
            cancel()
        }
    }

    override suspend fun getChannelData(channelId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getClipData(channelId: String) {
        TODO("Not yet implemented")
    }
}