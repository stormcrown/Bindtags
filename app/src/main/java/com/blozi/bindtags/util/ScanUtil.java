package com.blozi.bindtags.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;

import com.blozi.bindtags.activities.GoodsInfoActivity;
import com.jar.BarcodeManager;
import com.mexxen.barcode.BarcodeConfig;


public class ScanUtil {
    /**apls android*/
    private static com.jar.ScannerInterface apls_Android_scanner;
    /**apls 4KT0Q*/
    private static com.scandecode.inf.ScanInterface apls_4KT0Q_scanner;
    /**DataTerminal Data Terminal*/
    private static BarcodeManager mBarcodeManager;
    private static BarcodeConfig mBarcodeConfig;
    private static  boolean isContinue = false;	//连续扫描的标志
    private static  boolean repeat=false;
    private static Activity context;
    public ScanUtil(Activity context) {
        this.context = context;
    }

    public static  void setRepeat(boolean repeat0) {
        repeat = repeat0;
    }
    public static  void initScan(){
        try {
            initAplsAndroid();
        }catch (Exception e){
            Log.i( "扫描初始化","AplsAndroid扫描头不存在" );
        }
//        try {
//            initApls4KT0Q();
//        }catch (Exception e){
//            Log.i( "扫描初始化","initApls4KT0Q扫描头不存在" );
//        }
        try {
            initDataTerminal();
        }catch (Exception e){
            Log.i( "扫描初始化","initDataTerminal扫描头不存在" );
        }
    }
    public  void exitScan(){
        finishAplsAndroidScanner();
        if(apls_4KT0Q_scanner!=null){
            apls_4KT0Q_scanner.onDestroy();
            apls_4KT0Q_scanner=null;
        }
        if(mBarcodeManager!=null)mBarcodeManager.dismiss();
    }
    public  void stopScan(){
        if(apls_Android_scanner!=null){
            //apls_Android_scanner.scan_stop();
            apls_Android_scanner.continceScan(false);
        }
        if(apls_4KT0Q_scanner!=null)apls_4KT0Q_scanner.stopScan();
        if ( "DataTerminal".equals(SystemUtil.getDeviceBrand())){
            if(mBarcodeManager!=null)mBarcodeManager.dismiss();
        }
    }
    public static void stopScan(boolean ifStop){
        try{
            if ( "DataTerminal".equals(SystemUtil.getDeviceBrand())){
                if ( Build.VERSION.SDK_INT>22 &&  ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_SETTINGS}, 1);
                }else{
                    if(mBarcodeConfig!=null) mBarcodeConfig.setScanner(ifStop);
                    if(!ifStop && "DataTerminal".equals(SystemUtil.getDeviceBrand()) ) Thread.sleep(200L);
                }
            }
        }catch (Exception e){
            Log.e("DataTerminal","DataTerminal 关闭扫描需要权限");
            e.printStackTrace();
        }
    }
    private static  com.jar.ScannerInterface initAplsAndroid() throws Exception{
        if(apls_Android_scanner==null) apls_Android_scanner = new com.jar.ScannerInterface(context);
            apls_Android_scanner.open();	//打开扫描头上电   apls_Android_scanner.close();//打开扫描头下电
            apls_Android_scanner.enablePlayBeep(true);//是否允许蜂鸣反馈
            apls_Android_scanner.enableFailurePlayBeep(false);//扫描失败蜂鸣反馈
            apls_Android_scanner.enablePlayVibrate(true);//是否允许震动反馈
            apls_Android_scanner.enableAddKeyValue(1);/**附加无、回车、Teble、换行*/
            apls_Android_scanner.timeOutSet(2);//设置扫描延时2秒
            apls_Android_scanner.intervalSet(1000); //设置连续扫描间隔时间
            apls_Android_scanner.lightSet(false);//关闭右上角扫描指示灯
            apls_Android_scanner.enablePower(true);//省电模式
            //		apls_Android_scanner.addPrefix("AAA");//添加前缀
            //		apls_Android_scanner.addSuffix("BBB");//添加后缀
            //		apls_Android_scanner.interceptTrimleft(2); //截取条码左边字符
            //		apls_Android_scanner.interceptTrimright(3);//截取条码右边字符
            //		apls_Android_scanner.filterCharacter("R");//过滤特定字符
            apls_Android_scanner.SetErrorBroadCast(true);//扫描错误换行
            //apls_Android_scanner.resultScan();//恢复iScan默认设置

            //		apls_Android_scanner.lockScanKey();
            //锁定设备的扫描按键,通过iScan定义扫描键扫描，用户也可以自定义按键。
            apls_Android_scanner.unlockScanKey();
            //释放扫描按键的锁定，释放后iScan无法控制扫描按键，用户可自定义按键扫描。
            /**设置扫描结果的输出模式，参数为0和1：
             * 0为模拟输出（在光标停留的地方输出扫描结果）；
             * 1为广播输出（由应用程序编写广播接收者来获得扫描结果，并在指定的控件上显示扫描结果）
             * 这里采用接收扫描结果广播并在TextView中显示*/
            apls_Android_scanner.scanKeySet(139,0);
            apls_Android_scanner.scanKeySet(140,0);
            apls_Android_scanner.scanKeySet(141,0);
            apls_Android_scanner.setOutputMode(1);

        return apls_Android_scanner;
    }
    private  com.scandecode.inf.ScanInterface initApls4KT0Q() throws Exception{
        if(apls_4KT0Q_scanner==null) apls_4KT0Q_scanner = new com.scandecode.ScanDecode(context);

        apls_4KT0Q_scanner.initService("true");//初始化扫描服务
        return apls_4KT0Q_scanner;
    }
    private static  void initDataTerminal() throws Exception{
        mBarcodeConfig = new BarcodeConfig(context);
        mBarcodeManager = new BarcodeManager(context);
//        mBarcodeManager.addListener(new BarcodeListener() {
//            @Override
//            public void barcodeEvent(BarcodeEvent event) {
//                // 当条码事件的命令为“SCANNER_READ”时，进行操作
//                if (event.getOrder().equals(SCANNER_READ)) {
//                    String barcode = mBarcodeManager.getBarcode();
//                    Bundle bundle = new Bundle();
//                    bundle.putString(SystemConstants.scanMsg, barcode);
//                    Message message = new Message();
//                    message.setData(bundle);
//                    message.what = SystemConstants.scanSuccess;
//                    if(handler!=null)handler.sendMessage(message);
//                }
//            }
//        });
        if(!mBarcodeConfig.isScannerOn())mBarcodeConfig.setScanner(true); // 扫描头开启
        mBarcodeConfig.setPrefix("");
        mBarcodeConfig.setSuffix("");
        mBarcodeConfig.setTerminalChar(3);
    }

    /**
     * 结束扫描
     */
    private  void finishAplsAndroidScanner(){
        if(apls_Android_scanner !=null){
            Log.i("关闭","关闭扫描");
            apls_Android_scanner.scan_stop();
            apls_Android_scanner.close();	//关闭iscan  非正常关闭会造成iScan异常退出
            apls_Android_scanner.continceScan(false);
            apls_Android_scanner=null;
        }
    }
    public  void onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 139&&event.getRepeatCount()==0){
           if(apls_Android_scanner!=null) apls_Android_scanner.scan_start();
            if(apls_4KT0Q_scanner!=null)apls_4KT0Q_scanner.starScan();
        }
    }
    public  void onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == 139){	/**按键弹起，停止扫描*/
            if(apls_Android_scanner!=null) apls_Android_scanner.scan_stop();
            if(apls_4KT0Q_scanner!=null)apls_4KT0Q_scanner.stopScan();
        }else if (keyCode == 140 || keyCode == 141 ){
            if(!repeat){
                if(apls_Android_scanner!=null){
                    isContinue = false;
                    apls_Android_scanner.continceScan(false);
                    apls_Android_scanner.scan_start();
                }

            }else{
          //      if(apls_Android_scanner!=null) apls_Android_scanner.scan_stop();
                isContinue=!isContinue;
                if(apls_Android_scanner!=null) apls_Android_scanner.continceScan(isContinue);
            }

        }

    }
}
