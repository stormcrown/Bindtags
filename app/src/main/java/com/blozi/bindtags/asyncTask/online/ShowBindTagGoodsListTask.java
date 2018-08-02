package com.blozi.bindtags.asyncTask.online;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.widget.Toast;

//import com.blozi.blindtags.activities.ShowBindGoodsListActivity;
import com.blozi.bindtags.util.StringFilter;
import com.blozi.bindtags.util.XmlUtil;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fly_liu on 2017/6/27.
 */

public class ShowBindTagGoodsListTask extends BaseTask {

    private String theUserPasswordWithOutMD5;
    @Override
    public Activity getCurrentActivity() {
        return currentActivity;
    }

    @Override
    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public ShowBindTagGoodsListTask(Activity currentActivity, String webserviceUrl) {
        super(currentActivity, webserviceUrl);
    }

    /**
     * request的入参格式如下：
     * 	<?xml version="1.0" encoding="UTF-8" standalone="yes" ?>
     *  <xmp>
     *	 <request>
     *		<action>ShowBindTagGoodsList</action>
     *	 	<loginId>fly</loginId>
     *	 	<goodsBarcode>123456</goodsBarcode>
     *	    <goodsName>123456</goodsName>
     *	    <isBind>y</isBind>
     *	    <firstRow>123456</firstRow>
     *	 </request>
     *	 </xmp>
     */
    @Override
    protected String doInBackground(String... params){
        String loginId = params[0];
        String password = params[1];
        String goodsBarcode = params[2];
        String goodsName = params[3];
        String isBind = params[4];
        String firstRow = params[5];
        this.theUserPasswordWithOutMD5 = password;
        Map map=new HashMap();

        map.put("loginId",loginId);
        map.put("loginPassword", StringFilter.getMD5(password));
        map.put("goodsBarcode",goodsBarcode);
        map.put("goodsName",goodsName);

        map.put("firstRow",firstRow);

        XmlUtil.mapToXml(map,"request");
        return super.doInBackground(  XmlUtil.mapToXml(map,"request").asXML() );

//        String requestXml = "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>";
//        requestXml += 		"<request> ";
//        requestXml += 			"<action>ShowBindTagGoodsList</action>";
//        requestXml += 			"<loginId>"+loginId+"</loginId>";
//        requestXml += 			"<loginPassword>"+ StringFilter.getMD5(password)+"</loginPassword>";
//        requestXml += 			"<goodsBarcode>"+goodsBarcode+"</goodsBarcode>";
//        requestXml += 			"<goodsName>"+goodsName+"</goodsName>";
//        requestXml += 			"<isBind>"+isBind+"</isBind>";
//        requestXml += 			"<firstRow>"+firstRow+"</firstRow>";
//        requestXml += 		"</request> ";
//        String responseXml = super.doInBackground(requestXml);//返回的值
//        return responseXml;

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
     * 			<goodsTagMiddle>123456</goodsTagMiddle>
     * 		    <goodsBarcode>admin</goodsBarcode>
     * 		    <goodsName>123456</goodsName>
     * 			<firstRow>2</firstRow>
     * 		    <totalRows>4</totalRows>
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
            String msg = root.elementText("msg");
            String operator = root.elementText("operator");
            String operatorTime = root.elementText("operatorTime");

            Element parameterMap = null;
            String goodsTagMiddle = null;//将商品列表信息放入
            String goodsBarcode = null;//商品条形码
            String goodsName = null;//商品名称
            String firstRow = null;//页数
            String totalPages = null;//总页数

            if("y".equals(isSuccess)){
                //返回"y"说明登录成功
                parameterMap = root.element("parameterMap");
                goodsTagMiddle = parameterMap.elementText("goodsTagMiddle");
                goodsBarcode = parameterMap.elementText("goodsBarcode");
                goodsName = parameterMap.elementText("goodsName");
                firstRow = parameterMap.elementText("firstRow");
                totalPages = parameterMap.elementText("totalPages");

                Bundle bundle = new Bundle();
                bundle.putString("goodsTagMiddle", goodsTagMiddle);
                bundle.putString("goodsBarcode", goodsBarcode);
                bundle.putString("goodsName", goodsName);
                bundle.putString("firstRow", firstRow);
                bundle.putString("totalPages", totalPages);

                Message message = new Message();
                message.setData(bundle);
                if("0".equals(firstRow)){
                    message.what = 1;//设置线程数
                }
                else{
                    message.what = 2;//设置线程数
                }
//                ShowBindGoodsListActivity activity =  (ShowBindGoodsListActivity) currentActivity;
//                Handler handler = activity.getHandler();
//                handler.sendMessage(message);

            } else {
                Toast.makeText(this.getCurrentActivity(), msg, Toast.LENGTH_LONG).show();
            }

        } catch (Exception e){
            Toast.makeText(this.getCurrentActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


}
