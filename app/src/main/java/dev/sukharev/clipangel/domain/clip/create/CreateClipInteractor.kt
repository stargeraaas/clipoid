package dev.sukharev.clipangel.domain.clip.create

import dev.sukharev.clipangel.domain.clip.Clip
import kotlinx.coroutines.flow.Flow

interface CreateClipInteractor {
    fun create(channelId: String): Flow<Clip>
}