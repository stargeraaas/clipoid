package dev.sukharev.clipangel.domain.clip.create

import dev.sukharev.clipangel.data.local.repository.clip.ClipRepository
import dev.sukharev.clipangel.data.remote.repository.clip.ClipRemoteRepository
import dev.sukharev.clipangel.domain.clip.Clip
import dev.sukharev.clipangel.domain.models.Result
import dev.sukharev.clipangel.domain.models.asFailure
import dev.sukharev.clipangel.domain.models.asSuccess
import dev.sukharev.clipangel.domain.models.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class CreateClipCaseImpl(private val  repository: ClipRepository,
                         private val remoteRepository: ClipRemoteRepository): CreateClipCase {

    override fun create(channelId: String): Flow<Result<Clip>> = flow {
        remoteRepository.get(channelId).collect {
            if (it.isSuccess()) {
                repository.create(it.asSuccess().value).collect {
                    if (it.isSuccess())
                        emit(Result.Success.Value(it.asSuccess().value.filter { it.channelId == channelId }.last()))
                    else emit(it.asFailure())
                }
            } else emit(it.asFailure())
        }

    }

}