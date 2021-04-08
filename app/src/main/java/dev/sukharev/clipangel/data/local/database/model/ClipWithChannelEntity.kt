package dev.sukharev.clipangel.data.local.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class ClipWithChannelEntity(
        @Embedded
        val clip: ClipEntity,
        @Relation(parentColumn = "channel_id", entity = ChannelEntity::class, entityColumn = "id")
        val channel: ChannelEntity?
)
