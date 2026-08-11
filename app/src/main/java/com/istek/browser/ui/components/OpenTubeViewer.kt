package com.istek.browser.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.istek.browser.ui.theme.*

data class VideoItem(
    val id: String,
    val title: String,
    val channel: String,
    val views: String,
    val duration: String,
    val category: String,
    val thumbnailEmoji: String
)

@Composable
fun OpenTubeViewer(
    onNavigateUrl: (String) -> Unit
) {
    var isPlaying by remember { mutableStateOf(true) }
    var selectedQuality by remember { mutableStateOf("1080p HD") }

    val mockVideos = remember {
        listOf(
            VideoItem("v1", "Pados Game High Speed Gameplay Walkthrough", "GeniusVideoGamer2", "142K views", "12:45", "Gaming", "🎮"),
            VideoItem("v2", "Building High Speed Modern Browsers with Kotlin", "ISTEK Tech Channel", "89K views", "24:10", "Tech", "⚡"),
            VideoItem("v3", "Mersin Okulları Science & Tech Festival 2026", "İSTEK Mersin", "54K views", "18:30", "Education", "🏫"),
            VideoItem("v4", "OpenTube Privacy Architecture Deep Dive", "OpenTube Engine", "210K views", "15:20", "Software", "🔒")
        )
    }

    var currentVideo by remember { mutableStateOf(mockVideos[0]) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate950)
            .padding(16.dp)
    ) {
        // Top Banner
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Icon(Icons.Default.FlashOn, contentDescription = null, tint = IstekCyan, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("OpenTube Ad-Free Video Engine", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.weight(1f))
            Surface(
                color = TrackerGreen.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, TrackerGreen)
            ) {
                Text("0 Ads Allowed", color = TrackerGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
            }
        }

        // Simulated Player Canvas
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            colors = CardDefaults.cardColors(containerColor = Slate900),
            border = androidx.compose.foundation.BorderStroke(1.dp, Slate800)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(currentVideo.thumbnailEmoji, fontSize = 56.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(currentVideo.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("${currentVideo.channel} • ${currentVideo.views}", fontSize = 12.sp, color = Slate400)

                    Row(
                        modifier = Modifier.padding(top = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { isPlaying = !isPlaying }) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                                contentDescription = "Play/Pause",
                                tint = IstekCyan,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        AssistChip(
                            onClick = {
                                selectedQuality = if (selectedQuality == "1080p HD") "4K Ultra" else "1080p HD"
                            },
                            label = { Text(selectedQuality, fontSize = 11.sp) },
                            colors = AssistChipDefaults.assistChipColors(containerColor = Slate800, labelColor = Color.White)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Recommended Ad-Free Streams", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Slate400, modifier = Modifier.padding(bottom = 8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(mockVideos) { video ->
                Card(
                    onClick = { currentVideo = video },
                    colors = CardDefaults.cardColors(containerColor = Slate900),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (currentVideo.id == video.id) IstekCyan else Slate800),
                    modifier = Modifier.fillMaxWidth().testTag("video_item_${video.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(video.thumbnailEmoji, fontSize = 28.sp, modifier = Modifier.padding(end = 12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(video.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("${video.channel} • ${video.duration}", fontSize = 11.sp, color = Slate400)
                        }
                    }
                }
            }
        }
    }
}
