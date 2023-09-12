package com.ezmanagement.society.Visitors.CheckedInVisitorList

import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import com.ezmanagement.society.CheckVisitorRegisterQuery
import com.ezmanagement.society.Retrofit.ApiClient
import com.ezmanagement.society.VisitorListBySocietyIdQuery
import com.ezmanagement.society.Visitors.AddVisitor
import com.ezmanagement.society.sharedPreference.SharedPref
import com.google.gson.Gson

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
}




