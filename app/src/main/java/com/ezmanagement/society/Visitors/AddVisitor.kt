package com.ezmanagement.society.Visitors


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import com.ezmanagement.society.AppConstants
import com.ezmanagement.society.CheckVisitorRegisterQuery
import com.ezmanagement.society.R
import com.ezmanagement.society.databinding.ActivityAddVisitorBinding
import androidx.lifecycle.lifecycleScope
import com.ezmanagement.society.CheckVisitorNumberMutation
import com.ezmanagement.society.Model.VisitorModel
import com.ezmanagement.society.Visitors.RegisterVisirtors.AddVisitorDetails
import com.ezmanagement.society.sharedPreference.SharedPref
import org.json.JSONObject

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
       var contact_no = intent.getStringExtra(AppConstants.CONTACT_NO)
        if(contact_no?.isNotEmpty() == true){
            binding.mobileNumberTextInputLayoutEditText.setText(contact_no)
        }

        val items = arrayOf("+91")

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, items)

        binding.codeTextView.setAdapter(adapter)
        if (items.isNotEmpty()) {
            binding.codeTextView.setText(items[0], false)
        }
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

//    override fun isVisitorRegister(checkVisitorRegisterQuerySociety_visitor: CheckVisitorRegisterQuery.Society_visitor) {
//        val visitorModel =
//            VisitorModel(
//            contact_no = checkVisitorRegisterQuerySociety_visitor.contact_no,
//            created_at = checkVisitorRegisterQuerySociety_visitor.created_at, // Replace with the actual value
//            guard_id = checkVisitorRegisterQuerySociety_visitor.guard_id,   // Replace with the actual value
//            id = checkVisitorRegisterQuerySociety_visitor.id,         // Replace with the actual value
//            last_visited_at = checkVisitorRegisterQuerySociety_visitor.last_visited_at, // Replace with the actual value
//            society_id = checkVisitorRegisterQuerySociety_visitor.society_id, // Replace with the actual value
//            name = checkVisitorRegisterQuerySociety_visitor.name,
//            verified = checkVisitorRegisterQuerySociety_visitor.verified,
//            updated_at = checkVisitorRegisterQuerySociety_visitor.updated_at  // Replace with the actual value
//        )
//        var intent=Intent(this@AddVisitor, AddVisitorDetails::class.java)
//        intent.putExtra(AppConstants.REGISTERED_VISITOR,visitorModel)
//        startActivity(intent)
//    }

    override fun isRecheckIn(checkVisitorNumberMutation: CheckVisitorNumberMutation.CheckVisitorNumber) {
        val json = checkVisitorNumberMutation.visitor_data?.let { JSONObject(it) }
        val visitorModel =
            json?.getBoolean("verified")?.let {
                VisitorModel(
                    contact_no = json.getString("contact_no"),
                    created_at = json.getString("created_at"), // Replace with the actual value
                    guard_id = json.getString("guard_id"),   // Replace with the actual value
                    id = json.getString("id"),         // Replace with the actual value
                    last_visited_at = json.getString("last_visited_at"), // Replace with the actual value
                    society_id = json.getString("society_id"), // Replace with the actual value
                    name = json.getString("name"),
                    image = json.getString("image"),
                    verified = it,
                    updated_at = json.getString("updated_at")  // Replace with the actual value
                )
            }
        var intent=Intent(this@AddVisitor, AddVisitorDetails::class.java)
        intent.putExtra(AppConstants.VISITORTYPE,checkVisitorNumberMutation.visitor_type)
        intent.putExtra(AppConstants.REGISTERED_VISITOR,visitorModel)
        startActivity(intent)
        this.finish()

    }

    override fun isNewVisitor(contactNumber: String, visitor_type: String) {
        var intent=Intent(this@AddVisitor, AddVisitorDetails::class.java)
        intent.putExtra(AppConstants.CONTACT_NO,contactNumber)
        intent.putExtra(AppConstants.VISITORTYPE,visitor_type)
        startActivity(intent)
        this.finish()
    }



    override fun onError() {

    }

    override fun onFailure(message: String) {
        Log.d("Result.Failure",message)

    }
}
