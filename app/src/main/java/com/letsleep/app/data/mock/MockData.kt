package com.letsleep.app.data.mock

import com.letsleep.app.data.model.BedtimeTask
import com.letsleep.app.data.model.LateReason
import com.letsleep.app.data.model.ReviewEntry
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

object MockData {

    fun defaultTasks(): List<BedtimeTask> = listOf(
        BedtimeTask(id = uuid(), title = "洗澡", minutesBeforePlan = 45),
        BedtimeTask(id = uuid(), title = "刷牙", minutesBeforePlan = 20),
        BedtimeTask(id = uuid(), title = "收拾明天的东西", minutesBeforePlan = 30),
    )

    /** 最近 7 天模拟复盘，仅供 UI 演示。 */
    fun mockReviewHistory(today: LocalDate = LocalDate.now()): List<ReviewEntry> {
        val plan = LocalTime.of(23, 0)
        val deltas = listOf(15, -5, 30, 0, 45, 10, 20)
        val reasons = listOf(
            LateReason.ShortVideo,
            LateReason.NotSleepy,
            LateReason.RelaxMore,
            LateReason.WorkLate,
            LateReason.ShortVideo,
            LateReason.Other,
            LateReason.RelaxMore,
        )
        return (6 downTo 0).map { offset ->
            val date = today.minusDays(offset.toLong())
            val delta = deltas[6 - offset]
            ReviewEntry(
                date = date,
                planEndTime = plan,
                actualEndTime = plan.plusMinutes(delta.toLong()),
                deltaMinutes = delta,
                reason = reasons[6 - offset],
                isMock = true,
            )
        }
    }

    private fun uuid(): String = UUID.randomUUID().toString()
}
