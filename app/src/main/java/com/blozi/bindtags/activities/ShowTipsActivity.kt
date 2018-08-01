package com.blozi.bindtags.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.view.ViewPager
import android.support.v7.widget.AppCompatImageButton
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.Toast

import com.blozi.bindtags.R
import com.blozi.bindtags.adapter.ImagePageAdapter
import com.blozi.bindtags.asyncTask.online.LoginTask
import com.blozi.bindtags.util.BLOZIPreferenceManager
import com.blozi.bindtags.util.SystemConstants

import java.util.ArrayList

class ShowTipsActivity : CommonActivity(), View.OnClickListener {
    private var viewPager: ViewPager? = null
    private var radio1: RadioButton? = null
    private var radio2: RadioButton? = null
    private var radio3: RadioButton? = null
    private var radio4: RadioButton? = null
    private var radio5: RadioButton? = null
    private var radio6: RadioButton? = null
    private var enter: AppCompatImageButton? = null
    private var progress: ProgressBar? = null
    private var allow = false
    private var isLogin = false
    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            val bundle = msg.data
            if (msg.what == 1) {
                msg.what = 0
                val m = bundle.get("msg") as String
                Toast.makeText(this@ShowTipsActivity, m, Toast.LENGTH_LONG).show()
                startActivity(Intent(this@ShowTipsActivity, LoginActivity::class.java))
                finish()
            }
        }
    }
    private val runable = Runnable { allow = true;login() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_tips)
        initId()
        showJrunmp()
        handler.postDelayed(runable, 1000)
        val pathList = SystemConstants.productsImagsInt
        val viewAdapter = ImagePageAdapter(pathList.toList())
        viewPager!!.adapter = viewAdapter

        viewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
                //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                //   TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onPageSelected(index: Int) {
                if (index == SystemConstants.productsImags.size - 1) enter!!.visibility = View.VISIBLE
                when (index) {
                    0 -> {
                        radio1!!.isChecked = true
                    }
                    1 -> {
                        radio2!!.isChecked = true
                    }
                    2 -> {
                        radio3!!.isChecked = true
                    }
                    3 -> {
                        radio4!!.isChecked = true
                    }
                    4 -> {
                        radio5!!.isChecked = true
                    }
                    5 -> {
                        radio6!!.isChecked = true
                    }
                }
            }
        })
        radio1!!.setOnClickListener(this)
        radio2!!.setOnClickListener(this)
        radio3!!.setOnClickListener(this)
        radio4!!.setOnClickListener(this)
        radio5!!.setOnClickListener(this)
        radio6!!.setOnClickListener(this)
        enter!!.setOnClickListener(this)
        radio1!!.isChecked = true
    }

    private fun initId() {
//        var blozi = BLOZIPreferenceManager.getInstance(this)
//        if (blozi != null && !TextUtils.isEmpty(blozi.loginid) && !TextUtils.isEmpty(blozi.password) && blozi.lastLogin == BLOZIPreferenceManager.OnlineSystem) {//判断是否登录
//            startActivity(Intent(this, MainActivity::class.java))
//            val loginService = LoginTask(this, blozi.compliteURL)
//            loginService.execute(blozi.loginid, blozi.password)
//        }
        viewPager = findViewById(R.id.activity_index_view_page)
        radio1 = findViewById(R.id.radio1)
        radio2 = findViewById(R.id.radio2)
        radio3 = findViewById(R.id.radio3)
        radio4 = findViewById(R.id.radio4)
        radio5 = findViewById(R.id.radio5)
        radio6 = findViewById(R.id.radio6)
        enter = findViewById(R.id.enter)
        progress = findViewById(R.id.progress)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.radio1 -> {
                viewPager!!.setCurrentItem(0, false)
            }
            R.id.radio2 -> {
                viewPager!!.setCurrentItem(1, false)
            }
            R.id.radio3 -> {
                viewPager!!.setCurrentItem(2, false)
            }
            R.id.radio4 -> {
                viewPager!!.setCurrentItem(3, false)
            }
            R.id.radio5 -> {
                viewPager!!.setCurrentItem(4, false)
            }
            R.id.radio6 -> {
                viewPager!!.setCurrentItem(5, false)
            }
            R.id.enter -> {
                if (isLogin) {
                    Toast.makeText(this, R.string.loging, Toast.LENGTH_SHORT).show()
                } else {
                    handler.removeCallbacks(runable)
                    startActivity(Intent(this@ShowTipsActivity, LoginActivity::class.java))
                    overridePendingTransition(R.anim.in_right, R.anim.out_left)
                    finish()
                }

            }
        }

    }

    private fun showJrunmp() {
        if (!TextUtils.isEmpty(userSession) && !TextUtils.isEmpty(userPass) && bloziPreferenceManager != null && bloziPreferenceManager.lastLogin == BLOZIPreferenceManager.OnlineSystem) {//判断是否登录
            enter!!.visibility = View.VISIBLE
        }
    }

    private fun login() {
        if (!TextUtils.isEmpty(userSession) && !TextUtils.isEmpty(userPass) && bloziPreferenceManager != null && bloziPreferenceManager.lastLogin == BLOZIPreferenceManager.OnlineSystem) {//判断是否登录
            val webServiceUrl = bloziPreferenceManager.compliteURL
            progress!!.visibility = View.VISIBLE
            isLogin = true
            val loginService = LoginTask(this@ShowTipsActivity, webServiceUrl)
            loginService.execute(userSession, userPass)
        }
    }


    override fun getHandler(): Handler {
        return handler
    }

    override fun onResume() {
        super.onResume()
    }


}

