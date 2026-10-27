package com.letsleep.app.data

import com.letsleep.app.data.model.AppSettings
import com.letsleep.app.data.model.BedtimeTask
import com.letsleep.app.data.model.NightPlan
import com.letsleep.app.data.model.ReminderSimulationLog
import com.letsleep.app.data.model.ReviewEntry

data class LetSleepUiState(
    val nightPlan: NightPlan = NightPlan(),
    val tasks: List<BedtimeTask> = emptyList(),
    val settings: AppSettings = AppSettings(),
    val reviewHistory: List<ReviewEntry> = emptyList(),
    val reminderLogs: List<ReminderSimulationLog> = emptyList(),
    val lastReminderFeedback: String? = null,
) {
    val tasksDoneCount: Int get() = tasks.count { it.isDone }
    val tasksTotal: Int get() = tasks.size
}
