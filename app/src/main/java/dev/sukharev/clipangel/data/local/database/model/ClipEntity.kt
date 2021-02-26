package dev.sukharev.clipangel.data.local.database.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(tableName = "clip", foreignKeys = [
    ForeignKey(entity = ChannelEntity::class,
            parentColumns = ["id"],
            childColumns = ["channel_id"],
            onDelete = CASCADE)])
data class ClipEntity(
        @PrimaryKey val id: String,
        @ColumnInfo(name = "channel_id") val channelId: String,
        @ColumnInfo(name = "data") val data: String,
        @ColumnInfo(name = "create_date") val createDate: Long,
        @ColumnInfo(name = "is_favorite") val isFavorite: Boolean = false
)