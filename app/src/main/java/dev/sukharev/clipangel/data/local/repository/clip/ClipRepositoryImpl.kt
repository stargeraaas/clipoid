package dev.sukharev.clipangel.data.local.repository.clip

import android.content.SharedPreferences
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import dev.sukharev.clipangel.core.App
import dev.sukharev.clipangel.data.local.database.dao.ClipDao
import dev.sukharev.clipangel.data.local.database.model.mapToDomain
import dev.sukharev.clipangel.data.local.database.model.mapToEntity
import dev.sukharev.clipangel.data.local.repository.channel.ChannelRepository
import dev.sukharev.clipangel.domain.clip.Clip
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class ClipRepositoryImpl(private val clipDao: ClipDao,
                         private val channelRepository: ChannelRepository) : ClipRepository {

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.app)

    override fun create(clip: Clip): Flow<Clip> = flow {
        clipDao.insert(clip.mapToEntity())
        deleteClips()
        emit(clip)
    }.flowOn(Dispatchers.IO)

    private suspend fun deleteClips() {
        val notFavoriteClips = clipDao.getAll().sortedByDescending { it.createdDate }
                .filter { !it.isFavorite }

        val maxClipsCount: Int = preferences.getString("max_count_clip_stored", "0")!!.toInt()
        if (maxClipsCount > 0 && notFavoriteClips.isNotEmpty() &&
                notFavoriteClips.size > maxClipsCount) {
            clipDao.delete(notFavoriteClips.last())
            deleteClips()
        }
    }

    override fun update(clip: Clip): Flow<Clip> = flow {
        clipDao.insert(clip.mapToEntity())
        emit(clip)
    }.flowOn(Dispatchers.IO)

    override fun delete(clip: Clip): Flow<Clip> = flow {
        clipDao.delete(clip.mapToEntity())
        emit(clip)
    }.flowOn(Dispatchers.IO)

    override fun getClipById(id: String): Flow<Clip> = flow {
        val clip = clipDao.getClipById(id) ?: throw NullPointerException()
        emit(clip.mapToDomain())
    }

    override fun getAll(): Flow<List<Clip>> = flow {
        emit(clipDao.getAll().map { entity -> entity.mapToDomain() })
    }.flowOn(Dispatchers.IO)

    override fun getAllWithSubscription(): Flow<List<Clip>> {
        return clipDao.getAllWithSubscription().map { entities ->
            entities.map { entity ->
                entity.mapToDomain()
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun protectClip(clipId: String): Flow<Clip> = flow {
        clipDao.getClipById(clipId)?.let { entity ->
            entity.isProtected = !entity.isProtected
            clipDao.insert(entity)
            emit(entity.mapToDomain())
        }
    }.flowOn(Dispatchers.IO)
}