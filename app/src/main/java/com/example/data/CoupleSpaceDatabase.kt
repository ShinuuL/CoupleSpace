package com.example.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpaceConfig(
    val id: Int = 1,
    val title: String = "couplespace",
    val subtitle: String? = null,
    @SerialName("user1_name") val user1Name: String = "",
    @SerialName("user2_name") val user2Name: String = "",
    @SerialName("secret_key") val secretKey: String = "",
    @SerialName("is_setup") val isSetup: Boolean? = false,
    @SerialName("user1_avatar") val user1Avatar: String? = null,
    @SerialName("user2_avatar") val user2Avatar: String? = null,
    @SerialName("spotify_url") val spotifyUrl: String? = null,
    @SerialName("first_photo_url") val firstPhotoUrl: String? = null,
    @SerialName("first_photo_caption") val firstPhotoCaption: String? = null,
    @SerialName("custom_audio_url") val customAudioUrl: String? = null,
    @SerialName("custom_audio_name") val customAudioName: String? = null,
)

@Serializable
data class Memory(
    val id: String = "",
    @SerialName("image_url") val imageUrl: String = "",
    val caption: String? = null,
    val rotation: Float? = 0f,
    @SerialName("created_at") val createdAt: String? = null,
)

@Serializable
data class Feeling(
    val user: String = "",
    val emoji: String = "",
    val text: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null,
)

@Serializable
data class CalendarEventData(
    val id: String = "",
    val title: String = "",
    val description: String? = null,
    val category: String = "",
    @SerialName("event_date") val eventDate: String = "",
    @SerialName("created_at") val createdAt: String? = null,
    val user: String = "",
)

@Serializable
data class MessageData(
    val id: String = "",
    val sender: String = "",
    val text: String? = null,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("audio_url") val audioUrl: String? = null,
    @SerialName("audio_duration") val audioDuration: Float? = null,
    @SerialName("reply_to_id") val replyToId: String? = null,
    val reactions: Map<String, kotlinx.serialization.json.JsonElement>? = null,
    @SerialName("created_at") val createdAt: String? = null,
) {
    val isReceived: Boolean get() = currentUserName.isNotEmpty() && sender != currentUserName
}

// Legacy data classes for UI backward compatibility
// Kept to minimize changes in screen composables

data class DiaryNote(
    val id: String = "",
    val text: String,
    val imageUrl: String? = null,
    val dateText: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val tiltAngle: Float = 0f
)

data class MoodLog(
    val id: String = "",
    val mood: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class CalendarEvent(
    val id: String = "",
    val title: String,
    val description: String = "",
    val dateStr: String,
    val category: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class ChatMessage(
    val id: String = "",
    val sender: String,
    val text: String,
    val imageUrl: String? = null,
    val audioUrl: String? = null,
    val audioDuration: Float? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isReceived: Boolean
)
