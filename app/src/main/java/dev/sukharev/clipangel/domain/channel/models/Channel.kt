package dev.sukharev.clipangel.domain.channel.models



data class Channel(
        val id: String,
        val secureKey: String,
        val createTime: Long,
        val isDeleted: Boolean = false
)
