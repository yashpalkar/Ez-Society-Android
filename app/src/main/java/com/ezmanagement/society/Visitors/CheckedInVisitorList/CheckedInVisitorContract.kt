package com.ezmanagement.society.Visitors.CheckedInVisitorList

import com.ezmanagement.society.VisitorListBySocietyIdQuery
import java.time.LocalDateTime

interface CheckedInVisitorContract {
    interface View {
        fun showItems(checkinVisitorList:List<VisitorListBySocietyIdQuery.Society_visitors_checkin>?)
        fun showError(message: String?)
        fun updateVisitorList()
    }

    interface Presenter {
        fun loadVisitorItems(
            societyid: String, jwttoken: String
        )

        fun updatecheckVisitor(
            societyid: String,
            checkinVisitorId: String,
            checkOutTime: String,
            attendedTime: String,
            jwttoken: String
        )
    }
}