package com.ezmanagement.society.Visitors.CheckInVisitors

import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import com.ezmanagement.society.CheckVisitorRegisterQuery
import com.ezmanagement.society.GetVisitorCheckInQuery
import com.ezmanagement.society.Retrofit.ApiClient
import com.ezmanagement.society.Visitors.AddVisitor
import com.ezmanagement.society.sharedPreference.SharedPref
import com.google.gson.Gson

class CheckInVisitorPresenter(private val lifecycleScope: LifecycleCoroutineScope,) {
        var sharedPref: SharedPref? = null
        fun getcheckVisitor(
            societyid: String,
            jwttoken: String,addVisitor: AddVisitor
        ) {
            sharedPref = SharedPref(addVisitor);
            var apiClient = ApiClient()

//        var response:Response
            lifecycleScope.launchWhenResumed {
                try {
                    val response =
                        apiClient!!.getApolloClient().query(GetVisitorCheckInQuery((societyid))).addHttpHeader(
                            "Authorization", "Bearer $jwttoken"
                        ).execute()
                    Log.d("RESPONSE_ERROR", response.errors.toString())
                    var data: GetVisitorCheckInQuery.Data? = response.data;
                    val gson = Gson()
                    val result = gson.toJson(data)
                    var societyVisitorList: List<GetVisitorCheckInQuery.Society_visitors_checkin>? =
                        data?.society_visitors_checkin
                    if(societyVisitorList.isNullOrEmpty()){

                    }else{

                    }
                } catch (e: Exception) {
                    Log.d("apierror", e.message.toString())
                    addVisitor.onFailure(e.message.toString())
                }
//            return response?.data?.society_id
            }
        }
    }
