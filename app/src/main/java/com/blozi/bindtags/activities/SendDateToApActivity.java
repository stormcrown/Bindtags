//package com.blozi.blindtags.activities;
//
//import android.app.AlertDialog;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
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
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.blozi.blindtags.R;
//import com.blozi.blindtags.model.GoodsEdBarcode;
//import com.blozi.blindtags.util.SocketUtil;
//import com.blozi.blindtags.util.SystemConstants;
//import com.zxing_new.activity.CodeUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class SendDateToApActivity extends CommonActivity {
//
//    private BroadcastReceiver mBrReceiver;
//    private String flag;
//    private EditText goods_code;
//    private EditText tag_code;
//    private List<GoodsEdBarcode> dateList;
//    private ListView updateList;
//    private BaseAdapter baseAdapter;
//    private TextView isUpdate;
//    private String goodsCode;
//    private String tagCode;
//
//    public static final int REQUEST_CODE = 111;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        setContentView(R.layout.activity_send_date_to_ap);
//        super.onCreate(savedInstanceState);
//        getsystemscandata();
//        goods_code = (EditText) findViewById(R.id.goods_code);
//        tag_code = (EditText) findViewById(R.id.tag_code);
//        isUpdate = (TextView) findViewById(R.id.is_update);
//        updateList = (ListView) findViewById(R.id.update_date_list);
//        updateList.setEmptyView(findViewById(R.id.empty));
//        dateList = new ArrayList();
//        baseAdapter = new DateAdapter(SendDateToApActivity.this,dateList);
//        updateList.setAdapter(baseAdapter);
//
//        //重置两个条形码
//        Button to_reset = (Button) findViewById(R.id.to_reset);
//        to_reset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goods_code.setText("");
//                tag_code.setText("");
//            }
//        });
//
//        //扫描条形码
//        Button scan = (Button) findViewById(R.id.scan);
//        scan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(SendDateToApActivity.this);
//                builder.setTitle(R.string.title) ;
//                builder.setPositiveButton(R.string.goods, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        flag = "goods";
//                        //吊起摄像头
//                        Intent openCameraIntent = new Intent(SendDateToApActivity.this, com.zxing_new.activity.CaptureActivity.class);
//                        startActivityForResult(openCameraIntent, REQUEST_CODE);
//                    }
//                });
//                builder.setNegativeButton(R.string.tags, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        flag = "tags";
//                        //吊起摄像头
//                        Intent openCameraIntent = new Intent(SendDateToApActivity.this, com.zxing_new.activity.CaptureActivity.class);
//                        startActivityForResult(openCameraIntent, REQUEST_CODE);
//                    }
//                });
//                builder.show();
//            }
//        });
//
//        //上传
//        Button updateDate = (Button) findViewById(R.id.update_date);
//        updateDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //先判断两个条形码是否拿到
//                goodsCode = goods_code.getText().toString();
//                tagCode = tag_code.getText().toString();
//                if ("".equals(goodsCode)||goodsCode==null){
//                    Toast.makeText(SendDateToApActivity.this,"请扫描商品条码",Toast.LENGTH_SHORT).show();
//                }
//                else if("".equals(tagCode)||tagCode==null){
//                    Toast.makeText(SendDateToApActivity.this,"请扫描价签条码",Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    //两个都拿到了，判断是否已经上传过该组信息
//                    boolean isGo = true;
//                    for (int i=0;i<dateList.size();i++){
//                        GoodsEdBarcode goodsEdBarcode = dateList.get(i);
//                        String goodsBarcode = goodsEdBarcode.getGoodsBarcode();
//                        String edBarcode = goodsEdBarcode.getEdBarcode();
//                        if(goodsBarcode.equals(goodsCode)&&edBarcode.equals(tagCode)){
//                            //说明已经上传过改组数据
//                            isGo = false;
//                            break;
//                        }
//                    }
//                    if(isGo){
//                        // 进行上传功能
//                        final String info = "**"+userSession+"_"+userPass+"_"+goodsCode+"_"+tagCode+";,,";
//                        final String ip = SendDateToApActivity.this.getString(R.string.socket_ip);
//                        final int port = Integer.parseInt(SendDateToApActivity.this.getString(R.string.socket_port));
//                        new Thread(){
//                            @Override
//                            public void run(){
//                                SocketUtil socketUtil = new SocketUtil(SendDateToApActivity.this);
//                                socketUtil.sendDate(ip,port,info);
//                            }
//                        }.start();
//                    }
//                    else{
//                        Toast.makeText(SendDateToApActivity.this,"已经上传过该组数据",Toast.LENGTH_SHORT).show();
//
//                    }
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
//                    String id = "";
//                    if(dataArr.length>1){
//                        id = dataArr[1];
//                    }
//                    else{
//                        id = dataArr[0];
//                    }
//                    //这里做一个判断，长度为10的条形码时ed的条形码，其他一律为商品条形码
//                    if(id.length()==10){
//                        tag_code.setText(id);
//                    }
//                    else{
//                        //商品条形码
//                        goods_code.setText(id);
//                    }
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
//                    if("goods".equals(flag)){
//                        goods_code.setText(result);
//                    }
//                    if("tags".equals(flag)){
//                        tag_code.setText(result);
//                    }
//                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
//                    Toast.makeText(SendDateToApActivity.this, "解析失败", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode,KeyEvent event){
//        switch(keyCode){
////            case KeyEvent.KEYCODE_BACK:
////                Intent intent = new Intent(this,SelectGoActivity.class);
////                this.startActivity(intent);
////                finish();
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
//                    Toast.makeText(SendDateToApActivity.this,"上传成功！",Toast.LENGTH_SHORT).show();
//                    GoodsEdBarcode goodsEdBarcode = new GoodsEdBarcode();
//                    goodsEdBarcode.setGoodsBarcode(goodsCode);
//                    goodsEdBarcode.setEdBarcode(tagCode);
//                    dateList.add(0,goodsEdBarcode);
//                    isUpdate.setText("(已上传"+dateList.size()+"组)");
//                    goods_code.setText("");
//                    tag_code.setText("");
//                    baseAdapter.notifyDataSetChanged();
//                }
//                else{
//                    Toast.makeText(getApplicationContext(),"上传失败，请重试！",Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    };
//}
