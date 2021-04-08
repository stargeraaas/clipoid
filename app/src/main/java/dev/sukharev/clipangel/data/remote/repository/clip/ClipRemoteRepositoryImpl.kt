package dev.sukharev.clipangel.data.remote.repository.clip

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.database.FirebaseDatabase
import dev.sukharev.clipangel.BuildConfig.CLIPANGEL_AES_IV
import dev.sukharev.clipangel.data.local.repository.channel.ChannelRepository
import dev.sukharev.clipangel.domain.clip.Clip
import dev.sukharev.clipangel.utils.aes.AESCrypt
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class ClipRemoteRepositoryImpl(private var firebaseDb: FirebaseDatabase,
                               private val channelRepository: ChannelRepository) : ClipRemoteRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalCoroutinesApi
    override fun addClip(channelId: String): Flow<Clip> = callbackFlow {
        firebaseDb.getReference(channelId).apply { keepSynced(true) }
                .child("data").get().addOnSuccessListener { snapshot ->
                    GlobalScope.launch {
                        // No clip data in database
                        if (snapshot.value == null) {
                            close(NoClipDataInDatabaseException(channelId))
                        }

                        channelRepository.get(channelId).collect {
                            offer(Clip.create(channelId,
                                    decrypt(snapshot.value.toString(), it.secureKey), Date().time))
                        }

                    }
                }.addOnFailureListener {
                    if (it.message == "Client is offline") {
                        firebaseDb.goOnline()
                        addClip(channelId)
                    } else {
                        close(it)
                    }

                }

        awaitClose()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun decrypt(data: String, secret: String) = String(AESCrypt.decrypt(
            Base64.getDecoder().decode(CLIPANGEL_AES_IV),
            Base64.getDecoder().decode(secret),
            Base64.getDecoder().decode(data)
    ))
}