package dev.sukharev.clipangel.domain.channel.models



data class Channel(
        val id: String,
        val name: String,
        val secureKey: String,
        val createTime: String,
        val isDeleted: Boolean = false
)
