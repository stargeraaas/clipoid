package dev.sukharev.clipangel.data.local.database.model

import dev.sukharev.clipangel.domain.clip.Clip

fun Clip.mapToEntity(): ClipEntity {
    return ClipEntity(id, channelId, data, deliveredTime, createdTime, isFavorite, isProtected)
}

fun ClipEntity.mapToDomain(): Clip {
    return Clip(id, data, createdDate, deliveredDate, channelId, isFavorite, isProtected)
}