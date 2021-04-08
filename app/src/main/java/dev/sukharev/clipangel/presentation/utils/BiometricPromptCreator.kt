package dev.sukharev.clipangel.presentation.utils

import android.content.Context
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.presentation.viewmodels.channellist.MainViewModel

class BiometricPromptCreator {

    companion object {
        fun create(fragmentActivity: FragmentActivity,
                   mainViewModel: MainViewModel): BiometricPrompt = BiometricPrompt(fragmentActivity,
                ContextCompat.getMainExecutor(fragmentActivity),
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(
                            result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        mainViewModel.accessAllowed()
                    }
                })

        fun createPromptInfo(context: Context, accessType: MainViewModel.PermitAccess) = when(accessType) {
            is MainViewModel.PermitAccess.Category -> BiometricPrompt.PromptInfo.Builder()
                    .setTitle(context.getString(R.string.unblock_clip))
                    .setNegativeButtonText(context.getString(R.string.cancel))
                    .build()

            is MainViewModel.PermitAccess.Clip -> BiometricPrompt.PromptInfo.Builder()
                    .setTitle(context.getString(R.string.unblock_private))
                    .setNegativeButtonText(context.getString(R.string.cancel))
                    .build()
        }

    }
}