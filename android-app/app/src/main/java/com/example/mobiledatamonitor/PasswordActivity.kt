package com.example.mobiledatamonitor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged

class PasswordActivity : AppCompatActivity() {

    private val defaultPassword = "123456"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)
        
        val passwordInput = findViewById<EditText>(R.id.password_input)
        val submitButton = findViewById<Button>(R.id.submit_button)
        
        submitButton.setOnClickListener {
            val enteredPassword = passwordInput.text.toString()
            if (enteredPassword == defaultPassword) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Senha incorreta", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
