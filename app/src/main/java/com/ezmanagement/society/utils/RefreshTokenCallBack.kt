package com.ezmanagement.society.utils

interface RefreshTokenCallBack {
fun onSucess()
    fun onError()
    fun onRefreshTokenExpired(errorMessage:String)
}