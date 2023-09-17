package com.ezmanagement.society.RoundUp.Dialog

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import com.ezmanagement.society.InsertRoundupMutation
import com.ezmanagement.society.Retrofit.ApiClient

import com.google.gson.Gson

class RoundupDialogPresenter(
    private val view: DialogContract.View,
    private val lifecycleScope: LifecycleCoroutineScope
) : DialogContract.Presenter {

    override fun addRoundup(
        imageUrl: Uri,
        notes: String,
        roundup_at: String,
        roundup_by: String,
        roundup_id: String,
        societyid: String,
        jwttoken: String
    ) {
        var apiClient = ApiClient()


//        var response:Response
        lifecycleScope.launchWhenResumed {
            try {
                val response =
                    apiClient!!.getApolloClient().mutation(
                        InsertRoundupMutation(
                            imageUrl.toString(),
                            notes,
                            roundup_at, roundup_by, roundup_id, societyid
                        )
                    ).addHttpHeader(
                        "Authorization", "Bearer $jwttoken"
                    ).execute()
                Log.d("RESPONSE_ERROR", response.errors.toString())
                var data: InsertRoundupMutation.Data? = response.data;
                val gson = Gson()
                val result = gson.toJson(data)
                var updatevisitor: InsertRoundupMutation.Insert_society_roundups_checkins_one? =
                    data?.insert_society_roundups_checkins_one

                if (updatevisitor != null) {
                  view.onRoundSuccessfull()
                }
                else{
                    view.onError("Roundup Failed")
                }
            } catch (e: Exception) {
                Log.d("apierror", e.message.toString())
                view.onError( e.message.toString())
            }
//            return response?.data?.society_id
        }
    }


}