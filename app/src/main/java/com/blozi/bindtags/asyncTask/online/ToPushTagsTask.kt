package com.blozi.bindtags.asyncTask.online

import android.app.Activity
import android.os.Bundle
import android.os.Message
import android.util.Log
import com.blozi.bindtags.activities.RackDetailActivity
import com.blozi.bindtags.application.Global
import com.blozi.bindtags.util.LoadingDialog
import com.blozi.bindtags.util.StringFilter
import com.blozi.bindtags.util.XmlUtil
import org.dom4j.Document
import org.dom4j.DocumentHelper
import org.json.JSONObject
import java.util.HashMap

class ToPushTagsTask:BaseTask {
    constructor(currentActivity: Activity?, webserviceUrl: String?) : super(currentActivity, webserviceUrl)


    override fun doInBackground(vararg params: String): String {
        val loginId = params[0]
        val password = params[1]
        val tagIds = params[2]
        val storeInfoId = params[3]
        val map = HashMap<Any, Any>()
        map["action"] = "ToPushTagsByAPP"
        map["loginId"] = loginId
        map["loginPassword"] = StringFilter.getMD5(password)
        map["storeInfoId"] = storeInfoId
        map["tagIds"] = tagIds

        val d = XmlUtil.mapToXml(map, "request");
        Log.i("上传价签",d.asXML());
        return super.doInBackground(d.asXML())
    }

    override fun onPostExecute(result: String) {
        try {
            Log.i("重推价签",result)
            val doc = DocumentHelper.parseText(result)
            val root = doc.rootElement
            var isSuccess :String?=null
//            var msg :String?=null
            val map = XmlUtil.xmlToMap(doc.asXML())




        } catch (e: Exception) {
            e.printStackTrace()
        }finally {
            LoadingDialog.closeDialog()
        }

    }

}