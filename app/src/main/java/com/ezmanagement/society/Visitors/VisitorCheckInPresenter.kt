package com.ezmanagement.society.Visitors

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleCoroutineScope
import com.apollographql.apollo3.api.Optional
import com.ezmanagement.society.NewVisitorCheckINMutation
import com.ezmanagement.society.Retrofit.ApiClient
import com.ezmanagement.society.Visitors.RegisterVisirtors.AddVisitorDetails
import com.ezmanagement.society.sharedPreference.SharedPref
import com.ezmanagement.society.type.VisitorDataInput
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class VisitorCheckInPresenter (private val lifecycleScope: LifecycleCoroutineScope,) {
    var sharedPref: SharedPref? = null

    @RequiresApi(Build.VERSION_CODES.O)
    fun visitorCheckIn(
        jwt_token: String,
        visitorname: String?,
        visitorType: String?,
        contact_no: String?,
        check_in: String,
        guardId: String,
        flat_no: String,
        societyId: String,
        isverified: Boolean,
        image:String,
        addVisitorDetails: AddVisitorDetails
    ) {
        val gson = GsonBuilder().create()
        var apiClient = ApiClient()

        lifecycleScope.launchWhenResumed {
            try {

               val visitorDatainput=VisitorDataInput(check_in,Optional.present(contact_no),Optional.present(flat_no),guardId,Optional.present(image),Optional.present(check_in),Optional.present(visitorname),societyId,Optional.present(isverified.toString()))
                val response = apiClient!!.getApolloClient().mutation(
                    NewVisitorCheckINMutation(visitor_type = visitorType.toString(), visitor_data = visitorDatainput)
                ).addHttpHeader(
                    "Authorization", "Bearer $jwt_token"
                ).execute()
                Log.d("RESPONSE_ERROR", response.errors.toString())
                var data: NewVisitorCheckINMutation.Data? = response.data;
                val gson = Gson()
                if(data?.visitorsCheckin!=null){
                    addVisitorDetails.visiterCheckInSuccessfully(data.visitorsCheckin!!.message,societyId)
                }else{
                   Toast.makeText(addVisitorDetails,"Visitor CheckIn Failed",Toast.LENGTH_SHORT).show()
                }
                val result = gson.toJson(data)
            } catch (e: Exception) {
                Log.d("apierror", e.message.toString())
                var response = e.message
            }

        }
    }
}