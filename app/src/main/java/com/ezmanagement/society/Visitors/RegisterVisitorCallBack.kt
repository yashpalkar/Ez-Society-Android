package com.ezmanagement.society.Visitors

import com.ezmanagement.society.CheckVisitorRegisterQuery

interface RegisterVisitorCallBack {

    fun visiterRegisterSuccessfully( checkVisitorRegisterQuery_: CheckVisitorRegisterQuery.Society_visitor);
    fun isEmpty(contactNumber:String)
    fun onError();
    fun onFailure(message: String);
}