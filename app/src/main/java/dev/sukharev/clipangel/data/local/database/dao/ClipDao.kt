package dev.sukharev.clipangel.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.sukharev.clipangel.data.local.database.model.ClipEntity
import kotlinx.coroutines.flow.Flow
import org.jetbrains.annotations.NotNull

@Dao
interface ClipDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(clip: ClipEntity)

    @Query("SELECT * FROM clip")
    fun getAll(): Flow<List<ClipEntity>>
}