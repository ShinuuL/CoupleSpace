package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random

class CoupleSpaceViewModel(private val repository: CoupleSpaceRepository) : ViewModel() {

    // Authentication / User state
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    // UI States observed from Database Flows
    val diaryNotes: StateFlow<List<DiaryNote>> = repository.diaryNotes
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val moodLogs: StateFlow<List<MoodLog>> = repository.moodLogs
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val calendarEvents: StateFlow<List<CalendarEvent>> = repository.calendarEvents
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val chatMessages: StateFlow<List<ChatMessage>> = repository.chatMessages
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        // Initialize with default seeds if empty
        viewModelScope.launch {
            repository.seedDatabaseIfEmpty()
        }
    }

    fun login(identity: String, secret: String): Boolean {
        if (identity.isBlank() || secret.isBlank()) {
            _loginError.value = "Identidade ou Chave Cósmica não podem ser vazias."
            return false
        }
        _username.value = identity.trim()
        _isLoggedIn.value = true
        _loginError.value = null
        return true
    }

    fun logout() {
        _isLoggedIn.value = false
        _username.value = ""
    }

    fun logSentiment(mood: String) {
        viewModelScope.launch {
            repository.addMoodLog(mood)
        }
    }

    fun addNote(text: String, imageUrl: String? = null) {
        viewModelScope.launch {
            // Pick a random tilt angle between -3.0 and 3.0 for scrapbook effect
            val angle = -3f + Random.nextFloat() * 6f
            repository.addDiaryNote(
                text = text,
                imageUrl = imageUrl,
                dateText = "Hoje",
                tiltAngle = angle
            )
        }
    }

    fun addEvent(title: String, description: String, dateStr: String, category: String) {
        viewModelScope.launch {
            repository.addCalendarEvent(
                title = title,
                description = description,
                dateStr = dateStr,
                category = category
            )
        }
    }

    fun deleteEvent(id: Int) {
        viewModelScope.launch {
            repository.deleteCalendarEvent(id)
        }
    }

    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            // Save user message
            repository.addChatMessage(
                sender = "Você",
                text = text,
                imageUrl = null,
                isReceived = false
            )

            // Simulate "Meu Bem" response delay
            delay(1500)

            // Pick a beautiful romantic phrase
            val autoReplies = listOf(
                "Você é o meu porto seguro, amor! ❤️",
                "Não vejo a hora de te ver de novo... ✨",
                "Que lindo ler isso! Você faz meus dias brilharem como o ciano estelar. 🌌",
                "Sempre guardo cada lembrança sua no meu coração.",
                "Que tal planejarmos nosso próximo jantar especial? 🍽️",
                "O céu hoje está lindo, mas não chega aos pés do seu sorriso. 🥰",
                "Amo colecionar memórias com você a cada segundo."
            )
            val replyText = autoReplies[Random.nextInt(autoReplies.size)]

            repository.addChatMessage(
                sender = "Meu Bem",
                text = replyText,
                imageUrl = null,
                isReceived = true
            )
        }
    }
}
