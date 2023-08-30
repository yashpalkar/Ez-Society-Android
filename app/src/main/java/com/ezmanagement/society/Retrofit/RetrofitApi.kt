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
    @POST("storage/o/public/user/{userId}/avatar/{filename}")
    fun uploadprofile_img(
        @Path("userId") userId: String,
        @Path("filename") filename: String,
        @Part file: MultipartBody.Part,
        @Header("Authorization") authHeader: String?
    ): Call<JsonObject>
}