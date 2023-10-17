package com.ezmanagement.society.Visitors.CheckedInVisitorList

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ezmanagement.society.BuildConfig
import com.ezmanagement.society.R
import com.ezmanagement.society.VisitorListBySocietyIdFilterQuery
import com.ezmanagement.society.databinding.CheckedinVisitorCardviewBinding
import java.time.*
import java.time.format.DateTimeFormatter

class CheckedInVisitorAdapter(
    context: Context,
    visitorList: List<VisitorListBySocietyIdFilterQuery.Society_visitors_checkin>,
    cellClickListener: CellClickListener
) : RecyclerView.Adapter<CheckedInVisitorAdapter.MyViewHolder>() {
    private var visitorList = visitorList
    private var cellClickListener = cellClickListener
    var context: Context = context

    inner class MyViewHolder(private val binding: CheckedinVisitorCardviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(visitor: VisitorListBySocietyIdFilterQuery.Society_visitors_checkin) {


            val check_in = visitor.check_in.toString().substring(0, 19)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            val check_inDateTime =
                LocalDateTime.parse(check_in, formatter).plusHours(5).plusMinutes(30)

            binding.visitorImageview?.let {
                Glide.with(context)
                    .load(BuildConfig.MINIO_URL + visitor.society_visitors_checkin_society_visitor.image)
                    .placeholder(R.drawable.profile_avatar)
                    .error(R.drawable.profile_avatar)
                    .fitCenter()
                    .into(it)
            }
            if (visitor.flat_no?.isEmpty() == true) {
                binding.flatnoValueTextView.visibility = (View.GONE)
                binding.flatnoTitileTextView.visibility = (View.GONE)
            } else {

                binding.flatnoValueTextView.visibility = (View.VISIBLE)
                binding.flatnoTitileTextView.visibility = (View.VISIBLE)
                binding.flatnoValueTextView.setText(visitor.flat_no.toString())
            }

            binding.checkinTimeValueTextView.text =
                check_inDateTime.dayOfMonth.toString() + " " + check_inDateTime.month.toString()
                    .substring(
                        0, 3
                    ) + " . " + check_inDateTime.hour + ":" + check_inDateTime.minute
            if (visitor.check_out != null) {
                val checkout = visitor.check_out.toString().substring(0, 19)
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                val checkoutDateTime =
                    LocalDateTime.parse(checkout, formatter).plusHours(5).plusMinutes(30)

                binding.checkoutLayout.visibility = View.GONE
                binding.checkoutTimeTitle.visibility = View.VISIBLE
                binding.checkoutTimeValueTextView.visibility = View.VISIBLE
                binding.attendedTimeTitle.visibility = View.VISIBLE
                binding.attendedTimeValueTextView.visibility = View.VISIBLE
                binding.checkoutTimeValueTextView.text =
                    checkoutDateTime.dayOfMonth.toString() + " " + checkoutDateTime.month.toString()
                        .substring(
                            0, 3
                        ) + " . " + checkoutDateTime.hour + ":" + checkoutDateTime.minute

                binding.attendedTimeValueTextView.text = visitor.attended_time.toString()
            } else {
                binding.checkoutTimeTitle.visibility = View.GONE
                binding.checkoutTimeValueTextView.visibility = View.GONE
                binding.attendedTimeTitle.visibility = View.GONE
                binding.attendedTimeValueTextView.visibility = View.GONE
                binding.checkoutLayout.visibility = View.VISIBLE
            }
            binding.checkoutLayout.setOnClickListener { cellClickListener.onClickcheckOut(visitor) }
            binding.visitorNameTextView.text =
                visitor.society_visitors_checkin_society_visitor.name
            binding.mobileNumberTextView.text =
                visitor.society_visitors_checkin_society_visitor.contact_no
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
        fun onClickcheckOut(data: VisitorListBySocietyIdFilterQuery.Society_visitors_checkin)
    }

    fun setItems(visitorList: List<VisitorListBySocietyIdFilterQuery.Society_visitors_checkin>) {
        this.visitorList = visitorList
        notifyDataSetChanged()
    }
}