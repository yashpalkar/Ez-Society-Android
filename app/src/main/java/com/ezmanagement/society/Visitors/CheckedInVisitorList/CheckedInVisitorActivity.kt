package com.ezmanagement.society.Visitors.CheckedInVisitorList

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ezmanagement.society.AppConstants
import com.ezmanagement.society.VisitorListBySocietyIdQuery
import com.ezmanagement.society.databinding.ActivityCheckedInVisitorBinding
import com.ezmanagement.society.sharedPreference.SharedPref

class CheckedInVisitorActivity : AppCompatActivity(), CheckedInVisitorContract.View,
    CheckedInVisitorAdapter.CellClickListener {
    private lateinit var presenter: CheckedInVisitorContract.Presenter
    private lateinit var checkedInVisitorAdapter: CheckedInVisitorAdapter
    lateinit var binding: ActivityCheckedInVisitorBinding
    var sharedPref: SharedPref? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckedInVisitorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = SharedPref(this);
        presenter = CheckInVisitorPresenter(this, lifecycleScope = lifecycleScope)
        val jwtToken = sharedPref!!.getUserData(
            AppConstants.JWTTOKEN,
            String::class.java,
            ""
        )
        val guardId = sharedPref!!.getUserData(
            AppConstants.GUARDID,
            String::class.java,
            ""
        )
        val societyId = sharedPref!!.getUserData(
            AppConstants.SOCIETY_ID,
            String::class.java,
            ""
        )
        presenter.loadVisitorItems(societyId,jwtToken)
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
        TODO("Not yet implemented")
    }

    override fun selectEmployee(data: VisitorListBySocietyIdQuery.Society_visitors_checkin) {

    }
}