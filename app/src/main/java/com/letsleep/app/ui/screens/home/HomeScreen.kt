package com.letsleep.app.ui.screens.home

import android.app.TimePickerDialog
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
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.letsleep.app.data.LetSleepViewModel
import com.letsleep.app.ui.components.FatCatIllustration
import com.letsleep.app.ui.components.LetSleepCard
import com.letsleep.app.ui.components.ReminderPreviewDialog
import com.letsleep.app.util.toDisplayString
import java.time.LocalTime

@Composable
fun HomeScreen(
    viewModel: LetSleepViewModel,
    onOpenTodos: () -> Unit,
    onOpenReview: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val plan = uiState.nightPlan
    val context = LocalContext.current
    var showReminderPreview by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("今晚计划", style = MaterialTheme.typography.headlineMedium)
        Text(
            "肥猫陪你慢慢收工，不催不骂。",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        LetSleepCard {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                FatCatIllustration()
                Spacer(Modifier.height(8.dp))
                Text(
                    text = if (plan.isActiveTonight) "今晚计划进行中" else "还没开启今晚计划",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }

        LetSleepCard {
            Text("计划结束时间", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Text(
                text = plan.planEndTime.toDisplayString(),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .clickable {
                        val t = plan.planEndTime
                        TimePickerDialog(
                            context,
                            { _, hour, minute -> viewModel.updatePlanEndTime(LocalTime.of(hour, minute)) },
                            t.hour,
                            t.minute,
                            true,
                        ).show()
                    }
                    .padding(vertical = 4.dp),
            )
            Text(
                "点按时间可修改",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { viewModel.startTonightPlan() },
                enabled = !plan.isActiveTonight,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(if (plan.isActiveTonight) "已开启今晚计划" else "开启今晚计划")
            }
        }

        LetSleepCard {
            Text("提醒状态", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            val status = when {
                !plan.reminderEnabled -> "提醒已关闭（可在设置中打开）"
                plan.isActiveTonight -> "已开启 · 到点会提醒你（未来版本）"
                else -> "开启今晚计划后生效"
            }
            Text(status, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = { showReminderPreview = true },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("预览提醒")
            }
        }

        LetSleepCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("睡前待办", style = MaterialTheme.typography.titleMedium)
                Text(
                    "${uiState.tasksDoneCount}/${uiState.tasksTotal}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            Spacer(Modifier.height(8.dp))
            if (uiState.tasksTotal > 0) {
                LinearProgressIndicator(
                    progress = { uiState.tasksDoneCount.toFloat() / uiState.tasksTotal },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick = onOpenTodos, modifier = Modifier.fillMaxWidth()) {
                Text("管理待办")
            }
        }

        LetSleepCard {
            Text("最近一晚复盘", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            val latest = viewModel.latestReview()
            if (latest != null) {
                Text(
                    "${latest.date} · 比计划${if (latest.deltaMinutes >= 0) "晚" else "早"} ${kotlin.math.abs(latest.deltaMinutes)} 分钟",
                    style = MaterialTheme.typography.bodyLarge,
                )
                if (latest.isMock) {
                    Text(
                        "（含模拟示例数据）",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            } else {
                Text("还没有记录", style = MaterialTheme.typography.bodyLarge)
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick = onOpenReview, modifier = Modifier.fillMaxWidth()) {
                Text("去复盘")
            }
        }
    }

    if (showReminderPreview) {
        ReminderPreviewDialog(
            planEndTime = plan.planEndTime,
            onChoice = { choice ->
                viewModel.onReminderPreviewChoice(choice)
                showReminderPreview = false
            },
            onDismiss = { showReminderPreview = false },
        )
    }
}
