package dev.sukharev.clipangel.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.sukharev.clipangel.data.local.database.model.ChannelEntity
import kotlinx.coroutines.flow.Flow
import org.jetbrains.annotations.NotNull


@Dao
interface ChannelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(entity: ChannelEntity)

    @Query("SELECT * FROM channel")
    suspend fun getAll(): List<ChannelEntity>

    @Query("DELETE FROM channel WHERE id=:id")
    suspend fun delete(id: String)

}