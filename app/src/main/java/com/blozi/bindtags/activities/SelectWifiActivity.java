//package com.blozi.blindtags.activities;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.net.wifi.ScanResult;
//import android.net.wifi.WifiConfiguration;
//import android.net.wifi.WifiManager;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import com.blozi.blindtags.R;
//import com.blozi.blindtags.util.StringFilter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class SelectWifiActivity extends AppCompatActivity {
//
//    private Button wifi_conn_scan_btn;
//    private WifiAdmin mWifiAdmin;
//    private boolean isOpen = false;
//    private ListView wifiList;
//    private Button wifi_switch_btn;
//    private WifiConnListAdapter mConnList;
//    private ArrayList<WifiElement> wifiElement = new ArrayList<WifiElement>();
//    private List<ScanResult> list;
//    private ScanResult mScanResult;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        setContentView(R.layout.activity_select_wifi);
//        super.onCreate(savedInstanceState);
//
//        mWifiAdmin = new WifiAdmin(SelectWifiActivity.this);//这里进行wifi模块的初始化
//        //控制打开关闭wifi
//        wifi_conn_scan_btn = (Button) findViewById(R.id.wifi_conn_scan_btn);
//        wifi_conn_scan_btn.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v){
//                //点击wifi控制开关后触发方法
//                if (isOpen) {
//                    Toast.makeText(getApplicationContext(), "正在关闭wifi", Toast.LENGTH_SHORT).show();
//                    if (mWifiAdmin.closeWifi()) {
//                        Toast.makeText(getApplicationContext(), "wifi关闭成功", Toast.LENGTH_SHORT).show();
//                        wifi_conn_scan_btn.setText("打开wifi");
//                        isOpen = false;
//                    } else {
//                        Toast.makeText(getApplicationContext(), "wifi关闭失败", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(getApplicationContext(), "正在打开wifi", Toast.LENGTH_SHORT).show();
//                    if (mWifiAdmin.OpenWifi()) {
//                        Toast.makeText(getApplicationContext(), "wifi打开成功", Toast.LENGTH_SHORT).show();
//                        wifi_conn_scan_btn.setText("关闭wifi");
//                        isOpen = true;
//                    } else {
//                        Toast.makeText(getApplicationContext(), "wifi打开失败", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
//
//        if (mWifiAdmin.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
//            wifi_conn_scan_btn.setText("打开wifi");
//        } else {
//            wifi_conn_scan_btn.setText("关闭wifi");
//            isOpen = true;
//        }
//
//        wifiList = (ListView) this.findViewById(R.id.wifi_conn_lv);
//        wifi_switch_btn = (Button) this.findViewById(R.id.wifi_conn_switch_btn);
//
//        wifi_switch_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mConnList = new WifiConnListAdapter(SelectWifiActivity.this, getAllNetWorkList());
//                wifiList.setAdapter(mConnList);
//            }
//        });
//
//        wifiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, long l) {
//                final String ssid = wifiElement.get(i).getSsid();
//                String currentSsid = mWifiAdmin.getSSID();//当前连接的wifi
//                String s = mWifiAdmin.getwifiInfo();
//                final WifiConfiguration wifiConfiguration = mWifiAdmin.IsExsits(ssid);
//                if (null == wifiConfiguration) {
//                    final View v = View.inflate(SelectWifiActivity.this, R.layout.wifi_pwd, null);
//                    AlertDialog.Builder dialog = new AlertDialog.Builder(SelectWifiActivity.this);
//                    dialog.setTitle(ssid + "：是否连接");
//                    dialog.setView(v);
//                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            EditText pwd = (EditText) v.findViewById(R.id.wifi_password);
//                            String password = pwd.getText().toString();
//                            setMessage(ssid, password);
//                        }
//                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            // TODO Auto-generated method stub
//
//                        }
//                    }).setNeutralButton("移除", new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            // TODO Auto-generated method stub
//                            if (null != wifiConfiguration) {
//                                int id = wifiConfiguration.networkId;
//                              //  System.out.println("id>>>>>>>>>>" + id);
//                                mWifiAdmin.removeNetworkLink(id);
//                            }
//                            wifiElement.remove(i);
//                            mConnList.notifyDataSetChanged();
//                        }
//                    }).create();
//                    dialog.show();
//                }else {
//                    if(currentSsid.equals("\""+ssid+"\"")){
//                        //当连接的wifi与已经连接的wifi相同时，则提示
//                        Toast.makeText(getApplicationContext(), "已经连接该wifi", Toast.LENGTH_SHORT).show();
//                        goNext();
//                    }
//                    else{
//                        boolean connect = mWifiAdmin.Connect(wifiConfiguration);
//                        showResult(connect);
//                    }
//                }
//            }
//        });
//        wifiList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                wifiElement.remove(i);
//                mConnList.notifyDataSetChanged();
//                return true;
//            }
//        });
//    }
//
//    private ArrayList<WifiElement> getAllNetWorkList() {
//        // 每次点击扫描之前清空上一次的扫描结果
//        wifiElement.clear();
//        // 开始扫描网络
//        mWifiAdmin.startScan();
//        list = mWifiAdmin.getWifiList();
//        WifiElement element;
//        if (list != null) {
//            for (int i = 0; i < list.size(); i++) {
//                // 得到扫描结果
//                mScanResult = list.get(i);
//                element = new WifiElement();
//                element.setSsid(mScanResult.SSID);
//                element.setBssid(mScanResult.BSSID);
//                element.setCapabilities(mScanResult.capabilities);
//                element.setFrequency(mScanResult.frequency);
//                element.setLevel(mScanResult.level);
//                wifiElement.add_orange(element);
//            }
//
//        }
//        return wifiElement;
//    }
//
//    private void setMessage(final String ssid,final String password) {
//        boolean flag = mWifiAdmin.Connect(ssid, password, WifiAdmin.WifiCipherType.WIFICIPHER_WPA);
//        if (flag) {
//            Toast.makeText(getApplicationContext(), "正在连接，请稍后", Toast.LENGTH_SHORT).show();
//            showResult(flag);
//        } else {
//            Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void showResult( final boolean flag){
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if(flag){
//                    Toast.makeText(getApplicationContext(), "连接成功", Toast.LENGTH_SHORT).show();
//                    goNext();
//                }
//                else{
//                    Toast.makeText(getApplicationContext(), "连接失败", Toast.LENGTH_SHORT).show();
//                }
//            }
//        },2000);
//    }
//
//    private void goNext(){
//        Intent intent = new Intent(SelectWifiActivity.this,getUserInfoActivity.class);
//        SelectWifiActivity.this.startActivity(intent);
//        finish();
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode,KeyEvent event){
//        switch(keyCode){
//            case KeyEvent.KEYCODE_BACK:
//                Intent intent = new Intent(this,LoginActivity.class);
//                this.startActivity(intent);
//                finish();
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//}
