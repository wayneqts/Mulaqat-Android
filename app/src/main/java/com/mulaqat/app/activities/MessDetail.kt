package com.mulaqat.app.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mulaqat.app.R
import com.mulaqat.app.databinding.ActivityMessDetailBinding

class MessDetail : BaseActivity() {
    lateinit var binding: ActivityMessDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}