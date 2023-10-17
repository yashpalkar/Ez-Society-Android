package com.ezmanagement.society.Visitors.CheckedInVisitorList

import com.ezmanagement.society.VisitorListBySocietyIdFilterQuery
import com.ezmanagement.society.VisitorListBySocietyIdQuery
import java.time.LocalDateTime

interface CheckedInVisitorContract {
    interface View {
        fun notifyAdapter(checkinVisitorList:List<VisitorListBySocietyIdFilterQuery.Society_visitors_checkin>?)
        fun emptyVisitor();
        fun showError(message: String?)
        fun updateVisitorList()
        fun hideProgressbar()
        fun showProgressbar()
    }

    interface Presenter {
        fun loadVisitorItems(
            societyid: String, jwttoken: String, offset: Int,limit:Int
        )
        fun loadVisitorbyFilter(
            societyid: String, jwttoken: String, offset: Int,limit:Int,endDate:String,startDate:String
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