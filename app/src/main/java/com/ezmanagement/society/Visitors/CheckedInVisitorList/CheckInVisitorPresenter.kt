package com.ezmanagement.society.Visitors.CheckedInVisitorList

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleCoroutineScope
import com.ezmanagement.society.Retrofit.ApiClient
import com.ezmanagement.society.UpdateCheckVisitorMutation
import com.ezmanagement.society.VisitorListBySocietyIdQuery
import com.google.gson.Gson

class CheckInVisitorPresenter(
    private val view: CheckedInVisitorContract.View,
    private val lifecycleScope: LifecycleCoroutineScope,
    var arraylistsiez:Int=0
) : CheckedInVisitorContract.Presenter {

    override fun loadVisitorItems(
        societyid: String,
        jwttoken: String, offset: Int,limit:Int
    ) {
        view.showProgressbar()
//        if (limit > arraylistsiez) {
//            view.hideProgressbar()
//        }
        var apiClient = ApiClient()
        lifecycleScope.launchWhenResumed {
            try {

                val response =
                    apiClient!!.getApolloClient()
                        .query(VisitorListBySocietyIdQuery(limit, offset, societyid)).addHttpHeader(
                        "Authorization", "Bearer $jwttoken"
                    ).execute()
                Log.d("RESPONSE_ERROR", response.errors.toString())
                var visitorList: List<VisitorListBySocietyIdQuery.Society_visitors_checkin>? =
                    response.data?.society_visitors_checkin;
                val gson = Gson()
                if (!visitorList.isNullOrEmpty()) {
                    if (limit > visitorList.size) {
                        arraylistsiez=visitorList.size
                        view.hideProgressbar()
                    }
                    view.showItems(visitorList)
                } else {
                    view.showError("error")
                    view.hideProgressbar()
                }

            } catch (e: Exception) {
                view.showError(e.message)
                view.hideProgressbar()
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
        view.showProgressbar()

//        var response:Response
        lifecycleScope.launchWhenResumed {
            try {
                val response =
                    apiClient!!.getApolloClient().mutation(
                        UpdateCheckVisitorMutation(
                            societyid,
                            checkinVisitorId,
                            attendedTime,
                            checkOutTime
                        )
                    ).addHttpHeader(
                        "Authorization", "Bearer $jwttoken"
                    ).execute()
                Log.d("RESPONSE_ERROR", response.errors.toString())
                var data: UpdateCheckVisitorMutation.Data? = response.data;
                val gson = Gson()
                val result = gson.toJson(data)
                var updatevisitor: UpdateCheckVisitorMutation.Update_society_visitors_checkin? =
                    data?.update_society_visitors_checkin

                if (updatevisitor!!.affected_rows > 0) {
                    view.updateVisitorList()
                    view.hideProgressbar()
                } else {
                    view.hideProgressbar()
                }
            } catch (e: Exception) {
                Log.d("apierror", e.message.toString())
                view.hideProgressbar()
            }
//            return response?.data?.society_id
        }
    }
}




