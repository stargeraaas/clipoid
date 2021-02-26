package dev.sukharev.clipangel.domain.clip

import java.util.*

data class Clip(
        val id: String,
        val data: String,
        val createDate: Long,
        var channelId: String
) {

    companion object {
        fun create(channelId: String, data: String) = Clip(
                UUID.randomUUID().toString(),
                data,
                Date().time,
                channelId
        )
    }

    fun getDateWithFormat(): String {
        return ""
    }


}
