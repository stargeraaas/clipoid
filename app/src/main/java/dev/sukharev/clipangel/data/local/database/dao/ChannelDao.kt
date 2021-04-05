package dev.sukharev.clipangel.data.local.database.dao

import androidx.room.*
import dev.sukharev.clipangel.data.local.database.model.ChannelEntity
import dev.sukharev.clipangel.data.local.database.model.ChannelWithClips

@Dao
interface ChannelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(entity: ChannelEntity)

    @Query("SELECT * FROM channel")
    suspend fun getAll(): List<ChannelEntity>

    @Query("DELETE FROM channel WHERE id=:id")
    suspend fun delete(id: String)

    @Transaction
    @Query("SELECT * FROM channel")
    fun getChannelWithClips(): List<ChannelWithClips>
}