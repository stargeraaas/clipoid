package dev.sukharev.clipangel.domain.settings

data class AppSettings(
        val notification: NotificationSettings,
        val clipStoreSettings: ClipStoreSettings
)
