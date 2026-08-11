package com.istek.browser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.lifecycleScope
import com.istek.browser.data.ai.LeoAiService
import com.istek.browser.data.db.AppDatabase
import com.istek.browser.data.models.*
import com.istek.browser.ui.components.*
import com.istek.browser.ui.theme.IstekBrowserTheme
import com.istek.browser.ui.theme.Slate950
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {

    private val leoAiService by lazy { LeoAiService() }
    private val database by lazy { AppDatabase.getDatabase(applicationContext) }
    private val dao by lazy { database.browserDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            IstekBrowserTheme {
                // Main Browser State
                var tabs by remember {
                    mutableStateOf(
                        listOf(
                            Tab(
                                id = "tab_ntp",
                                title = "New Tab",
                                url = "istek://newtab",
                                favicon = "⚡",
                                type = TabType.NTP,
                                active = true,
                                blockedCount = 0
                            )
                        )
                    )
                }

                var activeTabId by remember { mutableStateOf("tab_ntp") }

                var shieldSettings by remember { mutableStateOf(ShieldSettings()) }
                var shieldStats by remember { mutableStateOf(ShieldStats()) }
                var historySettings by remember { mutableStateOf(HistorySettings()) }
                var rewardsState by remember { mutableStateOf(RewardsState()) }

                var historyItems by remember { mutableStateOf<List<HistoryItem>>(emptyList()) }

                // Observe Room DB History
                LaunchedEffect(Unit) {
                    dao.getAllHistory().collect { dbHistory ->
                        if (dbHistory.isNotEmpty()) {
                            historyItems = dbHistory
                        } else {
                            // Seed initial history
                            val initial = listOf(
                                HistoryItem("h1", "site", "https://geniusvideogamer2.github.io/pados/", "Pados Game", "🎮", "1:30 PM", "Today"),
                                HistoryItem("h2", "search", "istek://search?q=Pados+Game", "ISTEK Search: Pados Game", "🔍", "1:28 PM", "Today"),
                                HistoryItem("h3", "site", "https://www.youtube.com", "YouTube", "▶️", "1:15 PM", "Today"),
                                HistoryItem("h4", "search", "https://www.google.com/search?q=YouTube", "Google Search: YouTube", "🔍", "1:10 PM", "Today")
                            )
                            initial.forEach { dao.insertHistory(it) }
                            historyItems = initial
                        }
                    }
                }

                // Leo Chat Messages
                var leoMessages by remember {
                    mutableStateOf(
                        listOf(
                            LeoChatMessage("m1", "assistant", "Hello! I'm Leo, your ISTEK AI Assistant. How can I help you analyze web pages or privacy safety today?")
                        )
                    )
                }

                // Modal States
                var isShieldModalOpen by remember { mutableStateOf(false) }
                var isLeoAiModalOpen by remember { mutableStateOf(false) }
                var isHistoryModalOpen by remember { mutableStateOf(false) }
                var isSettingsModalOpen by remember { mutableStateOf(false) }
                var isNetworkModalOpen by remember { mutableStateOf(false) }
                var isInstallerModalOpen by remember { mutableStateOf(false) }

                val activeTab = tabs.find { it.id == activeTabId } ?: tabs.first()

                // Blocked Trackers for current page
                val blockedTrackers = remember(shieldSettings.enabled, activeTab.id) {
                    if (!shieldSettings.enabled) emptyList()
                    else listOf(
                        BlockedTracker("bt1", "doubleclick.net", "Google Ad Network", "Ad Tracker", "High", 14, "10:45 AM"),
                        BlockedTracker("bt2", "facebook.net/pixel.js", "Meta Platforms", "Social Tracker", "High", 8, "10:45 AM"),
                        BlockedTracker("bt3", "google-analytics.com", "Google LLC", "Analytics", "Medium", 12, "10:45 AM")
                    )
                }

                // Navigation function
                val navigateToUrl: (String) -> Unit = { targetUrl ->
                    var newType = TabType.WEB
                    var title = targetUrl
                    var favicon = "🌐"
                    var blockedCount = if (shieldSettings.enabled) (3..12).random() else 0

                    val timeStr = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())

                    when {
                        targetUrl == "istek://newtab" || targetUrl == "brave://newtab" -> {
                            newType = TabType.NTP
                            title = "New Tab"
                            favicon = "⚡"
                            blockedCount = 0
                        }
                        targetUrl == "istek://rewards" || targetUrl == "brave://rewards" -> {
                            newType = TabType.REWARDS
                            title = "ISTEK Rewards"
                            favicon = "🦁"
                            blockedCount = 0
                        }
                        targetUrl.startsWith("istek://opentube") -> {
                            newType = TabType.OPENTUBE
                            title = "OpenTube Engine"
                            favicon = "⚡"
                            blockedCount = 0
                        }
                        targetUrl.startsWith("istek://yt-metadata") -> {
                            newType = TabType.YT_METADATA
                            title = "YT Metadata Tool"
                            favicon = "📊"
                            blockedCount = 0
                        }
                        targetUrl.startsWith("istek://installer") || targetUrl.startsWith("istek://setup") -> {
                            newType = TabType.INSTALLER
                            title = "ISTEK Setup Installer"
                            favicon = "💿"
                            blockedCount = 0
                        }
                        targetUrl.startsWith("istek://search") -> {
                            newType = TabType.ISTEK_SEARCH
                            val query = targetUrl.substringAfter("q=").replace("+", " ")
                            title = "ISTEK Search: $query"
                            favicon = "⚡"
                        }
                        targetUrl.startsWith("https://www.google.com/search") -> {
                            newType = TabType.GOOGLE_SEARCH
                            val query = targetUrl.substringAfter("q=").replace("+", " ")
                            title = "Google Search: $query"
                            favicon = "🔍"
                        }
                        targetUrl.contains("youtube.com") -> {
                            newType = TabType.WEB
                            title = "YouTube"
                            favicon = "▶️"
                        }
                        targetUrl.contains("pados") -> {
                            newType = TabType.WEB
                            title = "Pados Game"
                            favicon = "🎮"
                        }
                    }

                    // Update Tab
                    tabs = tabs.map {
                        if (it.id == activeTabId) {
                            it.copy(
                                title = title,
                                url = targetUrl,
                                favicon = favicon,
                                type = newType,
                                blockedCount = blockedCount
                            )
                        } else it
                    }

                    // Save to Room DB History
                    val isSearch = newType == TabType.ISTEK_SEARCH || newType == TabType.GOOGLE_SEARCH
                    if ((isSearch && historySettings.searchHistoryEnabled) || (!isSearch && historySettings.siteHistoryEnabled)) {
                        val newItem = HistoryItem(
                            id = "h_" + System.currentTimeMillis(),
                            type = if (isSearch) "search" else "site",
                            queryOrUrl = targetUrl,
                            title = title,
                            favicon = favicon,
                            timestamp = timeStr,
                            date = "Today"
                        )
                        lifecycleScope.launch {
                            dao.insertHistory(newItem)
                        }
                    }
                }

                // UI Layout
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("main_app_scaffold"),
                    containerColor = Slate950,
                    topBar = {
                        HeaderBar(
                            tabs = tabs,
                            activeTab = activeTab,
                            shieldSettings = shieldSettings,
                            onSelectTab = { id ->
                                activeTabId = id
                                tabs = tabs.map { it.copy(active = it.id == id) }
                            },
                            onCloseTab = { id ->
                                if (tabs.size > 1) {
                                    val newTabs = tabs.filter { it.id != id }
                                    tabs = newTabs
                                    if (activeTabId == id) {
                                        activeTabId = newTabs.first().id
                                    }
                                }
                            },
                            onNewTab = {
                                val newId = "tab_" + System.currentTimeMillis()
                                val newTab = Tab(
                                    id = newId,
                                    title = "New Tab",
                                    url = "istek://newtab",
                                    favicon = "⚡",
                                    type = TabType.NTP,
                                    active = true
                                )
                                tabs = tabs.map { it.copy(active = false) } + newTab
                                activeTabId = newId
                            },
                            onNavigateUrl = navigateToUrl,
                            onOpenShieldModal = { isShieldModalOpen = true },
                            onOpenLeoAiModal = { isLeoAiModalOpen = true },
                            onOpenRewardsPage = { navigateToUrl("istek://rewards") },
                            onOpenHistoryModal = { isHistoryModalOpen = true },
                            onOpenSettingsModal = { isSettingsModalOpen = true },
                            onOpenNetworkModal = { isNetworkModalOpen = true }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        // Main View Router based on TabType
                        when (activeTab.type) {
                            TabType.NTP -> NewTabPage(
                                shieldStats = shieldStats,
                                onNavigateUrl = navigateToUrl,
                                onOpenLeoAiModal = { isLeoAiModalOpen = true }
                            )
                            TabType.REWARDS -> IstekRewardsPage(
                                rewardsState = rewardsState,
                                onToggleRewards = { rewardsState = rewardsState.copy(rewardsEnabled = it) },
                                onTipCreator = { amount ->
                                    if (rewardsState.batBalance >= amount) {
                                        rewardsState = rewardsState.copy(
                                            batBalance = rewardsState.batBalance - amount,
                                            usdValue = (rewardsState.batBalance - amount) * 0.30
                                        )
                                    }
                                }
                            )
                            TabType.OPENTUBE -> OpenTubeViewer(onNavigateUrl = navigateToUrl)
                            TabType.YT_METADATA -> YouTubeMetadataPage()
                            TabType.INSTALLER -> SetupInstallerModal(onDismiss = { navigateToUrl("istek://newtab") })
                            TabType.ISTEK_SEARCH -> {
                                val query = activeTab.url.substringAfter("q=").replace("+", " ")
                                IstekSearchPage(query = query, onNavigateUrl = navigateToUrl)
                            }
                            TabType.GOOGLE_SEARCH -> {
                                val query = activeTab.url.substringAfter("q=").replace("+", " ")
                                GoogleSearchPage(query = query, onNavigateUrl = navigateToUrl)
                            }
                            TabType.WEB -> WebPageFrame(url = activeTab.url, shieldSettings = shieldSettings)
                        }

                        // Floating Left Bottom Quick History Widget
                        LeftBottomHistoryWidget(
                            historyItems = historyItems,
                            onNavigateUrl = navigateToUrl
                        )

                        // Modals
                        if (isShieldModalOpen) {
                            IstekShieldModal(
                                shieldSettings = shieldSettings,
                                blockedTrackers = blockedTrackers,
                                onUpdateSettings = { shieldSettings = it },
                                onDismiss = { isShieldModalOpen = false }
                            )
                        }

                        if (isLeoAiModalOpen) {
                            LeoAiAssistantModal(
                                messages = leoMessages,
                                currentUrl = activeTab.url,
                                onSendMessage = { promptText ->
                                    val userMsg = LeoChatMessage("user_" + System.currentTimeMillis(), "user", promptText)
                                    leoMessages = leoMessages + userMsg

                                    lifecycleScope.launch {
                                        val aiReplyText = leoAiService.generateResponse(promptText, activeTab.url)
                                        val aiMsg = LeoChatMessage("ai_" + System.currentTimeMillis(), "assistant", aiReplyText)
                                        leoMessages = leoMessages + aiMsg
                                    }
                                },
                                onDismiss = { isLeoAiModalOpen = false }
                            )
                        }

                        if (isHistoryModalOpen) {
                            HistoryModal(
                                historyItems = historyItems,
                                historySettings = historySettings,
                                onNavigateUrl = navigateToUrl,
                                onToggleSearchHistory = { historySettings = historySettings.copy(searchHistoryEnabled = !historySettings.searchHistoryEnabled) },
                                onToggleSiteHistory = { historySettings = historySettings.copy(siteHistoryEnabled = !historySettings.siteHistoryEnabled) },
                                onClearHistory = { type ->
                                    lifecycleScope.launch {
                                        if (type == null || type == "all") dao.clearAllHistory()
                                        else dao.clearHistoryByType(type)
                                    }
                                },
                                onDeleteItem = { id ->
                                    val itemToDelete = historyItems.find { it.id == id }
                                    if (itemToDelete != null) {
                                        lifecycleScope.launch { dao.deleteHistoryItem(itemToDelete) }
                                    }
                                },
                                onDismiss = { isHistoryModalOpen = false }
                            )
                        }

                        if (isSettingsModalOpen) {
                            SettingsModal(onDismiss = { isSettingsModalOpen = false })
                        }

                        if (isNetworkModalOpen) {
                            NetworkModal(onDismiss = { isNetworkModalOpen = false })
                        }
                    }
                }
            }
        }
    }
}
