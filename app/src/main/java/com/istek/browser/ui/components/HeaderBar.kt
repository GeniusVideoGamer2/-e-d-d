package com.istek.browser.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.istek.browser.data.models.ShieldSettings
import com.istek.browser.data.models.Tab
import com.istek.browser.ui.theme.*

@Composable
fun HeaderBar(
    tabs: List<Tab>,
    activeTab: Tab,
    shieldSettings: ShieldSettings,
    onSelectTab: (String) -> Unit,
    onCloseTab: (String) -> Unit,
    onNewTab: () -> Unit,
    onNavigateUrl: (String) -> Unit,
    onOpenShieldModal: () -> Unit,
    onOpenLeoAiModal: () -> Unit,
    onOpenRewardsPage: () -> Unit,
    onOpenHistoryModal: () -> Unit,
    onOpenSettingsModal: () -> Unit,
    onOpenNetworkModal: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var inputUrl by remember(activeTab.url) { mutableStateOf(activeTab.url) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Slate950)
            .border(1.dp, Slate800)
    ) {
        // Tab Strip
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LazyRow(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(tabs, key = { it.id }) { tab ->
                    val isActive = tab.id == activeTab.id
                    Surface(
                        onClick = { onSelectTab(tab.id) },
                        modifier = Modifier
                            .widthIn(min = 120.dp, max = 180.dp)
                            .height(36.dp)
                            .testTag("tab_item_${tab.id}"),
                        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
                        color = if (isActive) Slate800 else Slate900,
                        border = if (isActive) androidx.compose.foundation.BorderStroke(1.dp, Slate700) else null
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = tab.favicon,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(end = 6.dp)
                            )
                            Text(
                                text = tab.title,
                                color = if (isActive) Color.White else Slate400,
                                fontSize = 12.sp,
                                fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            if (tabs.size > 1) {
                                IconButton(
                                    onClick = { onCloseTab(tab.id) },
                                    modifier = Modifier
                                        .size(18.dp)
                                        .testTag("close_tab_${tab.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Close Tab",
                                        tint = Slate400,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            IconButton(
                onClick = onNewTab,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(32.dp)
                    .testTag("new_tab_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "New Tab",
                    tint = IstekCyan
                )
            }
        }

        // Navigation & Omnibox Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onNavigateUrl("istek://newtab") },
                modifier = Modifier
                    .size(36.dp)
                    .testTag("nav_home_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = Slate200
                )
            }

            IconButton(
                onClick = { onNavigateUrl(activeTab.url) },
                modifier = Modifier
                    .size(36.dp)
                    .testTag("nav_refresh_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    tint = Slate200
                )
            }

            // Omnibox Address Field
            OutlinedTextField(
                value = inputUrl,
                onValueChange = { inputUrl = it },
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .padding(horizontal = 4.dp)
                    .testTag("url_input_field"),
                shape = RoundedCornerShape(22.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Slate900,
                    unfocusedContainerColor = Slate900,
                    focusedBorderColor = IstekCyan,
                    unfocusedBorderColor = Slate800,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Slate200
                ),
                singleLine = true,
                leadingIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { onOpenShieldModal() }
                            .padding(start = 8.dp, end = 4.dp)
                            .testTag("shield_badge_icon")
                    ) {
                        Icon(
                            imageVector = if (shieldSettings.enabled) Icons.Default.Shield else Icons.Outlined.Shield,
                            contentDescription = "ISTEK Shield",
                            tint = if (shieldSettings.enabled) ShieldOrange else Slate400,
                            modifier = Modifier.size(18.dp)
                        )
                        if (activeTab.blockedCount > 0) {
                            Box(
                                modifier = Modifier
                                    .padding(start = 4.dp)
                                    .background(ShieldOrange, CircleShape)
                                    .padding(horizontal = 6.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = activeTab.blockedCount.toString(),
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (inputUrl.isNotBlank()) {
                                onNavigateUrl(inputUrl)
                                focusManager.clearFocus()
                            }
                        },
                        modifier = Modifier.testTag("go_url_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Go",
                            tint = IstekCyan
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                keyboardActions = KeyboardActions(onGo = {
                    if (inputUrl.isNotBlank()) {
                        onNavigateUrl(inputUrl)
                        focusManager.clearFocus()
                    }
                })
            )

            // Right Quick Actions
            IconButton(
                onClick = onOpenLeoAiModal,
                modifier = Modifier
                    .size(36.dp)
                    .testTag("leo_ai_button")
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Leo AI Assistant",
                    tint = IstekCyan
                )
            }

            IconButton(
                onClick = onOpenRewardsPage,
                modifier = Modifier
                    .size(36.dp)
                    .testTag("rewards_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Token,
                    contentDescription = "ISTEK Rewards",
                    tint = RewardYellow
                )
            }

            IconButton(
                onClick = onOpenHistoryModal,
                modifier = Modifier
                    .size(36.dp)
                    .testTag("history_button")
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "History",
                    tint = Slate200
                )
            }

            IconButton(
                onClick = onOpenNetworkModal,
                modifier = Modifier
                    .size(36.dp)
                    .testTag("network_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Wifi,
                    contentDescription = "Network Diagnostic",
                    tint = TrackerGreen
                )
            }

            IconButton(
                onClick = onOpenSettingsModal,
                modifier = Modifier
                    .size(36.dp)
                    .testTag("settings_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = Slate200
                )
            }
        }
    }
}
