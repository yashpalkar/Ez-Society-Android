package com.ezmanagement.society.Visitors.CheckedInVisitorList

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ezmanagement.society.AppConstants
import com.ezmanagement.society.VisitorListBySocietyIdQuery
import com.ezmanagement.society.databinding.ActivityCheckedInVisitorBinding
import com.ezmanagement.society.sharedPreference.SharedPref
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class CheckedInVisitorActivity : AppCompatActivity(), CheckedInVisitorContract.View,
    CheckedInVisitorAdapter.CellClickListener {
    private lateinit var presenter: CheckedInVisitorContract.Presenter
    private lateinit var checkedInVisitorAdapter: CheckedInVisitorAdapter
    lateinit var binding: ActivityCheckedInVisitorBinding
    var sharedPref: SharedPref? = null
   var societyId:String?=null
   var jwtToken:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckedInVisitorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        sharedPref = SharedPref(this);
        presenter = CheckInVisitorPresenter(this, lifecycleScope = lifecycleScope)
         jwtToken = sharedPref!!.getUserData(
            AppConstants.JWTTOKEN,
            String::class.java,
            ""
        )
        val guardId = sharedPref!!.getUserData(
            AppConstants.GUARDID,
            String::class.java,
            ""
        )
         societyId = sharedPref!!.getUserData(
            AppConstants.SOCIETY_ID,
            String::class.java,
            ""
        )
        presenter.loadVisitorItems(societyId!!,jwtToken!!)
//        var visitorList:List<VisitorListBySocietyIdQuery.Society_visitors_checkin>=List<>()
        val visitorList = ArrayList<VisitorListBySocietyIdQuery.Society_visitors_checkin>()
        checkedInVisitorAdapter = CheckedInVisitorAdapter(this, visitorList, this)
        var layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.checkedInVisitorRecylerView.layoutManager = layoutManager
        binding.checkedInVisitorRecylerView.adapter = checkedInVisitorAdapter
    }

    override fun showItems(checkinVisitorList: List<VisitorListBySocietyIdQuery.Society_visitors_checkin>?) {
        if (checkinVisitorList != null) {
            checkedInVisitorAdapter.setItems(checkinVisitorList)
        }
    }

    override fun showError(message: String?) {

    }

    override fun updateVisitorList() {
        societyId?.let { jwtToken?.let { it1 -> presenter.loadVisitorItems(it, it1) } }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClickcheckOut(data: VisitorListBySocietyIdQuery.Society_visitors_checkin) {

        val timeZone = ZoneId.of("Asia/Kolkata")
        val currentTime = ZonedDateTime.now(timeZone)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z")
        val dateTimeNow = currentTime.format(formatter)
        jwtToken?.let {
            societyId?.let { it1 ->
                presenter.updatecheckVisitor(
                    it1,data.id.toString(),LocalDateTime.now().toString(),dateTimeNow,
                    it
                )
            }
        }
    }

}