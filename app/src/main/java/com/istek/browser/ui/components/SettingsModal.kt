package com.istek.browser.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsModal(
    onDismiss: () -> Unit
) {
    var defaultEngine by remember { mutableStateOf("ISTEK Search") }
    var clearOnExit by remember { mutableStateOf(false) }
    var doNotTrack by remember { mutableStateOf(true) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Slate900,
        contentColor = Color.White,
        modifier = Modifier.testTag("settings_modal")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Settings, contentDescription = null, tint = IstekCyan, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text("ISTEK Browser Settings", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Divider(color = Slate800, modifier = Modifier.padding(vertical = 16.dp))

            // Default Search Engine
            Text("Default Search Engine", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Slate400)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("ISTEK Search", "Google", "DuckDuckGo").forEach { engine ->
                    FilterChip(
                        selected = defaultEngine == engine,
                        onClick = { defaultEngine = engine },
                        label = { Text(engine, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = IstekCyan,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Privacy Switches
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Send 'Do Not Track' Header", fontSize = 13.sp, color = Color.White)
                Switch(checked = doNotTrack, onCheckedChange = { doNotTrack = it })
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Clear Cookies & History on Exit", fontSize = 13.sp, color = Color.White)
                Switch(checked = clearOnExit, onCheckedChange = { clearOnExit = it })
            }

            Divider(color = Slate800, modifier = Modifier.padding(vertical = 16.dp))

            // About Box
            Card(
                colors = CardDefaults.cardColors(containerColor = Slate950),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate800),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("İSTEK BROWSER v2.4", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Mersin Okulları High-Performance Web Client", fontSize = 12.sp, color = IstekCyan)
                    Text("Replicating Shields, Search, Leo AI, and Rewards.", fontSize = 11.sp, color = Slate400, modifier = Modifier.padding(top = 4.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = IstekCyan),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save & Close", color = Color.White)
            }
        }
    }
}
