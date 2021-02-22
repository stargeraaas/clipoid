package dev.sukharev.clipangel.presentation.viewmodels.channellist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.sukharev.clipangel.domain.channel.ChannelInteractor
import dev.sukharev.clipangel.domain.channel.models.Channel
import dev.sukharev.clipangel.domain.channel.models.ChannelCredentials
import dev.sukharev.clipangel.domain.models.asFailure
import dev.sukharev.clipangel.domain.models.asSuccess
import dev.sukharev.clipangel.domain.models.isSuccess
import dev.sukharev.clipangel.presentation.fragments.ViewFragmentState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class ChannelListViewModel(private val channelInteractor: ChannelInteractor) : ViewModel() {


    private val _channelFragmentState = MutableLiveData<ViewFragmentState>()
    val viewFragmentState: LiveData<ViewFragmentState> = _channelFragmentState

    private var networkJob: Job? = null
    private var loadChannelsJob: Job? = null

    @ExperimentalCoroutinesApi
    fun loadState() {
        loadChannelsJob = CoroutineScope(Dispatchers.IO).launch {
            channelInteractor.getAllChannels().onStart {
                _channelFragmentState.postValue(ViewFragmentState.Loading())
            }.collect {
                if (it.isSuccess()) {
                    _channelFragmentState.postValue(ViewFragmentState.Content(it.asSuccess().value))
                } else {
                    _channelFragmentState.postValue(ViewFragmentState.Failure("A"))
                }
            }
        }
    }

    private var latestAction: Action? = null

    fun action(action: Action) {
        latestAction = action
        when (action) {
            is Action.DeattachChannel -> {
                networkJob = CoroutineScope(Dispatchers.IO).launch {
                    channelInteractor.deleteChannel(action.id)
                            .onStart {
//                                _channelFragmentState.postValue(ViewFragmentState.Loading())
                            }
                            .collect {
                                println()
                            }
                }

            }

            is Action.AttachChannel -> createChannel(action.credentials)

        }
    }

    @ExperimentalCoroutinesApi
    private fun createChannel(credentials: ChannelCredentials) {
        loadChannelsJob?.cancel()

        networkJob = CoroutineScope(Dispatchers.IO).launch {

            channelInteractor.createChannel(credentials).onStart {
                _channelFragmentState.postValue(ViewFragmentState.Loading())
            }.collect {
                if (it.isSuccess())
                    _channelFragmentState.postValue(ViewFragmentState.Content(it.asSuccess().value))
                else
                    _channelFragmentState.postValue(ViewFragmentState.Failure("Произошла ошибка", it.asFailure().error?.message))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        networkJob?.cancel()
        loadChannelsJob?.cancel()
    }

    fun getChannelById(id: String): Flow<Channel> = flow {
        channelInteractor.getAllChannels().collect {
            if (it.isSuccess())
                emit(it.asSuccess().value.find { it.id == id }!!)
        }
    }

    sealed class Action {
        class AttachChannel(val credentials: ChannelCredentials) : Action()
        class DeattachChannel(val id: String) : Action()
        class LoadAllChannels() : Action()
    }

}