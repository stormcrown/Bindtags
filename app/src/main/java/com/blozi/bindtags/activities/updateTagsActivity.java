//package com.blozi.blindtags.activities;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.view.KeyEvent;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.blozi.blindtags.R;
//import com.blozi.blindtags.util.SocketUtil;
//import com.blozi.blindtags.util.SystemConstants;
//import com.zxing_new.activity.CodeUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class updateTagsActivity extends CommonActivity {
//
//    private BroadcastReceiver mBrReceiver;
//    private EditText tag_code;
//    private Button updateDate;
//    private String tagCode;
//    private Button to_reset;
//    private Button scan;
//    private List<String> codeList;
//
//    public static final int REQUEST_CODE = 111;
//
//    private String flag;//用于判断进哪个webservice方法1:goods;2:tags;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_update_tags);
//        getsystemscandata();//注册接收数据广播，以TOAST形式显示，退出界面也可以查看数据
//        tag_code = (EditText) findViewById(R.id.tag_code);
//        codeList = new ArrayList<String>();
//        //重置两个条形码
//        to_reset = (Button) findViewById(R.id.to_reset);
//        to_reset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tag_code.setText("");
//            }
//        });
//
//        //扫描
//        scan = (Button) findViewById(R.id.scan);
//        scan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //吊起摄像头
//                Intent openCameraIntent = new Intent(updateTagsActivity.this, com.zxing_new.activity.CaptureActivity.class);
//                startActivityForResult(openCameraIntent, REQUEST_CODE);
//            }
//        });
//
//        //上传
//        updateDate = (Button) findViewById(R.id.update_date);
//        updateDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //先判断两个条形码是否拿到
//                tagCode = tag_code.getText().toString();
//                if("".equals(tagCode)||tagCode==null){
//                    Toast.makeText(updateTagsActivity.this,"请扫描价签条码",Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    codeList.add(tagCode);
//                    // 进行上传功能
//                    String info = "**";
//                    for(String code : codeList){
//                        info += userSession+"_"+userPass+"_"+null+"_"+code+";";
//                    }
//                    info += ",,";
//                    codeList.clear();
//                    final String codeStr = info;
//                    final String ip = updateTagsActivity.this.getString(R.string.socket_ip);
//                    final int port = Integer.parseInt(updateTagsActivity.this.getString(R.string.socket_port));
//                    new Thread(){
//                        @Override
//                        public void run(){
//                            SocketUtil socketUtil = new SocketUtil(updateTagsActivity.this);
//                            socketUtil.sendDate(ip,port,codeStr);
//                        }
//                    }.start();
//                }
//            }
//        });
//
//        Intent intent =new Intent("com.android.service_settings");
//        intent.putExtra("pda_sn","string");//机器码设置成“string”
//        this.sendBroadcast(intent);
//    }
//
//    /**
//     * 获取接受到的扫描数据,注册广播
//     */
//    public void getsystemscandata() {
//        final String getstr = "com.android.receive_scan_action";
//        mBrReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                if (intent.getAction().equals(getstr)) {
//                    String data = intent.getStringExtra("data");
//                    String[] dataArr = data.split(":");
//                    if(dataArr.length>1){
//                        tag_code.setText(dataArr[1]);
//                        codeList.add(dataArr[1]);
//                    }
//                    else{
//                        tag_code.setText(dataArr[0]);
//                        codeList.add(dataArr[0]);
//                    }
//                    //判断list中有10个数据，就自动上传
//                    isUpdateDate();
//                }
//            }
//        };
////        IntentFilter filter = new IntentFilter(getstr);
////        registerReceiver(mBrReceiver, filter);
//    }
//
//    @Override
//    protected void onResume(){
//        super.onResume();
//        IntentFilter intentFilter = new IntentFilter(SystemConstants.SACN_BordCast);
//        registerReceiver(mBrReceiver,intentFilter);
//    }
//    @Override
//    protected void onPause(){
//        super.onPause();
//        unregisterReceiver(mBrReceiver);
//    }
//    private void isUpdateDate() {
//        if (codeList.size()>=10){
//            // 进行上传功能
//            String info = "**_";
//            for(String code : codeList){
//                info += code+";";
//            }
//            info += ",,";
//            codeList.clear();
//            final String codeStr = info;
//            final String ip = updateTagsActivity.this.getString(R.string.socket_ip);
//            final int port = Integer.parseInt(updateTagsActivity.this.getString(R.string.socket_port));
//            new Thread(){
//                @Override
//                public void run(){
//                    SocketUtil socketUtil = new SocketUtil(updateTagsActivity.this);
//                    socketUtil.sendDate(ip,port,codeStr);
//                }
//            }.start();
//        }
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode,KeyEvent event){
//        switch(keyCode){
//            case KeyEvent.KEYCODE_BACK:
//                Intent intent = new Intent(this,SelectGoActivity.class);
//                this.startActivity(intent);
//                finish();
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    public Handler getHandler() {
//        return handler;
//    }
//
//    /*
//     * 处理传参
//     */
//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg){
//            if(msg.what==2){
//                Bundle bundle = msg.getData();
//                String flag = (String) bundle.get("flag");
//                if("true".equals(flag)){
//                    //将数据加入list中并显示出来
//                    Toast.makeText(updateTagsActivity.this,"上传成功！",Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Toast.makeText(getApplicationContext(),"上传失败，请重试！",Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    };
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE) {
//            //处理扫描结果（在界面上显示）
//            if (null != data) {
//                Bundle bundle = data.getExtras();
//                if (bundle == null) {
//                    return;
//                }
//                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
//                    String result = bundle.getString(CodeUtils.RESULT_STRING);
//                    tag_code.setText(result);
//                    //判断list中有10个数据，就自动上传
//                    isUpdateDate();
//                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
//                    Toast.makeText(updateTagsActivity.this, "解析失败", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//    }
//}
