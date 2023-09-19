package com.ezmanagement.society.RoundUp

import android.net.Uri
import com.ezmanagement.society.GetRoundUpIdQuery
import java.io.File

interface RoundUpContract {

        interface View {
            fun validQr(societyRoundup: GetRoundUpIdQuery.Society_roundups_by_pk)
            fun inValidQr(message: String?)
            fun showPopup(societyRoundup: GetRoundUpIdQuery.Society_roundups_by_pk,imageUri: Uri?)
            fun dismissPopup()
            fun onRoundUpImageUploaded( key:String)
            fun onRoundUpImageUploadFailed( key:String)

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

            fun uploadImage(file: File, societyId:String, roundupId:String)

        }
    }
