package dev.sukharev.clipangel.data.remote.repository.clip.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClipDTO(
        @SerialName("data") val data: String
)
