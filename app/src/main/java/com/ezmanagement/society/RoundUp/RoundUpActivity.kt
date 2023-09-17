package com.ezmanagement.society.RoundUp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.budiyev.android.codescanner.*
import com.ezmanagement.society.AppConstants
import com.ezmanagement.society.GetRoundUpIdQuery
import com.ezmanagement.society.MainActivity
import com.ezmanagement.society.RoundUp.Dialog.RoundupDialog

import com.ezmanagement.society.databinding.ActivityRoundUpBinding
import com.ezmanagement.society.sharedPreference.SharedPref
import org.json.JSONException
import org.json.JSONObject

class RoundUpActivity : AppCompatActivity(),RoundUpContract.View{
    lateinit var binding: ActivityRoundUpBinding
    private lateinit var codeScanner: CodeScanner
    private lateinit var presenter: RoundUpContract.Presenter
    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val REQUEST_IMAGE_CAPTURE = 101
    var jwtToken:String?=null
    var sharedPref: SharedPref? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoundUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        codeScanner = CodeScanner(this, binding.scannerView)
        presenter=RoundPresenter(this, lifecycleScope = lifecycleScope)
        sharedPref = SharedPref(this);
        jwtToken = sharedPref!!.getUserData(
            AppConstants.JWTTOKEN,
            String::class.java,
            ""
        )
        if (ContextCompat.checkSelfPermission(this@RoundUpActivity, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                this@RoundUpActivity,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            scanQr()
        }
    }

    private fun scanQr() {
        codeScanner.startPreview()
        codeScanner.camera =
            CodeScanner.CAMERA_BACK  // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,

        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {

                try {
                    var obj = JSONObject(it.text.toString())
                    var roundupid = obj.get("roundupId")
                    jwtToken?.let { it1 -> presenter.isQRValid(roundupid.toString(), it1) }
                    codeScanner.stopPreview()
                }catch(e:JSONException){
                    Toast.makeText(this,e.message.toString(),Toast.LENGTH_LONG).show()
                    codeScanner.startPreview()
                    return@runOnUiThread
                }

            }

        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(
                    this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (binding.scannerView?.getVisibility() == View.VISIBLE) {
            codeScanner.startPreview()
        }
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open the camera
                scanQr()
            } else {
                startActivity(Intent(this, MainActivity::class.java))
                Toast.makeText(this@RoundUpActivity, "Camera permission denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)

        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            binding.scannerView.visibility=View.GONE
            val imageUri: Uri? = data?.data
            presenter.onShowPopupClicked()
        }
    }

    override fun validQr(societyRoundup: GetRoundUpIdQuery.Society_roundups_by_pk) {
        if (ContextCompat.checkSelfPermission(this@RoundUpActivity, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this@RoundUpActivity,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE)
        } else {
            // Permission is already granted, open the camera

            openCamera()
        }

    }

    override fun inValidQr(message: String?) {
      Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        binding.scannerView.visibility=View.VISIBLE
        codeScanner.startPreview()
    }
    override fun showPopup() {
        // Create and show the popup dialog using a DialogFragment or a custom dialog class
        val popupDialog = RoundupDialog()
        popupDialog.show(supportFragmentManager, "roundup_dialog")
    }
    override fun dismissPopup() {
        // Dismiss the popup dialog
        val popupDialog = supportFragmentManager.findFragmentByTag("roundup_dialog") as? DialogFragment
        popupDialog?.dismiss()
    }
}