package com.ezmanagement.society.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity

import com.ezmanagement.society.AppConstants
import com.ezmanagement.society.LoginActivity
import com.ezmanagement.society.MainActivity

import com.ezmanagement.society.Retrofit.RetrofitApi
import com.ezmanagement.society.Retrofit.RetrofitService
import com.ezmanagement.society.sharedPreference.SharedPref
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RefreshTokenClass(context: Context) {

    var sharedPref: SharedPref? = null
    var context = context


     fun onGetRefreshToken(refreshToken: String?) {
         var mainActivity:MainActivity= MainActivity()

        sharedPref = SharedPref(context)
        var retrofit = RetrofitService.getInstance()
        val retrofitApi = retrofit.create(RetrofitApi::class.java)
        val call = retrofitApi.refreshToken(
            sharedPref?.getUserData(
                AppConstants.LOGIN_TOKEN, String::class.java, ""
            )!!
        )

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                if (response.code() == 200) {
                    val refresh_token = response.body()!!.get("refresh_token")
                    Log.d("refresh_token", refresh_token.toString())
                    sharedPref?.saveUserData(
                        AppConstants.LOGIN_TOKEN,
                        String::class.java,
                        refresh_token.asString)
                } else if (response.code() == 401) {
                    mainActivity.onError()

                } else {
                    mainActivity.onError()
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                mainActivity.onRefreshTokenExpired(t.message.toString())
            }
        })

    }





}