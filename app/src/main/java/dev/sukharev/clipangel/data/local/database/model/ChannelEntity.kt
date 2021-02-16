package dev.sukharev.clipangel.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "channel")
data class ChannelEntity(
        @PrimaryKey val id: String,
        @ColumnInfo(name = "secret") val secret: String,
        @ColumnInfo(name = "create_time") val createTime: Long
)