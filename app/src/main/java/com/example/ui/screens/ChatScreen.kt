package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.ChatMessage
import com.example.ui.CoupleSpaceViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: CoupleSpaceViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToAgenda: () -> Unit,
) {
    val messages by viewModel.chatMessages.collectAsState()
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Trigger auto scroll to bottom when new messages arrive
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Partner Circular Profile Photo
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color(0xFF00F0FF).copy(alpha = 0.4f), CircleShape)
                        ) {
                            AsyncImage(
                                model = "https://lh3.googleusercontent.com/aida-public/AB6AXuAJjlYQt-BYFjCO8wDzaJFvaxtXIjpryfzuYOWTXmMDBj5Ts7Tb6O7juNtJb7f2chVYDNIM6THO7WPM31S-CpwC_YH7LVqgGnqXTMyHNfHupwkKsIOEBnGQbgk5hRBGsgWUWNP_64bEhB4XsiHcjMf8MqdQViKQtQtNO1apDFTBNzMG4RvAbUa4UvgHf59DcvMvk2fP_wqB87g7hiM2hi4l4LZ9dtkKMGO7f9LesTWHXjuP6CdQNiOcEw",
                                contentDescription = "Meu Bem",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Column {
                            Text(
                                "Meu Bem",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(Color(0xFF00F0FF), CircleShape)
                                )
                                Text(
                                    "escrevendo...",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF00F0FF),
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateToHome) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color(0xFF00F0FF))
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.Videocam, contentDescription = "Vídeo", tint = Color(0xFFB9CACB))
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Settings, contentDescription = "Opções", tint = Color(0xFFB9CACB))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF10131F).copy(alpha = 0.8f)
                )
            )
        },
        bottomBar = {
            Column {
                // Inline Input composer bar
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF141828).copy(alpha = 0.9f)),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    border = BorderStroke(1.dp, Color(0xFF00F0FF).copy(alpha = 0.1f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.AddCircle, contentDescription = "Adicionar", tint = Color(0xFF00F0FF))
                        }
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.Image, contentDescription = "Galeria", tint = Color(0xFFB9CACB))
                        }

                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            placeholder = { Text("Envie uma mensagem estelar...", color = Color.Gray, fontSize = 14.sp) },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent
                            ),
                            singleLine = true
                        )

                        IconButton(
                            onClick = {
                                if (inputText.isNotBlank()) {
                                    viewModel.sendChatMessage(inputText)
                                    inputText = ""
                                }
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color(0xFF00F0FF)
                            )
                        ) {
                            Icon(Icons.Default.Send, contentDescription = "Enviar", tint = Color(0xFF00363A))
                        }
                    }
                }

                // Standard navigation bar
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
                        icon = { Icon(Icons.Default.ChatBubble, contentDescription = null, tint = Color(0xFF00F0FF)) },
                        label = { Text("Chat", color = Color(0xFF00F0FF), fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        selected = true,
                        onClick = {}
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Color(0xFFB9CACB)) },
                        label = { Text("Agenda", color = Color(0xFFB9CACB), fontSize = 11.sp) },
                        selected = false,
                        onClick = onNavigateToAgenda
                    )
                }
            }
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        StardustBackground {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                // Messages Scroll List
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        // Date indicator
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF272936).copy(alpha = 0.4f)),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color.White.copy(0.05f))
                            ) {
                                Text(
                                    text = "Hoje, 14 de Junho",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFFB9CACB),
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    items(messages) { message ->
                        ChatMessageBubble(message = message)
                    }
                }
            }
        }
    }
}

@Composable
fun ChatMessageBubble(message: ChatMessage) {
    var showReactionStar by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        showReactionStar = true
                        scope.launch {
                            delay(1200)
                            showReactionStar = false
                        }
                    }
                )
            },
        contentAlignment = if (message.isReceived) Alignment.CenterStart else Alignment.CenterEnd
    ) {
        Column(
            horizontalAlignment = if (message.isReceived) Alignment.Start else Alignment.End,
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            // Glassmorphic message card
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (message.isReceived) {
                        Color(0xFF313442).copy(alpha = 0.4f)
                    } else {
                        Color(0xFF00F0FF).copy(alpha = 0.15f)
                    }
                ),
                shape = RoundedCornerShape(
                    topStart = if (message.isReceived) 0.dp else 16.dp,
                    topEnd = if (message.isReceived) 16.dp else 0.dp,
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (message.isReceived) {
                        Color(0xFFB9CACB).copy(alpha = 0.1f)
                    } else {
                        Color(0xFF00F0FF).copy(alpha = 0.2f)
                    }
                )
            ) {
                Column(
                    modifier = Modifier.padding(if (message.imageUrl != null) 4.dp else 12.dp)
                ) {
                    if (message.imageUrl != null) {
                        Column(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF141828).copy(alpha = 0.5f))
                                .rotate(-1f)
                        ) {
                            AsyncImage(
                                model = message.imageUrl,
                                contentDescription = "Pressed flower photo",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = message.text,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    } else {
                        Text(
                            text = message.text,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (message.isReceived) Color.White else Color(0xFFDBFCFF)
                        )
                    }
                }
            }

            // Timestamp and checkmarks
            Row(
                modifier = Modifier.padding(top = 4.dp, start = 4.dp, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "14:25",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFFB9CACB).copy(0.6f)
                )
                if (!message.isReceived) {
                    Icon(
                        Icons.Default.DoneAll,
                        contentDescription = "Entregue",
                        tint = Color(0xFF00F0FF),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        // Pop Reaction animation
        AnimatedVisibility(
            visible = showReactionStar,
            enter = scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)) + fadeIn(),
            exit = scaleOut(animationSpec = tween(600)) + fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color(0xFF00F0FF).copy(0.2f), CircleShape)
                    .border(2.dp, Color(0xFF00F0FF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = "Love star",
                    tint = Color(0xFF00F0FF),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}
