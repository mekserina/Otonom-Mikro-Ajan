package com.otonom.mikroajan.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "events")
data class EventNode(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val packageName: String,
    val title: String?,
    val content: String?,
    val timestamp: Long,
    val priority: Int = 0,
    val recommendation: String? = null
)

@Dao
interface EventDao {
    @Query("SELECT * FROM events ORDER BY timestamp DESC")
    fun getAllEvents(): Flow<List<EventNode>>

    @Insert
    suspend fun insert(event: EventNode)
}

@Database(entities = [EventNode::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
}
