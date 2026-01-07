package com.example.mobiledatamonitor.data

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

enum class UsageRange(val daysBack: Long) {
    TODAY(0),
    LAST_7_DAYS(6),
    LAST_30_DAYS(29);

    fun computeStartTimestamp(nowMillis: Long = System.currentTimeMillis()): Long {
        val zoneId = ZoneId.systemDefault()
        val nowDate = Instant.ofEpochMilli(nowMillis).atZone(zoneId).toLocalDate()
        val startDate: LocalDate = nowDate.minusDays(daysBack)
        return startDate.atStartOfDay(zoneId).toInstant().toEpochMilli()
    }
}
