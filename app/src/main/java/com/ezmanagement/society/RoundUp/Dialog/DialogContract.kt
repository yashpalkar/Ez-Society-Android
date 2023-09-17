package com.ezmanagement.society.RoundUp.Dialog

import android.net.Uri

interface DialogContract {
    interface View {
        fun onRoundSuccessfull()
        fun onError(toString: String)
    }

    interface Presenter {
        fun addRoundup(
            imageUrl: Uri,
            notes: String,
            roundup_at: String,
            roundup_by: String,
            roundup_id: String,
            societyid: String,
            jwttoken: String
        )
    }
}