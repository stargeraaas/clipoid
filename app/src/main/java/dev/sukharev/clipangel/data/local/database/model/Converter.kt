package dev.sukharev.clipangel.data.local.database.model

import dev.sukharev.clipangel.domain.clip.Clip

fun Clip.mapToEntity(): ClipEntity {
    return ClipEntity(id, channelId, data, createDate)
}

fun ClipEntity.mapToDomain(): Clip {
    return Clip(id, data, createDate, channelId)
}