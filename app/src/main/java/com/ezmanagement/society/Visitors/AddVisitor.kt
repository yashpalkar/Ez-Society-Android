package com.ezmanagement.society.Visitors


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ezmanagement.society.AppConstants
import com.ezmanagement.society.CheckVisitorRegisterQuery
import com.ezmanagement.society.R
import com.ezmanagement.society.databinding.ActivityAddVisitorBinding
import androidx.lifecycle.lifecycleScope
import com.ezmanagement.society.Model.VisitorModel
import com.ezmanagement.society.sharedPreference.SharedPref

class AddVisitor : AppCompatActivity(),OnClickListener,VisitorCallBack.CheckVisitorCallBack {
    private lateinit var binding: ActivityAddVisitorBinding
    lateinit  var visitorPresenter:VisitorPresenter
    var sharedPref: SharedPref? = null
    var editor: SharedPreferences.Editor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVisitorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.submitNumberBtn.setOnClickListener(this)
        sharedPref = SharedPref(this);
         visitorPresenter=VisitorPresenter(lifecycleScope)
    }
    override fun onClick(p0: View?) {
        when (p0?.id) {
           R.id.submitNumberBtn -> {
               mobileNumberValidation(binding.mobileNumberTextInputLayoutEditText.text.toString())
//                startActivity(Intent(this, AddVisitor::class.java))
            }
        }
    }
    fun mobileNumberValidation(mobNumber: String){
        if(mobNumber.length!=10){
            Toast.makeText(this,"Please Check NUmber",Toast.LENGTH_SHORT).show()
        }
        else{
            val societyId = sharedPref!!.getUserData(
                AppConstants.SOCIETY_ID,
                String::class.java,
                ""
            )
            val jwtToken = sharedPref!!.getUserData(
                AppConstants.JWTTOKEN,
                String::class.java,
                ""
            )
            visitorPresenter.checkVisitor(societyId,mobNumber,jwtToken,this@AddVisitor)
        }

    }

    override fun isVisitorRegister(checkVisitorRegisterQuerySociety_visitor: CheckVisitorRegisterQuery.Society_visitor) {
        val visitorModel =
            VisitorModel(
            contact_no = checkVisitorRegisterQuerySociety_visitor.contact_no,
            created_at = checkVisitorRegisterQuerySociety_visitor.created_at, // Replace with the actual value
            guard_id = checkVisitorRegisterQuerySociety_visitor.guard_id,   // Replace with the actual value
            id = checkVisitorRegisterQuerySociety_visitor.id,         // Replace with the actual value
            last_visited_at = checkVisitorRegisterQuerySociety_visitor.last_visited_at, // Replace with the actual value
            society_id = checkVisitorRegisterQuerySociety_visitor.society_id, // Replace with the actual value
            name = checkVisitorRegisterQuerySociety_visitor.name,
            verified = checkVisitorRegisterQuerySociety_visitor.verified,
            updated_at = checkVisitorRegisterQuerySociety_visitor.updated_at  // Replace with the actual value
        )
        var intent=Intent(this@AddVisitor,AddVisitorDetails::class.java)
        intent.putExtra(AppConstants.REGISTERED_VISITOR,visitorModel)
        startActivity(intent)
    }

    override fun isEmpty(contactNumber:String) {
        var intent=Intent(this@AddVisitor,AddVisitorDetails::class.java)
        intent.putExtra(AppConstants.CONTACT_NO,contactNumber)
        startActivity(intent)
    }

    override fun onError() {

    }

    override fun onFailure(message: String) {
        Log.d("Result.Failure",message)

    }
}
