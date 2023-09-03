package com.ezmanagement.society.User

import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import com.ezmanagement.society.GetSocietyGuardbyIDQuery
import com.ezmanagement.society.LoginActivity
import com.ezmanagement.society.Retrofit.ApiClient
import com.google.gson.Gson

class UserPresenter(    private val lifecycleScope: LifecycleCoroutineScope,) {

    fun getProfileData(
        user_id: String,
        jwttoken: String,loginActivity: LoginActivity
    ) {

        var apiClient = ApiClient()
//        var response:Response
        lifecycleScope.launchWhenResumed {
            try {
                val response =
                    apiClient!!.getApolloClient().query(GetSocietyGuardbyIDQuery(user_id)).addHttpHeader(
                        "Authorization", "Bearer $jwttoken"
                    ).execute()
                Log.d("RESPONSE_ERROR", response.errors.toString())
                var data: GetSocietyGuardbyIDQuery.Data? = response.data;
                val gson = Gson()
                val result = gson.toJson(data)
                var society_guards: List<GetSocietyGuardbyIDQuery.Society_guard>? =
                    data?.society_guards
                Log.d("apierror", society_guards?.get(0)?.contact_no.toString())
                loginActivity.onSuccess()

//                Log.d("apierror", e.message.toString())

            } catch (e: Exception) {
                Log.d("apierror", e.message.toString())
                var response = e.message
            }
//            return response?.data?.society_id
        }
    }
}