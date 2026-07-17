package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class CoupleSpaceRepository(private val dao: CoupleSpaceDao) {

    val diaryNotes: Flow<List<DiaryNote>> = dao.getDiaryNotesFlow()
    val moodLogs: Flow<List<MoodLog>> = dao.getMoodLogsFlow()
    val calendarEvents: Flow<List<CalendarEvent>> = dao.getCalendarEventsFlow()
    val chatMessages: Flow<List<ChatMessage>> = dao.getChatMessagesFlow()

    suspend fun addDiaryNote(text: String, imageUrl: String?, dateText: String, tiltAngle: Float) {
        dao.insertDiaryNote(DiaryNote(text = text, imageUrl = imageUrl, dateText = dateText, tiltAngle = tiltAngle))
    }

    suspend fun addMoodLog(mood: String) {
        dao.insertMoodLog(MoodLog(mood = mood))
    }

    suspend fun addCalendarEvent(title: String, description: String, dateStr: String, category: String) {
        dao.insertCalendarEvent(CalendarEvent(title = title, description = description, dateStr = dateStr, category = category))
    }

    suspend fun deleteCalendarEvent(id: Int) {
        dao.deleteCalendarEvent(id)
    }

    suspend fun addChatMessage(sender: String, text: String, imageUrl: String?, isReceived: Boolean) {
        dao.insertChatMessage(ChatMessage(sender = sender, text = text, imageUrl = imageUrl, isReceived = isReceived))
    }

    // Populate default database content (matching the HTML designs exactly) if empty
    suspend fun seedDatabaseIfEmpty() {
        // Seed chat messages if empty
        val messages = chatMessages.first()
        if (messages.isEmpty()) {
            addChatMessage(
                sender = "Meu Bem",
                text = "Oi vida, estava pensando na gente e naquele café que tomamos ontem. Foi tão especial...",
                imageUrl = null,
                isReceived = true
            )
            addChatMessage(
                sender = "Você",
                text = "Eu também! Sinto que cada momento com você é como um capítulo lindo do nosso diário. 🌌",
                imageUrl = null,
                isReceived = false
            )
            addChatMessage(
                sender = "Meu Bem",
                text = "Olha o que achei entre as páginas do meu livro!",
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBOD4vc3hwAZNPkPBwYACZ9guZHn4gZD1zy7rDk4UcV7ODDO7yD_LIDdLIbwKCia1HUTHU1JKgqhoOKdMS2OYT0cAgXW6pDTxfA4WZ1IenqfbzaPP6QWZCmSeJvyz4tU0OlXS-k86eejCXMgMGEVd-_5r8dXubUR-RZe-5Zh94nflOEmL_MYJzG_KmgANdRxMUUwS4iAt11lmw2ZeEUAYwtJWhb1BK9f6Upot_ctC-4DPwgE6Rm4cH9Ig",
                isReceived = true
            )
            addChatMessage(
                sender = "Você",
                text = "Que coisa mais linda! Você sempre encontra poesia em tudo, até nas pequenas estrelas do dia a dia.",
                imageUrl = null,
                isReceived = false
            )
        }

        // Seed diary notes if empty
        val notes = diaryNotes.first()
        if (notes.isEmpty()) {
            // Note 1: Last memory
            addDiaryNote(
                text = "\"Aquele café da manhã que parecia durar uma eternidade...\"",
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCNURmHCTXzHaYWIrnI_dindiIPyOJckQEAy3cMNXwVexUsimf2inNFZWn4cCOXTM_GkXE-1mm3IQXijfSUO_ZIqK-vzZFLuATju1OV2v3dNoAl8xVi2zQTBKD8TJdD8DgxDymfcIPOO2tJ2_ZwPY7H2tzTob1Y62riznsu6lYNA1YBMsVrRYHHtDkzqZb9qqLxmxt7k44_XyAeqcypH7K8FN441XuFzmKRaQaGtTWQoF6OLMa3qkaloQ",
                dateText = "Ontem",
                tiltAngle = -1.5f
            )
            // Note 2: Photo grid item 1 (Wildflowers)
            addDiaryNote(
                text = "Flores silvestres recolhidas no caminho",
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuASnfAdKSys1UecHsW5zDUs8ZVMMAXBRH6toAifX23ffLgbf2qQaNLMrlSUhgYy7KJnZfRw-akpXw7nEl7L49sTW74rDbBKz9nOiD3Komt_gBGzMEqX5k05RhNtUkWFoWRuFhkYVubYfqvvWQQk3Uw1AOXewwlhRFOFSUPDxYfj-NxsE1t_RNMAuyW6S7HgkEpyGKyyNfpEdQIt5HMTzkUXVf4C2g4OGkA0ZxQDMeDYRrGAaFT9tPRwag",
                dateText = "Outubro",
                tiltAngle = 2f
            )
            // Note 3: Photo grid item 2 (Candle and books)
            addDiaryNote(
                text = "Leitura e luz quente nas noites frias",
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBf2QQCQ8Q92ujvuNX2q6rLwK2kDvxTFQkP9ElPLYSEkAgbACMmAd84j9s1fGMdKWfRFCJNLpbZOy7DCzDmRzuSuiONRjuFGA3-b2zwsY8iGYbFuFnWYAuX-Qm2qJteCLCCDLcwRg8A4jt81UxtDqOnEjOkWcdNEezr0xZKmzqlLqszQjSSPa-QG5_yudkkWPF4qhVrpaQhqChnPm8xmrPWNRd9oVUDs9YY7PYqyEGqWBYQnMjIPHYztQ",
                dateText = "Setembro",
                tiltAngle = -3f
            )
            // Note 4: Photo grid item 3 (Sunset)
            addDiaryNote(
                text = "Pôr do sol dourado através das cortinas",
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBb6p7t2gu-a-iXp9oNiTw05uuSlBSQJn6Hw1Kav-SbGw7VVV0YQ_5kyh4nVmzHsbCmCbpl53TIsm-SRnYNKQW5X3djDlYOCoYXpZn04djuie2nhGqHr0jq8UKGbylAnmbmYPfsK4mp79G3g1vGxtD0EXCXo9udsFWEewRcjlBVL7N_uAEmgpEw2QAb_h-j1UfTozdY1jppDG_Nj6Xs-9xG5C63BBttsjYHVMdjuuHhtazIi-BSbmbl4g",
                dateText = "Agosto",
                tiltAngle = 1.5f
            )
        }

        // Seed calendar events if empty
        val events = calendarEvents.first()
        if (events.isEmpty()) {
            addCalendarEvent(
                title = "Nosso Aniversário",
                description = "Preparar aquela surpresa especial ✨",
                dateStr = "2024-11-04",
                category = "anniversary"
            )
            addCalendarEvent(
                title = "Viagem Planejada",
                description = "Destino: Gramado - Frio e Fondue 🏔️",
                dateStr = "2024-11-15",
                category = "travel"
            )
            addCalendarEvent(
                title = "Jantar Especial",
                description = "Lugar novo que você salvou no Insta 🍽️",
                dateStr = "2024-11-22",
                category = "dinner"
            )
        }
    }
}
