package dev.sukharev.clipangel.data.local.repository.credentials

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Build
import android.provider.Settings
import java.util.*

class CredentialsClipAngel(val context: Context) : Credentials {

    private val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("credentials", MODE_PRIVATE)

    private val FIREBASE_TOKEN_EXTRA = "firebase_token"
    private val DEVICE_IDENTIFIER_EXTRA = "device_identifier"

    private fun createDeviceIdentifier(): String {
        val identifier = "${Build.BRAND} ${Build.DEVICE} ${UUID.randomUUID()}"
        sharedPreferences.edit().putString(DEVICE_IDENTIFIER_EXTRA, identifier).apply()
        return identifier
    }
    private fun deviceIdentifier(): String? {
        return sharedPreferences.getString(DEVICE_IDENTIFIER_EXTRA, null)
    }

    override fun saveFirebaseToken(token: String) {
        sharedPreferences.edit().putString(FIREBASE_TOKEN_EXTRA, token).apply()
    }

    override fun getFirebaseToken(): String? = sharedPreferences.getString(FIREBASE_TOKEN_EXTRA, null)

    override fun getDeviceIdentifier(): String = deviceIdentifier() ?: createDeviceIdentifier()
}