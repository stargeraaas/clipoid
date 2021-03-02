package dev.sukharev.clipangel.presentation.fragments.cliplist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.sukharev.clipangel.data.local.repository.clip.ClipRepository
import dev.sukharev.clipangel.domain.models.asSuccess
import dev.sukharev.clipangel.domain.models.isSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ClipListViewModel(private val clipRepository: ClipRepository) : ViewModel() {

    private val _clipItemsLiveData: MutableLiveData<List<ClipItemViewHolder.Model>> = MutableLiveData()
    val clipItemsLiveData: LiveData<List<ClipItemViewHolder.Model>> = _clipItemsLiveData

    fun loadClips() {
        CoroutineScope(Dispatchers.IO).launch {
            clipRepository.getAll().collect {
                if (it.isSuccess()) _clipItemsLiveData.postValue(it.asSuccess().value
                        .sortedByDescending { it.createdTime }
                        .map {
                            ClipItemViewHolder.Model(it.id, it.data, it.isFavorite, it.getCreatedTimeWithFormat())
                        })
            }
        }
    }

}