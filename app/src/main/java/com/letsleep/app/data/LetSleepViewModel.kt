package com.letsleep.app.data

import androidx.lifecycle.ViewModel
import com.letsleep.app.data.mock.MockData
import com.letsleep.app.data.model.AppSettings
import com.letsleep.app.data.model.BedtimeTask
import com.letsleep.app.data.model.NightPlan
import com.letsleep.app.data.model.ReminderChoice
import com.letsleep.app.data.model.ReminderSimulationLog
import com.letsleep.app.data.model.ReminderStyle
import com.letsleep.app.data.model.ReviewEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class LetSleepViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        LetSleepUiState(
            tasks = MockData.defaultTasks(),
            reviewHistory = MockData.mockReviewHistory(),
        ),
    )
    val uiState: StateFlow<LetSleepUiState> = _uiState.asStateFlow()

    fun updatePlanEndTime(time: LocalTime) {
        _uiState.update { it.copy(nightPlan = it.nightPlan.copy(planEndTime = time)) }
    }

    fun startTonightPlan() {
        _uiState.update { it.copy(nightPlan = it.nightPlan.copy(isActiveTonight = true)) }
    }

    fun toggleTaskDone(taskId: String) {
        _uiState.update { state ->
            state.copy(
                tasks = state.tasks.map { task ->
                    if (task.id == taskId) task.copy(isDone = !task.isDone) else task
                },
            )
        }
    }

    fun addTask(title: String, minutesBeforePlan: Int) {
        if (title.isBlank()) return
        val task = BedtimeTask(
            id = UUID.randomUUID().toString(),
            title = title.trim(),
            minutesBeforePlan = minutesBeforePlan.coerceAtLeast(0),
        )
        _uiState.update { it.copy(tasks = it.tasks + task) }
    }

    fun removeTask(taskId: String) {
        _uiState.update { it.copy(tasks = it.tasks.filterNot { task -> task.id == taskId }) }
    }

    fun updateTaskLeadTime(taskId: String, minutesBeforePlan: Int) {
        _uiState.update { state ->
            state.copy(
                tasks = state.tasks.map { task ->
                    if (task.id == taskId) {
                        task.copy(minutesBeforePlan = minutesBeforePlan.coerceAtLeast(0))
                    } else {
                        task
                    }
                },
            )
        }
    }

    fun setReminderEnabled(enabled: Boolean) {
        _uiState.update {
            it.copy(
                settings = it.settings.copy(reminderEnabled = enabled),
                nightPlan = it.nightPlan.copy(reminderEnabled = enabled),
            )
        }
    }

    fun setReminderStyle(style: ReminderStyle) {
        _uiState.update { it.copy(settings = it.settings.copy(reminderStyle = style)) }
    }

    fun onReminderPreviewChoice(choice: ReminderChoice) {
        val feedback = when (choice) {
            ReminderChoice.EndEntertainment -> "好耶，肥猫给你点个赞，今晚按计划收工～"
            ReminderChoice.SnoozeTenMinutes -> "再玩 10 分钟也行，记得等会儿回来找肥猫。"
            ReminderChoice.SkipTonight -> "今晚先跳过啦，明天我们再一起试试。"
        }
        val log = ReminderSimulationLog(
            date = LocalDate.now(),
            choice = choice,
            message = feedback,
        )
        _uiState.update {
            it.copy(
                reminderLogs = it.reminderLogs + log,
                lastReminderFeedback = feedback,
            )
        }
    }

    fun clearReminderFeedback() {
        _uiState.update { it.copy(lastReminderFeedback = null) }
    }

    fun saveReviewEntry(entry: ReviewEntry) {
        _uiState.update { state ->
            val withoutToday = state.reviewHistory.filterNot { it.date == entry.date }
            state.copy(reviewHistory = (withoutToday + entry).sortedByDescending { it.date })
        }
    }

    fun latestReview(): ReviewEntry? = _uiState.value.reviewHistory.maxByOrNull { it.date }

    fun applySettings(settings: AppSettings) {
        _uiState.update {
            it.copy(
                settings = settings,
                nightPlan = it.nightPlan.copy(reminderEnabled = settings.reminderEnabled),
            )
        }
    }

    fun resetNightPlanForDemo() {
        _uiState.update {
            it.copy(
                nightPlan = NightPlan(planEndTime = it.nightPlan.planEndTime),
                tasks = it.tasks.map { task -> task.copy(isDone = false) },
            )
        }
    }
}
