package com.istek.browser.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.istek.browser.data.models.HistoryItem
import com.istek.browser.data.models.HistorySettings
import com.istek.browser.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryModal(
    historyItems: List<HistoryItem>,
    historySettings: HistorySettings,
    onNavigateUrl: (String) -> Unit,
    onToggleSearchHistory: () -> Unit,
    onToggleSiteHistory: () -> Unit,
    onClearHistory: (String?) -> Unit,
    onDeleteItem: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("all") } // "all", "search", "site"

    val filteredItems = remember(historyItems, searchQuery, selectedFilter) {
        historyItems.filter { item ->
            val matchesType = when (selectedFilter) {
                "search" -> item.type == "search"
                "site" -> item.type == "site"
                else -> true
            }
            val matchesQuery = searchQuery.isBlank() ||
                    item.title.contains(searchQuery, ignoreCase = true) ||
                    item.queryOrUrl.contains(searchQuery, ignoreCase = true)
            matchesType && matchesQuery
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Slate900,
        contentColor = Color.White,
        modifier = Modifier.testTag("history_modal")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 480.dp, max = 650.dp)
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.History, contentDescription = null, tint = IstekCyan, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text("Browsing & Search History", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.weight(1f))
                TextButton(
                    onClick = { onClearHistory("all") },
                    modifier = Modifier.testTag("clear_all_history_btn")
                ) {
                    Text("Clear All", color = ShieldRed, fontSize = 12.sp)
                }
            }

            // Toggles Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = historySettings.searchHistoryEnabled,
                        onCheckedChange = { onToggleSearchHistory() },
                        colors = CheckboxDefaults.colors(checkedColor = IstekCyan)
                    )
                    Text("Track Search", fontSize = 12.sp, color = Slate400)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = historySettings.siteHistoryEnabled,
                        onCheckedChange = { onToggleSiteHistory() },
                        colors = CheckboxDefaults.colors(checkedColor = IstekCyan)
                    )
                    Text("Track Sites", fontSize = 12.sp, color = Slate400)
                }
            }

            // Filter Chips & Search Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("all" to "All", "search" to "Searches", "site" to "Sites").forEach { (key, label) ->
                    FilterChip(
                        selected = selectedFilter == key,
                        onClick = { selectedFilter = key },
                        label = { Text(label, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = IstekCyan,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search history...", color = Slate400, fontSize = 12.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Slate400) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(12.dp)
            )

            // History List
            if (filteredItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No history items found", color = Slate400, fontSize = 14.sp)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredItems, key = { it.id }) { item ->
                        Surface(
                            onClick = {
                                onNavigateUrl(item.queryOrUrl)
                                onDismiss()
                            },
                            shape = RoundedCornerShape(8.dp),
                            color = Slate950,
                            border = androidx.compose.foundation.BorderStroke(1.dp, Slate800),
                            modifier = Modifier.fillMaxWidth().testTag("history_item_${item.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(item.favicon, fontSize = 18.sp, modifier = Modifier.padding(end = 10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(item.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Text(item.queryOrUrl, fontSize = 11.sp, color = Slate400, maxLines = 1)
                                }
                                Text(item.timestamp, fontSize = 10.sp, color = Slate400, modifier = Modifier.padding(horizontal = 8.dp))
                                IconButton(
                                    onClick = { onDeleteItem(item.id) },
                                    modifier = Modifier.size(20.dp)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = ShieldRed, modifier = Modifier.size(14.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
