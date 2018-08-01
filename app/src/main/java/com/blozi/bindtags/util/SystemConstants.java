package com.blozi.bindtags.util;

import com.blozi.bindtags.R;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * Created by fly_liu on 2017/6/21.
 */
public class SystemConstants {
    public static final int WRITE_EXTERNAL_STORAGE_RequestCode = 748+1;
    public static final int INSTALL_PACKAGES_REQUESTCODE =748+2;
    public static final int Dowmload_APK_REQUESTCODE =748+3;
    public static final int CheckUpdate_APK_REQUESTCODE =748+4;
    public static final String DownloadApk = "DownloadApkSuccess";
    public static final int  DownloadApkSuccess = 748+5;
    public static final int READ_PHONE_STATE_RequestCode = 516;
    public static final int CAMERA_OK = 566;
    public static final int WRITE_SETTINGS_RequestCode = 21;
    public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     *用于活动之间传递数据的键
     * */
    public static final String RACK_TRANI = "rack_trans";
    public static final String RACKS_TRANI = "racks_trans";
    public static final String INDEX_TRANI = "index_trans";
    public static String[] backgroundImags ={
            "file:///android_asset/WebRoot/images/12.jpg",
            "file:///android_asset/WebRoot/images/13jpg",
            "file:///android_asset/WebRoot/images/14.jpg",
            "file:///android_asset/WebRoot/images/15.jpg",
            "file:///android_asset/WebRoot/images/16.jpg"
    };
    public static String[] productsImags ={
            "file:///android_asset/Images/1.jpg",
            "file:///android_asset/Images/2.jpg",
            "file:///android_asset/Images/7.png",
            "file:///android_asset/Images/4.jpg",
            "file:///android_asset/Images/6.png",
            "file:///android_asset/Images/8.png",
    };
    public static int[] productsImagsInt ={
            R.drawable.product_1,
            R.drawable.product_2,
            R.drawable.product_4,
            R.drawable.product_6,
            R.drawable.product_7,
            R.drawable.product_8
    };
    /**选择图片*/
    public static final int SELECT_PHOTO = 2;
    /**请求*/
    public static final int REQUEST_CODE = 111;

    public static final int GOODSIMAGEMaxLength=1024*1024;
    public static final String GOODSIMAGEMaxLength_M=GOODSIMAGEMaxLength/(1024f*1024f)+"M";
    public static final String GOODSIMAGEMaxLength_K=GOODSIMAGEMaxLength/(1024f)+"M";

    public static final String TEMP_DIR=SystemUtil.getSDCardPath()+"/Download/\u4fdd\u8d44/";
    /**系统有时会提供默认的前缀*/
    public static final String Public_TEMP_DIR= "Download/\u4fdd\u8d44/";
    public static final String fileRoot = SystemUtil.getSDCardPath()+"/\u4fdd\u8d44/";
    public static final String goodsImageFileRoot = SystemUtil.getSDCardPath()+"/\u4fdd\u8d44/\u5546\u54c1\u56fe\u7247/";
    public static final void createFileRoot(){
        File root = new File(fileRoot);
        if(!root.exists())root.mkdirs();
        File temp = new File(TEMP_DIR);
        if(!temp.exists())temp.mkdirs();
        File goodsImage = new File(goodsImageFileRoot);
        if(!goodsImage.exists())goodsImage.mkdirs();
    }
    public static final int over_time = 3*1000;
    /**
     * Kaicom 570  广播注册
     * */
    public static final String SACN_BordCast = "com.android.receive_scan_action";
    /**Kaicom 570 接收扫描广播数据*/
    public static final String Kaicom_recive = "data";
    /**
     *KT40Q 机型广播注册
     * */
    public static final String KT0_SCAN ="com.se4500.onDecodeComplete";
    /**KT40Q 接收广播数据*/
    public static final String KTO_recive="se4500";
    public static final int  scanSuccess = 5;

    public static final String scanMsg = "scanMsg";
    public static final String BloZi_scan_receive="com.blozi.recive.scan.code";
    /*AlpsAndroid**/
    public static final String RES_ACTION = "android.intent.action.SCANRESULT";
    /**
     * 用于存放超市管理系统服务器链接地址的本地文件名称
     */
//    public static String FILE_NAME_OF_WEBSERVICE = "blozi-market-webservice.properties";

    /**
     * 本地property文件当中所保存的超市管理系统服务器端地址项的名称
     */
    public static String PROPERTY_NAME_WEBSERVICE_URL = "marketWebserviceUrl";

    /**
     * 用于存放登录用户信息的本地文件名称
     */
    public static String FILE_NAME_OF_USER_INFO = "login-info.properties";

    /**
     * 本地property文件当中所保存的用户主键ID项的名称
     */
    public static String PROPERTY_NAME_USER_ID = "userId";

    /**
     * 本地property文件当中所保存的用户登录名的名称
     */
    public static String PROPERTY_NAME_LOGIN_ID = "loginId";

    /**
     * 本地property文件当中所保存的用户密码项的名称
     */
    public static String PROPERTY_NAME_PASSWORD = "password";

    /**
     * 本地property文件当中所保存的用户姓名
     */
    public static String PROPERTY_NAME_USER_NAME = "userName";

    /**
     * 数据库中记录的逻辑状态：有效的记录
     */
    public static String IS_EFFECT_YES = "y";

    /**
     * 数据库中记录的逻辑状态：已被逻辑删除的记录
     */
    public static String IS_EFFECT_NO = "n";

    /**
     * 操作状态：成功
     */
    public static String IS_SUCCESS_YES = "y";

    /**
     * 操作状态：失败
     */
    public static String IS_SUCCCESS_NO = "n";

    /**
     * 数据库中的排序顺序：顺序
     */
    public static String SORT_ASC = "ASC";

    /**
     * 数据库中的排序顺序：倒序
     */
    public static String SORT_DESC = "DESC";
}
