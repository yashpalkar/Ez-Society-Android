package com.ezmanagement.society.Visitors.CheckedInVisitorList

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ezmanagement.society.VisitorListBySocietyIdQuery
import com.ezmanagement.society.databinding.CheckedinVisitorCardviewBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CheckedInVisitorAdapter (
    context: Context,
    visitorList: List<VisitorListBySocietyIdQuery.Society_visitors_checkin>, cellClickListener: CellClickListener
    ) : RecyclerView.Adapter<CheckedInVisitorAdapter.MyViewHolder>() {
        private var visitorList=visitorList
        private var cellClickListener=cellClickListener
        var context: Context =context
        inner class MyViewHolder(private val binding: CheckedinVisitorCardviewBinding) : RecyclerView.ViewHolder(binding.root) {
            @RequiresApi(Build.VERSION_CODES.O)
            fun bind(visitor:VisitorListBySocietyIdQuery.Society_visitors_checkin) {
                val checkinDateTime: LocalDateTime =
                    LocalDateTime.parse(visitor.check_in.toString(), DateTimeFormatter.ISO_DATE_TIME)
                val checkoutDateTime: LocalDateTime =
                    LocalDateTime.parse(visitor.check_out.toString(), DateTimeFormatter.ISO_DATE_TIME)
                binding.checkinTimeValueTextView.text = checkinDateTime.dayOfMonth.toString()+" "+checkinDateTime.month.toString().substring(0,3)+" . "+checkinDateTime.hour+":"+checkinDateTime.minute
                binding.checkoutTimeValueTextView.text = checkoutDateTime.dayOfMonth.toString()+" "+checkoutDateTime.month.toString().substring(0,3)+" . "+checkoutDateTime.hour+":"+checkoutDateTime.minute
                binding.visitorNameTextView.text = visitor.society_visitors_checkin_society_visitor.name
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val binding = CheckedinVisitorCardviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return MyViewHolder(binding)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val visittor = visitorList[position]
            holder.bind(visittor)

        }



        override fun getItemCount(): Int {
            return visitorList.size
        }

        interface CellClickListener {
            fun selectEmployee(data: VisitorListBySocietyIdQuery.Society_visitors_checkin)
        }
     fun setItems(visitorList: List<VisitorListBySocietyIdQuery.Society_visitors_checkin>) {
        this.visitorList = visitorList
        notifyDataSetChanged()
    }
    }