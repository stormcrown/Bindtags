package com.blozi.bindtags.asyncTask.online;

import android.app.Activity;
import android.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.blozi.bindtags.R;
import com.blozi.bindtags.activities.GoodsInfoActivity;
import com.blozi.bindtags.activities.MainActivity;
import com.blozi.bindtags.activities.fragment.FragmentFactory;
import com.blozi.bindtags.activities.fragment.mainTab.GoodInfoManage;
import com.blozi.bindtags.util.BLOZIPreferenceManager;
import com.blozi.bindtags.util.LoadingDialog;
import com.blozi.bindtags.util.StringFilter;
import com.blozi.bindtags.util.SystemConstants;
import com.blozi.bindtags.util.XmlUtil;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UpdateGoodsInfoAsyncTask extends BaseTask {
    private static final String ACTION = "UpdateGoodsInfo";

    //    private BLOZIPreferenceManager bloziPreferenceManager;
    public UpdateGoodsInfoAsyncTask(Activity currentActivity, String webserviceUrl) {
        super(currentActivity, webserviceUrl);
        bloziPreferenceManager = BLOZIPreferenceManager.Companion.getInstance(currentActivity);
    }

    /**
     * request的入参格式如下：
     * <?xml version="1.0" encoding="UTF-8" standalone="yes" ?>
     * <xmp>
     * <request>
     * <action>ShowTagInfoTask</action>
     * <loginId>fly</loginId>
     * <loginPassword>123456</loginPassword>
     * <tagsId>1234567</tagsId>
     * </request>
     * </xmp>
     */
    @Override
    protected String doInBackground(String... params) {
        Map map = new HashMap<>();
        String loginId = bloziPreferenceManager.getLoginid();
        String password = bloziPreferenceManager.getPassword();
        String goodsInfoId = params[0];
        String goodsInfo = params[1];
        try {
//            byte[] goodsInfoBytes= goodsInfo.getBytes("utf-8");
//            map.put("goodsInfo", FileUtil.encodeBase64File(goodsInfoBytes));
            JSONObject jsonObject = new JSONObject(goodsInfo);
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = jsonObject.getString(key);
                if ("images".equals(key) && !TextUtils.isEmpty(value)) {
                    String[] files = value.split("\n");
                    File[] imagefiles = new File[files.length];
                    for (int i = 0; i < files.length; i++) {
                        if (!TextUtils.isEmpty(files[i])) {
                            File file = new File(files[i]);
                            if (file != null && file.exists()) {
                                imagefiles[i] = file;
                            }
                        }
                    }
                    map.put("images", imagefiles);

                }
            }
            jsonObject.remove("images");
            map.put("goodsInfo", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("goodsInfoUnchange", goodsInfo);
        }
        map.put("action", "updateGoodsInfoOnApp");
        map.put("loginId", loginId);
        map.put("loginPassword", StringFilter.getMD5(password));
        map.put("goodsInfoId", goodsInfoId);
//        Log.i("上传价签",map.toString());
        Document document = XmlUtil.mapToXml(map, "request");
        Log.i("测试", document.asXML());
        return super.doInBackground(document.asXML());
    }


    @Override
    protected void onPostExecute(String result) {
        try {
            Document doc = DocumentHelper.parseText(result);
            Map reponse = XmlUtil.xmlToMap(doc.asXML());
//            Log.i("成",doc.asXML());
            //        Log.i("成",reponse.toString());
            String msg = (String) reponse.get("msg");
            String isSuccess = (String) reponse.get("isSuccess");
            if (!TextUtils.isEmpty(msg) && SystemConstants.IS_EFFECT_YES.equals(isSuccess)) {
                Toast.makeText(currentActivity, msg, Toast.LENGTH_LONG).show();
                Object goodsInfo = reponse.get("newGoodsInfo");
                if (goodsInfo != null && goodsInfo instanceof JSONObject && currentActivity instanceof MainActivity) {
                    MainActivity mainActivity = (MainActivity) currentActivity;
//                    String fragment = mainActivity.getCurrentFragmentName();
//                    if (FragmentFactory.GOOD_INFO.equals(fragment)) {
//                        GoodInfoManage goodInfoManage = (GoodInfoManage) mainActivity.getCurrentFragmentObject();
                    GoodInfoManage goodInfoManage = FragmentFactory.getGoodInfoManage();
                    goodInfoManage.setGoodsInfo((JSONObject) goodsInfo, true);
//                    }
                } else if (goodsInfo != null && goodsInfo instanceof JSONObject && currentActivity instanceof GoodsInfoActivity) {
                    GoodsInfoActivity activity = (GoodsInfoActivity) currentActivity;
                    activity.setGoodsInfo((JSONObject) goodsInfo);
                }
            } else if (!TextUtils.isEmpty(msg) && SystemConstants.IS_EFFECT_NO.equals(isSuccess)) {
                AlertDialog.Builder ab = new AlertDialog.Builder(currentActivity)
                        .setTitle(R.string.message)
                        .setMessage(msg)
                        .setNegativeButton(currentActivity.getResources().getString(R.string.confirm), null);

                ab.create().show();
            } else {
                Toast.makeText(currentActivity, currentActivity.getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(currentActivity, currentActivity.getResources().getString(R.string.error) + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }finally {
            LoadingDialog.INSTANCE.closeDialog();
        }
    }
}
