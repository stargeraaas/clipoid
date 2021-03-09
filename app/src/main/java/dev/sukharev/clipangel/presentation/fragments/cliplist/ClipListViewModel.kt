package dev.sukharev.clipangel.presentation.fragments.cliplist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.sukharev.clipangel.data.local.repository.channel.ChannelRepository
import dev.sukharev.clipangel.data.local.repository.clip.ClipRepository
import dev.sukharev.clipangel.domain.channel.models.Channel
import dev.sukharev.clipangel.domain.models.asSuccess
import dev.sukharev.clipangel.domain.models.isSuccess
import dev.sukharev.clipangel.utils.copyInClipboardWithToast
import dev.sukharev.clipangel.utils.toDateFormat1
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.zip

class ClipListViewModel(private val clipRepository: ClipRepository,
                        private val channelRepository: ChannelRepository) : ViewModel() {

    private val _clipItemsLiveData: MutableLiveData<List<ClipItemViewHolder.Model>> = MutableLiveData()
    val clipItemsLiveData: LiveData<List<ClipItemViewHolder.Model>> = _clipItemsLiveData

    private val _detailedClip: MutableLiveData<DetailedClipModel> = MutableLiveData()
    val detailedClip: LiveData<DetailedClipModel> = _detailedClip

    private val _copyClipData: MutableLiveData<String> = MutableLiveData()
    val copyClipData: LiveData<String> = _copyClipData

    private val _onDeleteClip: MutableLiveData<Boolean> = MutableLiveData()
    val onDeleteClip: LiveData<Boolean> = _onDeleteClip

    fun loadClips() {
        CoroutineScope(Dispatchers.IO).launch {
            clipRepository.getAllWithSubscription()
                    .catch {
                        println("ERROR")
                    }
                    .collect { clips ->
                        _clipItemsLiveData.postValue(clips
                                .sortedByDescending { it.createdTime }
                                .map {
                                    ClipItemViewHolder.Model(it.id, it.data, it.isFavorite, it.getCreatedTimeWithFormat())
                                })
                    }
        }
    }

    fun getDetailedClipData(clipId: String) {
        val job = CoroutineScope(Dispatchers.IO).launch {
            clipRepository.getAll().zip(channelRepository.getAll()) { clips, channels ->
                clips.find { it.id == clipId }?.let { clip ->
                    val channelName: String = channels.find { clip.channelId == it.id }?.name ?: "<UNKNOWN>"
                    _detailedClip.postValue(DetailedClipModel(clip.id, channelName,
                            clip.createdTime.toDateFormat1(), clip.data, clip.isFavorite))
                }
            }.collect()
        }
    }

    fun copyClip(clipId: String) = CoroutineScope(Dispatchers.IO).launch {
        clipRepository.getAll().collect {
            it.find { it.id == clipId }?.apply {
                _copyClipData.postValue(data)
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun deleteClip(clipId: String) = CoroutineScope(Dispatchers.IO).launch {
        clipRepository.getAll().collect {
            it.find { it.id == clipId }?.apply {
                clipRepository.delete(this)
                        .catch { println("ERRR") }
                        .onCompletion { _onDeleteClip.postValue(true) }
                        .collect()
            }
        }
    }

    data class DetailedClipModel(
            val id: String,
            val channelName: String,
            val createDate: String,
            val data: String,
            val isFavorite: Boolean
    )


}