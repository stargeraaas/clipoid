package dev.sukharev.clipangel.data.local.repository.clip

import dev.sukharev.clipangel.domain.clip.Clip
import dev.sukharev.clipangel.domain.models.Result
import kotlinx.coroutines.flow.Flow

interface ClipRepository {
    fun create(clip: Clip): Flow<Clip>
    fun update(clip: Clip): Flow<Clip>
    fun delete(clip: Clip): Flow<Clip>
    fun getClipById(id: String): Flow<Clip>
    fun getAll(): Flow<List<Clip>>
    fun getAllWithSubscription(): Flow<List<Clip>>

    fun protectClip(clipId: String): Flow<Clip>
}