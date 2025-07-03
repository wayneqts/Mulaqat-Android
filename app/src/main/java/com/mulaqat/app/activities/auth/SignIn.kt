package com.mulaqat.app.activities.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.gson.JsonObject
import com.mulaqat.app.R
import com.mulaqat.app.activities.BaseActivity
import com.mulaqat.app.activities.MainActivity
import com.mulaqat.app.databinding.ActivitySignInBinding
import com.mulaqat.app.model.Profile
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignIn : BaseActivity() {
    lateinit var binding : ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btBack.setOnClickListener { finish() }
        binding.btLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val pw = binding.etPw.text.toString()
            if (email.isEmpty()){
                Toast.makeText(this, getString(R.string.email_is_required), Toast.LENGTH_SHORT).show()
            }else if (!tool.isEmailValid(email)){
                Toast.makeText(this, getString(R.string.email_is_invalid), Toast.LENGTH_SHORT).show()
            }else if (pw.isEmpty()){
                Toast.makeText(this, getString(R.string.password_is_required), Toast.LENGTH_SHORT).show()
            }else{
                login()
            }
        }
    }

    // login
    private fun login() {
        tool.showLoading()
        val rqBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("email", binding.etEmail.text.toString().trim())
            .addFormDataPart("password", binding.etPw.text.toString().trim())
            .build()

        api.callApiWithBody("login.php", rqBody).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val jsonObject: JSONObject
                if (response.isSuccessful) {
                    try {
                        jsonObject = JSONObject(response.body().toString())
                        if (jsonObject.optBoolean("Status")) {
                            startActivity(Intent(this@SignIn, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
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
                        }
                        Toast.makeText(this@SignIn, jsonObject.optString("Message"), Toast.LENGTH_SHORT).show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                tool.hideLoading()
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                tool.hideLoading()
                Toast.makeText(this@SignIn, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
            }
        })
    }
}