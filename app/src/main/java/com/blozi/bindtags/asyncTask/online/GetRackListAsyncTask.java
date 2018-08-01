package com.blozi.bindtags.asyncTask.online;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.blozi.bindtags.R;
import com.blozi.bindtags.activities.MainActivity;
import com.blozi.bindtags.activities.fragment.FragmentFactory;
import com.blozi.bindtags.activities.fragment.mainTab.RackListFragment;
import com.blozi.bindtags.model.RackInfo;
import com.blozi.bindtags.util.StringFilter;
import com.blozi.bindtags.util.SystemConstants;
import com.blozi.bindtags.util.XmlUtil;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GetRackListAsyncTask extends BaseTask {
    private int maxRows = 10,currentPage=1,firstRow=0;
    private boolean add=true;
    public GetRackListAsyncTask(Activity currentActivity, String webserviceUrl ,int maxRows,int currentPage,int firstRow,boolean add ) {
        super(currentActivity, webserviceUrl);
        this.maxRows =maxRows;
        this.currentPage = currentPage;
        this.firstRow = firstRow;
        this.add=add;
    }
    /**
     * request的入参格式如下：
     * 	<?xml version="1.0" encoding="UTF-8" standalone="yes" ?>
     *  <xmp>
     *	 <request>
     *		<action>ShowTagInfoTask</action>
     *	 	<loginId>fly</loginId>
     *	 	<loginPassword>123456</loginPassword>
     *
     *	 </request>
     *	 </xmp>
     */
    @Override
    protected String doInBackground(String... params){
        Map map =new HashMap<>();
        String storeInfoId = params[0];
        map.put("action","GetRackListOnApp");
        map.put("loginId",bloziPreferenceManager.getLoginid());
        map.put("loginPassword", StringFilter.getMD5(bloziPreferenceManager.getPassword()));
        map.put("storeInfoId",storeInfoId);
        map.put("maxRows",maxRows);
        map.put("currentPage",currentPage);
        map.put("firstRow",firstRow);
        map.put("isEffect", SystemConstants.IS_EFFECT_YES);
        Document document = XmlUtil.mapToXml(map,"request");
//        Log.i("测试",document.asXML());
        return super.doInBackground(document.asXML());
    }
    @Override
    protected void onPostExecute(String result) {
        Log.i("成",result);
        try{
//            if(currentActivity != null && currentActivity instanceof MainActivity){
////                currentFragment =  ((MainActivity)currentActivity).getCurrentFragmentObject();
//
//            }
            currentFragment = FragmentFactory.getRackListFragment();
            String x = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><response><flag name=\"flag\"><JSONObject><rackInfoId>2c93c74061e043270161e04bee970002</rackInfoId><storeInfoId>297e442d6081645a016081e0f5dd0001</storeInfoId><rackName>测试门店货架1</rackName><rackCode>20080301_0001</rackCode><storeInfoName>测试</storeInfoName><maxLay>5</maxLay><height>120</height><length>150</length><remarks></remarks><isEffect>y</isEffect><placeLength>12</placeLength><createTime>2018-03-01  14:42:08</createTime><updateTime>2018-07-25  13:26:51</updateTime></JSONObject><JSONObject><rackInfoId>2c93c74061e095c30161eeba0e350002</rackInfoId><storeInfoId>297e442d6081645a016081e0f5dd0001</storeInfoId><rackName>测试货架</rackName><rackCode>11111</rackCode><storeInfoName>测试</storeInfoName><maxLay>10</maxLay><height>5</height><length>0</length><remarks></remarks><isEffect>y</isEffect><placeLength>20</placeLength><createTime>2018-03-04  09:57:06</createTime><updateTime>2018-07-25  13:26:51</updateTime></JSONObject><JSONObject><rackInfoId>2c93c7406397b9a10164b68f0d28000c</rackInfoId><storeInfoId>297e442d6081645a016081e0f5dd0001</storeInfoId><rackName>山楂树</rackName><rackCode>1</rackCode><storeInfoName>测试</storeInfoName><maxLay>5</maxLay><height>557</height><length>0</length><remarks>我不晓得</remarks><isEffect>y</isEffect><placeLength>4</placeLength><createTime>2018-07-20  15:19:46</createTime><updateTime>2018-07-25  13:26:51</updateTime></JSONObject><JSONObject><rackInfoId>2c93c740635c9f6501639129ae8d0004</rackInfoId><storeInfoId>297e442d6081645a016081e0f5dd0001</storeInfoId><rackName>测试货架</rackName><rackCode>001</rackCode><storeInfoName>测试</storeInfoName><maxLay>5</maxLay><height>100</height><length>0</length><remarks></remarks><isEffect>y</isEffect><placeLength>12</placeLength><createTime>2018-05-24  16:00:18</createTime><updateTime>2018-07-22  20:49:14</updateTime></JSONObject><JSONObject><rackInfoId>2c93c74061e043270161e04b5db20001</rackInfoId><storeInfoId>297e442d6081645a016081e0f5dd0001</storeInfoId><rackName>名称</rackName><rackCode>0001</rackCode><storeInfoName>测试</storeInfoName><maxLay>5</maxLay><height>120</height><length>150</length><remarks>我们先写一个超过20000二十个字符的备注吧我们先写一个超过20000二十个字符的备注吧我们先写一个超过20000二十个字符的备注吧</remarks><isEffect>y</isEffect><placeLength>10</placeLength><createTime>2018-03-01  14:41:31</createTime><updateTime>2018-04-10  17:03:21</updateTime></JSONObject><JSONObject><rackInfoId>2c93c74062282e090162748ea0810006</rackInfoId><storeInfoId>297e442d6081645a016081e0f5dd0001</storeInfoId><rackName>空白货架</rackName><rackCode>0004</rackCode><storeInfoName>测试</storeInfoName><maxLay>5</maxLay><height>0</height><length>0</length><remarks>不存入商品信息</remarks><isEffect>y</isEffect><placeLength>10</placeLength><createTime>2018-03-30  09:38:47</createTime><updateTime>2018-04-10  17:03:21</updateTime></JSONObject><JSONObject><totalRows>6</totalRows></JSONObject></flag></response>";
            Document doc= DocumentHelper.parseText(x);
            Map reponse = XmlUtil.xmlToMap(doc.asXML());
//            Log.i("成",doc.asXML());
//            Log.i("成",reponse.toString());

            Object flag = reponse.get("flag");
            if(currentFragment instanceof RackListFragment  && currentFragment!=null ){
                ArrayList<RackInfo> list = new ArrayList<>();
                if( flag!=null && flag instanceof JSONArray){
                    for(int i=0;i< ((JSONArray) flag).length();i++){
                        try {
                            JSONObject jb = ((JSONArray) flag).getJSONObject(i);
                            Iterator<String> keys = jb.keys();
                            RackInfo rackInfo = new RackInfo();
                            while (keys.hasNext()){
                                String key = keys.next();
                                String value = jb.getString(key);
//                                Log.i("值",key+" = " +value );
                                if(rackInfo.getRackName_KEY().equals(key)){
                                    rackInfo.setRackName(value);
                                }else if(rackInfo.getRackCode_KEY().equals(key)){
                                    rackInfo.setRackCode(value);
                                } else if(rackInfo.getMaxLay_KEY().equals(key)){
                                    rackInfo.setMaxLay(jb.getInt(key));
                                }else if(rackInfo.getPlaceLength_KEY().equals(key)){
                                    rackInfo.setPlaceLength(jb.getInt(key));
                                }else if(rackInfo.getRackInfoId_KEY().equals(key)){
                                    rackInfo.setRackInfoId(value);
                                }else if(rackInfo.getRemarks_KEY().equals(key)){
                                    rackInfo.setRemarks(value);
                                }else if(rackInfo.getCreateTime_KEY().equals(key)){
                                    rackInfo.setCreateTime(SystemConstants.SDF.parse(jb.getString(key)) );
                                }else if (rackInfo.getHeight_KEY().equals(key)){
                                    rackInfo.setHeight(jb.getInt(key));
                                }else if (rackInfo.getLength_KEY().equals(key)){
                                    rackInfo.setLength(jb.getInt(key));
                                }else if(rackInfo.getIsEffect_KEY().equals(key)){
                                    rackInfo.setIsEffect(value);
                                }else if("totalRows".equals(key)){
                                    int totalRows = jb.getInt(key);
                                    ((RackListFragment) currentFragment).setTotalLows(totalRows);
                                }
                            }
                            if( !TextUtils.isEmpty(rackInfo.getRackInfoId()) ) list.add(rackInfo);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    if(firstRow>0)((RackListFragment) currentFragment).addDatas(list);
                    else{
                        ((RackListFragment) currentFragment).setDatas(list);
                    }

                }
            }
            try{
                String msg = (String) reponse.get("msg");
                if(!TextUtils.isEmpty(msg)) Toast.makeText(currentActivity ,msg,Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                e.printStackTrace();
            }

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(currentActivity,currentActivity.getResources().getString(R.string.error)+"\n"+e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
        }finally {
            ((RackListFragment) currentFragment).setRefresh(false);
        }
    }
}
