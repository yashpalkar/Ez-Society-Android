package com.ezmanagement.society.RoundUp.Dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.ezmanagement.society.R
import com.ezmanagement.society.databinding.RoundupDialogBinding

class RoundupDialog : DialogFragment(),DialogContract.View{
    private lateinit var dialogPresenter:RoundupDialogPresenter
    private lateinit var binding: RoundupDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=RoundupDialogBinding.inflate(inflater,container,false)
        val view = binding.root
        dialogPresenter = RoundupDialogPresenter(activity as DialogContract.View ,lifecycleScope)
        // Configure the UI elements in the dialog
        binding.cancelRoundup.setOnClickListener(

            dialogPresenter.addRoundup()
        )
        return view
    }



    override fun onRoundSuccessfull() {

    }

    override fun onError() {

    }
}