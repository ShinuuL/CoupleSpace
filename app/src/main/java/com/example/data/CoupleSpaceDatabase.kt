package com.example.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

// Entities
@Entity(tableName = "diary_notes")
data class DiaryNote(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val imageUrl: String? = null,
    val dateText: String,
    val timestamp: Long = System.currentTimeMillis(),
    val tiltAngle: Float = 0f
)

@Entity(tableName = "mood_logs")
data class MoodLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mood: String, // e.g. "Radiante", "Calmo", "Melancólico", "Feliz"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "calendar_events")
data class CalendarEvent(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val dateStr: String, // format "YYYY-MM-DD" e.g., "2024-11-04"
    val category: String, // e.g., "anniversary", "travel", "dinner"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sender: String, // "Meu Bem" or "Você"
    val text: String,
    val imageUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isReceived: Boolean
)

// DAOs
@Dao
interface CoupleSpaceDao {
    // Diary notes
    @Query("SELECT * FROM diary_notes ORDER BY timestamp DESC")
    fun getDiaryNotesFlow(): Flow<List<DiaryNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiaryNote(note: DiaryNote)

    // Mood logs
    @Query("SELECT * FROM mood_logs ORDER BY timestamp DESC")
    fun getMoodLogsFlow(): Flow<List<MoodLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoodLog(log: MoodLog)

    // Calendar events
    @Query("SELECT * FROM calendar_events ORDER BY dateStr ASC")
    fun getCalendarEventsFlow(): Flow<List<CalendarEvent>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalendarEvent(event: CalendarEvent)

    @Query("DELETE FROM calendar_events WHERE id = :id")
    suspend fun deleteCalendarEvent(id: Int)

    // Chat messages
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getChatMessagesFlow(): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessage)
}

// Database
@Database(
    entities = [DiaryNote::class, MoodLog::class, CalendarEvent::class, ChatMessage::class],
    version = 1,
    exportSchema = false
)
abstract class CoupleSpaceDatabase : RoomDatabase() {
    abstract fun dao(): CoupleSpaceDao
}
