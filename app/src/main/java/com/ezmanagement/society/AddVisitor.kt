package com.ezmanagement.society


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import com.ezmanagement.society.databinding.ActivityAddVisitorBinding


class AddVisitor : AppCompatActivity(),OnClickListener {
    private lateinit var binding: ActivityAddVisitorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVisitorBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
    override fun onClick(p0: View?) {
        when (p0?.id) {
           R.id.submitNumberBtn -> {
                startActivity(Intent(this, AddVisitor::class.java))
            }
        }
    }
    }
