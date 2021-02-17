package dev.sukharev.clipangel.data.remote.repository.channel

import com.google.firebase.database.FirebaseDatabase
import dev.sukharev.clipangel.domain.channel.models.Channel
import kotlinx.coroutines.runBlocking

class ChannelRemoteRepositoryImpl(private var firebaseDb: FirebaseDatabase): ChannelRemoteRepository {

    override fun createChannel(channel: Channel, deviceToken: String) {
        val channelDb = firebaseDb.getReference(channel.id)
        val recipient = channelDb.child("recipients")

        channelDb.get().addOnSuccessListener {
            println("CHANNEL NAME ${it.child("senderName").value}")
            println("CREATE TIME ${it.child("dataDate").value}")

            recipient.updateChildren(hashMapOf<String, Any>(deviceToken to "")).addOnCompleteListener {
                println("CHANNEL CREATED! ")
            }.addOnFailureListener {
                println()
            }
        }

    }

    override fun getChannelData(channelId: String) {
        TODO("Not yet implemented")
    }

    override fun getClipData(channelId: String) {
        TODO("Not yet implemented")
    }
}