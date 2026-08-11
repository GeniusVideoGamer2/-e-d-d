package com.istek.browser.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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

data class SearchResultItem(
    val title: String,
    val url: String,
    val snippet: String,
    val privacyScore: String
)

@Composable
fun IstekSearchPage(
    query: String,
    onNavigateUrl: (String) -> Unit
) {
    val results = remember(query) {
        listOf(
            SearchResultItem(
                "Pados Game - Official High Speed Web Game",
                "https://geniusvideogamer2.github.io/pados/",
                "Play Pados Game directly in your web browser with zero ads and zero tracking scripts.",
                "10/10 Safe"
            ),
            SearchResultItem(
                "İSTEK Mersin Okulları - Quality Private Education",
                "https://www.istek.k12.tr",
                "İSTEK Mersin Okulları modern educational web application and technological achievements.",
                "10/10 Safe"
            ),
            SearchResultItem(
                "YouTube - Enjoy Ad-Free Videos with ISTEK Shields",
                "https://www.youtube.com",
                "Watch your favorite video content uninterrupted with automated tracker and ad blocking.",
                "9/10 Safe"
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate950)
            .padding(16.dp)
    ) {
        // AI Answer Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Slate900),
            border = androidx.compose.foundation.BorderStroke(1.dp, IstekCyan)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = IstekCyan, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("ISTEK AI Instant Answer for '$query'", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Searching for '$query' on ISTEK Search index. All search requests are anonymized through ISTEK Privacy Shields. No user profile or location vector stored.",
                    fontSize = 12.sp,
                    color = Slate200
                )
            }
        }

        Text("Search Results for '$query'", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Slate400, modifier = Modifier.padding(bottom = 8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(results) { res ->
                Card(
                    onClick = { onNavigateUrl(res.url) },
                    colors = CardDefaults.cardColors(containerColor = Slate900),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Slate800),
                    modifier = Modifier.fillMaxWidth().testTag("search_result_card")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(res.url, fontSize = 11.sp, color = IstekCyan, maxLines = 1, modifier = Modifier.weight(1f))
                            Surface(
                                color = TrackerGreen.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(res.privacyScore, color = TrackerGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                        Text(res.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(vertical = 4.dp))
                        Text(res.snippet, fontSize = 12.sp, color = Slate400)
                    }
                }
            }
        }
    }
}

@Composable
fun GoogleSearchPage(
    query: String,
    onNavigateUrl: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate950)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 12.dp)) {
            Icon(Icons.Default.Search, contentDescription = null, tint = IstekBlue, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Google Search Engine View ($query)", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Card(
            onClick = { onNavigateUrl("https://www.google.com/search?q=$query") },
            colors = CardDefaults.cardColors(containerColor = Slate900),
            border = androidx.compose.foundation.BorderStroke(1.dp, Slate800),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Google Search Result for '$query'", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("https://www.google.com/search?q=$query", fontSize = 11.sp, color = IstekCyan)
                Text("Click to view full web frame.", fontSize = 12.sp, color = Slate400, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}
