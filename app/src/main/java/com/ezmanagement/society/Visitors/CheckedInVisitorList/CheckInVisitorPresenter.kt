package com.ezmanagement.society.Visitors.CheckedInVisitorList

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleCoroutineScope
import com.ezmanagement.society.CheckVisitorRegisterQuery
import com.ezmanagement.society.Retrofit.ApiClient
import com.ezmanagement.society.UpdateCheckVisitorMutation
import com.ezmanagement.society.VisitorListBySocietyIdQuery
import com.ezmanagement.society.Visitors.AddVisitor
import com.ezmanagement.society.sharedPreference.SharedPref
import com.google.gson.Gson
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class CheckInVisitorPresenter(
    private val view: CheckedInVisitorContract.View,
    private val lifecycleScope: LifecycleCoroutineScope
) : CheckedInVisitorContract.Presenter {

    override fun loadVisitorItems(
        societyid: String,
        jwttoken: String
    ) {

        var apiClient = ApiClient()
        lifecycleScope.launchWhenResumed {
            try {

                val response =
                    apiClient!!.getApolloClient().query(VisitorListBySocietyIdQuery(societyid)).addHttpHeader(
                        "Authorization", "Bearer $jwttoken"
                    ).execute()
                Log.d("RESPONSE_ERROR", response.errors.toString())
                var visitorList: List<VisitorListBySocietyIdQuery.Society_visitors_checkin>? =
                    response.data?.society_visitors_checkin;
                val gson = Gson()
                if (!visitorList.isNullOrEmpty()) {
                    view.showItems(visitorList)
                } else {
                    view.showError("error")
                }

            } catch (e: Exception) {
                view.showError(e.message)
            }

//            return response?.data?.society_id
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun updatecheckVisitor(
        societyid: String,
        checkinVisitorId: String,
        checkOutTime: String,
        attendedTime: String,
        jwttoken: String
    ) {
        var apiClient = ApiClient()


//        var response:Response
        lifecycleScope.launchWhenResumed {
            try {
                val response =
                    apiClient!!.getApolloClient().mutation(UpdateCheckVisitorMutation(societyid,checkinVisitorId,attendedTime,checkOutTime)).addHttpHeader(
                        "Authorization", "Bearer $jwttoken"
                    ).execute()
                Log.d("RESPONSE_ERROR", response.errors.toString())
                var data: UpdateCheckVisitorMutation.Data? = response.data;
                val gson = Gson()
                val result = gson.toJson(data)
                var updatevisitor: UpdateCheckVisitorMutation.Update_society_visitors_checkin? =
                    data?.update_society_visitors_checkin

                if(updatevisitor!!.affected_rows>0){
                    view.updateVisitorList()
                }else{

                }
            } catch (e: Exception) {
                Log.d("apierror", e.message.toString())
            }
//            return response?.data?.society_id
        }
    }
}




