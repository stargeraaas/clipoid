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
        @ColumnInfo(name = "delivered_date") val deliveredDate: Long,
        @ColumnInfo(name = "created_date") val createdDate: Long,
        @ColumnInfo(name = "is_favorite") var isFavorite: Boolean = false,
        @ColumnInfo(name = "is_protected") var isProtected: Boolean = false
)