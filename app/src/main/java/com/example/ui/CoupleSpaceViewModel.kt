package com.example.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CoupleSpaceViewModel(
    private val repository: CoupleSpaceRepository,
    private val app: Application? = null
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _secretKey = MutableStateFlow("")
    val secretKey: StateFlow<String> = _secretKey.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    val diaryNotes: StateFlow<List<DiaryNote>> = repository.diaryNotes
    val moodLogs: StateFlow<List<MoodLog>> = repository.moodLogs
    val calendarEvents: StateFlow<List<CalendarEvent>> = repository.calendarEvents
    val chatMessages: StateFlow<List<ChatMessage>> = repository.chatMessages
    val spaceConfig: StateFlow<SpaceConfig?> = repository.spaceConfig
    val partnerTyping: StateFlow<Boolean> = repository.partnerTyping

    init {
        viewModelScope.launch {
            val prefs = app?.let {
                androidx.preference.PreferenceManager.getDefaultSharedPreferences(it)
            }
            val savedName = prefs?.getString("username", "") ?: ""
            val savedKey = prefs?.getString("secret_key", "") ?: ""
            if (savedName.isNotBlank() && savedKey.isNotBlank()) {
                val success = repository.checkLocalSession(savedName, savedKey)
                if (success) {
                    _username.value = savedName
                    _secretKey.value = savedKey
                    _isLoggedIn.value = true
                }
            }
        }
    }

    fun login(identity: String, secret: String): Boolean {
        if (identity.isBlank() || secret.isBlank()) {
            _loginError.value = "Identidade ou Chave Cósmica não podem ser vazias."
            return false
        }

        viewModelScope.launch {
            val authenticated = repository.authenticate(identity, secret)
            if (authenticated) {
                _username.value = identity.trim()
                _secretKey.value = secret
                _isLoggedIn.value = true
                _loginError.value = null

                app?.let {
                    val prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(it)
                    prefs.edit()
                        .putString("username", identity.trim())
                        .putString("secret_key", secret)
                        .apply()
                }
            } else {
                _loginError.value = "Identidade ou Chave Cósmica inválida. Verifique com seu amor."
            }
        }
        return true
    }

    fun logout() {
        _isLoggedIn.value = false
        _username.value = ""
        _secretKey.value = ""
        currentUserName = ""

        app?.let {
            val prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(it)
            prefs.edit().clear().apply()
        }
    }

    fun logSentiment(mood: String) {
        viewModelScope.launch {
            repository.addMoodLog(mood)
        }
    }

    fun addNote(text: String, imageUrl: String? = null) {
        viewModelScope.launch {
            repository.addDiaryNote(text, imageUrl)
        }
    }

    fun addEvent(title: String, description: String, dateStr: String, category: String) {
        viewModelScope.launch {
            repository.addCalendarEvent(title, description, dateStr, category)
        }
    }

    fun deleteEvent(id: String) {
        viewModelScope.launch {
            repository.deleteCalendarEvent(id)
        }
    }

    fun sendChatAudio(uri: android.net.Uri) {
        viewModelScope.launch {
            try {
                val bytes = app?.contentResolver?.openInputStream(uri)?.readBytes() ?: return@launch
                val uploadedUrl = repository.uploadAudio(bytes)
                if (uploadedUrl != null) {
                    repository.addChatMessage(
                        sender = currentUserName.ifBlank { "Você" },
                        text = "",
                        isReceived = false,
                        audioUrl = uploadedUrl
                    )
                }
            } catch (_: Exception) { }
        }
    }

    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            repository.addChatMessage(
                sender = currentUserName.ifBlank { "Voc\u00EA" },
                text = text,
                imageUrl = null,
                isReceived = false
            )
        }
    }

    fun sendTyping(isTyping: Boolean) {
        viewModelScope.launch {
            repository.sendTypingStatus(currentUserName, isTyping)
        }
    }

    fun updateMusicConfig(spotifyUrl: String?, customAudioUrl: String?, customAudioName: String?) {
        viewModelScope.launch {
            repository.updateMusicConfig(spotifyUrl, customAudioUrl, customAudioName)
        }
    }

    fun uploadAndAddNote(text: String, imageUri: android.net.Uri?) {
        viewModelScope.launch {
            val imageUrl = if (imageUri != null && app != null) {
                val bytes = app.contentResolver.openInputStream(imageUri)?.readBytes()
                if (bytes != null) repository.uploadImage(bytes) else null
            } else null
            repository.addDiaryNote(text, imageUrl)
        }
    }
}
