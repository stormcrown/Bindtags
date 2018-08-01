package com.blozi.bindtags.asyncTask.online;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.blozi.bindtags.R;
import com.blozi.bindtags.activities.MainActivity;
import com.blozi.bindtags.util.BLOZIPreferenceManager;
import com.blozi.bindtags.util.SystemConstants;
import com.blozi.bindtags.util.SystemMathod;
import com.blozi.bindtags.util.XmlUtil;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.blozi.bindtags.util.SystemConstants.CheckUpdate_APK_REQUESTCODE;
import static com.blozi.bindtags.util.SystemConstants.WRITE_EXTERNAL_STORAGE_RequestCode;

public class GetGlobleMessageTask extends BaseTask {
    private boolean tip;
//    public GetGlobleMessageTask(Activity currentActivity, String webserviceUrl) {
//        super(currentActivity, webserviceUrl);
//        bloziPreferenceManager = BLOZIPreferenceManager.Companion.getInstance(currentActivity);
//    }
    public GetGlobleMessageTask(Activity currentActivity, String webserviceUrl ,boolean tip ) {
        super(currentActivity, webserviceUrl);
        this.tip = tip;
        bloziPreferenceManager = BLOZIPreferenceManager.Companion.getInstance(currentActivity);
    }
    @Override
    protected String doInBackground(String... params){
        Map map=new HashMap();
        map.put("action","GetAndroidAPPMessage");
        Document document = XmlUtil.mapToXml(map,"request");
        return super.doInBackground(document.asXML());
    }
    @Override
    protected void onPostExecute(String result) {
        try{
//            Log.i("GetGlobleMessageTask",result);
            String re="<?xml version=\"1.0\" encoding=\"UTF-8\"?><response><appMessage name=\"appMessage\" type=\"JSONObject\"><VersionCode>1758</VersionCode><VersionName>2.2.6</VersionName><APKFileName>BindTags-2.2.6.1649-release.apk</APKFileName><UpdateTime>2018-07-25 10:00:00</UpdateTime><UpdateContent>1, Fixed more than 6 of Android's system couldn't download and install APP.</UpdateContent><UpdateContentCNS>1，修复了安卓6.0以上系统不能下载安装APP的问题。</UpdateContentCNS><UpdateContentCNT>1，修複了安卓6.0以上系統不能下載安裝APP的問題。</UpdateContentCNT></appMessage><isSuccess name=\"isSuccess\">y</isSuccess><operatorTime name=\"operatorTime\">2018-08-01 10:34:31</operatorTime><msg name=\"msg\">成功</msg></response>";

            Document doc= DocumentHelper.parseText(result);
            Map reponse = XmlUtil.xmlToMap(doc.asXML());
//            Log.i("成",doc.asXML());
//            Log.i("成",reponse.toString());
            String msg =  (String) reponse.get("msg");
            String isSuccess = (String)reponse.get("isSuccess");
            if(! SystemConstants.IS_EFFECT_YES .equals(isSuccess) ){
               result = re;
            }
            doc = DocumentHelper.parseText(result);
            reponse = XmlUtil.xmlToMap(doc.asXML());

            JSONObject appMessage = (JSONObject) reponse.get("appMessage");
            Iterator<String> set = appMessage.keys();
            while (set.hasNext()){
                String key = set.next();
                String value = appMessage.getString(key);
                bloziPreferenceManager.setCommonValue(key,value);
            }
            if(currentActivity != null && currentActivity instanceof MainActivity  && tip ){
//                    currentFragment =  ((MainActivity)currentActivity).getCurrentFragmentObject();
                if (ContextCompat.checkSelfPermission(currentActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(currentActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CheckUpdate_APK_REQUESTCODE);
                } else {
                    SystemMathod.checkedToUpdate(tip, bloziPreferenceManager, currentActivity);
                }

            }

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(currentActivity,currentActivity.getResources().getString(R.string.error)+"\n"+e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
