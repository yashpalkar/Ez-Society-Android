package com.ezmanagement.society.Visitors.CheckInVisitors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ezmanagement.society.GetVisitorCheckInQuery
import com.ezmanagement.society.R

class VisitorAdapter (
        context: Context,
        employeList: List<GetVisitorCheckInQuery.Society_visitors_checkin>, cellClickListener: VisitorAdapter.CellClickListener
    ) : RecyclerView.Adapter<VisitorAdapter.MyViewHolder>() {
        private var employeList=employeList
        private var cellClickListener=cellClickListener
        var context: Context =context
        inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.nav_header, parent, false)
            var layoutParam: ViewGroup.LayoutParams = itemView.layoutParams
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

            holder.itemView.setOnClickListener {
                cellClickListener.selectEmployee(employeList.get(position))
            }

        }



        override fun getItemCount(): Int {
            return employeList.size
        }

        interface CellClickListener {
            fun selectEmployee(data: GetVisitorCheckInQuery.Society_visitors_checkin)
        }
    }