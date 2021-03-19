package dev.sukharev.clipangel.presentation.viewmodels.channellist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    private val _openProtectedClipConfirmation = MutableLiveData<String>()
    val openProtectedClipConfirmation: LiveData<String> = _openProtectedClipConfirmation

    val permitAccessForClip = MutableLiveData<String?>(null)

    fun openBiometryDialogForClip(clipId: String) {
        _openProtectedClipConfirmation.value = clipId
    }

    fun permitAccessForProtectedClip() {
        permitAccessForClip.value = _openProtectedClipConfirmation.value
    }

}