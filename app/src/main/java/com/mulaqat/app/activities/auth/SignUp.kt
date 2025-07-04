package com.mulaqat.app.activities.auth

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.mulaqat.app.R
import com.mulaqat.app.activities.BaseActivity
import com.mulaqat.app.activities.MainActivity
import com.mulaqat.app.databinding.ActivitySignUpBinding
import com.mulaqat.app.helper.CustomSpinnerAdapter
import com.mulaqat.app.model.Profile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class SignUp : BaseActivity() {
    lateinit var binding: ActivitySignUpBinding
    lateinit var list : MutableList<String>
    var state: String = ""
    var gender: String = "male"
    var bmImg : Bitmap? = null
    companion object {
        private const val SELECT_FILE   = 100
        private const val CAMERA_CODE   = 101
        private const val REQUEST_CAMERA = 102
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

        binding.btBack.setOnClickListener { finish() }
        binding.rgGender.setOnCheckedChangeListener { group, checkedId ->
            gender = if (checkedId == R.id.rb_male) {
                "Male"
            } else {
                "Female"
            }
        }
        binding.btSubmit.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val name = binding.etName.text.toString().trim()
            val age = binding.etAge.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val city = binding.etCity.text.toString().trim()
            val zip = binding.etZip.text.toString().trim()
            val pw = binding.etPw.text.toString()
            if (name.isEmpty()){
                Toast.makeText(this, getString(R.string.name_is_required), Toast.LENGTH_SHORT).show()
            }else if (age.isEmpty()){
                Toast.makeText(this, getString(R.string.age_is_required), Toast.LENGTH_SHORT).show()
            }else if (phone.isEmpty()){
                Toast.makeText(this, getString(R.string.contact_number_is_required), Toast.LENGTH_SHORT).show()
            }else if (email.isEmpty()){
                Toast.makeText(this, getString(R.string.email_is_required), Toast.LENGTH_SHORT).show()
            }else if (!tool.isEmailValid(email)){
                Toast.makeText(this, getString(R.string.email_is_invalid), Toast.LENGTH_SHORT).show()
            }else if (pw.isEmpty()){
                Toast.makeText(this, getString(R.string.password_is_required), Toast.LENGTH_SHORT).show()
            }else if (city.isEmpty()){
                Toast.makeText(this, getString(R.string.city_is_required), Toast.LENGTH_SHORT).show()
            }else if (state == ""){
                Toast.makeText(this, getString(R.string.state_is_required), Toast.LENGTH_SHORT).show()
            }else if (zip.isEmpty()){
                Toast.makeText(this, getString(R.string.zipcode_is_required), Toast.LENGTH_SHORT).show()
            }else if (bmImg == null){
                Toast.makeText(this, getString(R.string.photo_is_required), Toast.LENGTH_SHORT).show()
            }else{
                signUp()
            }
        }
        binding.rlImg.setOnClickListener {
            popupSelectPhoto()
        }
        binding.spinnerState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    (view as? TextView)?.setTextColor(ContextCompat.getColor(this@SignUp, R.color.grey))
                } else {
                    state = list[position]
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No action needed
            }
        }
    }

    // init UI
    fun init(){
        list = ArrayList()
        list.add("State/Province")
        val jsonArray = tool.loadJsonArrayFromAssets(this, "states_titlecase.json")
        val stateArr = Array(jsonArray?.length()!! + 1) { "" }
        stateArr[0] = "State/Province"
        jsonArray.let {
            for (i in 0 until it.length()) {
                val obj = it.getJSONObject(i)
                list.add(obj.optString("name"))
                stateArr[i + 1] = obj.optString("name")
            }
        }
        val religionAdapter = CustomSpinnerAdapter(this@SignUp, android.R.layout.simple_spinner_item, stateArr)
        religionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerState.adapter = religionAdapter
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                galleryIntent()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.permission_access_device_image_denied),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    fun popupSelectPhoto() {
        val items = arrayOf<CharSequence>("Take a photo", "Choose from library")
        val builder = AlertDialog.Builder(this)
        builder.setItems(
            items
        ) { dialog: DialogInterface?, item: Int ->
            if (items[item] == "Take a photo") {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.CAMERA),
                        REQUEST_CAMERA
                    )
                } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    val intent =
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.setData(uri)
                    startActivity(intent)
                } else {
                    cameraIntent()
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_MEDIA_IMAGES
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        galleryIntent()
                    } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES)) {
                        val intent =
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.setData(uri)
                        startActivity(intent)
                    } else {
                        requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    }
                } else {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ),
                            1
                        )
                    } else {
                        galleryIntent()
                    }
                }
            }
        }
        builder.show()
    }

    private fun galleryIntent() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply { type = "image/*" }
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE)
    }

    private fun cameraIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1 -> { // READ/WRITE_EXTERNAL_STORAGE
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    galleryIntent()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.permission_access_device_image_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            REQUEST_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraIntent()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.permission_access_camera_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }

    // sign up
    fun signUp(){
        tool.showLoading()
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart("name", binding.etName.text.toString().trim()).addFormDataPart("gender", gender)
            .addFormDataPart("age", binding.etAge.text.toString().trim()).addFormDataPart("country", binding.etCountry.text.toString())
            .addFormDataPart("state", state).addFormDataPart("phone", binding.etPhone.text.toString().trim())
            .addFormDataPart("email", binding.etEmail.text.toString().trim()).addFormDataPart("password", binding.etPw.text.toString())
            .addFormDataPart("city", binding.etCity.text.toString().trim()).addFormDataPart("zip", binding.etZip.text.toString().trim())
            .addFormDataPart("notes", binding.etDes.text.toString().trim())
        val file = tool.getFileFromBm(bmImg!!)
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        builder.addFormDataPart("img1", file.name, requestFile)
        api.callApiWithBody("signup_api.php", builder.build()).enqueue(object :
            Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val jsonObject: JSONObject
                if (response.isSuccessful) {
                    try {
                        jsonObject = JSONObject(response.body().toString())
                        if (jsonObject.optBoolean("success")) {
                            login()
                            Toast.makeText(this@SignUp, jsonObject.optString("Message"), Toast.LENGTH_SHORT).show()
                        }else{
                            val arrErr = jsonObject.optJSONArray("errors");
                            Toast.makeText(this@SignUp, arrErr.get(0).toString(), Toast.LENGTH_SHORT).show()
                            tool.hideLoading()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                tool.hideLoading()
                Toast.makeText(this@SignUp, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
            }
        })
    }

    // login
    private fun login() {
        val rqBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("email", binding.etEmail.text.toString().trim())
            .addFormDataPart("password", binding.etPw.text.toString().trim())
            .build()

        api.callApiWithBody("login_api.php", rqBody).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val jsonObject: JSONObject
                if (response.isSuccessful) {
                    try {
                        jsonObject = JSONObject(response.body().toString())
                        if (jsonObject.optBoolean("Status")) {
                            val jsData = jsonObject.optJSONObject("data")
                            val pf = Profile(
                                jsData.optString("id"),
                                jsData.optString("name"),
                                jsData.optString("phone"),
                                jsData.optString("email"),
                                jsData.optString("password"),
                                jsData.optString("IMEI"),
                                jsData.optString("created")
                            )
                            pref.setPf(pf)
                            startActivity(Intent(this@SignUp, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                tool.hideLoading()
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                tool.hideLoading()
                Toast.makeText(this@SignUp, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == CAMERA_CODE) {
                bmImg = data.extras?.get("data") as? Bitmap
            }
        }
        if (requestCode == SELECT_FILE) {
            val uri = data!!.data
            try {
                bmImg = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        Glide.with(this).load(bmImg).centerCrop().into(binding.ivAvatar)
    }
}