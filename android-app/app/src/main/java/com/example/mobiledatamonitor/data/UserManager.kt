package com.example.mobiledatamonitor.data

import android.content.SharedPreferences

class UserManager(private val prefs: SharedPreferences) {

    fun getCurrentEmployee(): EmployeeProfile? {
        val name = prefs.getString(KEY_NAME, null) ?: return null
        val email = prefs.getString(KEY_EMAIL, null)
        val phone = prefs.getString(KEY_PHONE, null)
        val roleString = prefs.getString(KEY_ROLE, UserRole.COMMON.name) ?: UserRole.COMMON.name
        val role = runCatching { UserRole.valueOf(roleString) }.getOrDefault(UserRole.COMMON)
        return EmployeeProfile(name = name, email = email, phone = phone, role = role)
    }

    fun saveEmployee(profile: EmployeeProfile) {
        prefs.edit()
            .putString(KEY_NAME, profile.name)
            .putString(KEY_EMAIL, profile.email)
            .putString(KEY_PHONE, profile.phone)
            .putString(KEY_ROLE, profile.role.name)
            .apply()
    }

    companion object {
        private const val KEY_NAME = "employee_name"
        private const val KEY_EMAIL = "employee_email"
        private const val KEY_PHONE = "employee_phone"
        private const val KEY_ROLE = "employee_role"
    }
}
