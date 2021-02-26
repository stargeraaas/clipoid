package dev.sukharev.clipangel.data.local.database

import androidx.room.*
import dev.sukharev.clipangel.data.local.database.dao.ChannelDao
import dev.sukharev.clipangel.data.local.database.dao.ClipDao
import dev.sukharev.clipangel.data.local.database.model.ChannelEntity
import dev.sukharev.clipangel.data.local.database.model.ClipEntity

@Database(entities = arrayOf(ChannelEntity::class, ClipEntity::class), version = 2)
abstract class ClipAngelDatabase: RoomDatabase() {
    abstract fun getChannelDao(): ChannelDao
    abstract fun getClipDao(): ClipDao
}

