package com.ezmanagement.society.Visitors

import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import com.ezmanagement.society.AppConstants
import com.ezmanagement.society.CheckVisitorRegisterQuery
import com.ezmanagement.society.GetSocietyGuardbyIDQuery
import com.ezmanagement.society.LoginActivity
import com.ezmanagement.society.Retrofit.ApiClient
import com.ezmanagement.society.sharedPreference.SharedPref
import com.google.gson.Gson
import java.util.UUID

class VisitorPresenter(private val lifecycleScope: LifecycleCoroutineScope,) {
    var sharedPref: SharedPref? = null
    fun checkVisitor(
        societyid: String,
        mobNumber:String,
        jwttoken: String,addVisitor: AddVisitor
    ) {
        sharedPref = SharedPref(addVisitor);
        var apiClient = ApiClient()
//        var response:Response
        lifecycleScope.launchWhenResumed {
            try {
                val response =
                    apiClient!!.getApolloClient().query(CheckVisitorRegisterQuery((societyid),mobNumber)).addHttpHeader(
                        "Authorization", "Bearer $jwttoken"
                    ).execute()
                Log.d("RESPONSE_ERROR", response.errors.toString())
                var data: CheckVisitorRegisterQuery.Data? = response.data;
                val gson = Gson()
                val result = gson.toJson(data)
                var society_visitor: List<CheckVisitorRegisterQuery.Society_visitor>? =
                    data?.society_visitors
                if(society_visitor!!.isEmpty()){
                    addVisitor.isEmpty(contactNumber = mobNumber)
                }else{
                    addVisitor.isVisitorRegister(society_visitor.get(0))
                }
            } catch (e: Exception) {
                Log.d("apierror", e.message.toString())
           addVisitor.onFailure(e.message.toString())
            }
//            return response?.data?.society_id
        }
    }
}