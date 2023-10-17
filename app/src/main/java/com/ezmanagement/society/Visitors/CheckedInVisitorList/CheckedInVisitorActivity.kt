package com.ezmanagement.society.Visitors.CheckedInVisitorList

import android.R
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ezmanagement.society.AppConstants
import com.ezmanagement.society.VisitorListBySocietyIdFilterQuery
import com.ezmanagement.society.databinding.ActivityCheckedInVisitorBinding
import com.ezmanagement.society.sharedPreference.SharedPref
import org.apache.commons.lang3.time.DurationFormatUtils
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*


class CheckedInVisitorActivity : AppCompatActivity(), CheckedInVisitorContract.View,
    CheckedInVisitorAdapter.CellClickListener {
    private lateinit var presenter: CheckedInVisitorContract.Presenter
    private lateinit var checkedInVisitorAdapter: CheckedInVisitorAdapter
    lateinit var binding: ActivityCheckedInVisitorBinding
    var sharedPref: SharedPref? = null
    var societyId: String? = null
    var jwtToken: String? = null
    var page = 0
    val visitorList = ArrayList<VisitorListBySocietyIdFilterQuery.Society_visitors_checkin>()
    var adapter: ArrayAdapter<String>? = null
    var nestedSV: NestedScrollView? = null
    var selectedItemPosition = -1

    @RequiresApi(Build.VERSION_CODES.O)
    var endDate = LocalDate.now().atTime(23, 59, 59).plusHours(5).plusMinutes(30)

    @RequiresApi(Build.VERSION_CODES.O)
    var startDate = LocalDate.now().atTime(LocalTime.MIDNIGHT).minusHours(5).minusMinutes(30)

    @RequiresApi(Build.VERSION_CODES.O)
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
        val items = arrayOf(
            "Today's Visitor's.",
            "Yesterday's Visitor's.",
            "This Week's Visitor's.",
            "This Month's Visitor's.",
            "This Year's Visitor's.",
            "All Visitor's."
        )

        adapter = ArrayAdapter(this, R.layout.simple_dropdown_item_1line, items)

        binding.filterTextView.setAdapter(adapter)
        if (items.isNotEmpty()) {
            presenter!!.loadVisitorbyFilter(
                societyId!!,
                jwtToken!!,
                0,
                7,
               endDate.toString(),
                startDate.toString(),
            )
            selectedItemPosition = 0
            binding.filterTextView.setText(items[0], false)
        }
        binding.filterTextView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                visitorList.clear()
                binding.visitorFilterTtitle.setText(items[position])
                selectedItemPosition = position
                when (position) {
                    0 -> {
                        presenter!!.loadVisitorbyFilter(
                            societyId!!,
                            jwtToken!!,
                            0,
                            7,
                            endDate.toString(),
                            startDate.toString(),
                        )
                        visitorList.clear()
                    }

                    1 -> {
                        presenter!!.loadVisitorbyFilter(
                            societyId!!,
                            jwtToken!!,
                            0,
                            7,
                            endDate.minusDays(1).toString(),
                            startDate.minusDays(1).toString(),
                        )
                        visitorList.clear()
                    }

                    2 -> {
                        presenter!!.loadVisitorbyFilter(
                            societyId!!,
                            jwtToken!!,
                            0,
                            7,
                            endDate.toString(),
                            startDate.minusDays(7).toString(),
                        )
                        visitorList.clear()
                    }
                    3 -> {
                        presenter!!.loadVisitorbyFilter(
                            societyId!!,
                            jwtToken!!,
                            0,
                            7,
                            endDate.toString(),
                            startDate.minusMonths(1).toString(),
                        )
                        visitorList.clear()
                    }
                    4 -> {
                        presenter!!.loadVisitorbyFilter(
                            societyId!!,
                            jwtToken!!,
                            0,
                            7,
                            endDate.toString(),
                            startDate.minusYears(1).toString(),
                        )
                        visitorList.clear()
                    }
                    5 -> {
                        presenter!!.loadVisitorbyFilter(
                            societyId!!,
                            jwtToken!!,
                            0,
                            7,
                            endDate.toString(),
                            startDate.minusYears(10).toString(),
                        )
                        visitorList.clear()
                    }
                    else -> "Invalid day" //
                }// This is like


            }
