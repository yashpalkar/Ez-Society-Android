package com.ezmanagement.society.Visitors

import com.ezmanagement.society.AddVisitorCheckinMutation
import com.ezmanagement.society.CheckVisitorRegisterQuery
import com.ezmanagement.society.RegisterVisitorMutation

interface VisitorCallBack {
    interface CheckVisitorCallBack {

        fun isVisitorRegister( checkVisitorRegisterQuery_: CheckVisitorRegisterQuery.Society_visitor);
        fun isEmpty(contactNumber:String)
        fun onError();
        fun onFailure(message: String);
    }
    interface RegisterVisitorCallBack {

        fun visiterRegisterSuccessfully( registerVisitor: RegisterVisitorMutation.Insert_society_visitors_one);
        fun onError();
        fun onFailure(message: String);
    }
    interface VisitorCheckInCallBack {

        fun visiterCheckInSuccessfully( addVisitorCheckinMutation: AddVisitorCheckinMutation.Insert_society_visitors_checkin_one);
        fun onError();
        fun onFailure(message: String);
    }
}