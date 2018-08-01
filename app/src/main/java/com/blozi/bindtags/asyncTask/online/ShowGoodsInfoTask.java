package com.blozi.bindtags.asyncTask.online;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.blozi.bindtags.R;
import com.blozi.bindtags.activities.GoodsInfoActivity;
import com.blozi.bindtags.activities.MainActivity;
//import com.blozi.blindtags.activities.ShowBindGoodsActivity;
import com.blozi.bindtags.activities.fragment.FragmentFactory;
import com.blozi.bindtags.activities.fragment.mainTab.BindingTagAndGood;
import com.blozi.bindtags.activities.fragment.mainTab.GoodInfoManage;
import com.blozi.bindtags.util.LoadingDialog;
import com.blozi.bindtags.util.StringFilter;
import com.blozi.bindtags.util.SystemConstants;
import com.blozi.bindtags.util.XmlUtil;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fly_liu on 2017/6/26.
 */

public class ShowGoodsInfoTask extends BaseTask {

    private String theUserPasswordWithOutMD5;
    private TextView msgTextView = null;
    private String goodsBarcode;

    @Override
    public Activity getCurrentActivity() {
        return currentActivity;
    }

    @Override
    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public ShowGoodsInfoTask(Activity currentActivity, String webserviceUrl) {
        super(currentActivity, webserviceUrl);
    }

    /**
     * request的入参格式如下：
     * <?xml version="1.0" encoding="UTF-8" standalone="yes" ?>
     * <xmp>
     * <request>
     * <action>ShowGoodsInfoOnApp</action>
     * <loginId>fly</loginId>
     * <loginPassword>123456</loginPassword>
     * <goodsBarcode>1234567</goodsBarcode>
     * </request>
     * </xmp>
     */
    @Override
    protected String doInBackground(String... params) {
        String loginId = params[0];
        String password = params[1];
        goodsBarcode = params[2];
        this.theUserPasswordWithOutMD5 = password;
        String storeInfoId = params[3];
        String ifDetail = "";
        if (params.length > 4) ifDetail = params[4];
        Map map = new HashMap<>();
        map.put("action", "ShowGoodsInfoOnApp");
        map.put("loginId", loginId);
        map.put("loginPassword", StringFilter.getMD5(password));
        map.put("storeInfoId", storeInfoId);
        map.put("goodsBarcode", goodsBarcode);
        map.put("ifDetail", ifDetail);
        Document document = XmlUtil.mapToXml(map, "request");
        Log.i("测试", document.asXML());
        return super.doInBackground(document.asXML());
    }

