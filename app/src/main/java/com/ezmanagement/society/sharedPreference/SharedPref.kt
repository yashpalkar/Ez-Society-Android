package com.ezmanagement.society.sharedPreference

import android.content.Context
import android.content.SharedPreferences
import com.ezmanagement.society.AppConstants
import com.ezmanagement.society.R

class SharedPref (var context:Context){
    var sharedPrefrence: SharedPreferences? =null
    var editor: SharedPreferences.Editor? = null
    init {
        sharedPrefrence = context.getSharedPreferences(
            AppConstants.SHARED_PREFERENCE,
            Context.MODE_PRIVATE
        )
        editor = sharedPrefrence!!.edit()
    }

    fun <T> saveUserData(key : String, dataType: Class<T>, data: T){
        editor = sharedPrefrence!!.edit()
        if(dataType == String::class.java){
            editor!!.putString(key, data.toString())
        }else if (dataType == Boolean::class.java){
            editor!!.putBoolean(key, data.toString().toBoolean())
        }else if (dataType == Int::class.java){
            editor!!.putInt(key, data.toString().toInt())
        }else if (dataType == Float::class.java){
            editor!!.putFloat(key, data.toString().toFloat())
        }
        editor!!.apply()
    }

    fun <T> getUserData(key: String, dataType : Class<T>, defaultValue : T) : T{
        var  data : T? = null
        if(dataType == String::class.java){
            data = sharedPrefrence!!.getString(key, defaultValue.toString()) as T
        }else if(dataType == Int::class.java){
            data = sharedPrefrence!!.getInt(key, defaultValue.toString().toInt()) as T
        }else if(dataType == Boolean::class.java){
            data = sharedPrefrence!!.getBoolean(key, defaultValue.toString().toBoolean()) as T
        }else if (dataType == Float::class.java){
            data = sharedPrefrence!!.getFloat(key, defaultValue.toString().toFloat()) as T
        }
        return data!!
    }
    fun clearedSharedPref(){
        editor!!.clear()
        editor!!.commit()
    }
}