package com.letsleep.app.ui.screens.todos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.letsleep.app.data.LetSleepViewModel
import com.letsleep.app.ui.components.LetSleepCard

@Composable
fun TodoScreen(viewModel: LetSleepViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var newTitle by remember { mutableStateOf("") }
    var leadMinutes by remember { mutableIntStateOf(30) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
    ) {
        Text("睡前待办", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text(
            "提前量仅作计划参考，当前版本不会真正定时提醒。",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(16.dp))

        LetSleepCard {
            OutlinedTextField(
                value = newTitle,
                onValueChange = { newTitle = it },
                label = { Text("新任务") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = leadMinutes.toString(),
                onValueChange = { value ->
                    leadMinutes = value.filter { it.isDigit() }.toIntOrNull() ?: 0
                },
                label = { Text("提前于计划结束（分钟）") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            Spacer(Modifier.height(8.dp))
            TextButton(
                onClick = {
                    viewModel.addTask(newTitle, leadMinutes)
                    newTitle = ""
                },
                modifier = Modifier.align(Alignment.End),
            ) {
                Text("添加")
            }
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(uiState.tasks, key = { it.id }) { task ->
                LetSleepCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = task.isDone,
                            onCheckedChange = { viewModel.toggleTaskDone(task.id) },
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                task.title,
                                style = MaterialTheme.typography.titleMedium,
                            )
                            OutlinedTextField(
                                value = task.minutesBeforePlan.toString(),
                                onValueChange = { value ->
                                    val minutes = value.filter { it.isDigit() }.toIntOrNull() ?: 0
                                    viewModel.updateTaskLeadTime(task.id, minutes)
                                },
                                label = { Text("提前（分钟）") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                            )
                        }
                        IconButton(onClick = { viewModel.removeTask(task.id) }) {
                            Icon(Icons.Outlined.Delete, contentDescription = "删除")
                        }
                    }
                }
            }
        }
    }
}
