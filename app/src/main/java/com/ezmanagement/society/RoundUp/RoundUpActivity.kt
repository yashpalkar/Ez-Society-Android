package com.ezmanagement.society.RoundUp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import com.ezmanagement.society.common.LogUtlis
import com.ezmanagement.society.databinding.ActivityRoundUpBinding
import com.ezmanagement.society.sharedPreference.SharedPref
import com.ezmanagement.society.utils.Utils
import org.json.JSONException
import org.json.JSONObject

class RoundUpActivity : AppCompatActivity(), RoundUpContract.View {
    lateinit var binding: ActivityRoundUpBinding
    private lateinit var codeScanner: CodeScanner
    private lateinit var presenter: RoundUpContract.Presenter
    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val REQUEST_IMAGE_CAPTURE = 101
    var jwtToken: String? = null
    var societyId: String? = null
    var societyRoundup: GetRoundUpIdQuery.Society_roundups_by_pk? = null
    var sharedPref: SharedPref? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoundUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        codeScanner = CodeScanner(this, binding.scannerView)
        presenter = RoundPresenter(this, lifecycleScope = lifecycleScope)
        sharedPref = SharedPref(this);
        jwtToken = sharedPref!!.getUserData(
            AppConstants.JWTTOKEN,
            String::class.java,
            ""
        )
        societyId = sharedPref!!.getUserData(
            AppConstants.SOCIETY_ID,
            String::class.java,
            ""
        )
        if (ContextCompat.checkSelfPermission(this@RoundUpActivity, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                this@RoundUpActivity,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
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
                    var roundupid = obj.get("location")
                    LogUtlis.info("roundupid",roundupid.toString());
                    jwtToken?.let { it1 -> presenter.isQRValid(roundupid.toString(), it1) }
                    codeScanner.stopPreview()
                } catch (e: JSONException) {
                    Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
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
            binding.scannerView.visibility = View.GONE
            val imageUri: Uri? = data?.data
            val imageBitmap = data?.extras?.get("data") as Bitmap?
            if (imageBitmap != null) {
                var file = Utils.saveBitmapToExternalFilesDir(this, imageBitmap)
                if (file != null) {
                    presenter.uploadImage(file, societyId.toString(), societyRoundup?.id.toString())
                }
            }
//            imageUri?.let { uri ->
//                // Save the captured image to storage
//                val savedUri = saveImageToStorage(uri)
//                societyRoundup?.let { presenter.onShowPopupClicked(it, savedUri) }
//            }

        } else {
            societyRoundup = null
        }
    }

    override fun validQr(societyRoundup: GetRoundUpIdQuery.Society_roundups_by_pk) {
        this.societyRoundup = societyRoundup
        if (!allPermissionsGranted()) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                this@RoundUpActivity,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {

            openCamera()
        }

    }

    override fun inValidQr(message: String?) {
        societyRoundup = null
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        binding.scannerView.visibility = View.VISIBLE
        codeScanner.startPreview()
    }

    override fun showPopup(
        societyRoundup: GetRoundUpIdQuery.Society_roundups_by_pk,
        imageUri: Uri?
    ) {
        val popupDialog = imageUri?.let { RoundupDialog(societyRoundup, it) }
        popupDialog?.isCancelable = false
        popupDialog?.show(supportFragmentManager, "roundup_dialog")
    }


    override fun dismissPopup() {
        // Dismiss the popup dialog
        val popupDialog =
            supportFragmentManager.findFragmentByTag("roundup_dialog") as? DialogFragment
        startActivity(Intent(this, MainActivity::class.java))
        popupDialog?.dismiss()
    }

    override fun onRoundUpImageUploaded(key: String) {
        societyRoundup?.let { presenter.onShowPopupClicked(it, Uri.parse(key)) }
    }

    override fun onRoundUpImageUploadFailed(key: String) {
        TODO("Not yet implemented")
    }

    //    private fun saveImageToStorage(bitmap: Bitmap): File? {
//        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
//        val fileName = "IMG_$timeStamp.jpg"
//
//        val imageFile = File(getExternalFilesDir(null), fileName)
//
//        try {
//            val outputStream = FileOutputStream(imageFile)
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//            outputStream.flush()
//            outputStream.close()
//
//            // The image has been saved to storage
//            Toast.makeText(this, "Image saved to: ${imageFile.absolutePath}", Toast.LENGTH_SHORT)
//                .show()
//            return imageFile
//        } catch (e: IOException) {
//            e.printStackTrace()
//            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show()
//        }
//        return null
//    }
    private fun allPermissionsGranted(): Boolean {
        return arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}