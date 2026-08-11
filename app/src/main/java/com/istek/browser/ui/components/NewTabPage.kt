package com.istek.browser.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.istek.browser.data.models.ShieldStats
import com.istek.browser.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

data class QuickShortcut(
    val title: String,
    val url: String,
    val icon: String,
    val badge: String? = null
)

@Composable
fun NewTabPage(
    shieldStats: ShieldStats,
    onNavigateUrl: (String) -> Unit,
    onOpenLeoAiModal: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedEngine by remember { mutableStateOf("istek") } // "istek" or "google"

    val shortcuts = remember {
        listOf(
            QuickShortcut("Pados Game", "https://geniusvideogamer2.github.io/pados/", "🎮", "HOT"),
            QuickShortcut("YouTube", "https://www.youtube.com", "▶️"),
            QuickShortcut("OpenTube Engine", "istek://opentube", "⚡", "FAST"),
            QuickShortcut("ISTEK Rewards", "istek://rewards", "🦁", "EARN"),
            QuickShortcut("YT Metadata", "istek://yt-metadata", "📊", "TOOL"),
            QuickShortcut("Setup Installer", "istek://installer", "💿", "EXE")
        )
    }

    val currentTime = remember {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        sdf.format(Date())
    }

    val currentDate = remember {
        val sdf = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
        sdf.format(Date())
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Slate950, Slate900, Slate950)
                )
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 800.dp)
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Clock & Greeting
            Text(
                text = currentTime,
                fontSize = 56.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Text(
                text = currentDate,
                fontSize = 16.sp,
                color = Slate400,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Brand Logo & Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(IstekCyan, IstekBlue)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "ISTEK Shield Logo",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "İSTEK BROWSER",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Mersin Okulları • Private & Ultra-Fast",
                        fontSize = 12.sp,
                        color = IstekCyan,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Search Box with Engine Switcher
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Slate700, RoundedCornerShape(28.dp)),
                shape = RoundedCornerShape(28.dp),
                color = Slate900
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        // Engine Toggle Chips
                        FilterChip(
                            selected = selectedEngine == "istek",
                            onClick = { selectedEngine = "istek" },
                            label = { Text("⚡ ISTEK Search", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = IstekCyan,
                                selectedLabelColor = Color.White,
                                containerColor = Slate800,
                                labelColor = Slate400
                            ),
                            modifier = Modifier.testTag("engine_chip_istek")
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        FilterChip(
                            selected = selectedEngine == "google",
                            onClick = { selectedEngine = "google" },
                            label = { Text("🔍 Google Search", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = IstekBlue,
                                selectedLabelColor = Color.White,
                                containerColor = Slate800,
                                labelColor = Slate400
                            ),
                            modifier = Modifier.testTag("engine_chip_google")
                        )
                    }

                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(
                                if (selectedEngine == "istek") "Search with ISTEK Search or enter URL..."
                                else "Search Google or type web address...",
                                color = Slate400
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = if (selectedEngine == "istek") Icons.Default.FlashOn else Icons.Default.Search,
                                contentDescription = "Search",
                                tint = if (selectedEngine == "istek") IstekCyan else Color.White
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = {
                                    val target = if (searchQuery.startsWith("http://") || searchQuery.startsWith("https://")) {
                                        searchQuery
                                    } else if (selectedEngine == "istek") {
                                        "istek://search?q=${searchQuery}"
                                    } else {
                                        "https://www.google.com/search?q=${searchQuery}"
                                    }
                                    onNavigateUrl(target)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowForward,
                                        contentDescription = "Execute Search",
                                        tint = IstekCyan
                                    )
                                }
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Slate200
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("ntp_search_input")
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Quick Shortcuts Grid
            Text(
                text = "Top Shortcuts",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Slate400,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                items(shortcuts) { shortcut ->
                    Surface(
                        onClick = { onNavigateUrl(shortcut.url) },
                        shape = RoundedCornerShape(12.dp),
                        color = Slate900,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Slate800),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(76.dp)
                            .testTag("shortcut_${shortcut.title.lowercase().replace(" ", "_")}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = shortcut.icon,
                                fontSize = 24.sp,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = shortcut.title,
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1
                                )
                                if (shortcut.badge != null) {
                                    Text(
                                        text = shortcut.badge,
                                        color = ShieldOrange,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Shield Stats Summary Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Card 1
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Slate900),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Slate800)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = ShieldOrange,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Trackers Blocked", color = Slate400, fontSize = 11.sp)
                        }
                        Text(
                            text = "%,d".format(shieldStats.trackersBlocked),
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                // Card 2
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Slate900),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Slate800)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DataUsage,
                                contentDescription = null,
                                tint = IstekCyan,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Bandwidth Saved", color = Slate400, fontSize = 11.sp)
                        }
                        Text(
                            text = "${shieldStats.bandwidthSavedMb} MB",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                // Card 3
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Slate900),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Slate800)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = TrackerGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Time Saved", color = Slate400, fontSize = 11.sp)
                        }
                        Text(
                            text = "${shieldStats.timeSavedMinutes} mins",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Leo AI Assistant Banner
            Surface(
                onClick = onOpenLeoAiModal,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("leo_ai_banner"),
                shape = RoundedCornerShape(16.dp),
                color = Slate900,
                border = androidx.compose.foundation.BorderStroke(1.dp, IstekCyan.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Leo AI",
                        tint = IstekCyan,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Leo AI Assistant is ready",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Ask questions, summarize web pages, or generate code instantly.",
                            color = Slate400,
                            fontSize = 12.sp
                        )
                    }
                    Button(
                        onClick = onOpenLeoAiModal,
                        colors = ButtonDefaults.buttonColors(containerColor = IstekCyan)
                    ) {
                        Text("Chat Leo", fontSize = 12.sp, color = Color.White)
                    }
                }
            }
        }
    }
}
