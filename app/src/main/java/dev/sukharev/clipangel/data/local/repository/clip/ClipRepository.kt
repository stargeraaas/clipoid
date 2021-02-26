package dev.sukharev.clipangel.data.local.repository.clip

import dev.sukharev.clipangel.domain.clip.Clip
import dev.sukharev.clipangel.domain.models.Result
import kotlinx.coroutines.flow.Flow

interface ClipRepository {
    fun create(clip: Clip): Flow<Result<List<Clip>>>
    fun update(clip: Clip): Flow<Result<List<Clip>>>
    fun delete(clip: Clip): Flow<Result<List<Clip>>>
    fun getAll(): Flow<Result<List<Clip>>>
}