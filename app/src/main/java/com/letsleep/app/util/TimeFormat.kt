package com.letsleep.app.util

import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val displayFormatter = DateTimeFormatter.ofPattern("HH:mm")

fun LocalTime.toDisplayString(): String = format(displayFormatter)

/** 实际结束时间相对计划的偏差（分钟），正数表示比计划晚。 */
fun minutesAfterPlan(plan: LocalTime, actual: LocalTime): Int {
    val planMinutes = plan.hour * 60 + plan.minute
    var actualMinutes = actual.hour * 60 + actual.minute
    if (actualMinutes < planMinutes) {
        actualMinutes += 24 * 60
    }
    return actualMinutes - planMinutes
}
