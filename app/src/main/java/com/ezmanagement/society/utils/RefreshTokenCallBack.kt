package com.ezmanagement.society.utils

interface RefreshTokenCallBack {

    fun onError()
    fun onRefreshTokenExpired(errorMessage:String)
}