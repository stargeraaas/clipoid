package dev.sukharev.clipangel.data.local.repository



data class Channel(
        val id: String,
        val secureKey: String,
        val createTime: Long,
        val isDeleted: Boolean = false
)
