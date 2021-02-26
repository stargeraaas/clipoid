package dev.sukharev.clipangel.data.remote.repository.clip

import dev.sukharev.clipangel.domain.clip.Clip
import dev.sukharev.clipangel.domain.models.Result
import kotlinx.coroutines.flow.Flow

interface ClipRemoteRepository {
    fun get(channelId: String): Flow<Result<Clip>>
}