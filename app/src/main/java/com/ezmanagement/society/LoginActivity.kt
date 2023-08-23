package com.ezmanagement.society

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ezmanagement.society.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            // Handle login button click
            val email = binding.emaileditText.text.toString()
            val password = binding.passwordeditText.text.toString()

            // Perform authentication logic
            // For example, validate inputs and call API

            // Sample: Navigate to the next screen on successful login
            if (isValid(email, password)) {
                startActivity(Intent(this,MainActivity::class.java))
            }
        }
    }

    private fun isValid(email: String, password: String): Boolean {
        // Add your validation logic here
        return email.isNotBlank() && password.isNotBlank()
    }
}