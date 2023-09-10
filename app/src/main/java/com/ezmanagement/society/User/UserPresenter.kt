package com.ezmanagement.society.User

import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import com.ezmanagement.society.AppConstants
import com.ezmanagement.society.GetSocietyGuardbyIDQuery
import com.ezmanagement.society.LoginActivity
import com.ezmanagement.society.Retrofit.ApiClient
import com.ezmanagement.society.sharedPreference.SharedPref
import com.google.gson.Gson

class UserPresenter(private val lifecycleScope: LifecycleCoroutineScope,) {
    var sharedPref: SharedPref? = null

    fun getProfileData(
        user_id: String,
        jwttoken: String,loginActivity: LoginActivity
    ) {
        sharedPref = SharedPref(loginActivity);
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
                sharedPref?.saveUserData(
                    AppConstants.ACTIVE_STATUS,
                    String::class.java,
                    society_guards?.get(0)?.active_status!!.toString()
                )
                sharedPref?.saveUserData(
                    AppConstants.CONTACT_NO,
                    String::class.java,
                    society_guards?.get(0)?.contact_no!!
                )
                sharedPref?.saveUserData(
                        AppConstants.ADDRESS,
                String::class.java,
                society_guards?.get(0)?.address!!
                )
                sharedPref?.saveUserData(
                        AppConstants.CREATED_AT,
                String::class.java,
                    society_guards?.get(0)?.created_at!! as String
                )
                sharedPref?.saveUserData(
                    AppConstants.SOCIETY_ID,
                    String::class.java,
                    society_guards?.get(0)?.society_id!! as String
                )
                sharedPref?.saveUserData(
                    AppConstants.GUARDID,
                    String::class.java,
                    society_guards?.get(0)?.id!! as String
                )

                loginActivity.onSuccess()

            } catch (e: Exception) {
                Log.d("apierror", e.message.toString())
                var response = e.message
            }
//            return response?.data?.society_id
        }
    }
}