package dev.sukharev.clipangel.domain.clip

import dev.sukharev.clipangel.domain.channel.models.Channel

data class Clip(
        val id: String,
        val data: String,
        val createDate: Long,
        var channelId: String
) {
    fun getDateWithFormat(): String {
        return ""
    }
}
