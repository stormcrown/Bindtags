package com.blozi.bindtags.reciver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.text.TextUtils
import android.util.Log
import com.blozi.bindtags.activities.GoodsInfoActivity

import com.blozi.bindtags.activities.MainActivity
import com.blozi.bindtags.activities.MainActivityLocal
import com.blozi.bindtags.activities.ScanBarcodeOfflineActivity
import com.blozi.bindtags.application.Global
import com.blozi.bindtags.util.SystemConstants

/**
 * Created by 骆长涛 on 2018/3/22.
 */

class BLOZIScanReciver : BroadcastReceiver {
//    private var activity: Activity? = null

    constructor() {
//        if (Global.currentActivity != null) activity = Global.currentActivity
    }

//    constructor(activity: Activity) : super() {
//        this.activity = activity
//    }

    override fun peekService(myContext: Context, service: Intent): IBinder {
        return super.peekService(myContext, service)
    }

    override fun onReceive(context: Context, intent: Intent) {
        //        String action = intent.getAction();
        val data = intent.getStringExtra("data")
        val apls_KT40Q = intent.getStringExtra("se4500")
        val apls_Android = intent.getStringExtra("value")
        val DataTerminal = intent.getStringExtra("scannerdata")
        Log.i("SCAN:", data + "\t" + apls_KT40Q + "\t" + apls_Android + "\t" + DataTerminal + "\t" + context.javaClass.name)
        //        Set<String> keys = intent.getExtras().keySet();
        //        Iterator<String> iterator= keys.iterator();
        //        while (iterator.hasNext()){
        //            String key = iterator.next();
        //            Log.i("广播接收",key+" = "+intent.getExtras().getString(key));
        //        }

        if (!TextUtils.isEmpty(data) || !TextUtils.isEmpty(apls_KT40Q) || !TextUtils.isEmpty(apls_Android) || !TextUtils.isEmpty(DataTerminal)) {
            var msg = ""
            if (!TextUtils.isEmpty(data))
                msg = data
            else if (!TextUtils.isEmpty(apls_KT40Q))
                msg = apls_KT40Q
            else if (!TextUtils.isEmpty(apls_Android))
                msg = apls_Android
            else if (!TextUtils.isEmpty(DataTerminal)) msg = DataTerminal
            Log.i("广播接收", msg)
            val bundle = Bundle()
            bundle.putString(SystemConstants.scanMsg, msg)
            val message = Message()
            message.data = bundle
            message.what = SystemConstants.scanSuccess
            var handler: Handler? = null
//            if (activity == null && Global.currentActivity != null) activity = Global.currentActivity
            ;
            if (Global.currentActivity != null && Global.currentActivity is MainActivity) {
                val activity0 = Global.currentActivity as MainActivity?
                handler = activity0!!.handler
            }else if (Global.currentActivity != null && Global.currentActivity is MainActivityLocal) {
                val activity0 = Global.currentActivity as MainActivityLocal?
                handler = activity0!!.handler
            }
            else if (Global.currentActivity != null && Global.currentActivity is GoodsInfoActivity) {
                val activity0 = Global.currentActivity as GoodsInfoActivity?
                handler = activity0!!.handler
            }else if (Global.currentActivity != null && Global.currentActivity is ScanBarcodeOfflineActivity) {
                val activity0 = Global.currentActivity as ScanBarcodeOfflineActivity?
                handler = activity0!!.handler
            }

            if (handler != null) handler.sendMessage(message)

        }
    }
}
