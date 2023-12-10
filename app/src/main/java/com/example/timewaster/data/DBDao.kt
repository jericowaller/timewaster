package com.example.timewaster.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DBDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: DB)

    @Update
    suspend fun update(item: DB)

    @Delete
    suspend fun delete(item: DB)

    @Query("SELECT * from DB WHERE id = :id")
    fun getDB(id: Int): Flow<DB>
}
