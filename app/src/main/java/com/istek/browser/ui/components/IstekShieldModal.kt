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
import com.istek.browser.data.models.BlockedTracker
import com.istek.browser.data.models.ShieldSettings
import com.istek.browser.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IstekShieldModal(
    shieldSettings: ShieldSettings,
    blockedTrackers: List<BlockedTracker>,
    onUpdateSettings: (ShieldSettings) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Slate900,
        contentColor = Color.White,
        modifier = Modifier.testTag("shield_modal")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = "Shield Icon",
                    tint = ShieldOrange,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "ISTEK Shields Protection",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = if (shieldSettings.enabled) "Shields ARE UP for this site" else "Shields are DOWN",
                        fontSize = 12.sp,
                        color = if (shieldSettings.enabled) TrackerGreen else ShieldRed
                    )
                }

                Switch(
                    checked = shieldSettings.enabled,
                    onCheckedChange = { onUpdateSettings(shieldSettings.copy(enabled = it)) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = ShieldOrange
                    ),
                    modifier = Modifier.testTag("shield_global_switch")
                )
            }

            Divider(color = Slate800, modifier = Modifier.padding(vertical = 16.dp))

            // Tracker Blocking Level Selector
            Text(
                text = "Trackers & Ads Blocking Level",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Slate400,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("aggressive", "standard", "off").forEach { level ->
                    val isSelected = shieldSettings.trackersBlockingLevel == level
                    Button(
                        onClick = { onUpdateSettings(shieldSettings.copy(trackersBlockingLevel = level)) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) ShieldOrange else Slate800,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.weight(1f).testTag("shield_level_$level")
                    ) {
                        Text(level.replaceFirstChar { it.uppercase() }, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Toggles
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Upgrade Connections to HTTPS", fontSize = 13.sp, color = Color.White)
                Switch(
                    checked = shieldSettings.httpsOnlyMode,
                    onCheckedChange = { onUpdateSettings(shieldSettings.copy(httpsOnlyMode = it)) }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Block Cross-Site Cookies", fontSize = 13.sp, color = Color.White)
                Switch(
                    checked = shieldSettings.blockCookies,
                    onCheckedChange = { onUpdateSettings(shieldSettings.copy(blockCookies = it)) }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Fingerprint Protection", fontSize = 13.sp, color = Color.White)
                Switch(
                    checked = shieldSettings.fingerprintProtection,
                    onCheckedChange = { onUpdateSettings(shieldSettings.copy(fingerprintProtection = it)) }
                )
            }

            Divider(color = Slate800, modifier = Modifier.padding(vertical = 16.dp))

            // Blocked Trackers List
            Text(
                text = "Blocked Trackers on Current Page (${blockedTrackers.sumOf { it.blockedCount }})",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                items(blockedTrackers) { tracker ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Slate950),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Slate800),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Block,
                                contentDescription = null,
                                tint = ShieldRed,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(tracker.domain, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("${tracker.company} • ${tracker.category}", fontSize = 11.sp, color = Slate400)
                            }
                            Text(
                                text = "${tracker.blockedCount} blocked",
                                fontSize = 11.sp,
                                color = ShieldOrange,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = IstekCyan),
                modifier = Modifier.fillMaxWidth().testTag("close_shield_modal_btn")
            ) {
                Text("Done", color = Color.White)
            }
        }
    }
}
