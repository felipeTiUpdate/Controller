package com.example.mobiledatamonitor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobiledatamonitor.data.EmployeeProfile
import com.example.mobiledatamonitor.data.UserManager
import com.example.mobiledatamonitor.data.UserRole

class EmployeeSetupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_setup)

        val nameInput = findViewById<EditText>(R.id.employee_name_input)
        val emailInput = findViewById<EditText>(R.id.employee_email_input)
        val phoneInput = findViewById<EditText>(R.id.employee_phone_input)
        val roleGroup = findViewById<RadioGroup>(R.id.employee_role_group)
        val saveButton = findViewById<Button>(R.id.save_employee_button)

        saveButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim().ifBlank { null }
            val phone = phoneInput.text.toString().trim().ifBlank { null }

            if (name.isEmpty()) {
                Toast.makeText(this, "Informe o nome do funcionário", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedRoleId = roleGroup.checkedRadioButtonId
            val role = if (selectedRoleId == R.id.role_admin) UserRole.ADMIN else UserRole.COMMON

            val prefs = getSharedPreferences("data_monitor_prefs", MODE_PRIVATE)
            val userManager = UserManager(prefs)
            val profile = EmployeeProfile(name = name, email = email, phone = phone, role = role)
            userManager.saveEmployee(profile)

            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
    }
}
