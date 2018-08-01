package com.blozi.bindtags.activities

import android.content.Context
import android.os.Bundle
import android.support.v7.view.menu.MenuBuilder
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Button

import android.widget.EditText

import com.blozi.bindtags.R
import com.blozi.bindtags.application.Global
//import com.blozi.blindtags.service.local.FileDbService;
import com.blozi.bindtags.util.BLOZIPreferenceManager
import com.blozi.bindtags.util.ParameterChecker

class ConfigureUrlActivity : CommonActivity() {
    private var pressBack: Boolean = false
    private var serverIPPortEdit:EditText?=null
    private var text:EditText?=null
    private var defaultValue:Button?=null
    private var toolbar: Toolbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_configure_url)
        bloziPreferenceManager = BLOZIPreferenceManager.getInstance(this@ConfigureUrlActivity)
        super.onCreate(savedInstanceState)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        toolbar!!.setNavigationIcon(R.drawable.pullup_orange)
        toolbar!!.setNavigationOnClickListener { v ->
            try {
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(this@ConfigureUrlActivity.getCurrentFocus()!!.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)
            } catch (e: Exception) {
                e.printStackTrace()
            }finally {
                onBackPressed()
            }
        }
        text = findViewById(R.id.web_service_url)
        defaultValue = findViewById(R.id.Default)
        defaultValue!!.setOnClickListener{ text!!.setText("http://47.100.50.216:35005") }
        //查看文件是否有保存服务器地址z
        //        String webServiceUrl = getPropertiesValue(SystemConstants.PROPERTY_NAME_WEBSERVICE_URL);
        var webServiceUrl: String? = bloziPreferenceManager!!.webServiceUrl
        if (!ParameterChecker.checkStringParameter(webServiceUrl)) {
            webServiceUrl = "http://47.100.50.216:35005"
        }
        if (!TextUtils.isEmpty(webServiceUrl)) {
            text!!.setText(webServiceUrl)
        }
        text!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable) {
                bloziPreferenceManager!!.webServiceUrl = s.toString()
            }
        })

        serverIPPortEdit = findViewById(R.id.ServerIPPort)
        val ipPort = bloziPreferenceManager!!.localIPPort
        serverIPPortEdit!!.setText(ipPort)
        serverIPPortEdit!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable) {
                bloziPreferenceManager!!.localIPPort = s.toString()
            }
        })
        toolbar!!.setTitle(R.string.app_name)
        toolbar!!.setTitleTextAppearance(this, R.style.blozi_collapseToolbar_collapsedTitleTextAppearance)
        setEditable(text,false)
        setEditable(serverIPPortEdit)

    }

    /**
     * 创建标题菜单
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.common, menu)
        try {
            val menuBuilder = menu as MenuBuilder
            menuBuilder.setOptionalIconsVisible(true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.allowEdit -> {
                bloziPreferenceManager.editable = !bloziPreferenceManager.editable
                setEditable(text)
                setEditable(serverIPPortEdit)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onResume() {
        super.onResume()
        pressBack = false
        if (Global.currentActivity !== this) Global.currentActivity = this
    }

    override fun onPause() {
        super.onPause()
        if (pressBack) overridePendingTransition(R.anim.in_top, R.anim.out_bottom)
    }
    override fun onBackPressed() {
        pressBack = true
        super.onBackPressed()
    }
}
