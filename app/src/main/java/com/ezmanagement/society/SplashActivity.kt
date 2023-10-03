package com.ezmanagement.society

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ezmanagement.society.sharedPreference.SharedPref
import com.ezmanagement.society.utils.RefreshTokenCallBack
import com.ezmanagement.society.utils.RefreshTokenClass

class SplashActivity : AppCompatActivity(), RefreshTokenCallBack {
    private lateinit var splashViewModel: SplashViewModel
    var sharedPref: SharedPref? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        sharedPref = SharedPref(this);
        splashViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        val login_token = sharedPref!!.getUserData(
            AppConstants.LOGIN_TOKEN,
            String::class.java,
            ""
        )
        if (login_token.isNotEmpty()) {
            var refreshTokenClass: RefreshTokenClass = RefreshTokenClass(this)
            refreshTokenClass.onGetRefreshToken(login_token, this)

        }else{
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            this.finish()
        }

    }

    override fun onSucess() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        this.finish()
    }

    override fun onError() {
        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
        this.finish()
    }

    override fun onRefreshTokenExpired(errorMessage: String) {
        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
        finish()
    }
}