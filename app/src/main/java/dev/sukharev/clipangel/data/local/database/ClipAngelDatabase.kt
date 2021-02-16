package dev.sukharev.clipangel.data.local.database

import androidx.room.*
import dev.sukharev.clipangel.data.local.database.dao.ChannelDao
import dev.sukharev.clipangel.data.local.database.model.ChannelEntity

@Database(entities = arrayOf(ChannelEntity::class), version = 1)
abstract class ClipAngelDatabase: RoomDatabase() {
    abstract fun getChannelDao(): ChannelDao
}

