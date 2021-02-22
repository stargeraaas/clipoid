package dev.sukharev.clipangel.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.sukharev.clipangel.domain.channel.ChannelInteractor
import dev.sukharev.clipangel.domain.channel.models.Channel
import dev.sukharev.clipangel.domain.channel.models.ChannelCredentials
import dev.sukharev.clipangel.domain.models.asSuccess
import dev.sukharev.clipangel.domain.models.isSuccess
import dev.sukharev.clipangel.presentation.fragments.ChannelFragmentState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class ChannelListViewModel(private val channelInteractor: ChannelInteractor): ViewModel() {



    init {
        println()
    }

    private val _isLoadingLiveData = MutableLiveData<Boolean>()
    val isLoadingLiveData: LiveData<Boolean> = _isLoadingLiveData

    private val _channelListLiveData = MutableLiveData<List<Channel>>()
    val channelListLiveData: LiveData<List<Channel>> = _channelListLiveData

    private val _channelFragmentState = MutableLiveData<ChannelFragmentState>()
    val channelFragmentState: LiveData<ChannelFragmentState> = _channelFragmentState

    private var networkJob: Job? = null
    private var localJob: Job? = null

    @ExperimentalCoroutinesApi
    fun loadState() {
        localJob = CoroutineScope(Dispatchers.IO).launch {
            channelInteractor.getAllChannels().onStart {
                _channelFragmentState.postValue(ChannelFragmentState.Loading())
            }.collect {
                if (it.isSuccess()) {
                    _channelFragmentState.postValue(ChannelFragmentState.Content(it.asSuccess().value))
                } else {
                    _channelFragmentState.postValue(ChannelFragmentState.Failure("ERR"))
                }
            }
        }
    }

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
        localJob?.cancel()
    }

}