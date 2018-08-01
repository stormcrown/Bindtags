package com.blozi.bindtags.activities

import android.os.Bundle
import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import com.blozi.bindtags.R
import com.blozi.bindtags.asyncTask.online.LoginTask
import com.blozi.bindtags.util.BLOZIPreferenceManager

import kotlinx.android.synthetic.main.activity_index.*

class Index : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try{
            var blozi= BLOZIPreferenceManager.getInstance(this)
            if ( blozi != null &&  !TextUtils.isEmpty(blozi.loginid) && !TextUtils.isEmpty(blozi.password) && blozi.lastLogin == BLOZIPreferenceManager.OnlineSystem) {//判断是否登录
                startActivity(Intent(this, MainActivity::class.java))
                val loginService = LoginTask(this, blozi.compliteURL)
                loginService.execute(blozi.loginid, blozi.password)
            }else{
                startActivity(Intent(this, ShowTipsActivity::class.java))
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        setContentView(R.layout.activity_index)
    }

}
