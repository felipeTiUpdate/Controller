package com.example.mobiledatamonitor.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object WorkManagerHelper {

    fun scheduleDataUsageMonitoring(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // Executar a cada 15 minutos (mínimo permitido pelo WorkManager)
        val workRequest = PeriodicWorkRequestBuilder<DataUsageMonitorWorker>(
            15, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            DataUsageMonitorWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    fun cancelDataUsageMonitoring(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(DataUsageMonitorWorker.WORK_NAME)
    }
}
