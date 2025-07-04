package com.mulaqat.app.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.mulaqat.app.R
import com.mulaqat.app.activities.auth.SignIn
import com.mulaqat.app.activities.auth.SignUp
import com.mulaqat.app.databinding.ActivityMainBinding
import com.mulaqat.app.fragment.FrmMess
import com.mulaqat.app.fragment.FrmNoti


class MainActivity : BaseActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var frmMng: FragmentManager
    lateinit var frmTs: FragmentTransaction
    lateinit var crFrm: Fragment
    var frmNoti: FrmNoti? = null
    var frmMess: FrmMess? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

        binding.btTb.setOnClickListener { binding.main.openDrawer(GravityCompat.START) }
        binding.main.addDrawerListener(object : DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerOpened(drawerView: View) {
                tool.hideKeyboard(drawerView)
            }

            override fun onDrawerClosed(drawerView: View) {
            }

            override fun onDrawerStateChanged(newState: Int) {
            }
        })
        binding.mnHome.setOnClickListener {
            selectedMn(
                binding.mnHome, binding.mnEvent, binding.mnBlog, binding.mnMess, binding.mnNoti, binding.mnSchedule, binding.mnSearch, binding.mnPricing, binding.mnService,
                binding.tvMnHome, binding.tvMnEvent, binding.tvMnBlog, binding.tvMnMess, binding.tvMnNoti, binding.tvMnPricing, binding.tvMnService, binding.tvMnSchedule, binding.tvMnSearch,
                binding.icMnHome, binding.icMnEvent, binding.icMnBlog, binding.icMnMess, binding.icMnNoti, binding.icMnPricing, binding.icMnService, binding.icMnSchedule, binding.icMnSearch
            )
            showWvHome()
            binding.wvHome.evaluateJavascript("window.scrollTo(0, 0);", null);
        }
        binding.mnService.setOnClickListener {
            selectedMn(
                binding.mnService, binding.mnEvent, binding.mnHome, binding.mnMess, binding.mnNoti, binding.mnSchedule, binding.mnSearch, binding.mnPricing, binding.mnBlog,
                binding.tvMnService, binding.tvMnEvent, binding.tvMnHome, binding.tvMnMess, binding.tvMnNoti, binding.tvMnPricing, binding.tvMnBlog, binding.tvMnSchedule, binding.tvMnSearch,
                binding.icMnService, binding.icMnEvent, binding.icMnHome, binding.icMnMess, binding.icMnNoti, binding.icMnPricing, binding.icMnBlog, binding.icMnSchedule, binding.icMnSearch
            )
            showWvHome()
            binding.wvHome.evaluateJavascript(
                "window.location.hash = '#services';", null)
        }
        binding.mnPricing.setOnClickListener {
            selectedMn(
                binding.mnPricing, binding.mnEvent, binding.mnHome, binding.mnMess, binding.mnNoti, binding.mnSchedule, binding.mnSearch, binding.mnBlog, binding.mnService,
                binding.tvMnPricing, binding.tvMnEvent, binding.tvMnHome, binding.tvMnMess, binding.tvMnNoti, binding.tvMnBlog, binding.tvMnService, binding.tvMnSchedule, binding.tvMnSearch,
                binding.icMnPricing, binding.icMnEvent, binding.icMnHome, binding.icMnMess, binding.icMnNoti, binding.icMnBlog, binding.icMnService, binding.icMnSchedule, binding.icMnSearch
            )
            showWvHome()
            binding.wvHome.evaluateJavascript(
                "window.location.hash = '#pricing';", null)
        }
        binding.mnEvent.setOnClickListener {
            selectedMn(
                binding.mnEvent, binding.mnBlog, binding.mnHome, binding.mnMess, binding.mnNoti, binding.mnSchedule, binding.mnSearch, binding.mnPricing, binding.mnService,
                binding.tvMnEvent, binding.tvMnBlog, binding.tvMnHome, binding.tvMnMess, binding.tvMnNoti, binding.tvMnPricing, binding.tvMnService, binding.tvMnSchedule, binding.tvMnSearch,
                binding.icMnEvent, binding.icMnBlog, binding.icMnHome, binding.icMnMess, binding.icMnNoti, binding.icMnPricing, binding.icMnService, binding.icMnSchedule, binding.icMnSearch
            )
            showWvPage()
            binding.pbLoading.visibility = View.VISIBLE
            binding.wvPage.loadUrl(getString(R.string.event_url))
        }
        binding.mnSchedule.setOnClickListener {
            selectedMn(
                binding.mnSchedule, binding.mnEvent, binding.mnHome, binding.mnMess, binding.mnNoti, binding.mnBlog, binding.mnSearch, binding.mnPricing, binding.mnService,
                binding.tvMnSchedule, binding.tvMnEvent, binding.tvMnHome, binding.tvMnMess, binding.tvMnNoti, binding.tvMnPricing, binding.tvMnService, binding.tvMnBlog, binding.tvMnSearch,
                binding.icMnSchedule, binding.icMnEvent, binding.icMnHome, binding.icMnMess, binding.icMnNoti, binding.icMnPricing, binding.icMnService, binding.icMnBlog, binding.icMnSearch
            )
            showWvPage()
            binding.pbLoading.visibility = View.VISIBLE
            binding.wvPage.loadUrl(getString(R.string.schedule_url))
        }
        binding.mnLogin.setOnClickListener {
            binding.main.closeDrawer(GravityCompat.START)
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, SignIn::class.java))
            }, 300)
        }
        binding.mnJoin.setOnClickListener {
            binding.main.closeDrawer(GravityCompat.START)
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, SignUp::class.java))
            }, 300)
        }
        binding.mnNoti.setOnClickListener {
            if (pref.getPf() != null){
                selectedMn(
                    binding.mnNoti, binding.mnEvent, binding.mnHome, binding.mnMess, binding.mnBlog, binding.mnSchedule, binding.mnSearch, binding.mnPricing, binding.mnService,
                    binding.tvMnNoti, binding.tvMnEvent, binding.tvMnHome, binding.tvMnMess, binding.tvMnBlog, binding.tvMnPricing, binding.tvMnService, binding.tvMnSchedule, binding.tvMnSearch,
                    binding.icMnNoti, binding.icMnEvent, binding.icMnHome, binding.icMnMess, binding.icMnBlog, binding.icMnPricing, binding.icMnService, binding.icMnSchedule, binding.icMnSearch
                )
                showFrm()
                if (frmNoti != null) {
                    if (crFrm !== frmNoti) {
                        showFrmWithTitle(frmNoti!!, getString(R.string.notifications))
                    }
                } else {
                    frmNoti = FrmNoti()
                    loadFrm(frmNoti!!, getString(R.string.notifications))
                }
            } else {
                binding.main.closeDrawer(GravityCompat.START)
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this, SignUp::class.java))
                }, 300)
            }
        }
        binding.mnMess.setOnClickListener {
            if (pref.getPf() != null){
                selectedMn(
                    binding.mnMess, binding.mnEvent, binding.mnHome, binding.mnBlog, binding.mnNoti, binding.mnSchedule, binding.mnSearch, binding.mnPricing, binding.mnService,
                    binding.tvMnMess, binding.tvMnEvent, binding.tvMnHome, binding.tvMnBlog, binding.tvMnNoti, binding.tvMnPricing, binding.tvMnService, binding.tvMnSchedule, binding.tvMnSearch,
                    binding.icMnMess, binding.icMnEvent, binding.icMnHome, binding.icMnBlog, binding.icMnNoti, binding.icMnPricing, binding.icMnService, binding.icMnSchedule, binding.icMnSearch
                )
                showFrm()
                if (frmMess != null) {
                    if (crFrm !== frmMess) {
                        showFrmWithTitle(frmMess!!, getString(R.string.messages))
                    }
                } else {
                    frmMess = FrmMess()
                    loadFrm(frmMess!!, getString(R.string.messages))
                }
            } else {
                binding.main.closeDrawer(GravityCompat.START)
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this, SignUp::class.java))
                }, 300)
            }
        }
        binding.mnSearch.setOnClickListener {
            selectedMn(
                binding.mnSearch, binding.mnEvent, binding.mnHome, binding.mnMess, binding.mnNoti, binding.mnSchedule, binding.mnBlog, binding.mnPricing, binding.mnService,
                binding.tvMnSearch, binding.tvMnEvent, binding.tvMnHome, binding.tvMnMess, binding.tvMnNoti, binding.tvMnPricing, binding.tvMnService, binding.tvMnSchedule, binding.tvMnBlog,
                binding.icMnSearch, binding.icMnEvent, binding.icMnHome, binding.icMnMess, binding.icMnNoti, binding.icMnPricing, binding.icMnService, binding.icMnSchedule, binding.icMnBlog
            )
            showWvPage()
            binding.pbLoading.visibility = View.VISIBLE
            binding.wvPage.loadUrl(getString(R.string.search_url))
        }
        binding.mnBlog.setOnClickListener {
            selectedMn(
                binding.mnBlog, binding.mnEvent, binding.mnHome, binding.mnMess, binding.mnNoti, binding.mnSchedule, binding.mnSearch, binding.mnPricing, binding.mnService,
                binding.tvMnBlog, binding.tvMnEvent, binding.tvMnHome, binding.tvMnMess, binding.tvMnNoti, binding.tvMnPricing, binding.tvMnService, binding.tvMnSchedule, binding.tvMnSearch,
                binding.icMnBlog, binding.icMnEvent, binding.icMnHome, binding.icMnMess, binding.icMnNoti, binding.icMnPricing, binding.icMnService, binding.icMnSchedule, binding.icMnSearch
            )
            showWvPage()
            binding.pbLoading.visibility = View.VISIBLE
            binding.wvPage.loadUrl(getString(R.string.blog_url))
        }
        binding.mnLogout.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to logout?")
                .setTitle(getString(R.string.app_name))
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, _ ->
                    binding.main.closeDrawer(GravityCompat.START)
                    pref.setPf(null)
                    checkMn()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.cancel()
                }

            val alertDialog = builder.create()
            alertDialog.show()
        }
    }

    // init UI
    private fun init(){
        frmMng = supportFragmentManager
        val width = (resources.displayMetrics.widthPixels / 1.3).toInt()
        val params = binding.rlMenu.layoutParams
        params.width = width
        binding.rlMenu.setLayoutParams(params)
        binding.rlMenu.bringToFront()
        val toggle = ActionBarDrawerToggle(
            this,
            binding.main,
            binding.toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )
        binding.main.addDrawerListener(toggle)
        toggle.syncState()
        val webSetting = binding.wvHome.settings
        webSetting.setJavaScriptEnabled(true);
        binding.wvHome.loadUrl(getString(R.string.home_url))
        binding.wvHome.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                binding.pbLoading.visibility = View.GONE
            }
        })
        val webStPage = binding.wvPage.settings
        webStPage.setJavaScriptEnabled(true);
        binding.wvPage.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                binding.pbLoading.visibility = View.GONE
            }
        })
        checkMn()
    }

    // check menu
    fun checkMn(){
        if (pref.getPf() != null) {
            binding.mnLogout.visibility = View.VISIBLE
            binding.mnDeleteAcc.visibility = View.VISIBLE
            binding.mnJoin.visibility = View.GONE
            binding.mnLogin.visibility = View.GONE
        }else {
            binding.mnLogout.visibility = View.GONE
            binding.mnDeleteAcc.visibility = View.GONE
            binding.mnJoin.visibility = View.VISIBLE
            binding.mnLogin.visibility = View.VISIBLE
        }
    }

    // show webview home
    fun showWvHome(){
        binding.wvHome.visibility = View.VISIBLE
        binding.wvPage.visibility = View.GONE
        binding.flMain.visibility = View.GONE
        binding.ivHomeLogo.visibility = View.VISIBLE
        binding.tvTitle.visibility = View.GONE
    }

    // show webview page
    fun showWvPage(){
        binding.wvHome.visibility = View.GONE
        binding.wvPage.visibility = View.VISIBLE
        binding.flMain.visibility = View.GONE
        binding.ivHomeLogo.visibility = View.VISIBLE
        binding.tvTitle.visibility = View.GONE
    }

    // show fragment
    fun showFrm(){
        binding.wvHome.visibility = View.GONE
        binding.wvPage.visibility = View.GONE
        binding.flMain.visibility = View.VISIBLE
        binding.ivHomeLogo.visibility = View.GONE
        binding.tvTitle.visibility = View.VISIBLE
    }

    // load fragment
    private fun loadFrm(frm: Fragment, title: String) {
        frmTs = frmMng.beginTransaction()
        frmTs.add(R.id.fl_main, frm).commit()
        crFrm = frm
        binding.tvTitle.setText(title)
    }

    // show fragment with title
    private fun showFrmWithTitle(frm: Fragment, title: String) {
        frmTs = frmMng.beginTransaction()
        frmTs.hide(crFrm).show(frm).commit()
        crFrm = frm
        binding.tvTitle.setText(title)
    }

    // selected menu
    private fun selectedMn(llSl: LinearLayout, llUnSl1 : LinearLayout, llUnSl2 : LinearLayout, llUnSl3 : LinearLayout, llUnSl4 : LinearLayout, llUnSl5 : LinearLayout, llUnSl6 : LinearLayout,
                           llUnSl7 : LinearLayout, llUnSl8 : LinearLayout,tvSl : TextView, tvUnSl1 : TextView, tvUnSl2 : TextView, tvUnSl3 : TextView, tvUnSl4 : TextView, tvUnSl5 : TextView,
                           tvUnSl6 : TextView, tvUnSl7 : TextView, tvUnSl8 : TextView,icSl : ImageView, icUnSl1 : ImageView, icUnSl2 : ImageView, icUnSl3 : ImageView, icUnSl4 : ImageView,
                           icUnSl5 : ImageView, icUnSl6 : ImageView, icUnSl7 : ImageView, icUnSl8 : ImageView){
        binding.main.closeDrawer(GravityCompat.START)
        llSl.setBackgroundResource(R.drawable.bg_mn_selected)
        llUnSl1.setBackgroundResource(android.R.color.transparent)
        llUnSl2.setBackgroundResource(android.R.color.transparent)
        llUnSl3.setBackgroundResource(android.R.color.transparent)
        llUnSl4.setBackgroundResource(android.R.color.transparent)
        llUnSl5.setBackgroundResource(android.R.color.transparent)
        llUnSl6.setBackgroundResource(android.R.color.transparent)
        llUnSl7.setBackgroundResource(android.R.color.transparent)
        llUnSl8.setBackgroundResource(android.R.color.transparent)
        tvSl.setTextColor(getColor(R.color.white))
        tvUnSl1.setTextColor(getColor(R.color.grey))
        tvUnSl2.setTextColor(getColor(R.color.grey))
        tvUnSl3.setTextColor(getColor(R.color.grey))
        tvUnSl4.setTextColor(getColor(R.color.grey))
        tvUnSl5.setTextColor(getColor(R.color.grey))
        tvUnSl6.setTextColor(getColor(R.color.grey))
        tvUnSl7.setTextColor(getColor(R.color.grey))
        tvUnSl8.setTextColor(getColor(R.color.grey))
        icSl.setColorFilter(getColor(R.color.white))
        icUnSl1.setColorFilter(getColor(R.color.grey))
        icUnSl2.setColorFilter(getColor(R.color.grey))
        icUnSl3.setColorFilter(getColor(R.color.grey))
        icUnSl4.setColorFilter(getColor(R.color.grey))
        icUnSl5.setColorFilter(getColor(R.color.grey))
        icUnSl6.setColorFilter(getColor(R.color.grey))
        icUnSl7.setColorFilter(getColor(R.color.grey))
        icUnSl8.setColorFilter(getColor(R.color.grey))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 123){
            checkMn()
        }
    }
}