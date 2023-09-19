package com.ezmanagement.society.Visitors

import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import com.ezmanagement.society.AddVisitorCheckinMutation
import com.ezmanagement.society.Retrofit.ApiClient
import com.ezmanagement.society.Visitors.RegisterVisirtors.AddVisitorContract
import com.ezmanagement.society.Visitors.RegisterVisirtors.AddVisitorDetails
import com.ezmanagement.society.sharedPreference.SharedPref
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class VisitorCheckInPresenter (private val lifecycleScope: LifecycleCoroutineScope,) {
    var sharedPref: SharedPref? = null

    fun visitorCheckIn(
        jwt_token: String,
        check_in: String,
        guardId: String,
        flat_no: String,
        societyId: String,
        is_allowed: Boolean,
        visitorId:String,
        addVisitorDetails: AddVisitorDetails
    ) {
        val gson = GsonBuilder().create()
        var apiClient = ApiClient()
        lifecycleScope.launchWhenResumed {
            try {

                val response = apiClient!!.getApolloClient().mutation(
                    AddVisitorCheckinMutation(check_in,flat_no,guardId,is_allowed.toString(),societyId, visitorId)
                ).addHttpHeader(
                    "Authorization", "Bearer $jwt_token"
                ).execute()
                Log.d("RESPONSE_ERROR", response.errors.toString())
                var data: AddVisitorCheckinMutation.Data? = response.data;
                val gson = Gson()
                if(data?.insert_society_visitors_checkin_one?.id!=null){
                    addVisitorDetails.visiterCheckInSuccessfully(data?.insert_society_visitors_checkin_one!!)
                }
                val result = gson.toJson(data)

            } catch (e: Exception) {
                Log.d("apierror", e.message.toString())
                var response = e.message
            }

        }
    }
}