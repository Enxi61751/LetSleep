package com.letsleep.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.letsleep.app.data.model.ReminderChoice
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

@Composable
fun ReminderPreviewDialog(
    planEndTime: LocalTime,
    onChoice: (ReminderChoice) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("提醒预览（App 内模拟）")
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                FatCatIllustration(size = 120.dp)
                Text(
                    text = "计划 ${planEndTime.format(timeFormatter)} 结束娱乐",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
                Text(
                    text = "这不是系统悬浮窗，仅用于预览未来提醒样式。",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            ) {
                Button(
                    onClick = { onChoice(ReminderChoice.EndEntertainment) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("结束娱乐")
                }
                OutlinedButton(
                    onClick = { onChoice(ReminderChoice.SnoozeTenMinutes) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("再玩 10 分钟")
                }
                TextButton(
                    onClick = { onChoice(ReminderChoice.SkipTonight) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("今晚跳过")
                }
            }
        },
        dismissButton = {},
    )
}
