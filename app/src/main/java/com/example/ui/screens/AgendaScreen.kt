package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.FormatQuote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.CoupleSpaceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreen(
    viewModel: CoupleSpaceViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToChat: () -> Unit,
) {
    val events by viewModel.calendarEvents.collectAsState()
    var showAddEventDialog by remember { mutableStateOf(false) }

    var selectedDayDetail by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "CoupleSpace",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFF00F0FF),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateToHome) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color(0xFF00F0FF))
                    }
                },
                actions = {
                    IconButton(onClick = { showAddEventDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Adicionar Marco", tint = Color(0xFF00F0FF))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF10131F).copy(alpha = 0.6f)
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF141828).copy(alpha = 0.9f),
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null, tint = Color(0xFFB9CACB)) },
                    label = { Text("Início", color = Color(0xFFB9CACB), fontSize = 11.sp) },
                    selected = false,
                    onClick = onNavigateToHome
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.ChatBubble, contentDescription = null, tint = Color(0xFFB9CACB)) },
                    label = { Text("Chat", color = Color(0xFFB9CACB), fontSize = 11.sp) },
                    selected = false,
                    onClick = onNavigateToChat
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Color(0xFFCABEFF)) },
                    label = { Text("Agenda", color = Color(0xFFCABEFF), fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    selected = true,
                    onClick = {}
                )
            }
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        StardustBackground {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // Page Header
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        "Marcos Afetivos",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color(0xFF00F0FF),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Onde o tempo guarda nossos sorrisos...",
                        style = MaterialTheme.typography.bodyLarge,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFFB9CACB).copy(alpha = 0.8f)
                    )
                }

                // Interactive Calendar Widget
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1F2C).copy(alpha = 0.8f)),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, Color(0xFFCABEFF).copy(alpha = 0.15f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        // Dynamic Month Header
                        val months = listOf("Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro")
                        val cal = remember { java.util.Calendar.getInstance() }
                        var currentYear by remember { mutableStateOf(cal.get(java.util.Calendar.YEAR)) }
                        var currentMonth by remember { mutableStateOf(cal.get(java.util.Calendar.MONTH)) }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = {
                                if (currentMonth == 0) {
                                    currentMonth = 11
                                    currentYear--
                                } else {
                                    currentMonth--
                                }
                            }) {
                                Icon(Icons.Outlined.ChevronLeft, contentDescription = "Anterior", tint = Color(0xFFCABEFF))
                            }
                            Text(
                                "${months[currentMonth]} $currentYear",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color(0xFFCABEFF),
                                fontWeight = FontWeight.SemiBold
                            )
                            IconButton(onClick = {
                                if (currentMonth == 11) {
                                    currentMonth = 0
                                    currentYear++
                                } else {
                                    currentMonth++
                                }
                            }) {
                                Icon(Icons.Outlined.ChevronRight, contentDescription = "Próximo", tint = Color(0xFFCABEFF))
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Weekdays headers
                        val weekdays = listOf("Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb")
                        Row(modifier = Modifier.fillMaxWidth()) {
                            weekdays.forEach { day ->
                                Text(
                                    text = day,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFFB9CACB),
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Dynamic calendar days from current month/year
                        val gridCal = remember(currentYear, currentMonth) {
                            java.util.Calendar.getInstance().apply {
                                set(java.util.Calendar.YEAR, currentYear)
                                set(java.util.Calendar.MONTH, currentMonth)
                                set(java.util.Calendar.DAY_OF_MONTH, 1)
                            }
                        }
                        val firstDayOfWeek = gridCal.get(java.util.Calendar.DAY_OF_WEEK) - 1 // 0=Sun
                        val daysInMonth = gridCal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH)

                        // Build previous month filler days
                        val prevCal = remember(currentYear, currentMonth) {
                            java.util.Calendar.getInstance().apply {
                                set(java.util.Calendar.YEAR, currentYear)
                                set(java.util.Calendar.MONTH, currentMonth)
                                set(java.util.Calendar.DAY_OF_MONTH, 1)
                                add(java.util.Calendar.MONTH, -1)
                            }
                        }
                        val daysInPrevMonth = prevCal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH)

                        val days = remember(currentYear, currentMonth) {
                            buildList {
                                // Previous month padding
                                for (i in (daysInPrevMonth - firstDayOfWeek + 1)..daysInPrevMonth) {
                                    add(i.toString() to false)
                                }
                                // Current month days
                                for (i in 1..daysInMonth) {
                                    add(i.toString() to true)
                                }
                            }
                        }

                        // Build a set of date strings that have events for quick lookup
                        val eventDays = remember(events, currentYear, currentMonth) {
                            val prefix = "$currentYear-${(currentMonth + 1).toString().padStart(2, '0')}-"
                            events.filter { it.dateStr.startsWith(prefix) }
                                .map { it.dateStr.removePrefix(prefix).toIntOrNull() }
                                .filterNotNull()
                                .toSet()
                        }

                        // Split into rows of 7 days
                        days.chunked(7).forEach { week ->
                            Row(modifier = Modifier.fillMaxWidth()) {
                                week.forEach { (day, isCurrentMonth) ->
                                    val dayNum = day.toIntOrNull() ?: 0
                                    val hasEvent = isCurrentMonth && eventDays.contains(dayNum)
                                    val eventCategory = if (hasEvent) {
                                        val dateStr = "$currentYear-${(currentMonth + 1).toString().padStart(2, '0')}-${day.padStart(2, '0')}"
                                        events.firstOrNull { it.dateStr == dateStr }?.category
                                    } else null
                                    val eventColor = when (eventCategory) {
                                        "anniversary" -> Color(0xFF00F0FF)
                                        "travel" -> Color(0xFFCABEFF)
                                        "dinner" -> Color(0xFFFFCBE6)
                                        else -> Color(0xFF00F0FF)
                                    }

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .padding(4.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(
                                                if (hasEvent) eventColor.copy(alpha = 0.15f)
                                                else Color.Transparent
                                            )
                                            .border(
                                                width = if (hasEvent) 1.dp else 0.dp,
                                                color = if (hasEvent) eventColor else Color.Transparent,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .clickable {
                                                if (isCurrentMonth) {
                                                    selectedDayDetail = "$currentYear-${(currentMonth + 1).toString().padStart(2, '0')}-${day.padStart(2, '0')}"
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                text = day,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = when {
                                                    !isCurrentMonth -> Color(0xFF3B494B)
                                                    hasEvent -> eventColor
                                                    else -> Color.White
                                                },
                                                fontWeight = if (hasEvent) FontWeight.Bold else FontWeight.Normal
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Selected date detail card
                if (selectedDayDetail != null) {
                    val filteredEvents = events.filter { it.dateStr == selectedDayDetail }
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF272936).copy(alpha = 0.8f)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Marcos em ${selectedDayDetail}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color(0xFF00F0FF)
                                )
                                IconButton(onClick = { selectedDayDetail = null }) {
                                    Icon(Icons.Default.Close, contentDescription = "Fechar", tint = Color.White)
                                }
                            }

                            if (filteredEvents.isEmpty()) {
                                Text(
                                    "Sem marcos agendados para este dia estelar.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFFB9CACB)
                                )
                            } else {
                                filteredEvents.forEach { event ->
                                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                                        Text(event.title, style = MaterialTheme.typography.titleLarge, color = Color.White)
                                        Text(event.description, style = MaterialTheme.typography.bodyLarge, color = Color(0xFFB9CACB))
                                    }
                                }
                            }
                        }
                    }
                }

                // Próximos Momentos List
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFFCABEFF))
                        Text(
                            "Próximos Momentos",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        events.forEach { event ->
                            val icon = when (event.category) {
                                "anniversary" -> Icons.Default.Favorite
                                "travel" -> Icons.Default.FlightTakeoff
                                "dinner" -> Icons.Default.Restaurant
                                else -> Icons.Default.Event
                            }
                            val themeColor = when (event.category) {
                                "anniversary" -> Color(0xFFCABEFF)
                                "travel" -> Color(0xFF00F0FF)
                                "dinner" -> Color(0xFFFFCBE6)
                                else -> Color.White
                            }

                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF181B27).copy(alpha = 0.8f)),
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, themeColor.copy(alpha = 0.15f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedDayDetail = event.dateStr }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(themeColor.copy(alpha = 0.1f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(icon, contentDescription = null, tint = themeColor, modifier = Modifier.size(28.dp))
                                    }

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = event.dateStr,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = themeColor,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = event.title,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.White,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = event.description,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color(0xFFB9CACB)
                                        )
                                    }

                                    IconButton(onClick = { viewModel.deleteEvent(event.id) }) {
                                        Icon(Icons.Default.DeleteOutline, contentDescription = "Deletar", tint = Color.Gray)
                                    }
                                }
                            }
                        }
                    }
                }

                // Decorative Polaroid Section at Bottom
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E1F3)),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .width(200.dp)
                            .rotate(2f)
                            .shadow(12.dp, RoundedCornerShape(4.dp))
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val config by viewModel.spaceConfig.collectAsState()
                            val firstPhotoUrl = config?.firstPhotoUrl?.takeIf { it.isNotBlank() }
                            if (firstPhotoUrl != null) {
                                AsyncImage(
                                    model = firstPhotoUrl,
                                    contentDescription = "Couple photo",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(160.dp)
                                        .clip(RoundedCornerShape(2.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = config?.firstPhotoCaption ?: "Nossa primeira foto...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF2D303D),
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(160.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(Color(0xFF272936)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Collections, contentDescription = null, tint = Color(0xFFB9CACB).copy(0.5f), modifier = Modifier.size(48.dp))
                                }
                                Text(
                                    text = "Suas memórias aparecerão aqui",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFFB9CACB).copy(0.5f),
                                    textAlign = TextAlign.Center
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Romantic Quote
                    Column(
                        modifier = Modifier.width(280.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Outlined.FormatQuote,
                            contentDescription = "Quote",
                            tint = Color(0xFFCABEFF),
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = "\"A felicidade é o tempo que passamos juntos, colecionando memórias em cada página.\"",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White,
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center
                        )
                        Box(
                            modifier = Modifier
                                .width(64.dp)
                                .height(1.dp)
                                .background(Color(0xFFCABEFF).copy(alpha = 0.3f))
                        )
                    }
                }
            }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Modal to Add Event
    if (showAddEventDialog) {
        var eventTitle by remember { mutableStateOf("") }
        var eventDesc by remember { mutableStateOf("") }
        var eventDateStr by remember { mutableStateOf(java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())) }
        var eventCategory by remember { mutableStateOf("anniversary") }

        AlertDialog(
            onDismissRequest = { showAddEventDialog = false },
            title = { Text("Adicionar Marco Afetivo", style = MaterialTheme.typography.titleLarge, color = Color.White) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = eventTitle,
                        onValueChange = { eventTitle = it },
                        placeholder = { Text("Título do momento...", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    OutlinedTextField(
                        value = eventDesc,
                        onValueChange = { eventDesc = it },
                        placeholder = { Text("Descrição especial...", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    OutlinedTextField(
                        value = eventDateStr,
                        onValueChange = { eventDateStr = it },
                        placeholder = { Text("Data (YYYY-MM-DD)...", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Text("Categoria:", style = MaterialTheme.typography.labelSmall, color = Color(0xFF00F0FF))
                    val categories = listOf("anniversary" to "Aniversário", "travel" to "Viagem", "dinner" to "Jantar")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        categories.forEach { (cat, name) ->
                            FilterChip(
                                selected = eventCategory == cat,
                                onClick = { eventCategory = cat },
                                label = { Text(name) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF00F0FF).copy(alpha = 0.2f),
                                    selectedLabelColor = Color(0xFF00F0FF)
                                )
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (eventTitle.isNotBlank() && eventDateStr.isNotBlank()) {
                            viewModel.addEvent(eventTitle, eventDesc, eventDateStr, eventCategory)
                            showAddEventDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4719C9))
                ) {
                    Text("Adicionar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddEventDialog = false }) {
                    Text("Cancelar", color = Color.LightGray)
                }
            },
            containerColor = Color(0xFF141828)
        )
    }
}
