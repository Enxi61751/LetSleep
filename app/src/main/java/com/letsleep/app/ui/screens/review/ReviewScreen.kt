package com.letsleep.app.ui.screens.review

import android.app.TimePickerDialog
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.letsleep.app.data.LetSleepViewModel
import com.letsleep.app.data.model.LateReason
import com.letsleep.app.data.model.ReviewEntry
import com.letsleep.app.ui.components.LetSleepCard
import com.letsleep.app.util.minutesAfterPlan
import com.letsleep.app.util.toDisplayString
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun ReviewScreen(viewModel: LetSleepViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val plan = uiState.nightPlan.planEndTime
    val context = LocalContext.current
    var actualEnd by remember { mutableStateOf(plan.plusMinutes(15)) }
    var selectedReason by remember { mutableStateOf(LateReason.ShortVideo) }
    val delta = minutesAfterPlan(plan, actualEnd)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("次日复盘", style = MaterialTheme.typography.headlineMedium)
        Text(
            "如实记录就好，肥猫不会唠叨你。",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        LetSleepCard {
            Text("计划结束", style = MaterialTheme.typography.titleMedium)
            Text(plan.toDisplayString(), style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(12.dp))
            Text("实际结束娱乐", style = MaterialTheme.typography.titleMedium)
            Text(
                actualEnd.toDisplayString(),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.clickable {
                    TimePickerDialog(
                        context,
                        { _, h, m -> actualEnd = LocalTime.of(h, m) },
                        actualEnd.hour,
                        actualEnd.minute,
                        true,
                    ).show()
                },
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "与计划相差：${if (delta >= 0) "晚" else "早"} ${kotlin.math.abs(delta)} 分钟",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        LetSleepCard {
            Text("晚睡原因", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            LateReason.entries.forEach { reason ->
                FilterChip(
                    selected = selectedReason == reason,
                    onClick = { selectedReason = reason },
                    label = { Text(reason.label) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                )
            }
            Spacer(Modifier.height(8.dp))
            TextButton(
                onClick = {
                    val entry = ReviewEntry(
                        date = LocalDate.now(),
                        planEndTime = plan,
                        actualEndTime = actualEnd,
                        deltaMinutes = delta,
                        reason = selectedReason,
                        isMock = false,
                    )
                    viewModel.saveReviewEntry(entry)
                },
            ) {
                Text("保存今日复盘")
            }
        }

        LetSleepCard {
            Text("最近 7 天", style = MaterialTheme.typography.titleMedium)
            Text(
                "下方趋势含模拟示例数据，标注 isMock 的条目仅供 UI 演示。",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(12.dp))
            SimpleTrendChart(entries = uiState.reviewHistory.take(7).reversed())
            Spacer(Modifier.height(12.dp))
            uiState.reviewHistory.take(7).forEach { entry ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(entry.date.toString(), style = MaterialTheme.typography.bodyMedium)
                    Text(
                        "${entry.deltaMinutes} 分 · ${entry.reason.label}${if (entry.isMock) " · 模拟" else ""}",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Composable
private fun SimpleTrendChart(entries: List<ReviewEntry>) {
    if (entries.isEmpty()) return
    val maxAbs = (entries.maxOf { kotlin.math.abs(it.deltaMinutes) }.coerceAtLeast(1)).toFloat()
    val barColor = MaterialTheme.colorScheme.primary
    val baselineColor = MaterialTheme.colorScheme.outline

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
    ) {
        val barWidth = size.width / (entries.size * 2f)
        val midY = size.height / 2f
        drawLine(
            color = baselineColor,
            start = Offset(0f, midY),
            end = Offset(size.width, midY),
            strokeWidth = 2f,
        )
        entries.forEachIndexed { index, entry ->
            val centerX = barWidth + index * (barWidth * 2f)
            val heightRatio = entry.deltaMinutes / maxAbs
            val barHeight = kotlin.math.abs(heightRatio) * (size.height / 2f - 8f)
            val top = if (entry.deltaMinutes >= 0) midY - barHeight else midY
            drawRect(
                color = barColor,
                topLeft = Offset(centerX - barWidth / 2f, top),
                size = Size(barWidth, barHeight.coerceAtLeast(4f)),
            )
        }
    }
}
