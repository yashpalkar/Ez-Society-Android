package com.ezmanagement.society.Visitors.RegisterVisirtors

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleCoroutineScope
import com.ezmanagement.society.RegisterVisitorMutation

import com.ezmanagement.society.Retrofit.ApiClient
import com.ezmanagement.society.Retrofit.RetrofitApi
import com.ezmanagement.society.Retrofit.RetrofitService
import com.ezmanagement.society.sharedPreference.SharedPref
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddVisitorDetailsPresenter (private val lifecycleScope: LifecycleCoroutineScope,):AddVisitorContract.Presenter {
    var sharedPref: SharedPref? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun registervisitor(
        jwt_token: String,
        contactNo: String,
        guardId: String,
        lastVisitedAT: String,
        visitorName: String,
        societyId: String,
        isVerifed: Boolean,
        imageString:String,
        addVisitorDetails: AddVisitorDetails
    ) {
        val gson = GsonBuilder().create()
        var apiClient = ApiClient()
        lifecycleScope.launchWhenResumed {
            try {

                val response = apiClient!!.getApolloClient().mutation(
                    RegisterVisitorMutation(contactNo,guardId,lastVisitedAT,visitorName,societyId, isVerifed,imageString)
                ).addHttpHeader(
                    "Authorization", "Bearer $jwt_token"
                ).execute()
                Log.d("RESPONSE_ERROR", response.errors.toString())
                var data:RegisterVisitorMutation.Data? = response.data;
                val gson = Gson()
                if(data?.insert_society_visitors_one?.id!=null){
                    addVisitorDetails.visiterRegisterSuccessfully(data.insert_society_visitors_one!!)
                }
                val result = gson.toJson(data)

            } catch (e: Exception) {
                Log.d("apierror", e.message.toString())
                var response = e.message
            }

        }
    }
     override fun uploadImage(file: File, societyId:String, visitorId:String) {


        val filePart = MultipartBody.Part.createFormData(
            "file",
            file.name,
            file.asRequestBody("image/*".toMediaTypeOrNull())
        )

        var retrofit = RetrofitService.getInstance()
        val retrofitApi = retrofit.create(RetrofitApi::class.java)

        val call: Call<JsonObject> = retrofitApi.uploadVisitor_img(societyId,visitorId,file.name,filePart).also {  }

        call.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                if(response.code()==200){
                    val key:String=response.body()!!.get("key").asString

                }
                else{

                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {

            }

        })
    }

    override fun updateIamgeinVisirtor(visitorId: String, ImageKey: String) {
        TODO("Not yet implemented")
    }
}