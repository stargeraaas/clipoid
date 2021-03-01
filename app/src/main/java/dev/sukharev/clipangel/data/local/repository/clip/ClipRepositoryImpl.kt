package dev.sukharev.clipangel.data.local.repository.clip

import dev.sukharev.clipangel.data.local.database.dao.ClipDao
import dev.sukharev.clipangel.data.local.database.model.ClipEntity
import dev.sukharev.clipangel.data.local.database.model.mapToDomain
import dev.sukharev.clipangel.data.local.database.model.mapToEntity
import dev.sukharev.clipangel.domain.clip.Clip
import dev.sukharev.clipangel.domain.models.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class ClipRepositoryImpl(private val clipDao: ClipDao): ClipRepository {

    override fun create(clip: Clip): Flow<Result<List<Clip>>> = flow {
        try {
            clipDao.create(clip.mapToEntity())
            clipDao.getAll().collect {
                emit(Result.Success.Value(it.map { it.mapToDomain() }))
            }

        } catch (e: Exception) {
            emit(Result.Failure.Error(e))
        }
    }

    override fun update(clip: Clip): Flow<Result<List<Clip>>> {
        TODO("Not yet implemented")
    }

    override fun delete(clip: Clip): Flow<Result<List<Clip>>> {
        TODO("Not yet implemented")
    }

    override fun getAll(): Flow<Result<List<Clip>>> = flow {
        clipDao.getAll().collect {
            emit(Result.Success.Value(it.map { it.mapToDomain() }))
        }
    }
}