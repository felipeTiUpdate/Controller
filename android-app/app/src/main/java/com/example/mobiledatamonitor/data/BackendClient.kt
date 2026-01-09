package com.example.mobiledatamonitor.data

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.time.Instant

class BackendClient(
    private val context: Context,
    private val prefs: SharedPreferences
) {

    companion object {
        private const val PREF_DEVICE_ID = "backend_device_id_v2"
        private const val EMULATOR_URL = "http://10.0.2.2:4000"
        private const val NETWORK_URL = "http://192.168.2.78:4000"
    }

    private val baseUrl: String = if (isRunningOnEmulator()) EMULATOR_URL else NETWORK_URL

    private val userManager = UserManager(prefs)

    suspend fun ensureDeviceId(settings: DataPlanSettings): Int? = withContext(Dispatchers.IO) {
        val existing = prefs.getInt(PREF_DEVICE_ID, -1)
        if (existing > 0) {
            return@withContext existing
        }

        val baseName = "${Build.MANUFACTURER} ${Build.MODEL}".trim().ifBlank { "Android Device" }
        val employee = userManager.getCurrentEmployee()
        val deviceName = if (employee != null) {
            "${employee.name} - $baseName"
        } else {
            baseName
        }
        val limitMb = settings.monthlyLimitBytes / (1024 * 1024)

        val payload = JSONObject().apply {
            put("name", deviceName)
            put("simNumber", JSONObject.NULL)
            put("dataLimitMb", limitMb)
        }

        val response = postJson("/devices", payload) ?: return@withContext null
        val id = response.optInt("id", -1)
        if (id > 0) {
            prefs.edit().putInt(PREF_DEVICE_ID, id).apply()
            id
        } else {
            null
        }
    }

    suspend fun sendUsage(deviceId: Int, megabytes: Double, recordedAt: Instant) = withContext(Dispatchers.IO) {
        val body = JSONObject().apply {
            put("megabytes", megabytes)
            put("networkType", "MOBILE")
            put("description", "Sync from Android app")
            put("recordedAt", recordedAt.toString())
        }

        postJson("/devices/$deviceId/usage", body)
    }

    private fun postJson(path: String, body: JSONObject): JSONObject? {
        val url = URL(baseUrl + path)
        val connection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 5000
            readTimeout = 5000
            doOutput = true
            setRequestProperty("Content-Type", "application/json")
        }

        return try {
            connection.outputStream.use { os ->
                OutputStreamWriter(os, Charsets.UTF_8).use { writer ->
                    writer.write(body.toString())
                }
            }

            val code = connection.responseCode
            if (code in 200..299) {
                val stream = connection.inputStream ?: return null
                val text = stream.bufferedReader(Charsets.UTF_8).use { it.readText() }
                if (text.isNotBlank()) JSONObject(text) else null
            } else {
                null
            }
        } catch (_: Exception) {
            null
        } finally {
            connection.disconnect()
        }
    }
}

private fun isRunningOnEmulator(): Boolean {
    val fingerprint = Build.FINGERPRINT
    val model = Build.MODEL
    val product = Build.PRODUCT

    return (fingerprint != null && fingerprint.contains("generic", ignoreCase = true)) ||
            (model != null && model.contains("Emulator", ignoreCase = true)) ||
            (product != null && product.contains("sdk", ignoreCase = true))
}
