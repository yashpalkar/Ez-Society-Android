package com.ezmanagement.society.RoundUp

import android.net.Uri
import com.ezmanagement.society.GetRoundUpIdQuery

interface RoundUpContract {

        interface View {
            fun validQr(societyRoundup: GetRoundUpIdQuery.Society_roundups_by_pk)
            fun inValidQr(message: String?)
            fun showPopup(societyRoundup: GetRoundUpIdQuery.Society_roundups_by_pk,imageUri: Uri?)
            fun dismissPopup()
        }

        interface Presenter {
            fun isQRValid(
                qrcode: String, jwttoken: String
            )
            fun onShowPopupClicked(
                societyRoundup: GetRoundUpIdQuery.Society_roundups_by_pk,
                imageUri: Uri?
            )
            fun onDismissPopupClicked()

        }
    }
