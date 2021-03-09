package dev.sukharev.clipangel.domain.clip.create

import dev.sukharev.clipangel.data.local.repository.clip.ClipRepository
import dev.sukharev.clipangel.data.remote.repository.clip.ClipRemoteRepository
import dev.sukharev.clipangel.domain.clip.Clip
import dev.sukharev.clipangel.domain.models.Result
import dev.sukharev.clipangel.domain.models.asFailure
import dev.sukharev.clipangel.domain.models.asSuccess
import dev.sukharev.clipangel.domain.models.isSuccess
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CreateClipCaseImpl(private val repository: ClipRepository,
                         private val remoteRepository: ClipRemoteRepository) : CreateClipCase {

    @FlowPreview
    override fun create(channelId: String): Flow<Clip> = remoteRepository.get(channelId)
            .flatMapMerge {
                repository.create(it)
            }
}