package dev.sukharev.clipangel.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.sukharev.clipangel.domain.channel.ChannelInteractor
import dev.sukharev.clipangel.domain.channel.models.Channel
import dev.sukharev.clipangel.domain.channel.models.ChannelCredentials
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ChannelListViewModel(private val channelInteractor: ChannelInteractor): ViewModel() {

    init {
        println()
    }

    private val _isLoadingLiveData = MutableLiveData<Boolean>()
    val isLoadingLiveData: LiveData<Boolean> = _isLoadingLiveData

    private var networkJob: Job? = null

    fun createChannel(credentials: ChannelCredentials) {
        networkJob = CoroutineScope(Dispatchers.IO).launch {
            channelInteractor.createChannel(credentials).onStart {
                println()
                _isLoadingLiveData.postValue(true)
            }.catch {
                _isLoadingLiveData.postValue(false)
                println()
            } .collect {
                println()
                _isLoadingLiveData.postValue(false)
            }
        }
    }

    fun getAllChannels(): Flow<List<Channel>> = flow {

    }

    override fun onCleared() {
        super.onCleared()
        networkJob?.cancel()
    }

}