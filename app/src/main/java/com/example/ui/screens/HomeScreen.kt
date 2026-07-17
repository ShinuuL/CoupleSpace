package com.example.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.DiaryNote
import com.example.ui.CoupleSpaceViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: CoupleSpaceViewModel,
    onNavigateToAgenda: () -> Unit,
    onNavigateToChat: () -> Unit,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val notes by viewModel.diaryNotes.collectAsState()
    val loggedUser by viewModel.username.collectAsState()
    val config by viewModel.spaceConfig.collectAsState()

    var showAddNoteDialog by remember { mutableStateOf(false) }
    var showMusicConfigDialog by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color(0xFF141828).copy(alpha = 0.95f),
                modifier = Modifier.width(280.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(24.dp)
                ) {
                    // Drawer Header - user avatar or initials
                    val avatarUrl = if (loggedUser == config?.user1Name) config?.user1Avatar else config?.user2Avatar
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, Color(0xFF00F0FF).copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                            .background(Color(0xFF00F0FF).copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (avatarUrl != null) {
                            AsyncImage(
                                model = avatarUrl,
                                contentDescription = "Avatar",
                                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(Icons.Default.Person, contentDescription = "Avatar", tint = Color(0xFF00F0FF), modifier = Modifier.size(40.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Meu Diário",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "CoupleSpace Estelar",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFB9CACB)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Online indicator
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color(0xFF00F0FF), CircleShape)
                        )
                        Text(
                            text = "ONLINE",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF00F0FF),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Menu Options
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = null, tint = Color(0xFF00F0FF)) },
                        label = { Text("Início", color = Color.White, fontWeight = FontWeight.Bold) },
                        selected = true,
                        onClick = { scope.launch { drawerState.close() } },
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = Color.Transparent,
                            selectedContainerColor = Color(0xFF00F0FF).copy(alpha = 0.15f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.ChatBubble, contentDescription = null, tint = Color(0xFFB9CACB)) },
                        label = { Text("Chat", color = Color(0xFFB9CACB)) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            onNavigateToChat()
                        },
                        colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Color(0xFFB9CACB)) },
                        label = { Text("Agenda", color = Color(0xFFB9CACB)) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            onNavigateToAgenda()
                        },
                        colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Logged User profile
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0B0E1A).copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            if (avatarUrl != null) {
                                AsyncImage(
                                    model = avatarUrl,
                                    contentDescription = null,
                                    modifier = Modifier.size(28.dp).clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFFCABEFF), modifier = Modifier.size(28.dp))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(loggedUser, style = MaterialTheme.typography.bodyLarge, color = Color.White, fontWeight = FontWeight.SemiBold)
                                Text("Exploradora", style = MaterialTheme.typography.bodyMedium, color = Color(0xFFB9CACB))
                            }
                            IconButton(onClick = { viewModel.logout() }) {
                                Icon(Icons.Default.Logout, contentDescription = "Sair", tint = Color(0xFFCABEFF).copy(alpha = 0.6f), modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }
        }
    ) {
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
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Outlined.Menu, contentDescription = "Menu", tint = Color(0xFF00F0FF))
                        }
                    },
                    actions = {
                        IconButton(onClick = { viewModel.logout() }) {
                            Icon(Icons.Outlined.Settings, contentDescription = "Sair", tint = Color(0xFF00F0FF))
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color(0xFF10131F).copy(alpha = 0.6f)
                    )
                )
            },
            bottomBar = {
                // Bottom Bar Mockup matching mobile nav
                NavigationBar(
                    containerColor = Color(0xFF141828).copy(alpha = 0.9f),
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = null, tint = Color(0xFF00F0FF)) },
                        label = { Text("Início", color = Color(0xFF00F0FF), fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        selected = true,
                        onClick = {}
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.ChatBubble, contentDescription = null, tint = Color(0xFFB9CACB)) },
                        label = { Text("Chat", color = Color(0xFFB9CACB), fontSize = 11.sp) },
                        selected = false,
                        onClick = onNavigateToChat
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Color(0xFFB9CACB)) },
                        label = { Text("Agenda", color = Color(0xFFB9CACB), fontSize = 11.sp) },
                        selected = false,
                        onClick = onNavigateToAgenda
                    )
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showAddNoteDialog = true },
                    containerColor = Color(0xFF00F0FF),
                    contentColor = Color(0xFF00363A),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Nova Lembrança", modifier = Modifier.size(28.dp))
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

                    // Hero Intro
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Nosso Cantinho",
                            style = MaterialTheme.typography.headlineLarge,
                            color = Color(0xFF00F0FF),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Um lugar seguro para guardar memórias, segredos e o carinho que nos une sob o mesmo céu.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFFB9CACB),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }

                    // Bento Grid Card 1: Last Memory (Dynamic Room load)
                    val lastMemory = notes.firstOrNull()
                    if (lastMemory != null) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF181B27).copy(alpha = 0.8f)),
                            shape = RoundedCornerShape(24.dp),
                            border = BorderStroke(1.dp, Color(0xFFCABEFF).copy(alpha = 0.15f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(Icons.Outlined.History, contentDescription = null, tint = Color(0xFF00F0FF))
                                        Text(
                                            text = "Última Lembrança",
                                            style = MaterialTheme.typography.titleLarge,
                                            color = Color.White,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }

                                    // Badge
                                    Box(
                                        modifier = Modifier
                                            .background(Color(0xFF00F0FF), RoundedCornerShape(12.dp))
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "Recente",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color(0xFF00363A),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Rotated Polaroid Frame
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f)
                                        .rotate(lastMemory.tiltAngle)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFF313442))
                                        .border(4.dp, Color(0xFF272936), RoundedCornerShape(16.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (lastMemory.imageUrl != null) {
                                        AsyncImage(
                                            model = lastMemory.imageUrl,
                                            contentDescription = "Polaroid image",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        Box(
                                            modifier = Modifier.fillMaxSize().background(Color(0xFF1C1F2C)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.Image, contentDescription = null, tint = Color.White.copy(0.3f), modifier = Modifier.size(48.dp))
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = lastMemory.text,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontStyle = FontStyle.Italic,
                                    color = Color(0xFFB9CACB),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    // Card 2: Interactive Mood Sentiment
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1F2C).copy(alpha = 0.8f)),
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(1.dp, Color(0xFFCABEFF).copy(alpha = 0.1f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(Color(0xFF00F0FF).copy(alpha = 0.05f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Favorite,
                                    contentDescription = "Sentiment Indicator",
                                    tint = Color(0xFF00F0FF),
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Como você está hoje?",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Interactive Hearts
                            val moods = listOf(
                                "Radiante" to Color(0xFFFFD639),
                                "Calmo" to Color(0xFF00F0FF),
                                "Melancólico" to Color(0xFFCABEFF),
                                "Feliz" to Color(0xFFFFCBE6)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                moods.forEach { (moodName, moodColor) ->
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.clickable {
                                            viewModel.logSentiment(moodName)
                                        }
                                    ) {
                                        Icon(
                                            Icons.Default.Favorite,
                                            contentDescription = moodName,
                                            tint = moodColor,
                                            modifier = Modifier.size(32.dp)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(moodName, style = MaterialTheme.typography.labelSmall, color = Color(0xFFB9CACB))
                                    }
                                }
                            }

                            val latestMoodLog by viewModel.moodLogs.collectAsState()
                            val currentMood = latestMoodLog.firstOrNull()
                            if (currentMood != null) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Seu último registro: ${currentMood.mood} ✨",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFFCABEFF)
                                )
                            }
                        }
                    }

                    // Quick Interaction: New note card
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF181B27).copy(alpha = 0.8f))
                            .border(1.dp, Color(0xFFCABEFF).copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                            .clickable { showAddNoteDialog = true }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF272936)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Outlined.EditNote, contentDescription = null, tint = Color(0xFF00F0FF))
                            }
                            Column {
                                Text("Nova nota", style = MaterialTheme.typography.labelSmall, color = Color(0xFF00F0FF), fontWeight = FontWeight.Bold)
                                Text("Escrever algo doce...", style = MaterialTheme.typography.bodyMedium, color = Color(0xFFB9CACB))
                            }
                        }
                        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color(0xFF00F0FF))
                    }

                    // Journey Timeline Section
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Title
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.weight(1f).height(1.dp).background(Color(0xFFCABEFF).copy(alpha = 0.15f)))
                            Text(
                                "NOSSA JORNADA",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFCABEFF),
                                letterSpacing = 2.sp,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                            Box(modifier = Modifier.weight(1f).height(1.dp).background(Color(0xFFCABEFF).copy(alpha = 0.15f)))
                        }

