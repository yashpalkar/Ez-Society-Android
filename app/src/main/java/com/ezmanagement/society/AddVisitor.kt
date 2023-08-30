package com.ezmanagement.society

import android.R
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ezmanagement.society.databinding.ActivityAddVisitorBinding


class AddVisitor : AppCompatActivity() {
    private lateinit var binding:ActivityAddVisitorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVisitorBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}