package com.example.mobiledatamonitor.data

import android.content.Context
import android.content.SharedPreferences
import java.time.Instant

data class UsageDeltaResult(
    val sentMegabytes: Double,
    val timestamp: Instant
)

object BackendSyncHelper {
    private const val KEY_LAST_TOTAL_MOBILE_BYTES = "last_total_mobile_bytes"
    private const val MIN_DELTA_MB = 0.1

    suspend fun syncDeltaIfNeeded(
        context: Context,
        prefs: SharedPreferences,
        totals: UsageTotals
    ): UsageDeltaResult? {
        val mobileBytes = totals.mobileRxBytes + totals.mobileTxBytes
        val lastBytes = prefs.getLong(KEY_LAST_TOTAL_MOBILE_BYTES, -1L)

        if (mobileBytes <= 0L) {
            prefs.edit().putLong(KEY_LAST_TOTAL_MOBILE_BYTES, mobileBytes).apply()
            return null
        }

        val delta = if (lastBytes >= 0L && mobileBytes > lastBytes) {
            mobileBytes - lastBytes
        } else {
            0L
        }

        prefs.edit().putLong(KEY_LAST_TOTAL_MOBILE_BYTES, mobileBytes).apply()
        if (delta <= 0L) return null

        val megabytes = delta.toDouble() / (1024.0 * 1024.0)
        if (megabytes < MIN_DELTA_MB) return null

        val backendClient = BackendClient(context.applicationContext, prefs)
        val deviceId = backendClient.ensureDeviceId(DataPlanSettings()) ?: return null
        val timestamp = Instant.now()
        backendClient.sendUsage(deviceId, megabytes, timestamp)

        return UsageDeltaResult(sentMegabytes = megabytes, timestamp = timestamp)
    }
}
