package com.ezmanagement.society.Visitors.RegisterVisirtors

import android.net.Uri
import com.ezmanagement.society.GetRoundUpIdQuery
import com.ezmanagement.society.RegisterVisitorMutation
import java.io.File

interface AddVisitorContract {
    interface View {

        fun visiterRegisterSuccessfully( registerVisitor: RegisterVisitorMutation.Insert_society_visitors_one);
        fun onError();
        fun onFailure(message: String);
    }

    interface Presenter {
        fun registervisitor(
            jwt_token: String,
            contactNo: String,
            guardId: String,
            lastVisitedAT: String,
            visitorName: String,
            societyId: String,
            isVerifed: Boolean,
            imageString: String,
            addVisitorDetails: AddVisitorDetails
        )

        fun uploadImage(file: File, societyId: String, visitorId: String)

        fun updateIamgeinVisirtor(visitorId: String,ImageKey:String)
    }
}