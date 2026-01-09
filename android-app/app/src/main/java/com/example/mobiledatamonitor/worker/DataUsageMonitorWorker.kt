package com.example.mobiledatamonitor.worker

import android.content.Context
import android.content.SharedPreferences
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.mobiledatamonitor.data.DataPlanSettings
import com.example.mobiledatamonitor.data.DataUsageRepository
import com.example.mobiledatamonitor.data.UsageRange
import com.example.mobiledatamonitor.data.BackendClient
import java.time.Instant
import com.example.mobiledatamonitor.notifications.NotificationHelper

class DataUsageMonitorWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val repository = DataUsageRepository(context)
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        const val WORK_NAME = "data_usage_monitor"
        private const val PREFS_NAME = "data_monitor_prefs"
        private const val KEY_LAST_PLAN_WARNING = "last_plan_warning"
        private const val KEY_LAST_PLAN_EXCEEDED = "last_plan_exceeded"
        private const val KEY_NOTIFIED_APPS_PREFIX = "notified_app_"
        private const val KEY_LAST_TOTAL_MOBILE_BYTES = "last_total_mobile_bytes"
        
        // Limites para alertas
        const val APP_HIGH_USAGE_THRESHOLD_MB = 50L // 50MB por app (mais sensível)
        const val PLAN_WARNING_PERCENTAGE = 70f // Alertar quando usar 70%
        const val PLAN_CRITICAL_PERCENTAGE = 90f // Alertar quando usar 90%
    }

    private suspend fun syncBackend() {
        val totals = repository.readUsage(UsageRange.TODAY) ?: return
        val mobileBytes = totals.mobileRxBytes + totals.mobileTxBytes
        val lastBytes = prefs.getLong(KEY_LAST_TOTAL_MOBILE_BYTES, -1L)

        if (mobileBytes <= 0L) {
            prefs.edit().putLong(KEY_LAST_TOTAL_MOBILE_BYTES, mobileBytes).apply()
            return
        }

        val delta = if (lastBytes >= 0L && mobileBytes > lastBytes) {
            mobileBytes - lastBytes
        } else {
            0L
        }

        prefs.edit().putLong(KEY_LAST_TOTAL_MOBILE_BYTES, mobileBytes).apply()
        if (delta <= 0L) return

        val megabytes = delta.toDouble() / (1024.0 * 1024.0)
        if (megabytes <= 0.1) return

        val client = BackendClient(applicationContext, prefs)
        val deviceId = client.ensureDeviceId(DataPlanSettings()) ?: return
        client.sendUsage(deviceId, megabytes, Instant.now())
    }

    override suspend fun doWork(): Result {
        return try {
            checkAppUsage()
            checkPlanUsage()
            syncBackend()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private fun checkAppUsage() {
        val appsUsage = repository.getAppsUsage(UsageRange.TODAY)
        val thresholdBytes = APP_HIGH_USAGE_THRESHOLD_MB * 1024 * 1024

        appsUsage.forEach { app ->
            // Verificar apenas consumo de dados móveis
            if (app.mobileTotalBytes >= thresholdBytes) {
                val notifiedKey = "$KEY_NOTIFIED_APPS_PREFIX${app.packageName}_${getTodayKey()}"
                val alreadyNotified = prefs.getBoolean(notifiedKey, false)

                if (!alreadyNotified) {
                    NotificationHelper.sendHighUsageAppNotification(
                        context = applicationContext,
                        appName = app.appName,
                        usageBytes = app.mobileTotalBytes,
                        notificationId = app.uid
                    )
                    prefs.edit().putBoolean(notifiedKey, true).apply()
                }
            }
        }
    }

    private fun checkPlanUsage() {
        val settings = DataPlanSettings()
        val status = repository.getDataPlanStatus(settings)

        val todayKey = getTodayKey()

        // Verificar se excedeu o limite
        if (status.isOverLimit) {
            val lastExceededNotification = prefs.getString(KEY_LAST_PLAN_EXCEEDED, "")
            if (lastExceededNotification != todayKey) {
                NotificationHelper.sendPlanExceededNotification(applicationContext)
                prefs.edit().putString(KEY_LAST_PLAN_EXCEEDED, todayKey).apply()
            }
            return
        }

        // Verificar se está em nível crítico (90%)
        if (status.usagePercentage >= PLAN_CRITICAL_PERCENTAGE) {
            val lastWarningKey = "${KEY_LAST_PLAN_WARNING}_critical_$todayKey"
            val alreadyNotified = prefs.getBoolean(lastWarningKey, false)
            
            if (!alreadyNotified) {
                NotificationHelper.sendPlanWarningNotification(
                    context = applicationContext,
                    usagePercentage = status.usagePercentage,
                    remainingGB = status.remainingGB,
                    daysRemaining = status.daysRemainingInCycle
                )
                prefs.edit().putBoolean(lastWarningKey, true).apply()
            }
            return
        }

        // Verificar se está em nível de alerta (70%)
        if (status.usagePercentage >= PLAN_WARNING_PERCENTAGE) {
            val lastWarningKey = "${KEY_LAST_PLAN_WARNING}_warning_$todayKey"
            val alreadyNotified = prefs.getBoolean(lastWarningKey, false)
            
            if (!alreadyNotified) {
                NotificationHelper.sendPlanWarningNotification(
                    context = applicationContext,
                    usagePercentage = status.usagePercentage,
                    remainingGB = status.remainingGB,
                    daysRemaining = status.daysRemainingInCycle
                )
                prefs.edit().putBoolean(lastWarningKey, true).apply()
            }
        }
    }

    private fun getTodayKey(): String {
        val calendar = java.util.Calendar.getInstance()
        return "${calendar.get(java.util.Calendar.YEAR)}_${calendar.get(java.util.Calendar.DAY_OF_YEAR)}"
    }
}
