package com.blozi.bindtags.asyncTask.online;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.blozi.bindtags.R;
import com.blozi.bindtags.activities.ShowTipsActivity;
import com.blozi.bindtags.activities.LoginActivity;
import com.blozi.bindtags.activities.MainActivity;
import com.blozi.bindtags.application.Global;
//import com.blozi.blindtags.service.local.FileDbService;
import com.blozi.bindtags.util.BLOZIPreferenceManager;
import com.blozi.bindtags.util.LoadingDialog;
import com.blozi.bindtags.util.StringFilter;
import com.blozi.bindtags.util.XmlUtil;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fly_liu on 2017/6/21.
 */

public class LoginTask extends BaseTask {

//    private FileDbService fileDbService;
    private String theUserPasswordWithOutMD5;
    @Override
    public Activity getCurrentActivity() {
        return currentActivity;
    }
    @Override
    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }
    public LoginTask(Activity currentActivity, String webserviceUrl) {
        super(currentActivity, webserviceUrl);
    }

    /**
     * request的入参格式如下：
     * 	<?xml version="1.0" encoding="UTF-8" standalone="yes" ?>
     *  <xmp>
     *	 <request>
     *		<action>loginOnApp</action>
     *	 	<loginId>fly</loginId>
     *	 	<loginPassword>123456</loginPassword>
     *	 </request>
     *	 </xmp>
     */
    @Override
    protected String doInBackground(String... params){
        String loginId = params[0];
        String password = params[1];
        if(!TextUtils.isEmpty(loginId) && !TextUtils.isEmpty(password) ){
            bloziPreferenceManager.setLoginid(loginId);
            bloziPreferenceManager.setPassword(password);
        }else {
            return "";
        }
        this.theUserPasswordWithOutMD5 = password;
        Map map=new HashMap();

        map.put("loginId",loginId);
        map.put("loginPassword",StringFilter.getMD5(password)  );
//        map.put("goodsBarcode","");
//        map.put("goodsName","");
//        map.put("firstRow",0);

        Document document = XmlUtil.mapToXml(map,"request");

        return super.doInBackground(document.asXML());
    }
    @Override
    protected void onPostExecute(String result) {
        try{
            Log.i("LoginTask",result);
            String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><response><parameterMap name=\"parameterMap\" type=\"JSONObject\"><codeId>7002</codeId><storeId>297e442d6081645a016081e0f5dd0001</storeId><storeName>测试</storeName><userId>2c93c74061d131710161e03dc2a906ee</userId><loginId>blozi_lzt</loginId><loginPassword>e35cf7b66449df565f93c607d5a81d09</loginPassword><userName>骆长涛</userName></parameterMap><isSuccess name=\"isSuccess\">y</isSuccess><operatorTime name=\"operatorTime\">2018-08-01 11:02:33</operatorTime><msg name=\"msg\">登陆成功！</msg><operator name=\"operator\">blozi_lzt</operator><childrenStore name=\"childrenStore\" type=\"JSONArray\"><JSONObject><storeId>2c93c7406492c3de0164eef17a1b000a</storeId><storeName>董晶</storeName></JSONObject><JSONObject><storeId>2c93c74061e043390161e0563eab0003</storeId><storeName>gdb测试门店</storeName></JSONObject><JSONObject><storeId>2c93c7406421fdac0164223474380001</storeId><storeName>测试接口是否调通</storeName></JSONObject><JSONObject><storeId>2c93c74063b00c890163fcef16320012</storeId><storeName>最新二代基站测试</storeName></JSONObject><JSONObject><storeId>2c93c74063b00c890163b8c5d1fd0005</storeId><storeName>二代基站价签测试门店</storeName></JSONObject><JSONObject><storeId>2c93c740635ca21301638fee4c830017</storeId><storeName>测试111</storeName></JSONObject><JSONObject><storeId>2c93c740635ca21301638feaba150016</storeId><storeName>测试添加门店</storeName></JSONObject><JSONObject><storeId>2c93c740635ca21301638c94459b0015</storeId><storeName>st002</storeName></JSONObject><JSONObject><storeId>2c93c740635ca21301638c56f2ff0014</storeId><storeName>Stetst001</storeName></JSONObject><JSONObject><storeId>2c93c740635ca21301638c54d7d00013</storeId><storeName>124612312</storeName></JSONObject><JSONObject><storeId>2c93c740635ca21301638c523b4b0012</storeId><storeName>StoreTest005</storeName></JSONObject><JSONObject><storeId>2c93c740635ca21301638c52095e0011</storeId><storeName>StoreTest004</storeName></JSONObject><JSONObject><storeId>2c93c740635ca21301638c4b30be0010</storeId><storeName>StoreTest003</storeName></JSONObject><JSONObject><storeId>2c93c740635ca21301638c48f43c000f</storeId><storeName>StoreTest001</storeName></JSONObject><JSONObject><storeId>2c93c740632a34ba016339d847770004</storeId><storeName>测试门店问题</storeName></JSONObject><JSONObject><storeId>2c93c74062ad35790162b22f9e3c0001</storeId><storeName>鹏鹏Test</storeName></JSONObject><JSONObject><storeId>2c93c74062d7cc8e0162dd01c5060002</storeId><storeName>测试接口门店041902</storeName></JSONObject><JSONObject><storeId>2c93c74062d7cc8e0162dd019a5c0001</storeId><storeName>测试接口门店041901</storeName></JSONObject><JSONObject><storeId>2c93c7406209e0d90162272202520004</storeId><storeName>刘飞测试门店</storeName></JSONObject><JSONObject><storeId>2c93c7406209e0d901621f8e3ce70003</storeId><storeName>食得鲜真实数据门店</storeName></JSONObject><JSONObject><storeId>2c93c7406209e0d9016217e82b760001</storeId><storeName>12321</storeName></JSONObject><JSONObject><storeId>2c93c74060e9bf3b0160e9c1890c0002</storeId><storeName>测试创建门店功能</storeName></JSONObject><JSONObject><storeId>2c93c7406163e0950161648ae2810b0e</storeId><storeName>成品模板店</storeName></JSONObject></childrenStore></response>";

            Document doc= DocumentHelper.parseText(str);
            Element root = doc.getRootElement();

            String isSuccess = root.elementText("isSuccess");
            String msg = root.elementText("msg");
            String operator = root.elementText("operator");
            String operatorTime = root.elementText("operatorTime");

            Element parameterMap = null;
            String userId = null;//用户主键
            String loginId = null;//登录账号
            String password = null;//登录密码
            String userName = null;//用户名称
            String codeId =null;

            if("y".equals(isSuccess)){
//                Log.i("活动名",currentActivity.getClass().getName());
//                BLOZIPreferenceManager bloziPreferenceManager =BLOZIPreferenceManager.getInstance(currentActivity);
                //返回"y"说明登录成功
                parameterMap = root.element("parameterMap");
                userId = parameterMap.elementText("userId");
                loginId = parameterMap.elementText("loginId");
                password = parameterMap.elementText("loginPassword");
                userName = parameterMap.elementText("userName");
                codeId = parameterMap.elementText("codeId");
            //    Log.i("密码",password);
                if(codeId!=null) bloziPreferenceManager.setCodeId(codeId);
                if(userName!=null)bloziPreferenceManager.setUseName(userName);
//                if(password!=null)bloziPreferenceManager.setPassword(password);
                if(loginId!=null)bloziPreferenceManager.setLoginid(loginId);
                if(userId!=null)bloziPreferenceManager.setUserid(userId);
                bloziPreferenceManager.pushOnlineAccount();
                Element storeId = parameterMap.element(Global.STOREID);
                if(storeId!=null){
                    Global.mainStore.put(Global.STOREID,storeId.getText());
                    Global.curryentStore.put(Global.STOREID,storeId.getText());
                }
                Element storeName = parameterMap.element(Global.STORE_NAME);
                if(storeName!=null){
                    Global.mainStore.put(Global.STORE_NAME,storeName.getText());
                    Global.curryentStore.put(Global.STORE_NAME,storeName.getText());
                }
                Element childrenStore =root.element("childrenStore");
                Global.childrenStores.clear();
                if(childrenStore!=null  ){
                    Attribute type = childrenStore.attribute("type");
                    List<Element> childrens= childrenStore.elements();
                    if("JSONArray".equals(type.getValue())){
//                        JSONArray jsonArray = new JSONArray();
                        for(Element element:childrens){
                            List<Element> arrts = element.elements();
                            Map map = new HashMap();
                            for(Element arrt:arrts){
                                map.put(arrt.getName(),arrt.getText());
                            }
                            Global.childrenStores.add(map);
                        }
                    }
                }

                Global.currentStoreIndex=Global.childrenStores.size();
//                Log.i("登陆成功",Global.childrenStores.size()+"\t");
                bloziPreferenceManager.setUseName(userName);
                bloziPreferenceManager.setLastLogin(BLOZIPreferenceManager.OnlineSystem);
                /*
				 * 登录成功，进入查看列表页面
				 */
                Intent intent = new Intent(this.getCurrentActivity(), MainActivity.class);

                this.getCurrentActivity().startActivity(intent);

                this.getCurrentActivity().finish();//关闭登录界面
            } else {
//                SharedPreferences sharedPreferences = this.currentActivity.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
//                sharedPreferences.cancelEdit().clear().commit();
                Bundle bundle = new Bundle();
                bundle.putString("msg", msg);
                Message message = new Message();
                message.setData(bundle);
                message.what = 1;//设置线程数
                Handler handler=null;
                if(currentActivity instanceof LoginActivity){
                    LoginActivity activity =  (LoginActivity) currentActivity;
                    handler = activity.getHandler();
                }else if(currentActivity instanceof ShowTipsActivity){
                    ShowTipsActivity activity0 = (ShowTipsActivity) currentActivity;
                    handler = activity0.getHandler();
                }
                handler.sendMessage(message);
            }

        } catch (Exception e){
            e.printStackTrace();
            String msg = this.currentActivity.getString(R.string.failure_login);
            Bundle bundle = new Bundle();
            bundle.putString("msg", msg);
            Message message = new Message();
            message.setData(bundle);
            message.what = 1;//设置线程数
            Handler handler=null;
//            LoginActivity activity;
            if(currentActivity instanceof LoginActivity){
                LoginActivity activity =  (LoginActivity) currentActivity;
                handler = activity.getHandler();
            }else if(currentActivity instanceof ShowTipsActivity){
                ShowTipsActivity activity0 = (ShowTipsActivity) currentActivity;
                handler = activity0.getHandler();
            }
            handler.sendMessage(message);
            e.printStackTrace();
        }finally {
            LoadingDialog.INSTANCE.closeDialog();
        }
    }
}
