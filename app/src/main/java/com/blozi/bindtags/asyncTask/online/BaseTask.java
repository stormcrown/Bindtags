package com.blozi.bindtags.asyncTask.online;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.blozi.bindtags.activities.fragment.BaseFragment;
import com.blozi.bindtags.util.BLOZIPreferenceManager;
import com.blozi.bindtags.util.SystemConstants;
import com.blozi.bindtags.util.SystemUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fly_liu on 2017/6/21.
 */
public class BaseTask extends AsyncTask<String, Void, String> {

    protected Activity currentActivity;
    protected BaseFragment currentFragment;
    private String webserviceUrl;
    private HttpClient client;
    private HttpPost post;
    protected BLOZIPreferenceManager bloziPreferenceManager;
    {
        client = new DefaultHttpClient();
        client.getParams().setParameter("Content-Type", "UTF-8");
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, SystemConstants.over_time);//连接时间
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,30*1000);//数据传输时间
    }
    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public String getWebserviceUrl() {
        return webserviceUrl;
    }

    public void setWebserviceUrl(String webserviceUrl) {
        this.webserviceUrl = webserviceUrl;
    }

    public BaseTask(Activity currentActivity, String webserviceUrl){
        super();
        this.currentActivity = currentActivity;

        bloziPreferenceManager= BLOZIPreferenceManager.Companion.getInstance(currentActivity);
        this.webserviceUrl = webserviceUrl;
    }
    public void cancelHttpPost(){
       if(post!=null) post.abort();
    }
    @Override
    protected String doInBackground(String... params) {
        String responseXml = "";//返回的值
        try {
            PackageInfo packageInfo = currentActivity.getPackageManager().getPackageInfo(currentActivity.getPackageName(), 0);
            //获取APP版本versionName
            String versionName = packageInfo.versionName;
            //获取APP版本versionCode
            int versionCode = packageInfo.versionCode;
            String requestXml = params[0];
            post = new HttpPost(this.getWebserviceUrl());
    //        Log.i("请求URL：",this.getWebserviceUrl());
//            Log.i("请求：",requestXml);
            ByteArrayEntity byteArrayEntity = new ByteArrayEntity(requestXml.getBytes("UTF-8"));
            post.setEntity(byteArrayEntity);
            post.setHeader("Accept-Charset", "UTF-8");
            post.setHeader("APP",
                    "{android :"+ SystemUtil.getSystemVersion()
                            +", version:" + versionName +", model:"+
                    SystemUtil.getSystemModel() +", BRAND:"+SystemUtil.getDeviceBrand()
                    +"}" );
//            post.setHeader("User-Agent", "appVersion:"+versionName+"\t"+versionCode);
            //获取response响应

            HttpResponse httpResponse = client.execute(post);

            HttpEntity httpEntity = httpResponse.getEntity();
            byte[] responseContent = EntityUtils.toByteArray(httpEntity);

            responseXml = new String(responseContent,"UTF-8");


        } catch (Exception e) {
//            String msg = e.getLocalizedMessage();
            String msg = e.getMessage();
            if(e instanceof ConnectTimeoutException) msg="连接超时";
            else if(e instanceof ConnectException)msg="连接错误";
            e.printStackTrace();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            responseXml = "<?xml version='1.0' encoding='utf-8' standalone='yes'?>";
            responseXml += "<response>";
            responseXml += "<isSuccess>n</isSuccess>";
            responseXml += "<msg>"+msg+"</msg>";
            responseXml += "<operator></operator>";
            responseXml += "<operatorTime>"+sdf.format(new Date())+"</operatorTime>";
            responseXml += "<parameterMap></parameterMap>";
            responseXml += "</response>";
        }
//        Log.i("响应：",responseXml);
        return responseXml;
    }
}
