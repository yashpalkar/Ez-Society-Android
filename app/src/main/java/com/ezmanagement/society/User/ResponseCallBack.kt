package com.ezmanagement.society.User

interface ResponseCallBack {
    fun onSuccess();
    fun onError();
    fun onFailure( message:String);
}