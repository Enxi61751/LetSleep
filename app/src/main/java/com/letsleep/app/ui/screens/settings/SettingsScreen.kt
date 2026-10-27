package com.letsleep.app.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.letsleep.app.data.LetSleepViewModel
import com.letsleep.app.data.model.ReminderStyle
import com.letsleep.app.ui.components.LetSleepCard

@Composable
fun SettingsScreen(viewModel: LetSleepViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val settings = uiState.settings
    var showPermissionInfo by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("设置", style = MaterialTheme.typography.headlineMedium)

        LetSleepCard {
            RowSwitch(
                title = "睡前提醒",
                subtitle = "当前仅保存偏好，不会发送真实通知",
                checked = settings.reminderEnabled,
                onCheckedChange = viewModel::setReminderEnabled,
            )
        }

        LetSleepCard {
            Text("提醒方式", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            ReminderStyle.entries.forEach { style ->
                RowRadio(
                    selected = settings.reminderStyle == style,
                    label = style.label,
                    onClick = { viewModel.setReminderStyle(style) },
                )
            }
        }

        LetSleepCard {
            Text("权限说明", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Text(
                "未来版本可能申请通知、悬浮窗等权限。当前版本不会弹出系统授权框。",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = { showPermissionInfo = true }) {
                Text("查看说明")
            }
        }
    }

    if (showPermissionInfo) {
        AlertDialog(
            onDismissRequest = { showPermissionInfo = false },
            title = { Text("权限说明（演示）") },
            text = {
                Text(
                    "· 通知：到点 gentle 提醒结束娱乐\n" +
                        "· 悬浮窗/强提醒：在其他 App 上方显示提醒（需用户授权）\n\n" +
                        "本阶段仅在 App 内模拟交互，不申请任何系统权限。",
                )
            },
            confirmButton = {
                TextButton(onClick = { showPermissionInfo = false }) {
                    Text("知道了")
                }
            },
        )
    }
}

@Composable
private fun RowSwitch(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun RowRadio(
    selected: Boolean,
    label: String,
    onClick: () -> Unit,
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Text(label, style = MaterialTheme.typography.bodyLarge)
    }
}
