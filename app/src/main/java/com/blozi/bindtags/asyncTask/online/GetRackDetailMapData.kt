package com.blozi.bindtags.asyncTask.online

import android.app.Activity
import android.os.Bundle
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.widget.TextView

import com.blozi.bindtags.R
import com.blozi.bindtags.activities.RackDetailActivity
import com.blozi.bindtags.activities.RackDetailMapActivity
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

class GetRackDetailMapData(currentActivity: Activity, webserviceUrl: String) : BaseTask(currentActivity, webserviceUrl) {

    override fun getCurrentActivity(): Activity {
        return currentActivity
    }

    override fun setCurrentActivity(currentActivity: Activity) {
        this.currentActivity = currentActivity
    }


    override fun doInBackground(vararg params: String): String {
        val map = HashMap<Any, Any?>()
        map["action"] = "showRackDetail"
        map["loginId"] = bloziPreferenceManager.loginid
        map["loginPassword"] = StringFilter.getMD5(bloziPreferenceManager.password)
        if(params.size==2){
            map["storeInfoId"] = params[0]
            map["rackInfoId"] = params[1]
        }
        return super.doInBackground(XmlUtil.mapToXml(map, "request").asXML())
    }

    override fun onPostExecute(result: String) {
        try {
            Log.i("棚格图数据",result)
            val doc = DocumentHelper.parseText(result)
            val root = doc.rootElement
            val map = XmlUtil.xmlToMap(doc.asXML())
            val flag = map["flag"]
            if(flag is JSONObject){
                Log.i("棚格图数据",flag.toString())
                val bundle = Bundle()
                val message =Message()
                message.what = RackDetailMapActivity.handleGetRackMapData
                message.data = bundle
                if(Global.currentActivity is RackDetailMapActivity){
                    (Global.currentActivity as RackDetailMapActivity ).handler.handleMessage(message)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }finally {
            LoadingDialog.closeDialog()
        }

    }
}
