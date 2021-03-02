package dev.sukharev.clipangel.data.local.repository.credentials

interface Credentials {
    fun saveFirebaseToken(token: String)
    fun getFirebaseToken(): String?
    fun getDeviceIdentifier(): String
}