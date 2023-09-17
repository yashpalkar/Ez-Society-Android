package com.ezmanagement.society.RoundUp.Dialog

interface DialogContract {
    interface View {

      fun onRoundSuccessfull()
        fun onError()
    }

    interface Presenter {
        fun addRoundup(
            imageUrl: String,
            notes: String,
            roundup_at: String,
            roundup_by: String,
            roundup_id: String,
            societyid: String,
            jwttoken: String
        )
    }
}