// Music Player Card - custom audio or Spotify from space_config
                        val customAudioUrl = config?.customAudioUrl
                        val customAudioName = config?.customAudioName
                        val spotifyUrl = config?.spotifyUrl
                        val musicTitle = when {
                            !customAudioName.isNullOrBlank() -> customAudioName
                            !customAudioUrl.isNullOrBlank() -> "Áudio Personalizado"
                            !spotifyUrl.isNullOrBlank() -> "Spotify"
                            else -> null
                        }
                        val musicUrl = when {
                            !customAudioUrl.isNullOrBlank() -> customAudioUrl
                            !spotifyUrl.isNullOrBlank() -> spotifyUrl
                            else -> null
                        }
                        val context = LocalContext.current
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF272936).copy(alpha = 0.6f)),
                            shape = RoundedCornerShape(24.dp),
                            border = BorderStroke(1.dp, Color(0xFFCABEFF).copy(alpha = 0.1f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .clickable {
                                    if (musicUrl != null) {
                                        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(musicUrl))
                                        context.startActivity(intent)
                                    } else {
                                        showMusicConfigDialog = true
                                    }
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(if (musicUrl != null) Color(0xFF00F0FF) else Color(0xFFCABEFF).copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Outlined.MusicNote,
                                        contentDescription = null,
                                        tint = if (musicUrl != null) Color(0xFF00363A) else Color(0xFFCABEFF).copy(alpha = 0.4f),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Nossa Música", style = MaterialTheme.typography.labelSmall, color = Color.White)
                                    Text(
                                        musicTitle ?: "Nenhuma música configurada",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = if (musicTitle != null) Color(0xFF00F0FF) else Color(0xFFCABEFF).copy(alpha = 0.4f),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                if (musicUrl != null) {
                                    IconButton(onClick = { showMusicConfigDialog = true }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Alterar", tint = Color(0xFFCABEFF).copy(alpha = 0.5f), modifier = Modifier.size(20.dp))
                                    }
                                }
                            }
                        }

                        // Lazy Row Scrapbook photo grid loaded from Room
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        ) {
                            // Show all notes except the first one (which is featured)
                            val remainingNotes = notes.drop(1)
                            items(remainingNotes) { note ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF181B27).copy(alpha = 0.4f)),
                                    shape = RoundedCornerShape(16.dp),
                                    border = BorderStroke(1.dp, Color(0xFFCABEFF).copy(alpha = 0.1f)),
                                    modifier = Modifier
                                        .width(130.dp)
                                        .fillMaxHeight()
                                        .rotate(note.tiltAngle)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(6.dp)
                                    ) {
                                        if (note.imageUrl != null) {
                                            AsyncImage(
                                                model = note.imageUrl,
                                                contentDescription = note.text,
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clip(RoundedCornerShape(12.dp)),
                                                contentScale = ContentScale.Crop
                                            )
                                        } else {
                                            Box(
                                                modifier = Modifier.fillMaxSize().background(Color(0xFF272936)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(note.text, style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Center)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    // Modal Dialog to add a Sweet Note
    if (showAddNoteDialog) {
        var noteText by remember { mutableStateOf("") }
        var selectedImageUri by remember { mutableStateOf<android.net.Uri?>(null) }

        val galleryLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            selectedImageUri = uri
        }

        AlertDialog(
            onDismissRequest = { showAddNoteDialog = false },
            title = {
                Text(
                    "Escrever algo doce...",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = noteText,
                        onValueChange = { noteText = it },
                        placeholder = { Text("Seu carinho estelar de hoje...", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    // Galeria picker button
                    Text("Adicionar Foto:", style = MaterialTheme.typography.labelSmall, color = Color(0xFF00F0FF))
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF272936).copy(alpha = 0.5f))
                            .clickable { galleryLauncher.launch("image/*") }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.Image, contentDescription = "Galeria", tint = Color(0xFF00F0FF))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            if (selectedImageUri != null) "Foto selecionada" else "Selecionar da Galeria",
                            color = if (selectedImageUri != null) Color(0xFF00F0FF) else Color(0xFFB9CACB)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (noteText.isNotBlank()) {
                            viewModel.uploadAndAddNote(noteText, selectedImageUri)
                            showAddNoteDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4719C9))
                ) {
                    Text("Salvar no Diário", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddNoteDialog = false }) {
                    Text("Cancelar", color = Color.LightGray)
                }
            },
            containerColor = Color(0xFF141828)
        )
    }

    // Music Config Dialog
    if (showMusicConfigDialog) {
        var spotifyInput by remember { mutableStateOf(config?.spotifyUrl ?: "") }
        var customAudioInput by remember { mutableStateOf(config?.customAudioUrl ?: "") }
        var customNameInput by remember { mutableStateOf(config?.customAudioName ?: "") }

        AlertDialog(
            onDismissRequest = { showMusicConfigDialog = false },
            title = { Text("Nossa Música", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = spotifyInput,
                        onValueChange = { spotifyInput = it },
                        label = { Text("Link do Spotify", color = Color.Gray) },
                        placeholder = { Text("https://open.spotify.com/...", color = Color.DarkGray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = Color(0xFF00F0FF), unfocusedBorderColor = Color(0xFFCABEFF).copy(alpha = 0.3f)),
                        singleLine = true
                    )
                    Text("— ou —", color = Color.Gray, modifier = Modifier.align(Alignment.CenterHorizontally))
                    OutlinedTextField(
                        value = customAudioInput,
                        onValueChange = { customAudioInput = it },
                        label = { Text("Link de Áudio Personalizado", color = Color.Gray) },
                        placeholder = { Text("https://...mp3", color = Color.DarkGray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = Color(0xFF00F0FF), unfocusedBorderColor = Color(0xFFCABEFF).copy(alpha = 0.3f)),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = customNameInput,
                        onValueChange = { customNameInput = it },
                        label = { Text("Nome do Áudio", color = Color.Gray) },
                        placeholder = { Text("Ex: Nosso Som", color = Color.DarkGray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = Color(0xFF00F0FF), unfocusedBorderColor = Color(0xFFCABEFF).copy(alpha = 0.3f)),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateMusicConfig(
                            spotifyUrl = spotifyInput.ifBlank { null },
                            customAudioUrl = customAudioInput.ifBlank { null },
                            customAudioName = customNameInput.ifBlank { null }
                        )
                        showMusicConfigDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4719C9))
                ) {
                    Text("Salvar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showMusicConfigDialog = false }) {
                    Text("Cancelar", color = Color.LightGray)
                }
            },
            containerColor = Color(0xFF141828)
        )
    }
}
