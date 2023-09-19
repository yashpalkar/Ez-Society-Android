package com.ezmanagement.society.RoundUp

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import com.ezmanagement.society.GetRoundUpIdQuery
import com.ezmanagement.society.Retrofit.ApiClient
import com.ezmanagement.society.Retrofit.RetrofitApi
import com.ezmanagement.society.Retrofit.RetrofitService
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class RoundPresenter(private val view: RoundUpContract.View,
                      private val lifecycleScope: LifecycleCoroutineScope
): RoundUpContract.Presenter {
    override fun isQRValid(qrcode: String, jwttoken: String) {

        var apiClient = ApiClient()
        lifecycleScope.launchWhenResumed {
            try {

                val response =
                    apiClient!!.getApolloClient().query(GetRoundUpIdQuery(qrcode))
                        .addHttpHeader(
                            "Authorization", "Bearer $jwttoken"
                        ).execute()
                Log.d("RESPONSE_ERROR", response.errors.toString())
                var society_roundups_by_pk: GetRoundUpIdQuery.Society_roundups_by_pk? =
                    response.data?.society_roundups_by_pk;
                val gson = Gson()
                if (society_roundups_by_pk?.id?.equals(qrcode) == true) {
                    view.validQr(society_roundups_by_pk)
                } else {
                    view.inValidQr("Invalid QR code")
                }

            } catch (e: Exception) {
                view.inValidQr(e.message.toString())
            }

//            return response?.data?.society_id
        }
    }
    override fun onShowPopupClicked(
        societyRoundup: GetRoundUpIdQuery.Society_roundups_by_pk,
        imageUri: Uri?
    ) {

        view.showPopup(societyRoundup,imageUri)
    }

    override fun onDismissPopupClicked() {
        // Implement logic to dismiss the popup
        view.dismissPopup()
    }

     override fun uploadImage(file: File, societyId:String, roundupId:String) {


        val filePart = MultipartBody.Part.createFormData(
            "file",
            file.name,
            file.asRequestBody("image/*".toMediaTypeOrNull())
        )

        var retrofit = RetrofitService.getInstance()
        val retrofitApi = retrofit.create(RetrofitApi::class.java)

        val call: Call<JsonObject> = retrofitApi.uploadRoundUp_img(societyId,roundupId,file.name,filePart).also {  }

        call.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                if(response.code()==200){
                        val key:String=response.body()!!.get("key").asString
                    view.onRoundUpImageUploaded(key)
                }
                else{
                    view.onRoundUpImageUploadFailed(response.code().toString())
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                view.onRoundUpImageUploadFailed(t.message.toString())
            }

        })
    }


}