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

    private var attachChannelJob: Job? = null
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
            is Action.DeattachChannel -> deattachChannel(action.id)
            is Action.AttachChannel -> attachChannel(action.credentials)
        }
    }

    @ExperimentalCoroutinesApi
    private fun attachChannel(credentials: ChannelCredentials) {
        loadChannelsJob?.cancel()
        attachChannelJob = CoroutineScope(Dispatchers.IO).launch {
            channelInteractor.createChannel(credentials)
                    .onStart {
                        _channelFragmentState.postValue(ViewFragmentState.Loading())
                    }
                    .catch { e -> _channelFragmentState.postValue(ViewFragmentState.Failure("Произошла ошибка", e.message)) }
                    .collect { channelList ->
                        action(Action.Nothing())
                        _channelFragmentState.postValue(ViewFragmentState.Content(channelList))
                    }
        }
    }

    @ExperimentalCoroutinesApi
    private fun deattachChannel(id: String) {
        loadChannelsJob?.cancel()
        attachChannelJob = CoroutineScope(Dispatchers.IO).launch {
            channelInteractor.deleteChannel(id).onStart {
                _channelFragmentState.postValue(ViewFragmentState.Loading())
            }.collect {
                if (it.isSuccess()) {
                    action(Action.Nothing())
                    _channelFragmentState.postValue(ViewFragmentState.Content(it.asSuccess().value))
                } else _channelFragmentState.postValue(ViewFragmentState.Failure("Произошла ошибка", it.asFailure().error?.message))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        attachChannelJob?.cancel()
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
        class Nothing() : Action()
    }

}