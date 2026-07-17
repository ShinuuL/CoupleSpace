package com.example.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.*
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlin.random.Random

var currentUserName = ""

class CoupleSpaceRepository(private val supabase: SupabaseClient) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _diaryNotes = MutableStateFlow<List<DiaryNote>>(emptyList())
    val diaryNotes: StateFlow<List<DiaryNote>> = _diaryNotes.asStateFlow()

    private val _moodLogs = MutableStateFlow<List<MoodLog>>(emptyList())
    val moodLogs: StateFlow<List<MoodLog>> = _moodLogs.asStateFlow()

    private val _calendarEvents = MutableStateFlow<List<CalendarEvent>>(emptyList())
    val calendarEvents: StateFlow<List<CalendarEvent>> = _calendarEvents.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _spaceConfig = MutableStateFlow<SpaceConfig?>(null)
    val spaceConfig: StateFlow<SpaceConfig?> = _spaceConfig.asStateFlow()

    private val _partnerTyping = MutableStateFlow(false)
    val partnerTyping: StateFlow<Boolean> = _partnerTyping.asStateFlow()

    private val offlineQueue = mutableListOf<suspend () -> Unit>()

    init {
        setupRealtimeSubscriptions()
    }

    private fun setupRealtimeSubscriptions() {
        scope.launch {
            supabase.from("memories").select().decodeList<Memory>().let { memories ->
                _diaryNotes.value = memories.map { it.toDiaryNote() }
            }
        }

        scope.launch {
            supabase.from("feelings").select().decodeList<Feeling>().let { feelings ->
                _moodLogs.value = feelings.map { it.toMoodLog() }
            }
        }

        scope.launch {
            supabase.from("events").select().decodeList<CalendarEventData>().let { events ->
                _calendarEvents.value = events.map { it.toCalendarEvent() }
            }
        }

        scope.launch {
            supabase.from("messages").select().decodeList<MessageData>().let { messages ->
                _chatMessages.value = messages.map { it.toChatMessage() }
            }
        }

        scope.launch {
            supabase.realtime.connect()
        }

        scope.launch {
            val channel = supabase.channel("schema-public-changes")
            scope.launch {
                channel.postgresChangeFlow<PostgresAction>(schema = "public") {
                    table = "messages"
                }.collect { change -> handleRealtimeChange("messages", change) }
            }
            scope.launch {
                channel.postgresChangeFlow<PostgresAction>(schema = "public") {
                    table = "memories"
                }.collect { change -> handleRealtimeChange("memories", change) }
            }
            scope.launch {
                channel.postgresChangeFlow<PostgresAction>(schema = "public") {
                    table = "feelings"
                }.collect { change -> handleRealtimeChange("feelings", change) }
            }
            scope.launch {
                channel.postgresChangeFlow<PostgresAction>(schema = "public") {
                    table = "events"
                }.collect { change -> handleRealtimeChange("events", change) }
            }
            scope.launch {
                channel.postgresChangeFlow<PostgresAction>(schema = "public") {
                    table = "typing_presence"
                }.collect { change -> handleRealtimeChange("typing_presence", change) }
            }
            channel.subscribe()
        }
    }

    private suspend fun handleRealtimeChange(table: String, change: PostgresAction) {
        try {
            when (table) {
                "messages" -> {
                    if (change is PostgresAction.Insert) {
                        val newMsg = decodeMessageFromJson(change.record)
                        if (newMsg != null && _chatMessages.value.none { it.id == newMsg.id }) {
                            _chatMessages.value = _chatMessages.value + newMsg
                        }
                    } else {
                        refreshTable(table)
                    }
                }
                "typing_presence" -> {
                    val raw = when (change) {
                        is PostgresAction.Insert, is PostgresAction.Update -> change.record
                        else -> return
                    }
                    val user = raw["user"]?.jsonPrimitive?.content
                    val isTyping = raw["is_typing"]?.jsonPrimitive?.booleanOrNull ?: false
                    if (user != null && user != currentUserName) {
                        _partnerTyping.value = isTyping
                    }
                }
                else -> refreshTable(table)
            }
        } catch (_: Exception) { }
    }

    private suspend fun refreshTable(table: String) {
        when (table) {
            "messages" -> {
                val fresh = supabase.from("messages").select().decodeList<MessageData>()
                _chatMessages.value = fresh.map { it.toChatMessage() }
            }
            "memories" -> {
                val fresh = supabase.from("memories").select().decodeList<Memory>()
                _diaryNotes.value = fresh.map { it.toDiaryNote() }
            }
            "feelings" -> {
                val fresh = supabase.from("feelings").select().decodeList<Feeling>()
                _moodLogs.value = fresh.map { it.toMoodLog() }
            }
            "events" -> {
                val fresh = supabase.from("events").select().decodeList<CalendarEventData>()
                _calendarEvents.value = fresh.map { it.toCalendarEvent() }
            }
        }
    }

    private fun decodeMessageFromJson(json: JsonObject): ChatMessage? {
        return try {
            val id = json["id"]?.jsonPrimitive?.content ?: return null
            val sender = json["sender"]?.jsonPrimitive?.content ?: return null
            val text = json["text"]?.jsonPrimitive?.content ?: ""
            val imageUrl = json["image_url"]?.jsonPrimitive?.content
            val createdAt = json["created_at"]?.jsonPrimitive?.content
            ChatMessage(
                id = id,
                sender = sender,
                text = text,
                imageUrl = if (imageUrl.isNullOrBlank()) null else imageUrl,
                isReceived = currentUserName.isNotEmpty() && sender != currentUserName,
                timestamp = parseTimestamp(createdAt)
            )
        } catch (_: Exception) { null }
    }

    private fun parseTimestamp(ts: String?): Long {
        if (ts == null) return System.currentTimeMillis()
        return try {
            java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.US).apply {
                timeZone = java.util.TimeZone.getTimeZone("UTC")
            }.parse(ts.take(19))?.time ?: System.currentTimeMillis()
        } catch (_: Exception) { System.currentTimeMillis() }
    }

    suspend fun authenticate(name: String, secretKey: String): Boolean {
        return try {
            val configs = supabase.from("space_config")
                .select { filter { eq("id", 1) } }
                .decodeList<SpaceConfig>()
            val config = configs.firstOrNull()
            if (config != null) {
                val nameValid = name == config.user1Name || name == config.user2Name
                val keyValid = secretKey == config.secretKey
                if (nameValid && keyValid) {
                    currentUserName = name
                    _spaceConfig.value = config
                    true
                } else {
                    false
                }
            } else {
                false
            }
        } catch (_: Exception) { false }
    }

    fun checkLocalSession(name: String, key: String): Boolean {
        return name.isNotBlank() && key.isNotBlank()
    }

    suspend fun addDiaryNote(text: String, imageUrl: String? = null) {
        try {
            val memory = supabase.from("memories").insert(
                mapOf(
                    "image_url" to (imageUrl ?: ""),
                    "caption" to text,
                    "rotation" to (Random.nextFloat() * 6f - 3f)
                )
            ) { select() }.decodeSingle<Memory>()
            val note = memory.toDiaryNote()
            _diaryNotes.value = _diaryNotes.value + note
        } catch (_: Exception) {
            offlineQueue.add {
                addDiaryNote(text, imageUrl)
            }
        }
    }

    suspend fun addMoodLog(mood: String) {
        try {
            val emojiMap = mapOf(
                "Radiante" to "\uD83C\uDF1F",
                "Calmo" to "\uD83D\uDCA7",
                "Melancólico" to "\uD83C\uDF19",
                "Feliz" to "\u2764\uFE0F"
            )
            supabase.from("feelings").upsert(
                mapOf(
                    "user" to currentUserName,
                    "emoji" to (emojiMap[mood] ?: mood),
                    "text" to mood
                )
            ) { onConflict = "user" }
            val fresh = supabase.from("feelings").select().decodeList<Feeling>()
            _moodLogs.value = fresh.map { it.toMoodLog() }
        } catch (_: Exception) {
            offlineQueue.add { addMoodLog(mood) }
        }
    }

    suspend fun addCalendarEvent(title: String, description: String, dateStr: String, category: String) {
        try {
            supabase.from("events").insert(
                mapOf(
                    "title" to title,
                    "description" to description,
                    "event_date" to dateStr,
                    "category" to category,
                    "user" to currentUserName
                )
            )
            val fresh = supabase.from("events").select().decodeList<CalendarEventData>()
            _calendarEvents.value = fresh.map { it.toCalendarEvent() }
        } catch (_: Exception) {
            offlineQueue.add { addCalendarEvent(title, description, dateStr, category) }
        }
    }

    suspend fun deleteCalendarEvent(localId: String) {
        try {
            supabase.from("events").delete { filter { eq("id", localId) } }
            _calendarEvents.value = _calendarEvents.value.filter { it.id != localId }
        } catch (_: Exception) { }
    }

    suspend fun addChatMessage(sender: String, text: String, imageUrl: String? = null, isReceived: Boolean, audioUrl: String? = null, audioDuration: Float? = null) {
        try {
            val msgMap = mutableMapOf<String, Any?>(
                "sender" to sender,
                "text" to text
            )
            if (imageUrl != null) msgMap["image_url"] = imageUrl
            if (audioUrl != null) msgMap["audio_url"] = audioUrl
            if (audioDuration != null) msgMap["audio_duration"] = audioDuration

            supabase.from("messages").insert(msgMap)
        } catch (_: Exception) {
            offlineQueue.add { addChatMessage(sender, text, imageUrl, isReceived, audioUrl, audioDuration) }
        }
    }

    suspend fun sendTypingStatus(user: String, isTyping: Boolean) {
        try {
            supabase.from("typing_presence").upsert(
                mapOf(
                    "user" to user,
                    "is_typing" to isTyping
                )
            )
        } catch (_: Exception) { }
    }

    suspend fun updateMusicConfig(spotifyUrl: String?, customAudioUrl: String?, customAudioName: String?) {
        try {
            val updateMap = mutableMapOf<String, Any?>()
            if (spotifyUrl != null) updateMap["spotify_url"] = spotifyUrl
            if (customAudioUrl != null) updateMap["custom_audio_url"] = customAudioUrl
            if (customAudioName != null) updateMap["custom_audio_name"] = customAudioName
            supabase.from("space_config").update(updateMap) { filter { eq("id", 1) } }
            val fresh = supabase.from("space_config").select().decodeList<SpaceConfig>()
            _spaceConfig.value = fresh.firstOrNull()
        } catch (_: Exception) { }
    }

    suspend fun uploadAudio(bytes: ByteArray): String? {
        return try {
            val fileName = "audio/${java.util.UUID.randomUUID()}.mp3"
            supabase.storage.from("memories").upload(fileName, bytes)
            supabase.storage.from("memories").publicUrl(fileName).toString()
        } catch (_: Exception) { null }
    }

    suspend fun uploadImage(bytes: ByteArray): String? {
        return try {
            val fileName = "notes/${java.util.UUID.randomUUID()}.jpg"
            supabase.storage.from("memories").upload(fileName, bytes)
            supabase.storage.from("memories").publicUrl(fileName).toString()
        } catch (_: Exception) { null }
    }

    private fun Memory.toDiaryNote() = DiaryNote(
        id = id,
        text = caption ?: "",
        imageUrl = if (imageUrl.isBlank()) null else imageUrl,
        dateText = "",
        tiltAngle = rotation ?: 0f,
        timestamp = parseTimestamp(createdAt)
    )

    private fun Feeling.toMoodLog() = MoodLog(
        id = user,
        mood = text ?: emoji,
        timestamp = parseTimestamp(updatedAt)
    )

    private fun CalendarEventData.toCalendarEvent() = CalendarEvent(
        id = id,
        title = title,
        description = description ?: "",
        dateStr = eventDate,
        category = category,
        timestamp = parseTimestamp(createdAt)
    )

    private fun MessageData.toChatMessage() = ChatMessage(
        id = id,
        sender = sender,
        text = text ?: "",
        imageUrl = imageUrl,
        audioUrl = audioUrl,
        audioDuration = audioDuration,
        isReceived = isReceived,
        timestamp = parseTimestamp(createdAt)
    )
}
