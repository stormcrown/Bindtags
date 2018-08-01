package com.blozi.bindtags.asyncTask.online;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.blozi.bindtags.R;
import com.blozi.bindtags.activities.MainActivity;
//import com.blozi.blindtags.activities.ShowBindGoodsActivity;
import com.blozi.bindtags.activities.fragment.FragmentFactory;
import com.blozi.bindtags.activities.fragment.mainTab.BindingTagAndGood;
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

public class BlindTagGoodsTask extends BaseTask {

    private String theUserPasswordWithOutMD5;
    TextView msgTextView=null;
    public BlindTagGoodsTask(Activity currentActivity, String webserviceUrl) {
        super(currentActivity, webserviceUrl);
    }

    /**
     * request的入参格式如下：
     * 	<?xml version="1.0" encoding="UTF-8" standalone="yes" ?>
     *  <xmp>
     *	 <request>
     *		<action>blindTagGoods</action>
     *	 	<loginId>fly</loginId>
     *	 	<loginPassword>123456</loginPassword>
     *	    <goodsInfoId>12345152</goodsInfoId>
     *	    <tagInfoId>2141251</tagInfoId>
     *	 </request>
     *	 </xmp>
     */
    @Override
    protected String doInBackground(String... params){
        String loginId = params[0];
        String password = params[1];
        String goodsInfoId = params[2];
        String tagInfoId = params[3];
        String tagCode = params[4];
        this.theUserPasswordWithOutMD5 = password;
        String storInfoId =params[5];

        Map map = new HashMap();
        map.put("action","blindTagGoods");
        map.put("loginId",loginId);
        map.put("loginPassword",StringFilter.getMD5(password));
        map.put("goodsInfoId",goodsInfoId);
        map.put("tagInfoId",tagInfoId);
        map.put("tagCode",tagCode);
        map.put("storeInfoId",storInfoId);
//        Log.i("绑定请求" , XmlUtil.mapToXml(map,"request").asXML());
        return super.doInBackground(XmlUtil.mapToXml(map,"request").asXML());

//        StringBuilder requestXml =new StringBuilder( "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>");
//        requestXml.append("<request><action>blindTagGoods</action><loginId>")
//                .append(loginId)
//                .append("</loginId><loginPassword>")
//                .append( StringFilter.getMD5(password))
//                .append("</loginPassword><goodsInfoId>")
//                .append(goodsInfoId)
//                .append("</goodsInfoId><tagInfoId>")
//                .append(tagInfoId)
//                .append("</tagInfoId><tagCode>").append(tagCode).append("</tagCode>");
//        if(params!=null && params.length>5&&params[5]!=null && !"".equals(params[5]))requestXml.append("<storeInfoId>").append(params[5]).append("</storeInfoId>");
//        requestXml.append("</request> ");
//        String responseXml = super.doInBackground(requestXml.toString());//返回的值
//        return responseXml;

    }

    /**
     * 向服务器端发起异步请求并拿到响应参数后
     * 程序自动调用本方法，解析响应参数
     * <?xml version='1.0' encoding='utf-8' standalone='yes'?>
     * 	<response>
     * 		<isSuccess>y</isSuccess>
     * 		<msg>成功</msg>
     * 		<operator>fly</operator>
     * 		<operatorTime>2017-06-08</operatorTime>
     * 		<parameterMap>
     * 			<goodsName>123456</goodsName>
     * 		    <goodsBarcode>admin</goodsBarcode>
     * 		    <tagBarcode>123456</tagBarcode>
     * 		    <tagInfoId>123</tagInfoId>
     * 		    <goodsInfoId>1234</goodsInfoId>
     * 		</parameterMap>
     * 	</response>
     *
     * @param result
     */
    @Override
    protected void onPostExecute(String result) {
        Log.i("BlindTagGoodsTask",result);
        try{
//            if(currentActivity!=null && currentActivity instanceof ShowBindGoodsActivity ){
//                msgTextView  = (TextView)this.getCurrentActivity().findViewById(R.id.TEXT_VIEW_MSG);
//            }else
//                if(currentActivity !=null && currentActivity instanceof MainActivity) {
//                MainActivity mainActivity = (MainActivity) currentActivity;
//                BindingTagAndGood bindingTagAndGood =(BindingTagAndGood)mainActivity.getCurrentFragmentObject();
                    BindingTagAndGood bindingTagAndGood = FragmentFactory.getBindingTagAndGood();
                msgTextView = (TextView)bindingTagAndGood.getMyActivity().findViewById(R.id.binding_fragment_msg);
//            }
            Document doc= DocumentHelper.parseText(result);
            Element root = doc.getRootElement();

            String isSuccess = root.elementText("isSuccess");
            String msg = root.elementText("msg");
            String operator = root.elementText("operator");
            String operatorTime = root.elementText("operatorTime");

            Element parameterMap = null;
            String goodsName = null;//商品名称
            String goodsBarcode = null;//商品编号
            String tagBarcode = null;//价签唯一标识符
            String tagInfoId = null;//价签Id
            String goodsInfoId = null;//商品Id

            Bundle bundle = new Bundle();
            bundle.putString("msg",msg);
            bundle.putString("isSuccess",isSuccess);

            Message message = new Message();
            message.setData(bundle);
            message.what = 3;//设置线程数
//            if(currentActivity!=null && currentActivity instanceof  ShowBindGoodsActivity ){
//                ShowBindGoodsActivity activity = (ShowBindGoodsActivity) currentActivity;
//                Handler handler = activity.getHandler();
//                handler.sendMessage(message);
//            }else
//                if(currentActivity !=null && currentActivity instanceof MainActivity){
//                MainActivity mainActivity = (MainActivity) currentActivity;
//                BindingTagAndGood bindingTagAndGood =(BindingTagAndGood) mainActivity.getCurrentFragmentObject();
                Handler handler = bindingTagAndGood.getHandler();
                handler.sendMessage(message);
//            }

        } catch (Exception e){
            if(msgTextView!=null)msgTextView.setText(e.getLocalizedMessage());
            e.printStackTrace();
        }finally {
            if(currentActivity!=null && currentActivity instanceof MainActivity){
                MainActivity mainActivity = (MainActivity) currentActivity;
                mainActivity.closeLoad();
            }
        }
    }
}
