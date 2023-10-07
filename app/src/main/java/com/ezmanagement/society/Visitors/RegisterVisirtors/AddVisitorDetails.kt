package com.ezmanagement.society.Visitors.RegisterVisirtors


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.ezmanagement.society.*
import com.ezmanagement.society.Model.VisitorModel
import com.ezmanagement.society.Visitors.AddVisitor
import com.ezmanagement.society.Visitors.VisitorCallBack
import com.ezmanagement.society.Visitors.VisitorCheckInPresenter
import com.ezmanagement.society.databinding.ActivityAddVisitorDetailsBinding
import com.ezmanagement.society.sharedPreference.SharedPref
import com.ezmanagement.society.utils.Utils
import java.io.File
import java.time.LocalDateTime


class AddVisitorDetails : BaseActivity(),
    View.OnClickListener, AddVisitorContract.View,
    VisitorCallBack.RegisterVisitorCallBack, VisitorCallBack.VisitorCheckInCallBack {
    private lateinit var binding: ActivityAddVisitorDetailsBinding
    var sharedPref: SharedPref? = null
    var visitorModel: VisitorModel? = null
    var presenter: AddVisitorDetailsPresenter? = null
    var visitorDetailsPresenter: VisitorCheckInPresenter? = null
    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val REQUEST_IMAGE_CAPTURE = 101
    var file: File? = null
    var visitorType: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVisitorDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = SharedPref(this);
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        presenter = AddVisitorDetailsPresenter(lifecycleScope, this)
        visitorDetailsPresenter = VisitorCheckInPresenter(lifecycleScope)

        visitorModel = intent.getParcelableExtra<VisitorModel>(AppConstants.REGISTERED_VISITOR)
        val mobNumber = intent.getStringExtra(AppConstants.CONTACT_NO)
        visitorType = intent.getStringExtra(AppConstants.VISITORTYPE)
        if (visitorModel != null) {
            binding.visitorNameTextInputLayoutEditText.setText(visitorModel?.name)
            binding.visitorMobileTextInputLayoutEditText.setText(visitorModel?.contact_no)
            Glide.with(this)
                .load(BuildConfig.MINIO_URL+ visitorModel!!.image)
                .placeholder(R.drawable.profile_avatar)
                .error(R.drawable.profile_avatar)
                .fitCenter()
                .into(binding.visitiorIcon)
        } else if (mobNumber != null) {
            binding.visitorMobileTextInputLayoutEditText.setText(mobNumber)
        }
        val jwtToken = sharedPref!!.getUserData(
            AppConstants.JWTTOKEN,
            String::class.java,
            ""
        )
        binding.submitVisitorButton.setOnClickListener(this)
        binding.addimageButton.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(p0: View?) {
        when (p0?.id) {

            R.id.submitVisitorButton -> {
                if (!validatedetails()) return
                val currentDateTime = LocalDateTime.now()

                val jwtToken = sharedPref!!.getUserData(
                    AppConstants.JWTTOKEN, String::class.java, ""
                )
                val guardId = sharedPref!!.getUserData(
                    AppConstants.GUARDID, String::class.java, ""
                )
                val societyId = sharedPref!!.getUserData(
                    AppConstants.SOCIETY_ID, String::class.java, ""
                )
                if (visitorType.equals(AppConstants.NEWVISITOR)) {
                    if (file != null) {
                        visitorType?.let {
                            visitorDetailsPresenter?.visitorCheckIn(
                                jwtToken,
                                visitorname = binding.visitorNameTextInputLayoutEditText.text.toString(),
                                visitorType = it,
                                contact_no = binding.visitorMobileTextInputLayoutEditText.text.toString(),
                                check_in = currentDateTime.toString(),
                                guardId = guardId,
                                flat_no = binding.personToMeetLayoutEditText.text.toString(),
                                societyId = societyId,
                                false,
                                image = "public/society/" + societyId + "/visitor/" + file!!.name,
                                this@AddVisitorDetails
                            )
                        }
                    } else {
                        Toast.makeText(this, "Please Add Image", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    var image:String
                    if (file?.name == null) {
                      image = visitorModel?.image.toString()
                    }else {
                        image ="public/society/" + societyId + "/visitor/"+ file?.name!!
                    }
                    visitorModel?.let {
                        visitorType?.let { it1 ->
                            visitorModel!!.contact_no?.let { it2 ->
                                presenter?.visitorRecheckIn(
                                    jwtToken,
                                    it1,
                                    it.id.toString(),
                                    currentDateTime.toString(),
                                    binding.personToMeetLayoutEditText.text.toString(),
                                    guardId,
                                   image,
                                visitorModel?.last_visited_at.toString(),
                                binding.visitorNameTextInputLayoutEditText.text.toString(),
                                false.toString(),
                                societyId,
                                it2,
                                this@AddVisitorDetails
                                )
                            }
                        }
                    }
                }
            }


        R.id.addimageButton -> {
            if (ContextCompat.checkSelfPermission(
                    this@AddVisitorDetails,
                    Manifest.permission.CAMERA
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is not granted, request it
                ActivityCompat.requestPermissions(
                    this@AddVisitorDetails,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST_CODE
                )
            } else {
                // Permission is already granted, open the camera
                openCamera()
            }
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
        val imageBitmap = data?.extras?.get("data") as Bitmap?
        if (imageBitmap != null) {
            file = Utils.saveBitmapToExternalFilesDir(this, imageBitmap)
            if (file != null) {
binding.visitiorIcon.setImageBitmap(imageBitmap)
            }
        }
    }
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
            openCamera()
        } else {

            Toast.makeText(this@AddVisitorDetails, "Camera permission denied", Toast.LENGTH_SHORT)
                .show()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
override fun visiterRegisterSuccessfully(visitorId: String, societyId: String) {
    val currentDateTime = LocalDateTime.now()
    if (file != null) {
        file?.let { presenter?.uploadImage(it, societyId) }
    } else {
        startActivity(Intent(this, MainActivity::class.java))
        this.finish()
    }
}

override fun onError() {
    TODO("Not yet implemented")
}

override fun onFailure(message: String) {
    TODO("Not yet implemented")
}

@RequiresApi(Build.VERSION_CODES.O)
override fun visiterCheckInSuccessfully(message: String, societyId: String) {
    Toast.makeText(this, "Visitor Check-in Successfully", Toast.LENGTH_SHORT).show()
    val currentDateTime = LocalDateTime.now()
    if (file != null) {
        file?.let { presenter?.uploadImage(it, societyId) }
    } else {
        startActivity(Intent(this, MainActivity::class.java))
        this.finish()
    }
}


override fun imageuploadfailed() {

    startActivity(Intent(this, MainActivity::class.java))
    this.finish()
}

override fun imageuploadSuccessfully() {
    startActivity(Intent(this, MainActivity::class.java))
    this.finish()
}

override fun showToast(message: String) {
    TODO("Not yet implemented")
}

override fun showAlert(message: String) {
    TODO("Not yet implemented")
}

override fun showProgressDialog(resId: Int) {
    TODO("Not yet implemented")
}

override fun showProgressDialog() {
    TODO("Not yet implemented")
}

override fun dismissProgressDialog() {
    TODO("Not yet implemented")
}

fun validatedetails(): Boolean {
    if (binding.visitorMobileTextInputLayoutEditText.text.isNullOrEmpty() || binding.visitorNameTextInputLayoutEditText.text.isNullOrEmpty()) {
        Toast.makeText(this, "Please Fill required details", Toast.LENGTH_SHORT).show()
        return false
    }
    return true

}

    override fun onBackPressed() {
        super.onBackPressed()
        var intent=Intent(this@AddVisitorDetails, AddVisitor::class.java)
        intent.putExtra(AppConstants.CONTACT_NO,binding.visitorMobileTextInputLayoutEditText.text.toString())
        startActivity(intent)
        this.finish()
    }

}