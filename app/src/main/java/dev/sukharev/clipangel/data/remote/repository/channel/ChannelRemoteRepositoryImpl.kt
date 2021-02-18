package dev.sukharev.clipangel.data.remote.repository.channel

import android.os.Build
import com.google.firebase.database.FirebaseDatabase
import dev.sukharev.clipangel.domain.channel.models.Channel
import dev.sukharev.clipangel.domain.channel.models.ChannelCredentials
import dev.sukharev.clipangel.domain.models.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.runBlocking
import java.lang.Exception

class ChannelRemoteRepositoryImpl(private var firebaseDb: FirebaseDatabase): ChannelRemoteRepository {

    @ExperimentalCoroutinesApi
    override suspend fun createChannel(channel: ChannelCredentials, deviceToken: String) = callbackFlow {
        val channelDb = firebaseDb.getReference(channel.id)
        val recipient = channelDb.child("recipients")

        channelDb.get().addOnSuccessListener { snapshot ->
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


    override suspend fun getChannelData(channelId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getClipData(channelId: String) {
        TODO("Not yet implemented")
    }
}