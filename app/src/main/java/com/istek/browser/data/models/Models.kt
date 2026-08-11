package com.istek.browser.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TabType {
    NTP, WEB, REWARDS, OPENTUBE, YT_METADATA, INSTALLER, ISTEK_SEARCH, GOOGLE_SEARCH
}

data class Tab(
    val id: String,
    val title: String,
    val url: String,
    val favicon: String = "⚡",
    val type: TabType = TabType.NTP,
    val active: Boolean = false,
    val blockedCount: Int = 0,
    val httpsUpgraded: Boolean = true
)

data class ShieldSettings(
    val enabled: Boolean = true,
    val trackersBlockingLevel: String = "aggressive", // aggressive, standard, off
    val httpsOnlyMode: Boolean = true,
    val blockCookies: Boolean = true,
    val blockScripts: Boolean = false,
    val fingerprintProtection: Boolean = true
)

data class ShieldStats(
    val trackersBlocked: Long = 3482,
    val adsBlocked: Long = 2190,
    val fingerprintsBlocked: Long = 412,
    val httpsUpgrades: Long = 890,
    val bandwidthSavedMb: Long = 842,
    val timeSavedMinutes: Long = 148
)

data class BlockedTracker(
    val id: String,
    val domain: String,
    val company: String,
    val category: String,
    val threatLevel: String, // High, Medium, Low
    val blockedCount: Int,
    val timestamp: String
)

data class HistorySettings(
    val searchHistoryEnabled: Boolean = true,
    val siteHistoryEnabled: Boolean = true
)

@Entity(tableName = "history_items")
data class HistoryItem(
    @PrimaryKey val id: String,
    val type: String, // "site" or "search"
    val queryOrUrl: String,
    val title: String,
    val favicon: String,
    val timestamp: String,
    val date: String
)

@Entity(tableName = "bookmarks")
data class BookmarkItem(
    @PrimaryKey val id: String,
    val title: String,
    val url: String,
    val favicon: String
)

data class LeoChatMessage(
    val id: String,
    val sender: String, // "user" or "assistant"
    val message: String,
    val timestamp: String = ""
)

data class RewardsState(
    val batBalance: Double = 34.85,
    val usdValue: Double = 10.45,
    val adsViewedThisMonth: Int = 142,
    val estimatedEarnings: Double = 4.20,
    val rewardsEnabled: Boolean = true,
    val autoContributeEnabled: Boolean = true
)
