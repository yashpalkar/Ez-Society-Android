package com.ezmanagement.society.Visitors

import com.ezmanagement.society.AddVisitorCheckinMutation
import com.ezmanagement.society.CheckVisitorNumberMutation
import com.ezmanagement.society.CheckVisitorRegisterQuery
import com.ezmanagement.society.RegisterVisitorMutation

interface VisitorCallBack {
    interface CheckVisitorCallBack {

        fun isRecheckIn( checkVisitorNumberMutation: CheckVisitorNumberMutation.CheckVisitorNumber);
        fun isNewVisitor(contactNumber:String,visitor_type:String)
        fun onError();
        fun onFailure(message: String);
    }
    interface RegisterVisitorCallBack {

        fun visiterRegisterSuccessfully(visitorId:String,societyId:String);
        fun onError();
        fun onFailure(message: String);
    }
    interface VisitorCheckInCallBack {

        fun visiterCheckInSuccessfully( message: String,societyId: String);
        fun onError();
        fun onFailure(message: String);
    }
}