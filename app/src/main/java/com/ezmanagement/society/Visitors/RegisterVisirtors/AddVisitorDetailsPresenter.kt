package com.ezmanagement.society.Visitors.RegisterVisirtors


import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleCoroutineScope
import com.ezmanagement.society.Retrofit.ApiClient
import com.ezmanagement.society.Retrofit.RetrofitApi
import com.ezmanagement.society.Retrofit.RetrofitService
import com.ezmanagement.society.VisitorRecheckInMutation
import com.ezmanagement.society.Visitors.AddVisitor
import com.ezmanagement.society.common.LogUtlis
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

class AddVisitorDetailsPresenter() :
    AddVisitorContract.Presenter {
    var sharedPref: SharedPref? = null
    lateinit var view:AddVisitorContract.View
    lateinit var lifecycleScope: LifecycleCoroutineScope
    constructor( lifecycleScope: LifecycleCoroutineScope,view:AddVisitorContract.View) : this() {
        this.view=view
        this.lifecycleScope=lifecycleScope
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun visitorRecheckIn(
        jwt_token: String,
        visitor_type: String,
        visitor_id: String,
        check_in_time: String,
        flat_no: String,
        guard_id: String,
        image: String,
        last_visited_at: String,
        name: String,
        verified: String,
        society_id: String,
        contact_no: String,
        addVisitorDetails: AddVisitorDetails
    ) {
        val gson = GsonBuilder().create()
        var apiClient = ApiClient()

        lifecycleScope.launchWhenResumed {
            try {

                val response = apiClient!!.getApolloClient().mutation(
                    VisitorRecheckInMutation(
                        visitor_type = visitor_type,
                        visitor_id,
                        check_in_time,
                        flat_no,
                        guard_id,
                        image,
                        last_visited_at,
                        name,
                        verified,
                        society_id,
                        contact_no
                    )
                ).addHttpHeader(
                    "Authorization", "Bearer $jwt_token"
                ).execute()
                Log.d("RESPONSE_ERROR", response.errors.toString())
                var data: VisitorRecheckInMutation.Data? = response.data;
                val gson = Gson()
                if (data?.visitorsCheckin?.message.equals("Visitor updated and checked in successfully")) {
                    Toast.makeText(
                        addVisitorDetails,
                        data?.visitorsCheckin?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    addVisitorDetails.visiterRegisterSuccessfully(visitor_id, society_id)
                }
                val result = gson.toJson(data)

            } catch (e: Exception) {
                Log.d("apierror", e.message.toString())
                var response = e.message
            }

        }
    }

    override fun uploadImage(file: File, societyId: String) {


        val filePart = MultipartBody.Part.createFormData(
            "file",
            file.name,
            file.asRequestBody("image/*".toMediaTypeOrNull())
        )

        var retrofit = RetrofitService.getInstance()
        val retrofitApi = retrofit.create(RetrofitApi::class.java)

        val call: Call<JsonObject> =
            retrofitApi.uploadVisitor_img(societyId, file.name, filePart).also { }

        call.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                if (response.code() == 200) {
                    val key: String = response.body()!!.get("key").asString
                    LogUtlis.info("IMAgeKey", key)
                    view.imageuploadSuccessfully()
                } else {

                    view.imageuploadfailed()
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {

            }

        })
    }

    override fun updateIamgeinVisirtor(visitorId: String, ImageKey: String) {

    }
}