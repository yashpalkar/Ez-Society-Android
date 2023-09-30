package com.ezmanagement.society.Visitors.RegisterVisirtors

import android.net.Uri
import com.ezmanagement.society.GetRoundUpIdQuery
import com.ezmanagement.society.RegisterVisitorMutation
import com.ezmanagement.society.common.BaseView
import java.io.File

interface AddVisitorContract {
    interface View :BaseView{

        fun imageuploadfailed();
        fun imageuploadSuccessfully();


    }

    interface Presenter {
        fun visitorRecheckIn(
            jwt_token: String,
            visitor_type: String,
            visitor_id: String,
            check_in_time: String,
            flat_no: String,
            guard_id: String,
            image: String,
            last_visited_at: String,
            name: String,
            verified: String,
            society_id: String,
            contact_no: String,
            addVisitorDetails: AddVisitorDetails
        )

        fun uploadImage(file: File, societyId: String)
        fun updateIamgeinVisirtor(visitorId: String,ImageKey:String)

    }
}