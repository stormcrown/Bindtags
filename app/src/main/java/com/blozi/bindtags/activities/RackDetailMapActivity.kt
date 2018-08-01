package com.blozi.bindtags.activities

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.support.v7.widget.Toolbar
import android.util.Log
import com.blozi.bindtags.R
import com.blozi.bindtags.application.GlideApp
import com.blozi.bindtags.application.Global
import com.blozi.bindtags.asyncTask.online.GetRackDetailMapData
import com.blozi.bindtags.model.RackInfo
import com.blozi.bindtags.util.BLOZIPreferenceManager
import com.blozi.bindtags.util.SystemConstants
import com.blozi.bindtags.util.SystemMathod
import com.bumptech.glide.Glide

class RackDetailMapActivity : BaseActivity {
    companion object {
        val handleGetRackMapData = 1122
    }
    constructor() : super()

    private var pressBack = false
    private var rackImage: ImageView? = null
    private var toolbar: Toolbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.rack_detail_map_activity)
        initId()
        super.onCreate(savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        if (pressBack) overridePendingTransition(R.anim.in_left, R.anim.out_right)
    }

    override fun onBackPressed() {
        pressBack = true
        super.onBackPressed()
    }

    private fun initId() {
        toolbar = findViewById(R.id.toolbar)
        toolbar!!.setNavigationOnClickListener { v ->
            onBackPressed()
        }
        rackImage = findViewById(R.id.rack_image)
        GlideApp.with(this).load(R.drawable.good_image).into(rackImage!!);
        val rack = intent.getParcelableExtra<RackInfo>(SystemConstants.RACK_TRANI)
        Log.i("Rack_TRANI", rack.toString())
        val bloziPreferenceManager = BLOZIPreferenceManager.getInstance(this)
        val getRackDetailMapData = GetRackDetailMapData(this, bloziPreferenceManager!!.compliteURL)
        getRackDetailMapData.execute(Global.curryentStore[Global.STOREID], rack.rackInfoId)


    }
    val handler : Handler= RackDetailMapHandler()
    internal inner class RackDetailMapHandler : Handler() {
        override fun handleMessage(msg: Message) {
            val bundle = msg.data
            if (msg.what == 1) {
            }
        }
    }

}