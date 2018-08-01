package com.blozi.bindtags.asyncTask.online;

import android.app.Activity;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.blozi.bindtags.R;
import com.blozi.bindtags.activities.GoodsInfoActivity;
import com.blozi.bindtags.util.FileUtil;
import com.blozi.bindtags.util.StringFilter;
import com.blozi.bindtags.util.SystemConstants;
import com.blozi.bindtags.util.XmlUtil;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class GetGoodsImagesAsyncTask extends BaseTask {
    public GetGoodsImagesAsyncTask(Activity currentActivity, String webserviceUrl) {
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
     *	    <tagsId>1234567</tagsId>
     *	 </request>
     *	 </xmp>
     */
    @Override
    protected String doInBackground(String... params){
        Map map =new HashMap<>();
        String loginId = bloziPreferenceManager.getLoginid();
        String password = bloziPreferenceManager.getPassword();
        String goodsImageId = params[0];
        map.put("action","getGoodsImageOnApp");
        map.put("loginId",loginId);
        map.put("loginPassword", StringFilter.getMD5(password));
        map.put("goodsImageId",goodsImageId);
        map.put("original",Boolean.FALSE.toString());
        Document document = XmlUtil.mapToXml(map,"request");
        Log.i("测试",document.asXML());
        return super.doInBackground(document.asXML());
    }
    @Override
    protected void onPostExecute(String result) {
        File imageFile=null;
        try{

            Map reponse = new HashMap();
            Object image =null;
            Object type = null;
            Object fileName = null;
            Object goodsImageId = null;
            Object errorMsg = null ;
            try{
                Document doc= DocumentHelper.parseText(result);

                reponse = XmlUtil.xmlToMap(result);
                image =  reponse.get("image") ;
                type = reponse.get("type");
                fileName = reponse.get("fileName");
                goodsImageId = reponse.get("goodsImageId");
                errorMsg = reponse.get("errorMsg");
            }catch (Exception e){
                e.printStackTrace();
            }
//            Log.i("成",doc.asXML());
            //        Log.i("成",reponse.toString());
            if(errorMsg==null && image instanceof String && ! TextUtils.isEmpty((String)image)  && !"noPic".equals(image) && Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ) {
                try {
                    if( image !=null && ((String)image).getBytes().length>1024*1024  ){
                        Toast.makeText(currentActivity,String.format(currentActivity.getResources().getString(R.string.GoodsImageisLargeThanORDonotExist),SystemConstants.GOODSIMAGEMaxLength_M ) ,Toast.LENGTH_SHORT ).show();
                    }else{
                        byte[] bytes = FileUtil.decoderBase64File((String) image);
                        imageFile = new File(SystemConstants.goodsImageFileRoot+"/"+fileName );
                        SystemConstants.createFileRoot();
                        if(!imageFile.exists())imageFile.createNewFile();
                        OutputStream os = new FileOutputStream(imageFile);
                        os.write(bytes);
                        os.flush();
                        os.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }

            if(imageFile!=null && imageFile.exists()){
                if( currentActivity instanceof GoodsInfoActivity){
                    GoodsInfoActivity activity = (GoodsInfoActivity)currentActivity;
                    activity.addImagePath(imageFile.getAbsolutePath());
                }
            }



        }catch (Exception e){
            e.printStackTrace();
            if(currentActivity!=null)Toast.makeText(currentActivity,currentActivity.getResources().getString(R.string.error)+" : "+e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
