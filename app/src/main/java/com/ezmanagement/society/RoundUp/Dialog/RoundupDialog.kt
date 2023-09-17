package com.ezmanagement.society.RoundUp.Dialog

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.ezmanagement.society.AppConstants
import com.ezmanagement.society.GetRoundUpIdQuery
import com.ezmanagement.society.MainActivity
import com.ezmanagement.society.databinding.RoundupDialogBinding
import com.ezmanagement.society.sharedPreference.SharedPref
import java.time.LocalDateTime
import java.time.ZonedDateTime

class RoundupDialog(societyRoundup: GetRoundUpIdQuery.Society_roundups_by_pk, imageurl: Uri) :
    DialogFragment(), DialogContract.View {

    private lateinit var dialogPresenter: RoundupDialogPresenter
    private lateinit var binding: RoundupDialogBinding
    var societyRoundup: GetRoundUpIdQuery.Society_roundups_by_pk = societyRoundup
    var imageurl: Uri = imageurl
    var jwtToken: String? = null
    var guardId: String? = null
    var sharedPref: SharedPref? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RoundupDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        dialogPresenter = RoundupDialogPresenter(this, lifecycleScope)
        sharedPref = SharedPref(activity as Context);
        jwtToken = sharedPref!!.getUserData(
            AppConstants.JWTTOKEN,
            String::class.java,
            ""
        )
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
        // Configure the UI elements in the dialog
        binding.submitRoundup.setOnClickListener {

            val currentDateTime =   LocalDateTime.now()
            dialogPresenter.addRoundup(
                imageurl,
                binding.notesInputLayoutEditText.text.toString(),
                currentDateTime.toString(),
                guardId,
                societyRoundup?.id.toString(),
                societyRoundup?.society_id.toString(),
                jwtToken!!
            )

        }
        binding.cancelRoundup.setOnClickListener {
            dismiss()
        }
        return view
    }



    override fun onRoundSuccessfull() {
        dismiss()
        Toast.makeText(activity, "Roundup Sucessfull", Toast.LENGTH_SHORT).show()
        startActivity(Intent(activity, MainActivity::class.java))
        if (context is Activity) {
            (context as Activity).finish()
        }
    }

    override fun onError(toString: String) {
        dismiss()
        Toast.makeText(activity, "Roundup Sucessfull", Toast.LENGTH_SHORT).show()
        startActivity(Intent(activity, MainActivity::class.java))
        if (context is Activity) {
            (context as Activity).finish()
        }
    }
}