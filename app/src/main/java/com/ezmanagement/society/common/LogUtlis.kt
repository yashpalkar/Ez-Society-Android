package com.ezmanagement.society.common

import android.util.Log
import com.ezmanagement.society.BuildConfig

class LogUtlis {

    companion object {
        fun info(tag:String,message:String) {
            if(BuildConfig.BUILD_TYPE.equals("release"))
                return;
            Log.d(tag,message)
        }

        val myStaticProperty: Int = 42
    }
}