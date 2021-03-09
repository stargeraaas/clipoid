package dev.sukharev.clipangel.data.local.database.dao

import androidx.room.*
import dev.sukharev.clipangel.data.local.database.model.ClipEntity
import kotlinx.coroutines.flow.Flow
import org.jetbrains.annotations.NotNull

@Dao
interface ClipDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(clip: ClipEntity)

    @Query("SELECT * FROM clip")
    suspend fun getAll(): List<ClipEntity>

    @Query("SELECT * FROM clip WHERE id =:id")
    suspend fun getClipById(id: String): ClipEntity?

    @Query("SELECT * FROM clip")
    fun getAllWithSubscription(): Flow<List<ClipEntity>>

    @Delete
    suspend fun delete(clip: ClipEntity)

}