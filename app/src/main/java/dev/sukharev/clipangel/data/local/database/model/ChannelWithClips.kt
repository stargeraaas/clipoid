package dev.sukharev.clipangel.data.local.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class ChannelWithClips(
        @Embedded val channelEntity: ChannelEntity,
        @Relation(parentColumn = "id", entity = ClipEntity::class, entityColumn = "channel_id")
        val clips: List<ClipEntity>
)
