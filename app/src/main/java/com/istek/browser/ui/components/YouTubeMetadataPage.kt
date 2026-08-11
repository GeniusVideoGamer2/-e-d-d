package com.istek.browser.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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

@Composable
fun YouTubeMetadataPage() {
    var videoUrlInput by remember { mutableStateOf("https://www.youtube.com/watch?v=pados_game_2026") }
    var isAnalyzing by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate950)
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 16.dp)) {
            Icon(Icons.Default.Analytics, contentDescription = null, tint = ShieldOrange, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text("YT Metadata & SEO Inspector", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("Extract tags, descriptions, channel IDs, and thumbnail URLs", fontSize = 12.sp, color = Slate400)
            }
        }

        // Input box
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = videoUrlInput,
                onValueChange = { videoUrlInput = it },
                placeholder = { Text("Paste YouTube URL...", color = Slate400) },
                singleLine = true,
                modifier = Modifier.weight(1f).testTag("yt_meta_url_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Slate900,
                    unfocusedContainerColor = Slate900,
                    focusedBorderColor = ShieldOrange,
                    unfocusedBorderColor = Slate800,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Slate200
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { isAnalyzing = true },
                colors = ButtonDefaults.buttonColors(containerColor = ShieldOrange),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(52.dp).testTag("analyze_yt_btn")
            ) {
                Text("Analyze", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Analysis Cards
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Slate900),
            border = androidx.compose.foundation.BorderStroke(1.dp, Slate800)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Extracted Video Metadata", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ShieldOrange)
                Spacer(modifier = Modifier.height(12.dp))

                Text("Title: Pados Game High Speed Gameplay Walkthrough", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("Channel ID: UC_GeniusVideoGamer2_Official", fontSize = 12.sp, color = Slate400)
                Text("Duration: 12 minutes 45 seconds (765s)", fontSize = 12.sp, color = Slate400)
                Text("Published Date: 2026-03-15", fontSize = 12.sp, color = Slate400)

                Spacer(modifier = Modifier.height(12.dp))
                Text("SEO Keywords & Tags:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate200)

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 6.dp)
                ) {
                    items(listOf("pados game", "geniusvideogamer2", "istek browser", "android webview", "gaming walkthrough", "jetpack compose")) { tag ->
                        SuggestionChip(
                            onClick = {},
                            label = { Text("#$tag", fontSize = 10.sp) },
                            colors = SuggestionChipDefaults.suggestionChipColors(containerColor = Slate800, labelColor = IstekCyan)
                        )
                    }
                }
            }
        }
    }
}
