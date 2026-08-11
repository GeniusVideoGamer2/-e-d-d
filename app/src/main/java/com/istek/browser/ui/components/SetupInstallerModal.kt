package com.istek.browser.ui.components

import androidx.compose.foundation.background
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

@Composable
fun SetupInstallerModal(
    onDismiss: () -> Unit
) {
    var progress by remember { mutableStateOf(0.75f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate950)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.width(360.dp),
            colors = CardDefaults.cardColors(containerColor = Slate900),
            border = androidx.compose.foundation.BorderStroke(1.dp, IstekCyan)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.DownloadForOffline, contentDescription = null, tint = IstekCyan, modifier = Modifier.size(48.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text("İSTEK BROWSER Setup", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("Mersin Okulları Standalone Client Package", fontSize = 12.sp, color = Slate400, modifier = Modifier.padding(bottom = 20.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = IstekCyan,
                    trackColor = Slate800
                )

                Text(
                    text = "${(progress * 100).toInt()}% - Extracting Shields & Leo AI engine...",
                    fontSize = 12.sp,
                    color = Slate400,
                    modifier = Modifier.padding(top = 8.dp, bottom = 20.dp)
                )

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = IstekCyan),
                    modifier = Modifier.fillMaxWidth().testTag("installer_finish_btn")
                ) {
                    Text("Launch Browser", color = Color.White)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkModal(
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Slate900,
        contentColor = Color.White,
        modifier = Modifier.testTag("network_modal")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Wifi, contentDescription = null, tint = TrackerGreen, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text("Network Diagnostics & DNS Shield", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Divider(color = Slate800, modifier = Modifier.padding(vertical = 16.dp))

            Text("Connection Status: Connected (Mersin Fiber 1Gbps)", fontSize = 13.sp, color = TrackerGreen, fontWeight = FontWeight.Bold)
            Text("Secure Encrypted DNS: Enabled (1.1.1.1 + ISTEK Shield Filter)", fontSize = 12.sp, color = Slate400)
            Text("Ping Latency: 12ms", fontSize = 12.sp, color = Slate400)
            Text("IPv6 Status: Active", fontSize = 12.sp, color = Slate400)

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = TrackerGreen),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Done", color = Color.White)
            }
        }
    }
}
