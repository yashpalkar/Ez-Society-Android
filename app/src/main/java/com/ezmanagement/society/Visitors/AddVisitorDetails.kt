package com.ezmanagement.society.Visitors

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ezmanagement.society.AppConstants
import com.ezmanagement.society.CheckVisitorRegisterQuery
import com.ezmanagement.society.Model.VisitorModel
import com.ezmanagement.society.R
import com.ezmanagement.society.databinding.ActivityAddVisitorBinding
import com.ezmanagement.society.databinding.ActivityAddVisitorDetailsBinding

class AddVisitorDetails : AppCompatActivity(){
    private lateinit var binding: ActivityAddVisitorDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_visitor_details)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        binding = ActivityAddVisitorDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val visitorModel = intent.getParcelableExtra<VisitorModel>(AppConstants.REGISTERED_VISITOR)
        val mobNumber = intent.getStringExtra(AppConstants.CONTACT_NO)
        if (visitorModel != null) {
            binding.visitorNameTextInputLayoutEditText.setText(visitorModel.name)
            binding.visitorMobileTextInputLayoutEditText.setText(visitorModel.contact_no)

        } else if(mobNumber!=null) {
            binding.visitorMobileTextInputLayoutEditText.setText(mobNumber)
        }
    }

}