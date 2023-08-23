package com.ezmanagement.society

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.ViewModelProvider

class SplashActivity : AppCompatActivity() {
    private lateinit var splashViewModel: SplashViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        splashViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

        // Add any logic related to the splash screen here, such as animations, delays, etc.

        // Example delay before moving to the next activity
        Handler().postDelayed({
            // Start the main activity or the appropriate next screen
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            finish()
        }, 2000) // 2-second delay as an example
    }
}