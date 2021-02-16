package dev.sukharev.clipangel.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.sukharev.clipangel.data.local.database.model.ChannelEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ChannelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(entity: ChannelEntity)

    @Query("SELECT * FROM channelentity")
    suspend fun getAll(): Flow<List<ChannelEntity>>

    @Query("DELETE FROM channelentity WHERE id=:id")
    suspend fun delete(id: String)

}