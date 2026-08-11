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
import com.istek.browser.data.models.RewardsState
import com.istek.browser.ui.theme.*

@Composable
fun IstekRewardsPage(
    rewardsState: RewardsState,
    onToggleRewards: (Boolean) -> Unit,
    onTipCreator: (Double) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate950)
            .padding(24.dp)
    ) {
        // Top Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Slate900),
            border = androidx.compose.foundation.BorderStroke(1.dp, RewardYellow.copy(alpha = 0.5f))
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Token,
                    contentDescription = null,
                    tint = RewardYellow,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("ISTEK Rewards", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Earn BAT tokens for privacy-preserving notifications", fontSize = 12.sp, color = Slate400)
                }

                Switch(
                    checked = rewardsState.rewardsEnabled,
                    onCheckedChange = onToggleRewards,
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = RewardYellow),
                    modifier = Modifier.testTag("toggle_rewards_switch")
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Balance Overview Card
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = Slate900),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate800)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("BAT Balance", fontSize = 12.sp, color = Slate400)
                    Text(
                        text = "%.2f BAT".format(rewardsState.batBalance),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = RewardYellow,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text("≈ $%.2f USD".format(rewardsState.usdValue), fontSize = 12.sp, color = TrackerGreen)
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = Slate900),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate800)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("Monthly Estimated Earnings", fontSize = 12.sp, color = Slate400)
                    Text(
                        text = "%.2f BAT".format(rewardsState.estimatedEarnings),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = IstekCyan,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text("${rewardsState.adsViewedThisMonth} ads viewed this month", fontSize = 12.sp, color = Slate400)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Creator Tipping Box
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Slate900),
            border = androidx.compose.foundation.BorderStroke(1.dp, Slate800)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Tip Verified Creators & Sites", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("Send BAT directly to content creators you love without third-party fees.", fontSize = 12.sp, color = Slate400, modifier = Modifier.padding(bottom = 16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    listOf(1.0, 5.0, 10.0).forEach { amount ->
                        Button(
                            onClick = { onTipCreator(amount) },
                            colors = ButtonDefaults.buttonColors(containerColor = Slate800),
                            modifier = Modifier.weight(1f).testTag("tip_btn_${amount.toInt()}")
                        ) {
                            Text("Send ${amount.toInt()} BAT", fontSize = 12.sp, color = RewardYellow)
                        }
                    }
                }
            }
        }
    }
}
