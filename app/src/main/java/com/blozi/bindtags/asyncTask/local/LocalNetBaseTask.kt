package com.blozi.bindtags.asyncTask.local

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.blozi.bindtags.R
import com.blozi.bindtags.activities.ShowTipsActivity
import com.blozi.bindtags.activities.LoginActivity
import com.blozi.bindtags.activities.MainActivityLocal
import com.blozi.bindtags.activities.fragment.FragmentFactory
import com.blozi.bindtags.activities.fragment.mainTab.BindingTagAndGood
import com.blozi.bindtags.activities.fragment.mainTab.GoodInfoManage
import com.blozi.bindtags.model.GoodsInfo
import com.blozi.bindtags.util.BLOZIPreferenceManager
import com.blozi.bindtags.util.LoadingDialog
import org.json.JSONObject
import java.util.*

class LocalNetBaseTask : AsyncTask<String, Unit, String> {
    private var context: Context
    private var goodsBarCode: String? = null
    private var tagBarCode: String? = null
    private var netError = "NetError"
    protected var bloziPreferenceManager: BLOZIPreferenceManager? = null

    companion object {

    }

    constructor(context: Context) : super() {
        this.context = context
        netError = context.getString(R.string.NetError)
        bloziPreferenceManager = BLOZIPreferenceManager.getInstance(context)
    }

    override fun doInBackground(vararg params: String): String {
        if (params.size > 1) goodsBarCode = params[1]
        if (params.size > 1 && LocalDeskSocket.getRequestCode(LocalDeskSocket.RequestCode.GetGoodsInfo, params[1]).equals(params[0])) {
            tagBarCode = params[1]
        }
//        for(str in params)Log.i("JSONObject",str)
        var start = Date()
        while (true) {
            try {
                if (LocalDeskSocket.me == null) {
                    LocalDeskSocket.getInstance(bloziPreferenceManager!!.localIPPort!!)
                }
                if (!TextUtils.isEmpty(params[0])) return LocalDeskSocket.me!!.sendMessage(params[0] as String)
            } catch (e: Exception) {
                e.printStackTrace();
                var end = Date()
                LocalDeskSocket.closeAll()
                if (end.time - start.time < 3000) {
                    Thread.sleep(100)
                    continue
                } else return e.localizedMessage ?: netError
            }
        }
    }

    override fun onPostExecute(result: String) {
        try {
            val bundle = Bundle()
            val message = Message()
            var response = TextUtils.split(result, LocalDeskSocket.msgSplit)
            if (!TextUtils.isEmpty(result) && !netError.equals(result) && response.size >= 4) {
                var action = LocalDeskSocket.getRequestCode(response[1].toInt())
                if (LocalDeskSocket.n.equals(response[2])) {
                    Toast.makeText(context, response[3], Toast.LENGTH_SHORT).show()
                }
                when (action) {
                    LocalDeskSocket.RequestCode.Login -> {
                        if (response[3].equals("登录成功") && LocalDeskSocket.y.equals(response[2])) {
                            bloziPreferenceManager!!.pushLocalAccount()
                            if (context is LoginActivity || context is ShowTipsActivity) {
                                val intent = Intent(context, MainActivityLocal::class.java)
                                context.startActivity(intent)
                                (context as Activity).finish()//关闭登录界面
                            }
                            bloziPreferenceManager!!.lastLogin = BLOZIPreferenceManager.LocalSystem
                        } else {
                            bundle.putString("msg", response[3])
                            message.data = bundle
                            message.what = 1//设置线程数
                            var handler: Handler? = null
                            if (context is LoginActivity) {
                                val activity = context as LoginActivity
                                handler = activity.handler
                            }
                            handler!!.sendMessage(message)
                        }
                    }
                    LocalDeskSocket.RequestCode.GetGoodsName -> {
                        if (LocalDeskSocket.y.equals(response[2])) {
                            bundle.putString("goodsName", response[3])
                            bundle.putString("goodsBarcode", goodsBarCode)
                            message.data = bundle
                            message.what = 1//设置线程数
                            if (context is MainActivityLocal) {
                                val activity = context as MainActivityLocal
                                val fragment = activity.getCurrentFragmentObject()
                                if (fragment is BindingTagAndGood) {
                                    val handler = fragment.handler
                                    handler.sendMessage(message)
                                }
                            }
                        }
                    }
                    LocalDeskSocket.RequestCode.UploadTagAndGoodsCode -> {
                        if (LocalDeskSocket.y.equals(response[2])) {
                            val bundle = Bundle()
                            bundle.putString("msg", response[3])
                            bundle.putString("isSuccess", response[2])
                            val message = Message()
                            message.data = bundle
                            message.what = 3//设置线程数
                            if (context is MainActivityLocal) {
                                val activity = context as MainActivityLocal
                                val fragment = activity.getCurrentFragmentObject()
                                if (fragment is BindingTagAndGood) {
                                    val handler = fragment.handler
                                    handler.sendMessage(message)
                                }
                            }
                        }
                    }
                    LocalDeskSocket.RequestCode.GetGoodsInfo -> {
                        var goodInfoManage = FragmentFactory.getGoodInfoManageLocal()
                        if (LocalDeskSocket.y.equals(response[2]) && response.size == 7) {
                            var goodInfo = JSONObject()
                            goodInfo.put("goodsInfoId", goodsBarCode);
                            goodInfo.put("goodsBarcode", goodsBarCode)
                            goodInfo.put("goodsName", response[3])
                            goodInfo.put("goodsPrice", response[4].toDouble())
                            goodInfo.put("promotionPrice", response[5].toDouble())
                            goodInfoManage.setGoodsInfo(goodInfo, true)
                        }
                    }
                    LocalDeskSocket.RequestCode.UpdateGoodsInfo -> {
                        if (LocalDeskSocket.y.equals(response[2]) && response.size == 5) {
                            Toast.makeText(context, response[3], Toast.LENGTH_LONG).show()
                            var goodInfoManage = FragmentFactory.getGoodInfoManageLocal()
                            goodInfoManage.regetGoodsInfo()
                        }
                    }
                }
            } else {
                Toast.makeText(context, result, Toast.LENGTH_LONG).show()
                when (context) {
                    is LoginActivity -> {
                        bundle.putString("msg", result)
                        message.data = bundle
                        message.what = 1//设置线程数
                        var handler: Handler? = null
                        if (context is LoginActivity) {
                            val activity = context as LoginActivity
                            handler = activity.handler
                        }
                        handler!!.sendMessage(message)
                    }
                    is ShowTipsActivity -> {
                        context.startActivity(Intent(context, LoginActivity::class.java))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            LoadingDialog.closeDialog()
        }


    }
}