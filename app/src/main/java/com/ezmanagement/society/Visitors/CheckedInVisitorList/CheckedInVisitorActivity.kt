package com.ezmanagement.society.Visitors.CheckedInVisitorList

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ezmanagement.society.AppConstants
import com.ezmanagement.society.VisitorListBySocietyIdQuery
import com.ezmanagement.society.databinding.ActivityCheckedInVisitorBinding
import com.ezmanagement.society.sharedPreference.SharedPref
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class CheckedInVisitorActivity : AppCompatActivity(), CheckedInVisitorContract.View,
    CheckedInVisitorAdapter.CellClickListener {
    private lateinit var presenter: CheckedInVisitorContract.Presenter
    private lateinit var checkedInVisitorAdapter: CheckedInVisitorAdapter
    lateinit var binding: ActivityCheckedInVisitorBinding
    var sharedPref: SharedPref? = null
   var societyId:String?=null
   var jwtToken:String?=null
    var page = 0
    val visitorList = ArrayList<VisitorListBySocietyIdQuery.Society_visitors_checkin>()

          var nestedSV:NestedScrollView? = null
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
        presenter.loadVisitorItems(societyId!!,jwtToken!!,0,7)
//        var visitorList:List<VisitorListBySocietyIdQuery.Society_visitors_checkin>=List<>()
//        val visitorList = ArrayList<VisitorListBySocietyIdQuery.Society_visitors_checkin>()
        checkedInVisitorAdapter = CheckedInVisitorAdapter(this, visitorList, this)
        var layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.checkedInVisitorRecylerView.layoutManager = layoutManager
        binding.checkedInVisitorRecylerView.adapter = checkedInVisitorAdapter

        binding.idNestedSV.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            // on scroll change we are checking when users scroll as bottom.
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {

                page++
                presenter.loadVisitorItems(societyId!!,jwtToken!!,page*7,7)
            }
        })

    }

    override fun showItems(checkinVisitorList: List<VisitorListBySocietyIdQuery.Society_visitors_checkin>?) {
        if (checkinVisitorList != null) {
            visitorList.addAll(checkinVisitorList)
            checkedInVisitorAdapter.setItems(visitorList)
        }
    }

    override fun showError(message: String?) {

    }

    override fun updateVisitorList() {
        visitorList.clear()
         page = 0

        societyId?.let { jwtToken?.let { it1 -> presenter.loadVisitorItems(it, it1,0,7) } }
    }

    override fun hideProgressbar() {
        binding.idPBLoading.visibility=View.GONE
    }

    override fun showProgressbar() {
        binding.idPBLoading.visibility=View.VISIBLE
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