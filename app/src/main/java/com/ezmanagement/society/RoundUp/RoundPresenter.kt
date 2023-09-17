package com.ezmanagement.society.RoundUp

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import com.ezmanagement.society.GetRoundUpIdQuery
import com.ezmanagement.society.Retrofit.ApiClient
import com.google.gson.Gson

class RoundPresenter(private val view: RoundUpContract.View,
                      private val lifecycleScope: LifecycleCoroutineScope
): RoundUpContract.Presenter {
    override fun isQRValid(qrcode: String, jwttoken: String) {

        var apiClient = ApiClient()
        lifecycleScope.launchWhenResumed {
            try {

                val response =
                    apiClient!!.getApolloClient().query(GetRoundUpIdQuery(qrcode))
                        .addHttpHeader(
                            "Authorization", "Bearer $jwttoken"
                        ).execute()
                Log.d("RESPONSE_ERROR", response.errors.toString())
                var society_roundups_by_pk: GetRoundUpIdQuery.Society_roundups_by_pk? =
                    response.data?.society_roundups_by_pk;
                val gson = Gson()
                if (society_roundups_by_pk?.id?.equals(qrcode) == true) {
                    view.validQr(society_roundups_by_pk)
                } else {
                    view.inValidQr("Invalid QR code")
                }

            } catch (e: Exception) {
                view.inValidQr(e.message.toString())
            }

//            return response?.data?.society_id
        }
    }
    override fun onShowPopupClicked(
        societyRoundup: GetRoundUpIdQuery.Society_roundups_by_pk,
        imageUri: Uri?
    ) {

        view.showPopup(societyRoundup,imageUri)
    }

    override fun onDismissPopupClicked() {
        // Implement logic to dismiss the popup
        view.dismissPopup()
    }


}