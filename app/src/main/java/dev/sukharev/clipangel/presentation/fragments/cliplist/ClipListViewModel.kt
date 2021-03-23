package dev.sukharev.clipangel.presentation.fragments.cliplist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.sukharev.clipangel.data.local.repository.channel.ChannelRepository
import dev.sukharev.clipangel.data.local.repository.clip.ClipRepository
import dev.sukharev.clipangel.domain.channel.models.Channel
import dev.sukharev.clipangel.domain.clip.Clip
import dev.sukharev.clipangel.presentation.models.Category
import dev.sukharev.clipangel.utils.toDateFormat1
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

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

    val categoryTypeLiveData = MutableLiveData<Category>(Category.All())

    private val _errorLiveData = MutableLiveData<Throwable>(null)
    val errorLiveData: LiveData<Throwable> = _errorLiveData

    private val allClips = mutableListOf<Clip>()

    val permitClipAccessLiveData = MutableLiveData<String>()

   val clipAction = MutableLiveData<ClipAction>(null)

    private val channelList = mutableSetOf<Channel>()

    fun loadClips() {
        CoroutineScope(Dispatchers.IO).launch {
            channelRepository.getAll()
                    .catch { e -> _errorLiveData.postValue(e) }
                    .collect {
                        channelList.addAll(it.toSet())
                    }

            clipRepository.getAllWithSubscription()
                    .catch { e -> _errorLiveData.postValue(e) }
                    .collect { clips ->
                            allClips.clear()
                            allClips.addAll(clips)
                            changeCategoryType(categoryTypeLiveData.value!!)
                    }
        }
    }


    private fun getDetailedClipData(clipId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            clipRepository.getAll().zip(channelRepository.getAll()) { clips, channels ->
                clips.find { it.id == clipId }?.let { clip ->
                    val channelName: String = channels.find { clip.channelId == it.id }?.name
                            ?: "<UNKNOWN>"
                        _detailedClip.postValue(
                                DetailedClipModel(clip.id, channelName,
                                        clip.createdTime.toDateFormat1(), clip.data,
                                        clip.isFavorite, clip.isProtected)
                        )
                }
            }.collect()
        }
    }

    fun changeCategoryType(type: Category) {
        GlobalScope.launch(Dispatchers.Unconfined) {
            val filteredClipList = when (type) {
                is Category.All -> {
                    allClips
                            .sortedByDescending { it.createdTime }
                }
                is Category.Favorite -> {
                    allClips
                            .sortedByDescending { it.createdTime }
                            .filter { it.isFavorite }
                }
                is Category.Private -> {
                    allClips
                            .sortedByDescending { it.createdTime }
                            .filter { it.isProtected }
                }
            }

            _clipItemsLiveData.postValue(filteredClipList.map {
                ClipItemViewHolder.Model(it.id, it.data, it.isFavorite, it.isProtected,
                        it.getCreatedTimeWithFormat(), getChannelNameById(it.channelId))
            })

            categoryTypeLiveData.postValue(type)
        }
    }

    private fun getChannelNameById(id: String) = channelList.find { it.id == id }?.name ?: "Undefined"

    private fun filterClipsByCategory(category: Category) {
        when (category) {
            is Category.All -> {
            }
            is Category.Favorite -> {
            }
            is Category.Private -> {
            }
        }
    }

    fun copyClip(clipId: String) = CoroutineScope(Dispatchers.IO).launch {
        clipRepository.getAll()
                .catch { e -> _errorLiveData.postValue(e) }
                .collect {
            it.find { it.id == clipId }?.apply {
                _copyClipData.postValue(data)
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun markAsFavorite(clipId: String) = CoroutineScope(Dispatchers.IO).launch {
        clipRepository.getClipById(clipId)
                .catch { e -> _errorLiveData.postValue(e) }
                .collect {
                    clipRepository.update(it.apply {
                        it.isFavorite = !it.isFavorite
                    }).catch { e -> _errorLiveData.postValue(e) }
                            .collect {
                                getDetailedClipData(it.id)
                            }
                }
    }

    @ExperimentalCoroutinesApi
    fun deleteClip(clipId: String) = CoroutineScope(Dispatchers.IO).launch {
        clipRepository.getClipById(clipId).collect { clip ->
            clipRepository.delete(clip)
                    .catch { e -> _errorLiveData.postValue(e) }
                    .onCompletion { _onDeleteClip.postValue(true) }
                    .collect()
        }
    }

    fun protectClip(clipId: String) {
        GlobalScope.launch {
            clipRepository.protectClip(clipId)
                    .catch { e -> e.printStackTrace() }
                    .collect {
                        getDetailedClipData(it.id)
                    }
        }
    }

    fun createClipAction(value: ClipAction?) {
        clipAction.value = value
        val clip: Clip? = allClips.find { it.id == value?.clipId }

        clip?.let {
            if (it.isProtected && value?.isPermit != true) {
                permitClipAccessLiveData.value = it.id
                return@let
            }

            when(value) {
                is ClipAction.ShowDetail -> getDetailedClipData(value.clipId)
                is ClipAction.Copy -> copyClip(value.clipId)
            }
        }
    }

    sealed class ClipAction(val clipId: String, val isPermit: Boolean) {
        class ShowDetail(clipId: String, isPermit: Boolean): ClipAction(clipId, isPermit)
        class Copy(clipId: String, isPermit: Boolean): ClipAction(clipId, isPermit)
    }

    data class DetailedClipModel(
            val id: String,
            val channelName: String,
            val createDate: String,
            val data: String,
            val isFavorite: Boolean,
            val isProtected: Boolean
    )

    sealed class DetailedClip {
        class Clip(val model: DetailedClipModel): DetailedClip()
        class Protect(val clipId: String): DetailedClip()
        class Error(val throwable: Throwable): DetailedClip()
    }

}