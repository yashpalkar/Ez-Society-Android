package com.ezmanagement.society.common

interface BaseView {
    fun showToast(message:String)
    fun showAlert(message: String)
    fun showProgressDialog(resId:Int)
    fun showProgressDialog()
    fun dismissProgressDialog()
}