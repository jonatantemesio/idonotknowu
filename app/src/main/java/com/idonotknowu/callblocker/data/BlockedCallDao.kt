package com.idonotknowu.callblocker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BlockedCallDao {
    @Insert
    suspend fun insert(call: BlockedCall)

    @Query("SELECT * FROM blocked_calls ORDER BY timestamp DESC")
    fun observeAll(): Flow<List<BlockedCall>>
}
