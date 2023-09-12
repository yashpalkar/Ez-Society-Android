package com.ezmanagement.society.Visitors.CheckedInVisitorList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ezmanagement.society.R
import com.ezmanagement.society.databinding.ActivityAddVisitorDetailsBinding
import com.ezmanagement.society.databinding.ActivityCheckInVisitorsBinding

class CheckInVisitors : AppCompatActivity() {
    private lateinit var binding: ActivityCheckInVisitorsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckInVisitorsBinding.inflate(layoutInflater)
        setContentView( binding.root)


    }
}