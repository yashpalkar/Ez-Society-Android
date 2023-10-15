package com.ezmanagement.society

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.ezmanagement.society.databinding.ActivityMainBinding
import com.ezmanagement.society.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.editProfileButton.setOnClickListener(View.OnClickListener { Toast.makeText(this,"Under Development",Toast.LENGTH_SHORT).show() })
    }
}