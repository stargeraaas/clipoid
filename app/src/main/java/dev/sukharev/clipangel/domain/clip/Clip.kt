package dev.sukharev.clipangel.domain.clip

import dev.sukharev.clipangel.utils.toDateFormat1
import java.util.*

data class Clip(
        val id: String,
        val data: String,
        val deliveredTime: Long,
        val createdTime: Long,
        var channelId: String,
        var isFavorite: Boolean = false,
        var isProtected: Boolean = false,
) {

    companion object {
        fun create(channelId: String, data: String, createdTime: Long) = Clip(
                UUID.randomUUID().toString(),
                data,
                Date().time,
                createdTime,
                channelId,
                false,
                false
        )
    }

    fun getCreatedTimeWithFormat(): String {
        return createdTime.toDateFormat1()
    }

    fun getDeliveredTimeWithFormat(): String {
        return deliveredTime.toDateFormat1()
    }


}
