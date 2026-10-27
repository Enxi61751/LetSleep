package com.letsleep.app.data.model

import java.time.LocalDate
import java.time.LocalTime

data class BedtimeTask(
    val id: String,
    val title: String,
    val minutesBeforePlan: Int,
    val isDone: Boolean = false,
)

enum class LateReason(val label: String) {
    ShortVideo("短视频停不下来"),
    RelaxMore("还想放松一会儿"),
    WorkLate("学习工作结束太晚"),
    NotSleepy("没有困意"),
    Other("其他"),
}

enum class ReminderStyle(val label: String) {
    NormalNotification("普通通知"),
    StrongPopup("强弹窗（演示）"),
}

enum class ReminderChoice {
    EndEntertainment,
    SnoozeTenMinutes,
    SkipTonight,
}

data class NightPlan(
    val planEndTime: LocalTime = LocalTime.of(23, 0),
    val isActiveTonight: Boolean = false,
    val reminderEnabled: Boolean = true,
)

data class ReminderSimulationLog(
    val date: LocalDate,
    val choice: ReminderChoice,
    val message: String,
)

data class ReviewEntry(
    val date: LocalDate,
    val planEndTime: LocalTime,
    val actualEndTime: LocalTime,
    val deltaMinutes: Int,
    val reason: LateReason,
    val isMock: Boolean,
)

data class AppSettings(
    val reminderEnabled: Boolean = true,
    val reminderStyle: ReminderStyle = ReminderStyle.NormalNotification,
)
