package com.blozi.bindtags.asyncTask.online;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.blozi.bindtags.activities.MainActivity;
import com.blozi.bindtags.activities.fragment.FragmentFactory;
import com.blozi.bindtags.activities.fragment.mainTab.ImportTags;
import com.blozi.bindtags.util.StringFilter;
import com.blozi.bindtags.util.XmlUtil;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fly_liu on 2017/9/5.
 */

public class UpdateTagsTask extends BaseTask {
    private boolean isDelete =false;
    @Override
    public Activity getCurrentActivity() {
        return currentActivity;
    }

    @Override
    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public UpdateTagsTask(Activity currentActivity, String webserviceUrl) {
        super(currentActivity, webserviceUrl);
    }

    public UpdateTagsTask(Activity currentActivity, String webserviceUrl, boolean isDelete) {
        super(currentActivity, webserviceUrl);
        this.isDelete=isDelete;
    }
    /**
     * request的入参格式如下：
     * 	<?xml version="1.0" encoding="UTF-8" standalone="yes" ?>
     *  <xmp>
     *	 <request>
     *		<action>ShowTagInfoTask</action>
     *	 	<loginId>fly</loginId>
     *	 	<loginPassword>123456</loginPassword>
     *	    <tagsId>1234567</tagsId>
     *	 </request>
     *	 </xmp>
     */
    @Override
    protected String doInBackground(String... params){
        String loginId = params[0];
        String password = params[1];
        String tagsId = params[2];
        String  storeInfoId= params[3];
        Map map =new HashMap<>();
        if(isDelete)map.put("action","toBeDeleteTagsOnApp");
        else if(!isDelete) map.put("action","updateTags");
        map.put("loginId",loginId);
        map.put("loginPassword", StringFilter.getMD5(password));
        map.put("storeInfoId", storeInfoId);
        map.put("tagsId", tagsId);
//        Log.i("上传价签",map.toString());
        return super.doInBackground(XmlUtil.mapToXml(map,"request").asXML());
    }

    /**
     * 向服务器端发起异步请求并拿到响应参数后
     * 程序自动调用本方法，解析响应参数
     * <?xml version='1.0' encoding='utf-8' standalone='yes'?>
     * 	<response>
     * 		<isSuccess>y</isSuccess>
     * 		<msg>123</msg>
     * 		<operator>fly</operator>
     * 		<operatorTime>2017-06-08</operatorTime>
     * 	</response>
     *
     * @param result
     */
    @Override
    protected void onPostExecute(String result) {
//        Log.i("上传返回",result);
        try{
            Document doc= DocumentHelper.parseText(result);
            Element root = doc.getRootElement();

            String isSuccess = root.elementText("isSuccess");
            String msg = root.elementText("msg");
//            String operator = root.elementText("operator");
//            String operatorTime = root.elementText("operatorTime");
            String doubleTagMsg = root.elementText("doubleTagMsg");
            String nullTagMsg = root.elementText("nullTagMsg");
            String reUpdateMsg = root.elementText("reUpdateMsg");
            if("y".equals(isSuccess)){
                //返回"y"说明上传成功
                Toast.makeText(this.currentActivity,"成功！"+msg,Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this.currentActivity,msg,Toast.LENGTH_SHORT).show();
            }
            Bundle bundle = new Bundle();
            bundle.putString("isSuccess",isSuccess);
            if(msg!=null)bundle.putString("msg",msg);
            if(doubleTagMsg!=null)bundle.putString("doubleTagMsg",doubleTagMsg);
            if(nullTagMsg!=null)bundle.putString("nullTagMsg",nullTagMsg);
            if(reUpdateMsg!=null)bundle.putString("reUpdateMsg",reUpdateMsg);
            Message message = new Message();
            message.setData(bundle);
            message.what = 2;//设置线程数
//            if(currentActivity!=null && currentActivity instanceof ImportTagsActivity){
//                ImportTagsActivity activity = (ImportTagsActivity) currentActivity;
//                Handler handler = activity.getHandler();
//                handler.sendMessage(message);
//            }else
                if(currentActivity!=null && currentActivity instanceof MainActivity){
                MainActivity mainActivity = (MainActivity) currentActivity;
//                String fragment = mainActivity.getCurrentFragmentName();
                    ImportTags importTags =null;
                if(isDelete){
                    importTags = FragmentFactory.getDeleteTags();
                }else {
                    importTags =FragmentFactory.getImportTags();
                }
                if(importTags!=null){
                    Handler handler = importTags.getHandler();
                    handler.sendMessage(message);
                }
//                if(FragmentFactory.IMPORT_TAGS.equals(fragment) || FragmentFactory.DELETE_TAGS.equals(fragment) ){
//                    ImportTags importTags =(ImportTags) mainActivity.getCurrentFragmentObject();
//
//                }
            }

        } catch (Exception e){
            Toast.makeText(this.currentActivity,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }finally {
            if(currentActivity!=null && currentActivity instanceof MainActivity){
                MainActivity mainActivity = (MainActivity) currentActivity;
                mainActivity.closeLoad();
            }
        }
    }
}
