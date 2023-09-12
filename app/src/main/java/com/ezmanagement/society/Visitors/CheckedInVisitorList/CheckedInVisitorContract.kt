package com.ezmanagement.society.Visitors.CheckedInVisitorList

import android.content.ClipData
import com.ezmanagement.society.VisitorListBySocietyIdQuery
import com.ezmanagement.society.Visitors.AddVisitor

interface CheckedInVisitorContract {
    interface View {
        fun showItems(checkinVisitorList:List<VisitorListBySocietyIdQuery.Society_visitors_checkin>?)
        fun showError(message: String?)
    }

    interface Presenter {
        fun loadVisitorItems( societyid: String,
                       jwttoken: String
        )
    }
}