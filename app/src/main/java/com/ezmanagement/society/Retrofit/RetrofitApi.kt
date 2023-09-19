package com.ezmanagement.society.Retrofit


import android.database.Observable
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface RetrofitApi {

    @POST("auth/login")
    fun login(
        @Body body: HashMap<String, Any>
    ): Call<JsonObject>


    @GET("auth/token/refresh")
    fun refreshToken(
        @Query("refresh_token") token: String,
    ): Call<JsonObject>

    @POST("change-password")
    fun changepassword(
        @Body body: HashMap<String, Any>, @Header("Authorization") authHeader: String?
    ): Call<JsonObject>

    @Multipart
    @POST("storage/o/public/society/{societyId}/roundup/{roundupId}/{filename}")
    fun uploadRoundUp_img(
        @Path("societyId") societyId: String,
        @Path("roundupId") roundupId: String,
        @Path("filename") filename: String,
        @Part file: MultipartBody.Part,

    ): Call<JsonObject>

    @Multipart
    @POST("storage/o/public/society/{societyId}/visitor/{visitorId}/{filename}")
    fun uploadVisitor_img(
        @Path("societyId") societyId: String,
        @Path("visitorId") visitorId: String,
        @Path("filename") filename: String,
        @Part file: MultipartBody.Part,

        ): Call<JsonObject>
}
//http://localhost:5200/storage/o/public/society/a1da4e77-a812-47c2-ac35-4ae962297333/visitor/193405b1-fd1d-4bec-ae31-73c7f1b53def/profile/ss_platte.png