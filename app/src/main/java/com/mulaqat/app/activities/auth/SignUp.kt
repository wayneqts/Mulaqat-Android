package com.mulaqat.app.activities.auth

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mulaqat.app.R
import com.mulaqat.app.activities.BaseActivity
import com.mulaqat.app.databinding.ActivitySignUpBinding

class SignUp : BaseActivity() {
    lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btBack.setOnClickListener { finish() }
        binding.btSubmit.setOnClickListener {

        }
    }
}