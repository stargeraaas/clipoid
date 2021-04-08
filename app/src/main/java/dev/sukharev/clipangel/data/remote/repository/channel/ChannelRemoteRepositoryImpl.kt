package dev.sukharev.clipangel.data.remote.repository.channel

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import dev.sukharev.clipangel.data.local.repository.credentials.Credentials
import dev.sukharev.clipangel.domain.channel.models.Channel
import dev.sukharev.clipangel.domain.channel.models.ChannelCredentials
import dev.sukharev.clipangel.domain.clip.Clip
import dev.sukharev.clipangel.domain.models.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.*

class ChannelRemoteRepositoryImpl(private var firebaseDb: FirebaseDatabase,
                                  private val credentials: Credentials) : ChannelRemoteRepository {

    private val SENDER_NAME_DB_ALIAS = "senderName"
    private val RECIPIENTS_DB_ALIAS = "recipients"

    @ExperimentalCoroutinesApi
    override suspend fun createChannel(channel: ChannelCredentials, deviceToken: String) = callbackFlow {
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            credentials.saveFirebaseToken(it)
            firebaseDb.goOnline()
            firebaseDb.getReference(channel.id).get().addOnSuccessListener { snapshot ->
                getRecipientsByIdFromDB(channel.id).updateChildren(getFirebaseTokenAndDeviceIdentifier()).addOnCompleteListener {
                    offer(Channel(channel.id,
                            snapshot.child(SENDER_NAME_DB_ALIAS).value.toString(),
                            channel.secret, Date().time))
                }.addOnFailureListener {
                    close(it)
                }
            }
        }.addOnFailureListener {
            close(it)
        }

//        getClipData(channel.id).collect {
//            println()
//        }

        awaitClose()
    }

    // Getting device identifier and firebase token for device registration in FirebaseDB
    private fun getFirebaseTokenAndDeviceIdentifier() =
            mapOf(credentials.getDeviceIdentifier() to credentials.getFirebaseToken())

    @ExperimentalCoroutinesApi
    override suspend fun deleteChannel(id: String): Flow<Result<String>> = callbackFlow {
        firebaseDb.goOnline()
        getRecipientsByIdFromDB(id).child(credentials.getDeviceIdentifier()).removeValue().addOnSuccessListener {
            offer(Result.Success.Value(id))
        }.addOnFailureListener {
            offer(Result.Failure.Error(it))
        }
        awaitClose()
    }

    private fun getRecipientsByIdFromDB(id: String) = firebaseDb.getReference(id).child(RECIPIENTS_DB_ALIAS)

    override suspend fun getChannelData(channelId: String) {
        TODO("Not yet implemented")
    }

    @ExperimentalCoroutinesApi
    override suspend fun getClipData(channelId: String): Flow<Result<Clip>> = callbackFlow {
        firebaseDb.goOnline()
        firebaseDb.getReference(channelId).get().addOnSuccessListener {
            offer(Result.Success.Value(Clip(UUID.randomUUID().toString(),
                    (it.value as HashMap<*, *>)["data"].toString(),
                    Date().time,
                    (it.value as HashMap<*, *>)["dataTimestamp"].toString().toLong(),
                    channelId
            )))
        }.addOnFailureListener {
            offer(Result.Failure.Error(it))
        }

        awaitClose()
    }
}