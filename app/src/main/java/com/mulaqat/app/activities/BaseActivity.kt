package com.mulaqat.app.activities

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.mulaqat.app.api.APIUtils
import com.mulaqat.app.api.AllNameAPI
import com.mulaqat.app.helper.AppSharedPreferences
import com.mulaqat.app.helper.Tool

open class BaseActivity : AppCompatActivity() {
    lateinit var tool : Tool
    lateinit var api : AllNameAPI
    lateinit var pref : AppSharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        tool = Tool(this)
        api = APIUtils.getAPIService()
        pref = AppSharedPreferences(this)
    }
}