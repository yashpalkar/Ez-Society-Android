package com.ezmanagement.society.Visitors

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ezmanagement.society.*
import com.ezmanagement.society.Model.VisitorModel
import com.ezmanagement.society.databinding.ActivityAddVisitorDetailsBinding
import com.ezmanagement.society.sharedPreference.SharedPref
import java.time.ZonedDateTime
import java.util.*

class AddVisitorDetails : AppCompatActivity(), View.OnClickListener,
    VisitorCallBack.RegisterVisitorCallBack, VisitorCallBack.VisitorCheckInCallBack {
    private lateinit var binding: ActivityAddVisitorDetailsBinding
    var sharedPref: SharedPref? = null
    var visitorModel: VisitorModel? = null
    var addVisitorDetailsPresenter: AddVisitorDetailsPresenter? = null
    var visitorDetailsPresenter: VisitorCheckInPresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVisitorDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = SharedPref(this);
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        addVisitorDetailsPresenter = AddVisitorDetailsPresenter(lifecycleScope)
        visitorDetailsPresenter = VisitorCheckInPresenter(lifecycleScope)

        visitorModel = intent.getParcelableExtra<VisitorModel>(AppConstants.REGISTERED_VISITOR)
        val mobNumber = intent.getStringExtra(AppConstants.CONTACT_NO)
        if (visitorModel != null) {
            binding.visitorNameTextInputLayoutEditText.setText(visitorModel?.name)
            binding.visitorMobileTextInputLayoutEditText.setText(visitorModel?.contact_no)

        } else if (mobNumber != null) {
            binding.visitorMobileTextInputLayoutEditText.setText(mobNumber)
        }
        val jwtToken = sharedPref!!.getUserData(
            AppConstants.JWTTOKEN,
            String::class.java,
            ""
        )
        binding.submitVisitorButton.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(p0: View?) {
        when (p0?.id) {

            R.id.submitVisitorButton -> {
                val currentDateTime = ZonedDateTime.now()
                val checkInTime = currentDateTime.format(AppConstants.formatter)
                val jwtToken = sharedPref!!.getUserData(
                    AppConstants.JWTTOKEN,
                    String::class.java,
                    ""
                )
                val guardId = sharedPref!!.getUserData(
                    AppConstants.GUARDID,
                    String::class.java,
                    ""
                )
                val societyId = sharedPref!!.getUserData(
                    AppConstants.SOCIETY_ID,
                    String::class.java,
                    ""
                )
                if (visitorModel?.id != null) {
                    visitorDetailsPresenter?.visitorCheckIn(
                        jwtToken,
                        checkInTime,
                        guardId = visitorModel?.guard_id.toString(),
                        binding.personToMeetLayoutEditText.text.toString(),
                        visitorModel?.society_id.toString(),
                        true,
                        visitorModel?.id.toString(),
                        this@AddVisitorDetails
                    )

                } else {
                    addVisitorDetailsPresenter?.registervisitor(
                        jwtToken,
                        binding.visitorMobileTextInputLayoutEditText.text.toString(),
                        guardId,
                        checkInTime,
                        binding.visitorNameTextInputLayoutEditText.text.toString(),
                        societyId,
                        false,
                        this@AddVisitorDetails
                    )
                }

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun visiterRegisterSuccessfully(registerVisitor: RegisterVisitorMutation.Insert_society_visitors_one) {
        Log.d("registerVisitor.id", registerVisitor.id.toString())
        val currentDateTime = ZonedDateTime.now()
        val checkIn = currentDateTime.format(AppConstants.formatter)
        val jwtToken = sharedPref!!.getUserData(
            AppConstants.JWTTOKEN,
            String::class.java,
            ""
        )
        visitorDetailsPresenter?.visitorCheckIn(
            jwtToken,
            checkIn,
            guardId = registerVisitor.guard_id.toString(),
            binding.personToMeetLayoutEditText.text.toString(),
            registerVisitor.society_id.toString(),
            true,
            registerVisitor.id.toString(),
            this@AddVisitorDetails
        )

    }

    override fun visiterCheckInSuccessfully(addVisitorCheckinMutation: AddVisitorCheckinMutation.Insert_society_visitors_checkin_one) {
        Toast.makeText(this, "Visitor Check In SuccessFully", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this,MainActivity::class.java))
    }

    override fun onError() {
        TODO("Not yet implemented")
    }

    override fun onFailure(message: String) {
        TODO("Not yet implemented")
    }


}