package dev.sukharev.clipangel.presentation.viewmodels.channellist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    val accessRequest = MutableLiveData<PermitAccess>(null)
    val accessResult = MutableLiveData<PermitAccess>(null)

    val forceDetail = MutableLiveData<String>(null)


    fun allowAccess(action: PermitAccess?) {
        accessRequest.value = action
    }

    fun accessAllowed() {
        accessResult.value = accessRequest.value
    }

    sealed class PermitAccess {
        class Clip(val clipId: String): PermitAccess()
        class Category: PermitAccess()
    }

}