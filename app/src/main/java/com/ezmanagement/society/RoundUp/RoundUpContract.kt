package com.ezmanagement.society.RoundUp

import com.ezmanagement.society.GetRoundUpIdQuery
import com.ezmanagement.society.VisitorListBySocietyIdQuery

interface RoundUpContract {

        interface View {
            fun validQr(societyRoundup: GetRoundUpIdQuery.Society_roundups_by_pk)
            fun inValidQr(message: String?)
            fun showPopup()
            fun dismissPopup()
        }

        interface Presenter {
            fun isQRValid(
                qrcode: String, jwttoken: String
            )
            fun onShowPopupClicked()
            fun onDismissPopupClicked()

        }
    }
