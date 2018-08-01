package com.blozi.bindtags.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blozi.bindtags.R;
import com.blozi.bindtags.application.Global;
import com.blozi.bindtags.util.BLOZIPreferenceManager;
import com.blozi.bindtags.util.ScanBarcodeSocketUtil;
import com.blozi.bindtags.util.ScanUtil;
import com.blozi.bindtags.util.SystemConstants;

public class ScanBarcodeOfflineActivity extends BaseActivity {

//    private BroadcastReceiver mBrReceiver;
    private long firstTime=0;
    private Button reset;
    private EditText ipAddress,port,barcode;
    private Button update_barcode;
    private ScanBarcodeSocketUtil socketUtil;
    private BLOZIPreferenceManager bloziPreferenceManager;
    private ScanUtil scanUtil;
    private boolean pressBack;
    public static final String scanMsg = "scanMsg",Out="Out"  ,OutToRight="OutToRight",OutDown="OutDown" , OutTop="OutTop" ,TAG="ScanBarcodeOffline" ;
    private String outWay=null;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_scanbarcode);
        super.onCreate(savedInstanceState);
        Intent intentCreat = getIntent();
        //获取传递的值
        outWay = intentCreat.getStringExtra(Out);
        bloziPreferenceManager= BLOZIPreferenceManager.Companion.getInstance(this);
        scanUtil = new ScanUtil(this);
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextAppearance(this,R.style.blozi_collapseToolbar_collapsedTitleTextAppearance);

        if(OutToRight.equals(outWay)) toolbar.setNavigationIcon(R.drawable.pull_left_orange);
        else if(OutDown.equals(outWay))toolbar.setNavigationIcon(R.drawable.pullup_orange);
        else if(OutTop.equals(outWay))toolbar.setNavigationIcon(R.drawable.pull_down_orange);
        toolbar.setNavigationOnClickListener((v )->{
            try {
                InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(ScanBarcodeOfflineActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                onBackPressed();
            }

        });

//        getsystemscandata();//注册接收数据广播，以TOAST形式显示，退出界面也可以查看数据
        //获取扫描到的条形码的值
        barcode =  findViewById(R.id.barcode);
        ipAddress = findViewById(R.id.ip_address);
        port = findViewById(R.id.port);
        //重置
        reset =  findViewById(R.id.reset);
        update_barcode = findViewById(R.id.update_barcode);

        ipAddress.setText(bloziPreferenceManager.getScanBarcodeUpdateIP());
        ipAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                bloziPreferenceManager.setScanBarcodeUpdateIP(s.toString());
            }
        });

        port.setText(bloziPreferenceManager.getScanBarcodeUpdatePort());
        port.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                bloziPreferenceManager.setScanBarcodeUpdatePort(s.toString());
            }
        });


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barcode.setText("");
            }
        });


        update_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               updateDate();
            }
        });
        if(!"47.100.50.216".equals(bloziPreferenceManager.getScanBarcodeUpdateIP())  )editAble();

        Intent intent =new Intent("com.android.service_settings");
        intent.putExtra("pda_sn","string");//机器码设置成“string”
        this.sendBroadcast(intent);

    }
    private void editAble(){
        setEditable(ipAddress);
        setEditable(port);
        setEditable(barcode);
    }
    /**
     * 创建标题菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.common, menu);
        try {
            MenuBuilder menuBuilder = (MenuBuilder) menu;
            menuBuilder.setOptionalIconsVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.allowEdit:
                bloziPreferenceManager.setEditable(!bloziPreferenceManager.getEditable());
                editAble();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume(){
        super.onResume();
        scanUtil.initScan();
        pressBack=false;
        if (Global.currentActivity != this) Global.currentActivity = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(pressBack   ){
            if(OutToRight.equals(outWay)) overridePendingTransition(R.anim.in_left, R.anim.out_right);
            else if(OutDown.equals(outWay))overridePendingTransition(R.anim.in_top, R.anim.out_bottom);
            else if(OutTop.equals(outWay))overridePendingTransition(R.anim.in_bottom, R.anim.out_top);
        }
        scanUtil.stopScan();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        scanUtil.exitScan();
    }
    @Override
    public void onBackPressed() {
        pressBack=true;
        super.onBackPressed();
    }
    public Handler getHandler() {
        return handler;
    }

    /*
     * 处理传参
     */
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            Bundle bundle = msg.getData();
            if(msg.what == ScanBarcodeSocketUtil.Check){
                String flag = (String) bundle.get("flag");
                Toast.makeText(ScanBarcodeOfflineActivity.this,flag, Toast.LENGTH_SHORT).show();
            }else if( msg.what == SystemConstants.scanSuccess ){
                String scanMsg = (String) bundle.get(SystemConstants.scanMsg);
                if (TextUtils.isEmpty(scanMsg))return;
                barcode.setText(scanMsg);
                updateDate();
            }
        }
    };

    private void updateDate(){
        //判断是否填写两个值
        final String address = ipAddress.getText().toString();
        String portStr = port.getText().toString();
        String id = barcode.getText().toString();
        if("".equals(address)||address==null){
            Toast.makeText(ScanBarcodeOfflineActivity.this,R.string.PleaseFillInTheIPAddress, Toast.LENGTH_SHORT).show();
        }
        else if("".equals(portStr)||portStr==null){
            Toast.makeText(ScanBarcodeOfflineActivity.this,R.string.PleaseFillinThePortNumber, Toast.LENGTH_SHORT).show();
        }
        else if("".equals(id)||id==null){
            Toast.makeText(ScanBarcodeOfflineActivity.this,R.string.PleaseEnterABarCode, Toast.LENGTH_SHORT).show();
        }
        else{
            final String info = "$"+id+"#";
            final int lan = Integer.parseInt(portStr);
            if("".equals(socketUtil)||socketUtil==null){
                socketUtil = new ScanBarcodeSocketUtil(ScanBarcodeOfflineActivity.this);
            }
            new Thread(){
                @Override
                public void run(){
                    socketUtil.sendDate(address,lan,info);
                }
            }.start();
        }
    }
    public void setEditable(EditText editText){
        editText.setFocusable(bloziPreferenceManager.getEditable());
        editText.setClickable(bloziPreferenceManager.getEditable());
        editText.setFocusableInTouchMode(bloziPreferenceManager.getEditable());
        editText.setCursorVisible(bloziPreferenceManager.getEditable());
    }
}
