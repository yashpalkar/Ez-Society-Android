package com.ezmanagement.society.Visitors

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleCoroutineScope
import com.ezmanagement.society.RegisterVisitorMutation

import com.ezmanagement.society.Retrofit.ApiClient
import com.ezmanagement.society.sharedPreference.SharedPref
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class AddVisitorDetailsPresenter (private val lifecycleScope: LifecycleCoroutineScope,) {
    var sharedPref: SharedPref? = null
    @RequiresApi(Build.VERSION_CODES.O)
    fun registervisitor(
        jwt_token: String,
        contactNo: String,
        guardId: String,
        lastVisitedAT: String,
        visitorName: String,
        societyId: String,
        isVerifed: Boolean,
        addVisitorDetails: AddVisitorDetails
    ) {
        val gson = GsonBuilder().create()
        var apiClient = ApiClient()
        lifecycleScope.launchWhenResumed {
            try {

                val response = apiClient!!.getApolloClient().mutation(
                    RegisterVisitorMutation(contactNo,guardId,lastVisitedAT,visitorName,societyId, isVerifed)
                ).addHttpHeader(
                    "Authorization", "Bearer $jwt_token"
                ).execute()
                Log.d("RESPONSE_ERROR", response.errors.toString())
                var data:RegisterVisitorMutation.Data? = response.data;
                val gson = Gson()
                if(data?.insert_society_visitors_one?.id!=null){
                    addVisitorDetails.visiterRegisterSuccessfully(data.insert_society_visitors_one!!)
                }
                val result = gson.toJson(data)

            } catch (e: Exception) {
                Log.d("apierror", e.message.toString())
                var response = e.message
            }

        }
    }
}