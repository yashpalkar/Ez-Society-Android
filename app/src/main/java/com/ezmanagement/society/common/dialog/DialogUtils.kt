package com.ezmanagement.society.common.dialog

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.TextView
import com.ezmanagement.society.R

class DialogUtils {

    companion object {
        lateinit var alertDialog: AlertDialog
        fun ShowAlert(
            activity: Activity,
            title: String,
            message: String,
            positiveText: String,
            negativeText: String,
            dialogListener: DialogListener
        ) {
            var dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(activity)
            var inflater = activity.layoutInflater
            var dialogView = inflater.inflate(R.layout.dialog_common_base_alert, null)
            dialogBuilder.setView(dialogView)

            alertDialog = dialogBuilder.create()
//            alertDialog.window?.setBackgroundDrawable(
//                ColorDrawable(
//                    Color.TRANSPARENT
//                )
//            )

            var apptitileTextView = dialogView.findViewById<TextView>(R.id.apptitileTextView)
            var appPositiveTextView = dialogView.findViewById<TextView>(R.id.appPositiveTextView)
            var appNagativeTextView = dialogView.findViewById<TextView>(R.id.appNagativeTextView)
            var appMessageTextView = dialogView.findViewById<TextView>(R.id.appMessageTextView)

            if (title.isEmpty()) {
                apptitileTextView.visibility = View.GONE

            }
            if (message.isEmpty()) {
                appMessageTextView.visibility = View.GONE

            }
            if (negativeText.isEmpty()) {
                appNagativeTextView.visibility = View.GONE
            } else {
                appPositiveTextView.visibility = View.VISIBLE

            }
            apptitileTextView.setText(title)
            appMessageTextView.setText(message)
            appNagativeTextView.setText(negativeText)
            appPositiveTextView.setText(positiveText)
            appNagativeTextView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    alertDialog.dismiss()
                    if (dialogListener != null) dialogListener.onNegativeClicked()
                }

            })

            appPositiveTextView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    alertDialog.dismiss()
                    if (dialogListener != null) dialogListener.onPositiveClicked()
                }

            })
            alertDialog.setCancelable(false)
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()


        }
    }
}