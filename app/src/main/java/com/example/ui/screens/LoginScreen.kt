package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CoupleSpaceViewModel

@Composable
fun StardustBackground(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "stars")
    val alpha1 by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha1"
    )
    val alpha2 by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2300, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha2"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF10131F), Color(0xFF0B0E1A))
                )
            )
            .drawBehind {
                val r = java.util.Random(42) // Keep positions stable
                for (i in 0..80) {
                    val x = r.nextFloat() * size.width
                    val y = r.nextFloat() * size.height
                    val radius = 1.5f + r.nextFloat() * 2.5f
                    val a = if (i % 2 == 0) alpha1 else alpha2
                    drawCircle(
                        color = Color(0xFF00F0FF).copy(alpha = a * (0.2f + r.nextFloat() * 0.8f)),
                        radius = radius,
                        center = androidx.compose.ui.geometry.Offset(x, y)
                    )
                }
            }
    ) {
        content()
    }
}

@Composable
fun LoginScreen(viewModel: CoupleSpaceViewModel, onLoginSuccess: () -> Unit) {
    var identity by remember { mutableStateOf("Helena") }
    var password by remember { mutableStateOf("123456") }

    val loginError by viewModel.loginError.collectAsState()

    StardustBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Logo
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF141828).copy(alpha = 0.75f))
                    .border(1.dp, Color(0xFFCABEFF).copy(alpha = 0.3f), RoundedCornerShape(24.dp))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = "CoupleSpace Logo",
                    tint = Color(0xFF00F0FF),
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "CoupleSpace",
                style = MaterialTheme.typography.headlineLarge,
                color = Color(0xFF00F0FF),
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Text(
                text = "MEMÓRIAS SOB O CÉU GALÁCTICO",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFFB9CACB).copy(alpha = 0.8f),
                letterSpacing = 1.5.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Glassmorphic Card Container
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF141828).copy(alpha = 0.75f)
                ),
                shape = RoundedCornerShape(24.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFCABEFF).copy(alpha = 0.2f),
                            Color(0xFF00F0FF).copy(alpha = 0.1f)
                        )
                    )
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Seja bem-vindo(a)",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier
                            .width(64.dp)
                            .height(2.dp)
                            .background(
                                color = Color(0xFFCABEFF),
                                shape = RoundedCornerShape(1.dp)
                            )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Username field
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "IDENTIDADE",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFB9CACB),
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = identity,
                            onValueChange = { identity = it },
                            placeholder = { Text("Seu nome estelar", color = Color(0xFFB9CACB).copy(alpha = 0.5f)) },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFFCABEFF)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = Color(0xFF0B0E1A).copy(alpha = 0.5f),
                                unfocusedContainerColor = Color(0xFF0B0E1A).copy(alpha = 0.5f),
                                focusedBorderColor = Color(0xFFCABEFF),
                                unfocusedBorderColor = Color(0xFF3B494B)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password field
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "CHAVE CÓSMICA",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFB9CACB),
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            placeholder = { Text("sua senha secreta", color = Color(0xFFB9CACB).copy(alpha = 0.5f)) },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFFCABEFF)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = Color(0xFF0B0E1A).copy(alpha = 0.5f),
                                unfocusedContainerColor = Color(0xFF0B0E1A).copy(alpha = 0.5f),
                                focusedBorderColor = Color(0xFFCABEFF),
                                unfocusedBorderColor = Color(0xFF3B494B)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation()
                        )
                    }

                    if (loginError != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = loginError ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFFFA0A0)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Submit Button
                    Button(
                        onClick = {
                            val success = viewModel.login(identity, password)
                            if (success) {
                                onLoginSuccess()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4719C9)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "VIAJAR PARA O ESPAÇO",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color(0xFFCABEFF),
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.5.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Default.RocketLaunch, contentDescription = null, tint = Color(0xFF00F0FF))
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Esqueceu seu portal?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFB9CACB),
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row {
                        Text(
                            text = "Novos exploradores? ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFB9CACB)
                        )
                        Text(
                            text = "Criar conta",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFCABEFF),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
