package com.blozi.bindtags.asyncTask.online;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;

import com.blozi.bindtags.R;
//import com.blozi.blindtags.activities.ShowBindGoodsActivity;
import com.blozi.bindtags.util.StringFilter;
import com.blozi.bindtags.util.XmlUtil;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fly_liu on 2017/6/26.
 */

public class ShowTagInfoTask extends BaseTask {

    private String theUserPasswordWithOutMD5;

    @Override
    public Activity getCurrentActivity() {
        return currentActivity;
    }

    @Override
    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public ShowTagInfoTask(Activity currentActivity, String webserviceUrl) {
        super(currentActivity, webserviceUrl);
    }

    /**
     * request的入参格式如下：
     * 	<?xml version="1.0" encoding="UTF-8" standalone="yes" ?>
     *  <xmp>
     *	 <request>
     *		<action>ShowTagInfoTask</action>
     *	 	<loginId>fly</loginId>
     *	 	<loginPassword>123456</loginPassword>
     *	    <tagsBarcode>1234567</tagsBarcode>
     *	 </request>
     *	 </xmp>
     */
    @Override
    protected String doInBackground(String... params){
        String loginId = params[0];
        String password = params[1];
        String tagsBarcode = params[2];
        String  storeInfoId= params[3];
        this.theUserPasswordWithOutMD5 = password;
        Map map =new HashMap<>();

        map.put("loginId",loginId);
        map.put("loginPassword", StringFilter.getMD5(password));

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
     * 		<parameterMap>
     * 			<tagInfoId>123456</tagInfoId>
     * 		    <physicalIpAddress>121241</physicalIpAddress>
     *\         <goodsInfoId>123456</goodsInfoId>
     * 		    <goodsName>admin</goodsName>
     * 		    <goodsBarcode>123456</goodsBarcode>
     * 		</parameterMap>
     * 	</response>
     *
     * @param result
     */
    @Override
    protected void onPostExecute(String result) {
        try{
            Document doc= DocumentHelper.parseText(result);
            Element root = doc.getRootElement();

            String isSuccess = root.elementText("isSuccess");
            String isBind = root.elementText("isBind");
            String msg = root.elementText("msg");
            String operator = root.elementText("operator");
            String operatorTime = root.elementText("operatorTime");

            Element parameterMap = null;
            String goodsInfoId = null;//商品主键
            String goodsName = null;//商品名称
            String goodsBarcode = null;//商品条形码
            String tagInfoId = null;//价签主键
            String physicalIpAddress = null;//价签物理地址

            TextView msgTextView = (TextView)this.getCurrentActivity().findViewById(R.id.TEXT_VIEW_MSG);

            if("y".equals(isSuccess)){
                //返回"y"说明登录成功
                parameterMap = root.element("parameterMap");
                tagInfoId = parameterMap.elementText("tagInfoId");
                physicalIpAddress = parameterMap.elementText("physicalIpAddress");
                Bundle bundle = new Bundle();
                if ("y".equals(isBind)){
                    goodsInfoId = parameterMap.elementText("goodsInfoId");
                    goodsName = parameterMap.elementText("goodsName");
                    goodsBarcode = parameterMap.elementText("goodsBarcode");
                    bundle.putString("goodsInfoId", goodsInfoId);
                    bundle.putString("goodsName", goodsName);
                    bundle.putString("goodsBarcode", goodsBarcode);
                    msgTextView.setText("该价签已绑定了商品");
                }
                bundle.putString("tagInfoId", tagInfoId);
                bundle.putString("physicalIpAddress", physicalIpAddress);
                bundle.putString("isBind", isBind);
                Message message = new Message();
                message.setData(bundle);
                message.what = 2;//设置线程数
//                ShowBindGoodsActivity activity = (ShowBindGoodsActivity) currentActivity;
//                Handler handler = activity.getHandler();
//                handler.sendMessage(message);
                msgTextView.setText(msg);
            } else {
                msgTextView.setText(msg);
            }

        } catch (Exception e){
            TextView msgTextView = (TextView)this.getCurrentActivity().findViewById(R.id.TEXT_VIEW_MSG);
            msgTextView.setText(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}
