package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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

    var showAddNoteDialog by remember { mutableStateOf(false) }

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
                    // Drawer Header
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, Color(0xFF00F0FF).copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                            .background(Color(0xFF00F0FF).copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = "https://lh3.googleusercontent.com/aida-public/AB6AXuDxwg-GLB8AhUEPZwbusbW6eNQn3tND1WSmx7cMARSNW47fTM71m7Pm83cKSL5--PHszACgG8K14MFZf4Y-AIunKyoo5pXcuzDfz1gVK3nQQyS4v7WZd-IooSdN2xgoRnQDylsQgcajnZGZwPS9W1LVgN3y96Hd3plwG6-BCBzw4lEpTaFMG9r58fPDgX7IGgv-Ro3e5Twyo5d8ErDMi_FqUvAF1AsTjgkpb_dgQmzySCPCDSJ45IBm20Tv6cVk1Cwd4uw",
                            contentDescription = "Logo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
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
                            Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFFCABEFF), modifier = Modifier.size(28.dp))
                            Column {
                                Text(loggedUser, style = MaterialTheme.typography.bodyLarge, color = Color.White, fontWeight = FontWeight.SemiBold)
                                Text("Exploradora", style = MaterialTheme.typography.bodyMedium, color = Color(0xFFB9CACB))
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

                        // Reusable Music Player Card
                        var isPlaying by remember { mutableStateOf(false) }
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF272936).copy(alpha = 0.6f)),
                            shape = RoundedCornerShape(24.dp),
                            border = BorderStroke(1.dp, Color(0xFFCABEFF).copy(alpha = 0.1f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
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
                                        .background(Color(0xFF00F0FF), RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Outlined.MusicNote,
                                        contentDescription = null,
                                        tint = Color(0xFF00363A),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Nossa Música", style = MaterialTheme.typography.labelSmall, color = Color.White)
                                    Text(
                                        "Lembranças de Outono",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color(0xFF00F0FF),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                IconButton(onClick = { isPlaying = !isPlaying }) {
                                    Icon(
                                        imageVector = if (isPlaying) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                                        contentDescription = "Tocar",
                                        tint = Color(0xFF00F0FF),
                                        modifier = Modifier.size(40.dp)
                                    )
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
        var selectedImageUrl by remember { mutableStateOf<String?>(null) }

        // Custom list of direct image links from HTML so they can be selected easily!
        val predefinedImages = listOf(
            "https://lh3.googleusercontent.com/aida-public/AB6AXuCNURmHCTXzHaYWIrnI_dindiIPyOJckQEAy3cMNXwVexUsimf2inNFZWn4cCOXTM_GkXE-1mm3IQXijfSUO_ZIqK-vzZFLuATju1OV2v3dNoAl8xVi2zQTBKD8TJdD8DgxDymfcIPOO2tJ2_ZwPY7H2tzTob1Y62riznsu6lYNA1YBMsVrRYHHtDkzqZb9qqLxmxt7k44_XyAeqcypH7K8FN441XuFzmKRaQaGtTWQoF6OLMa3qkaloQ" to "Café",
            "https://lh3.googleusercontent.com/aida-public/AB6AXuASnfAdKSys1UecHsW5zDUs8ZVMMAXBRH6toAifX23ffLgbf2qQaNLMrlSUhgYy7KJnZfRw-akpXw7nEl7L49sTW74rDbBKz9nOiD3Komt_gBGzMEqX5k05RhNtUkWFoWRuFhkYVubYfqvvWQQk3Uw1AOXewwlhRFOFSUPDxYfj-NxsE1t_RNMAuyW6S7HgkEpyGKyyNfpEdQIt5HMTzkUXVf4C2g4OGkA0ZxQDMeDYRrGAaFT9tPRwag" to "Flores",
            "https://lh3.googleusercontent.com/aida-public/AB6AXuBf2QQCQ8Q92ujvuNX2q6rLwK2kDvxTFQkP9ElPLYSEkAgbACMmAd84j9s1fGMdKWfRFCJNLpbZOy7DCzDmRzuSuiONRjuFGA3-b2zwsY8iGYbFuFnWYAuX-Qm2qJteCLCCDLcwRg8A4jt81UxtDqOnEjOkWcdNEezr0xZKmzqlLqszQjSSPa-QG5_yudkkWPF4qhVrpaQhqChnPm8xmrPWNRd9oVUDs9YY7PYqyEGqWBYQnMjIPHYztQ" to "Vela",
            "https://lh3.googleusercontent.com/aida-public/AB6AXuBb6p7t2gu-a-iXp9oNiTw05uuSlBSQJn6Hw1Kav-SbGw7VVV0YQ_5kyh4nVmzHsbCmCbpl53TIsm-SRnYNKQW5X3djDlYOCoYXpZn04djuie2nhGqHr0jq8UKGbylAnmbmYPfsK4mp79G3g1vGxtD0EXCXo9udsFWEewRcjlBVL7N_uAEmgpEw2QAb_h-j1UfTozdY1jppDG_Nj6Xs-9xG5C63BBttsjYHVMdjuuHhtazIi-BSbmbl4g" to "Sunset"
        )

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

                    // Pick image link
                    Text("Escolher Imagem Galáctica:", style = MaterialTheme.typography.labelSmall, color = Color(0xFF00F0FF))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(predefinedImages) { (url, name) ->
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(
                                        width = if (selectedImageUrl == url) 2.dp else 1.dp,
                                        color = if (selectedImageUrl == url) Color(0xFF00F0FF) else Color.Gray,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { selectedImageUrl = url }
                            ) {
                                AsyncImage(
                                    model = url,
                                    contentDescription = name,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.Black.copy(0.4f))
                                        .align(Alignment.BottomCenter)
                                ) {
                                    Text(
                                        name,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White,
                                        fontSize = 8.sp,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (noteText.isNotBlank()) {
                            viewModel.addNote(noteText, selectedImageUrl)
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
}