    /**
     * 向服务器端发起异步请求并拿到响应参数后
     * 程序自动调用本方法，解析响应参数
     * <?xml version='1.0' encoding='utf-8' standalone='yes'?>
     * <response>
     * <isSuccess>y</isSuccess>
     * <msg>123</msg>
     * <isBind>y</isBind>
     * <operator>fly</operator>
     * <operatorTime>2017-06-08/operatorTime>
     * <parameterMap>
     * <goodsInfoId>123456</goodsInfoId>
     * <goodsName>admin</goodsName>
     * <goodsBarcode>123456</goodsBarcode>
     * <tagBarcode>123</tagBarcode>
     * <tagInfoId>123</tagInfoId>
     * </parameterMap>
     * </response>
     *
     * @param result
     */
    @Override
    protected void onPostExecute(String result) {
        try {
                      Log.i("测试",result);
            Document doc = DocumentHelper.parseText(result);
            Map map = XmlUtil.xmlToMap(result);
//            Set<String> keys = map.keySet();
//            Iterator<String> iterator=keys.iterator();
//            while (iterator.hasNext()){
//                String key = iterator.next();
//                Log.i("年轻",key+" = " +map.get(key)+"\t"+ map.get(key).getClass().getName());
//            }


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
            String tagBarcode = null;//价签标识符
            String tagInfoId = null;//价签主键
            boolean editable =false;
//            if (currentActivity != null && currentActivity instanceof ShowBindGoodsActivity) {
//                msgTextView =  this.getCurrentActivity().findViewById(R.id.TEXT_VIEW_MSG);
//            } else
                if (currentActivity != null && currentActivity instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) currentActivity;
                String fragment = mainActivity.getCurrentFragmentName();
                if (FragmentFactory.BindingTagAndGood.equals(fragment)) {
                    BindingTagAndGood bindingTagAndGood = (BindingTagAndGood) mainActivity.getCurrentFragmentObject();
                    msgTextView =  bindingTagAndGood.getMyActivity().findViewById(R.id.binding_fragment_msg);
                } else if (FragmentFactory.GOOD_INFO.equals(fragment)) {
                    GoodInfoManage goodInfoManage = (GoodInfoManage) mainActivity.getCurrentFragmentObject();
                    Object goodsImageIds = map.get("goodsImageIds");
                    String[] ids = null;
                    JSONObject goodsinfo = null;
                    if (goodsImageIds != null && goodsImageIds instanceof String) {
                        ids = new String[1];
                        ids[0] = (String) goodsImageIds;
                    } else if (goodsImageIds != null && goodsImageIds instanceof String[]) {
                        ids = (String[]) goodsImageIds;
                    }
                    if (map.get("parameterMap") instanceof JSONObject) {
                        goodsinfo = (JSONObject) map.get("parameterMap");
                        if(map.get("editable") !=null && SystemConstants.IS_EFFECT_YES.equals(map.get("editable")) )editable=true;
                        goodInfoManage.setGoodsInfo(goodsinfo,editable);
                    } else {
                        goodInfoManage.initStar(this.goodsBarcode);
                    }

                }

            } else if (currentActivity != null && currentActivity instanceof GoodsInfoActivity) {
                GoodsInfoActivity goodsInfoActivity = (GoodsInfoActivity) currentActivity;
                Object goodsImageIds = map.get("goodsImageIds");
                String[] ids = null;
                JSONObject goodsinfo = null;
                if (goodsImageIds != null && goodsImageIds instanceof String) {
                    ids = new String[1];
                    ids[0] = (String) goodsImageIds;
                } else if (goodsImageIds != null && goodsImageIds instanceof String[]) {
                    ids = (String[]) goodsImageIds;
                }
                if (map.get("parameterMap") instanceof JSONObject) {
                    goodsinfo = (JSONObject) map.get("parameterMap");
                    goodsInfoActivity.setGoodsInfo(goodsinfo);
                    goodsInfoActivity.setGoodImagesIds(ids);
                } else {
                    goodsInfoActivity.initStar(this.goodsBarcode);
                }
            }

//            if("y".equals(isSuccess)){
            //返回"y"说明登录成功
            parameterMap = root.element("parameterMap");
            goodsInfoId = parameterMap.elementText("goodsInfoId");
            goodsName = parameterMap.elementText("goodsName");
            goodsBarcode = parameterMap.elementText("goodsBarcode");
            Bundle bundle = new Bundle();
            if ("y".equals(isBind)) {
                tagBarcode = parameterMap.elementText("tagBarcode");
                tagInfoId = parameterMap.elementText("tagInfoId");
                bundle.putString("tagBarcode", tagBarcode);
                bundle.putString("tagInfoId", tagInfoId);
//                    msgTextView.setText("该商品已绑定了价签");
                String m = this.currentActivity.getString(R.string.is_bind_goods);
                bundle.putString("msg", m);
            }
            bundle.putString("goodsInfoId", goodsInfoId);
            bundle.putString("goodsName", goodsName);
            bundle.putString("goodsBarcode", goodsBarcode);
            bundle.putString("isBind", isBind);

            Message message = new Message();
            message.setData(bundle);
            message.what = 1;//设置线程数
//            if (currentActivity != null && currentActivity instanceof ShowBindGoodsActivity) {
//                ShowBindGoodsActivity activity = (ShowBindGoodsActivity) currentActivity;
//                Handler handler = activity.getHandler();
//                handler.sendMessage(message);
//            } else
                if (currentActivity != null && currentActivity instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) currentActivity;
                String fragment = mainActivity.getCurrentFragmentName();
                if (FragmentFactory.BindingTagAndGood.equals(fragment)) {
                    BindingTagAndGood bindingTagAndGood = (BindingTagAndGood) mainActivity.getCurrentFragmentObject();
                    Handler handler = bindingTagAndGood.getHandler();
                    handler.sendMessage(message);
                }
            } else if (currentActivity != null && currentActivity instanceof GoodsInfoActivity) {
                GoodsInfoActivity activity = (GoodsInfoActivity) currentActivity;
                Handler handler = activity.getHandler();
                handler.sendMessage(message);
            }
            if (msgTextView != null) msgTextView.setText(msg);
        } catch (Exception e) {
            if (msgTextView != null) msgTextView.setText(e.getLocalizedMessage());
            e.printStackTrace();
        } finally {
            LoadingDialog.INSTANCE.closeDialog();
//            if (currentActivity != null && currentActivity instanceof MainActivity) {
//                MainActivity mainActivity = (MainActivity) currentActivity;
//                mainActivity.closeLoad();
//            }

        }
    }

}