//        presenter.loadVisitorItems(societyId!!, jwtToken!!, 0, 7)
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

                when (selectedItemPosition) {
                    0 -> presenter!!.loadVisitorbyFilter(
                        societyId!!,
                        jwtToken!!,
                        page * 7,
                        7,
                        endDate.toString(),
                        startDate.toString(),
                    )

                    1 -> presenter!!.loadVisitorbyFilter(
                        societyId!!,
                        jwtToken!!,
                        page * 7,
                        7,
                        endDate.minusDays(1).toString(),
                        startDate.minusDays(1).toString(),
                    )

                    2 -> presenter!!.loadVisitorbyFilter(
                        societyId!!,
                        jwtToken!!,
                        page * 7,
                        7,
                        endDate.toString(),
                        startDate.minusDays(7).toString(),
                    )
                    3 -> presenter!!.loadVisitorbyFilter(
                        societyId!!,
                        jwtToken!!,
                        page * 7,
                        7,
                        endDate.toString(),
                        startDate.minusMonths(1).toString(),
                    )
                    4 -> presenter!!.loadVisitorbyFilter(
                        societyId!!,
                        jwtToken!!,
                        page * 7,
                        7,
                        endDate.toString(),
                        startDate.minusYears(1).toString(),
                    )
                    5 -> presenter!!.loadVisitorbyFilter(
                        societyId!!,
                        jwtToken!!,
                        page * 7,
                        7,
                        endDate.toString(),
                        startDate.minusYears(10).toString(),
                    )
                    else -> "Invalid day" //
                }

            }
        })

    }


    override fun notifyAdapter(checkinVisitorList: List<VisitorListBySocietyIdFilterQuery.Society_visitors_checkin>?) {
        if (checkinVisitorList != null) {
            visitorList.addAll(checkinVisitorList)
            checkedInVisitorAdapter.setItems(visitorList)
        }
    }

    override fun emptyVisitor() {
        var checkinVisitorList: List<VisitorListBySocietyIdFilterQuery.Society_visitors_checkin>?=listOf()
        if (checkinVisitorList != null) {
//            visitorList.addAll(checkinVisitorList)
            checkedInVisitorAdapter.setItems(visitorList)
        }
    }


    override fun showError(message: String?) {

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun updateVisitorList() {
        visitorList.clear()

        page = 0
        when (selectedItemPosition) {
            0 -> presenter!!.loadVisitorbyFilter(
                societyId!!,
                jwtToken!!,
                0,
                7,
                endDate.toString(),
                startDate.toString(),
            )

            1 -> presenter!!.loadVisitorbyFilter(
                societyId!!,
                jwtToken!!,
                0,
                7,
                endDate.minusDays(1).toString(),
                startDate.minusDays(1).toString()
            )

            2 -> presenter!!.loadVisitorbyFilter(
                societyId!!,
                jwtToken!!,
                0,
                7,
                endDate.toString(),
                startDate.minusDays(7).toString()
            )
            3 -> presenter!!.loadVisitorbyFilter(
                societyId!!,
                jwtToken!!,
                0,
                7,
                endDate.toString(),
                startDate.minusMonths(1).toString()
            )
            4 -> presenter!!.loadVisitorbyFilter(
                societyId!!,
                jwtToken!!,
                0,
                7,
                endDate.toString(),
                startDate.minusYears(1).toString()
            )
            5 -> presenter!!.loadVisitorbyFilter(
                societyId!!,
                jwtToken!!,
                0,
                7,
                endDate.toString(),
                startDate.minusYears(10).toString()
            )
            else -> "Invalid day" //
        }
//        societyId?.let { jwtToken?.let { it1 -> presenter.loadVisitorItems(it, it1, 0, 7) } }
    }

    override fun hideProgressbar() {
        binding.idPBLoading.visibility = View.GONE
    }

    override fun showProgressbar() {
        binding.idPBLoading.visibility = View.VISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClickcheckOut(data: VisitorListBySocietyIdFilterQuery.Society_visitors_checkin) {

        val checkInFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        val check_inDateTime =
            LocalDateTime.parse(data.check_in.toString(), formatter).plusHours(5).plusMinutes(30)
        val currentLocalTime = LocalTime.now()
        val currentUtcTime = Instant.now()
        val duration = Duration.between(currentLocalTime, check_inDateTime).abs()
//var isO8601String=
//        val hours = duration.toHours()
//        val minutes = (duration.toMinutes() % 60)
//        val seconds = (duration.seconds % 60)
        val milliseconds = (duration.toMillis() % 1000)
//       var attendedTime= "$hours:${String.format("%02d", minutes)}:${String.format("%02d", seconds)}.${String.format("%03d", milliseconds)}"
        // Add the duration to the parsed LocalTime
        var attendedTime =
            "P${duration.toDays()}DT${duration.toHours() % 24}H${duration.toMinutes() % 60}M${duration.seconds % 60}S"
        val days = duration.toDays()
        val hours = duration.toHours() % 24
        val minutes = duration.toMinutes() % 60
        val seconds = duration.seconds % 60

        val iso8601Interval = "P${days}DT${hours}H${minutes}M${seconds}S"
        // Add the duration to the parsed LocalTime

////        val timeZone = ZoneId.of("Asia/Kolkata")
////        val currentTime = ZonedDateTime.now(timeZone)
////        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z")
//        val dateTimeNow = currentTime.format(formatter)
        jwtToken?.let {
            societyId?.let { it1 ->
                presenter.updatecheckVisitor(
                    it1,
                    data.id.toString(),
                    currentUtcTime.toString(),
                    attendedTime,
                    it
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDurationToISO8601(duration: Duration): String {
        val millis = duration.toMillis()
        return DurationFormatUtils.formatDurationISO(millis)
    }

    fun loadedataonFilter() {}

}