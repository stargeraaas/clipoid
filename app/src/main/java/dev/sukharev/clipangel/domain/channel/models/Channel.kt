package dev.sukharev.clipangel.domain.channel.models

import java.util.*


data class Channel(
        val id: String,
        val name: String,
        val secureKey: String,
        val createTime: Long,
        val isDeleted: Boolean = false
) {
    fun getFormattedDate(): String {
        return Date(createTime).toString()
    }
}
