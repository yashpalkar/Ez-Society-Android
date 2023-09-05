package com.ezmanagement.society

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ezmanagement.society.Retrofit.ApiClient
import com.ezmanagement.society.Retrofit.RetrofitApi
import com.ezmanagement.society.Retrofit.RetrofitService
import com.ezmanagement.society.User.ResponseCallBack
import com.ezmanagement.society.User.UserPresenter
import com.ezmanagement.society.databinding.ActivityLoginBinding
import com.ezmanagement.society.sharedPreference.SharedPref
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class LoginActivity : AppCompatActivity(), OnClickListener, ResponseCallBack {
    var sharedPref: SharedPref? = null
    var editor: SharedPreferences.Editor? = null
    var retrofit: Retrofit? = null
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        sharedPref = SharedPref(this);
        setContentView(binding.root)
        retrofit = RetrofitService.getInstance()
        binding.loginButton.setOnClickListener(this)
    }


    private fun isValid(email: String, password: String): Boolean {
        // Add your validation logic here
        return email.isNotBlank() && password.isNotBlank()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.loginButton -> {
                val username = binding.emaileditText.text.toString()
                val password = binding.passwordeditText.text.toString()
                loginUser(username, password)
            }

        }
    }

    fun loginUser(username: String, password: String) {
        if (isValid(username, password)) {
            val apiService = ApiClient()
            apiService.getApolloClient()

            val jsonParams: HashMap<String, Any> = HashMap<String, Any>()
            jsonParams["email"] = username
            jsonParams["password"] = password
            val retrofitApi = retrofit?.create(RetrofitApi::class.java)
            val call = retrofitApi?.login(jsonParams)

            call?.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {
                            val json_user = response.body()!!.getAsJsonObject("user")
                            val user_id = json_user?.get("id")?.asString
                            val user_display_name = json_user?.get("display_name")
                            val user_email = json_user?.get("email")
                            val jwttoken = response.body()!!.get("jwt_token").asString.toString()
                            val cookielist = response.headers().values("Set-Cookie")
                            Log.d("Cookielist", cookielist.toString())
                            var refreshToken = cookielist[0].split(".").toTypedArray()[0].toString()
                            var refresh_token =
                                refreshToken.substring(refreshToken.indexOf("=") + 1).substring(4)
                            sharedPref?.saveUserData(
                                AppConstants.LOGIN_TOKEN,
                                String::class.java,
                                refreshToken.substring(refreshToken.indexOf("=") + 1).substring(4)
                            )
                            sharedPref?.saveUserData(
                                AppConstants.USER_ID,
                                String::class.java,
                            user_id)
                            sharedPref?.saveUserData(
                                AppConstants.JWTTOKEN,
                                String::class.java,
                                jwttoken)
                            var userPresenter=UserPresenter(lifecycleScope)
                            user_id?.let { userPresenter.getProfileData(it,jwttoken,this@LoginActivity) }
//                            getProfileData(user_id!!, jwttoken)

//                            startActivity(Intent(applicationContext, MainActivity::class.java))
//                            finish()
                            // Do something with the token and userId
                        } else {
                            // Handle the case where the response body is null
                        }
                    } else {
                        // Handle the case where the API call was not successful
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.d("Throwable", t.message.toString())
                    // Handle the network error here
                }
            })
        }
    }


    override fun onSuccess() {

 var intent=Intent(this@LoginActivity,MainActivity::class.java)
        startActivity(intent)

    }

    override fun onError() {

    }

    override fun onFailure(message: String) {
        Log.d("Throwable",message)
    }

}





