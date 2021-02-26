package dev.sukharev.clipangel.domain.clip.create

import dev.sukharev.clipangel.domain.clip.Clip
import dev.sukharev.clipangel.domain.models.Result
import kotlinx.coroutines.flow.Flow

interface CreateClipCase {
    fun create(channelId: String): Flow<Result<Clip>>
}