package com.ezmanagement.society

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class AddVisitorDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_visitor_details)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }



    }
}