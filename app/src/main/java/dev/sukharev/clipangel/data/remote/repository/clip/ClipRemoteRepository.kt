package dev.sukharev.clipangel.data.remote.repository.clip

import dev.sukharev.clipangel.domain.clip.Clip
import kotlinx.coroutines.flow.Flow

interface ClipRemoteRepository {
    fun addClip(channelId: String): Flow<Clip>
}