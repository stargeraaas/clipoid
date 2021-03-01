package dev.sukharev.clipangel.data.remote.repository.clip

class NoClipDataInDatabaseException(val channelId: String): Exception() {

    override val message: String
        get() = "No clip data for channel $channelId"

}