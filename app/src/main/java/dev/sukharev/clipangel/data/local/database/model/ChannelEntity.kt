package dev.sukharev.clipangel.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "channel")
data class ChannelEntity(
        @PrimaryKey val id: String,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "secret") val secret: String,
        @ColumnInfo(name = "register_time") val registerTime: Long
)