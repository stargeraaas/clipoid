package dev.sukharev.clipangel.data.local.repository.credentials

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Build
import android.provider.Settings

class CredentialsClipAngel(val context: Context): Credentials {

    private val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("credentials", MODE_PRIVATE)

    private val FIREBASE_TOKEN_EXTRA = "firebase_token"

    override fun saveFirebaseToken(token: String) {
        sharedPreferences.edit().putString(FIREBASE_TOKEN_EXTRA, token).apply()
    }

    override fun getFirebaseToken(): String? = sharedPreferences.getString(FIREBASE_TOKEN_EXTRA, null)

    override fun getDeviceIdentifier(): String = "${Build.BRAND} ${Build.DEVICE} ${Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)}"
}