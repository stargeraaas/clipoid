package dev.sukharev.clipangel.domain.clip.create

import dev.sukharev.clipangel.data.local.repository.clip.ClipRepository
import dev.sukharev.clipangel.data.remote.repository.clip.ClipRemoteRepository
import dev.sukharev.clipangel.domain.clip.Clip
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

class CreateClipInteractorImpl(private val repository: ClipRepository,
                               private val remoteRepository: ClipRemoteRepository) : CreateClipInteractor {

    @FlowPreview
    override fun create(channelId: String): Flow<Clip> = remoteRepository.addClip(channelId)
            .flatMapMerge { repository.create(it) }
}