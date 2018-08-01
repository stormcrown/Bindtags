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
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import com.blozi.blindtags.R;
//import com.blozi.blindtags.application.Global;
//import com.blozi.blindtags.asyncTask.online.UpdateTagsTask;
//import com.blozi.blindtags.util.SystemConstants;
//import com.zxing_new.activity.CodeUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ImportTagsActivity extends CommonActivity {
//
//    private BroadcastReceiver mBrReceiver;
//    private EditText tagId;//显示扫描到的价签的iD
//    private Button reset;//重置按钮
//    private Button updateTags;//上传价签Id
//    private ListView tagsList;
//    private List<String> tagList;
//    private BaseAdapter baseAdapter;
//    private Button scan;
//
//    public static final int REQUEST_CODE = 111;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        setContentView(R.layout.activity_import_tags);
//        super.onCreate(savedInstanceState);
//        getsystemscandata();//注册接收数据广播，以TOAST形式显示，退出界面也可以查看数据
//
//        tagId = (EditText) findViewById(R.id.tag_id);
//        reset = (Button) findViewById(R.id.to_reset);
//        updateTags = (Button) findViewById(R.id.update_tags);
//        tagList = new ArrayList();
//        baseAdapter = new TagAdapter(ImportTagsActivity.this,tagList);
//        tagsList = (ListView) findViewById(R.id.tags_list);
//        tagsList.setAdapter(baseAdapter);
//
//        //重置按钮，将扫描的数据全部清空
//        reset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                tagId.setText("");
//                tagList.clear();
//                baseAdapter.notifyDataSetChanged();
//            }
//        });
//
//        //扫描
//        scan = (Button) findViewById(R.id.scan);
//        scan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //吊起摄像头
//                Intent openCameraIntent = new Intent(ImportTagsActivity.this, com.zxing_new.activity.CaptureActivity.class);
//                startActivityForResult(openCameraIntent, REQUEST_CODE);
//            }
//        });
//
//        //上传扫描的数据，成功后清空列表
//        updateTags.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(tagList.size()>0){
//                    //首先取出保存的服务器地址信息
////                    String webServiceUrl = getPropertiesValue(SystemConstants.PROPERTY_NAME_WEBSERVICE_URL);
////                    String url = ImportTagsActivity.this.getString(R.string.web_service_url_address);
////                    webServiceUrl = webServiceUrl+url;
//                    String webServiceUrl = bloziPreferenceManager.getCompliteURL();
//                    String tagsId = "";
//                    for(String id : tagList){
//                        tagsId += id+"_";
//                    }
//
//                    UpdateTagsTask updateTagsService = new UpdateTagsTask(ImportTagsActivity.this, webServiceUrl);
//                    updateTagsService.execute(userSession, userPass, tagsId, Global.curryentStore.get(Global.STOREID));
//                }
//                else{
//                    Toast.makeText(ImportTagsActivity.this,"请扫描价签！",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        Intent intent =new Intent("com.android.service_settings");
//        intent.putExtra("pda_sn","string");//机器码设置成“string”
//        this.sendBroadcast(intent);
//    }
//
//    @Override
//    protected void onResume(){
//        super.onResume();
//        IntentFilter intentFilter = new IntentFilter(SystemConstants.SACN_BordCast);
//        registerReceiver(mBrReceiver,intentFilter);
//    }
//
//    protected void onPause(){
//        super.onPause();
//        unregisterReceiver(mBrReceiver);
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
//                        tagId.setText(dataArr[1]);
//                        isAddToList(dataArr[1]);
//                    }
//                    else{
//                        tagId.setText(dataArr[0]);
//                        isAddToList(dataArr[0]);
//                    }
//                }
//            }
//        };
////        IntentFilter filter = new IntentFilter(getstr);
////        registerReceiver(mBrReceiver, filter);
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode,KeyEvent event){
//        switch(keyCode){
//            case KeyEvent.KEYCODE_BACK:
//                Intent intent = new Intent(this,UserInfoActivity.class);
//                this.startActivity(intent);
//                finish();
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    private void isAddToList(String id){
//        //判断是否加入list中
//        boolean flag = false;//false表示没有重复的
//        for (int i = 0; i<tagList.size(); i++){
//            if(id.equals(tagList.get(i))){
//                flag = true;//表示已经存在与列表中
//            }
//        }
//        if(flag){
//            Toast.makeText(ImportTagsActivity.this,"已经扫描过该价签！",Toast.LENGTH_SHORT).show();
//        }
//        else{
//            tagList.add(id);
//            baseAdapter.notifyDataSetChanged();
//        }
//    }
//
//    public Handler getHandler() {
//        return handler;
//    }
//
//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            Bundle bundle = msg.getData();
//            String isSuccess = bundle.getString("isSuccess");
//            if("y".equals(isSuccess)){
//                tagId.setText("");
//                tagList.clear();
//                baseAdapter.notifyDataSetChanged();
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
//                    tagId.setText(result);
//                    isAddToList(result);
//                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
//                    Toast.makeText(ImportTagsActivity.this, "解析失败", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//    }
//}
