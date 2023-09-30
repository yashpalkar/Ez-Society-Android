package com.ezmanagement.society

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.Nullable

import androidx.appcompat.app.AppCompatActivity


abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun gotoActivity(target: Intent, killme:Boolean){
      startActivity(intent)
        if(killme){
            this.finish()
        }
        return


    }
}
