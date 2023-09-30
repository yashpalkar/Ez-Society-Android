package com.ezmanagement.society.Visitors

import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import com.ezmanagement.society.AppConstants
import com.ezmanagement.society.CheckVisitorNumberMutation
import com.ezmanagement.society.Retrofit.ApiClient
import com.ezmanagement.society.sharedPreference.SharedPref
import com.google.gson.Gson

class VisitorPresenter(private val lifecycleScope: LifecycleCoroutineScope) {
    var sharedPref: SharedPref? = null
    fun checkVisitor(
        societyid: String,
        mobNumber: String,
        jwttoken: String, addVisitor: AddVisitor
    ) {
        sharedPref = SharedPref(addVisitor);
        var apiClient = ApiClient()

//        var response:Response
        lifecycleScope.launchWhenResumed {
            try {
                val response =
                    apiClient!!.getApolloClient()
                        .mutation(CheckVisitorNumberMutation(mobNumber, societyid)).addHttpHeader(
                        "Authorization", "Bearer $jwttoken"
                    ).execute()
                Log.d("RESPONSE_ERROR", response.errors.toString())
                var data: CheckVisitorNumberMutation.Data? = response.data;
                val gson = Gson()
                val result = gson.toJson(data)
                var checkVisitorNumber: CheckVisitorNumberMutation.CheckVisitorNumber? =
                    data?.checkVisitorNumber
                if (checkVisitorNumber != null) {
                    if (checkVisitorNumber.visitor_type.equals(AppConstants.NEWVISITOR)) {
                        addVisitor.isNewVisitor(
                            contactNumber = mobNumber,
                            checkVisitorNumber.visitor_type
                        )
                    } else if (checkVisitorNumber.visitor_type.equals(AppConstants.RECHECKIN)) {
                        addVisitor.isRecheckIn(checkVisitorNumber)
                    }
                } else {
                    addVisitor.isNewVisitor(contactNumber = mobNumber, AppConstants.NEWVISITOR)
                }
            } catch (e: Exception) {
                Log.d("apierror", e.message.toString())
                addVisitor.onFailure(e.message.toString())
            }
//            return response?.data?.society_id
        }
    }


}