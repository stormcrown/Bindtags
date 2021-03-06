package com.blozi.bindtags.asyncTask.online

import android.app.Activity
import android.os.Bundle
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.widget.TextView

import com.blozi.bindtags.R
import com.blozi.bindtags.activities.RackDetailActivity
import com.blozi.bindtags.application.Global
import com.blozi.bindtags.util.LoadingDialog
import com.blozi.bindtags.util.StringFilter
import com.blozi.bindtags.util.XmlUtil

import org.dom4j.Document
import org.dom4j.DocumentHelper
import org.dom4j.Element
import org.dom4j.Text
import org.json.JSONObject

import java.util.HashMap

//import com.blozi.blindtags.activities.ShowBindGoodsActivity;

/**
 * Created by fly_liu on 2017/6/26.
 */

class SaveRackInfoTask(currentActivity: Activity, webserviceUrl: String) : BaseTask(currentActivity, webserviceUrl) {

    override fun getCurrentActivity(): Activity {
        return currentActivity
    }

    override fun setCurrentActivity(currentActivity: Activity) {
        this.currentActivity = currentActivity
    }


    override fun doInBackground(vararg params: String): String {
        val map = HashMap<Any, Any?>()

        map["loginId"] = bloziPreferenceManager.loginid
        map["loginPassword"] = StringFilter.getMD5(bloziPreferenceManager.password)

//        map["rackInfo"] =
        val rack = JSONObject( params[1])
        val keys = rack.keys()
        while (keys.hasNext()){
            val key =keys.next()
            val value = rack.get(key)
            map.put(key,value.toString()+"")
        }


        return super.doInBackground(XmlUtil.mapToXml(map, "request").asXML())
    }

    override fun onPostExecute(result: String) {
        try {
//            Log.i("货架",result)
            val doc = DocumentHelper.parseText(result)
            val root = doc.rootElement
            var isSuccess :String?=null
            var msg :String?=null
            val map = XmlUtil.xmlToMap(doc.asXML())
            val flag = map["flag"]
            if(flag is JSONObject){
                msg = flag.getString("msg")
                isSuccess= flag.getString("isSuccess")
            }
            val bundle = Bundle()
            val message =Message()
            bundle.putString("isSuccess",isSuccess)
            if(!TextUtils.isEmpty(msg))bundle.putString("msg",msg)
            message.what = RackDetailActivity.handlerEditResult
            message.data = bundle

            if(Global.currentActivity is RackDetailActivity){
                (Global.currentActivity as RackDetailActivity ).handler.handleMessage(message)
            }



        } catch (e: Exception) {
            e.printStackTrace()
        }finally {
            LoadingDialog.closeDialog()
        }

    }
}
