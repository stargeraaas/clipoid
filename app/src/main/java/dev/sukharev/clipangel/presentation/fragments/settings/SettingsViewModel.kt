package dev.sukharev.clipangel.presentation.fragments.settings

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import dev.sukharev.clipangel.core.App
import dev.sukharev.clipangel.domain.settings.AppSettings
import dev.sukharev.clipangel.domain.settings.ClipStoreSettings
import dev.sukharev.clipangel.domain.settings.NotificationSettings

class SettingsViewModel: ViewModel() {

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.app)

    val settingsLiveData = MutableLiveData<AppSettings>()

    fun getSettings() {
        settingsLiveData.value = AppSettings(NotificationSettings(true), loadClipStoreSettings())
    }

    private fun loadClipStoreSettings(): ClipStoreSettings =
            ClipStoreSettings(preferences.getInt("MAX_CLIP_COUNT", -1))

    fun setMaxClipCount(count: Int) {
        preferences.edit().putInt("MAX_CLIP_COUNT", count).apply()
    }

}