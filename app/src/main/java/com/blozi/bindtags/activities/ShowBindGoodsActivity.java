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
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.blozi.blindtags.R;
//import com.blozi.blindtags.application.Global;
//import com.blozi.blindtags.asyncTask.online.BlindTagGoodsTask;
//import com.blozi.blindtags.asyncTask.online.ShowGoodsInfoTask;
//import com.blozi.blindtags.util.SystemConstants;
//import com.blozi.blindtags.util.SystemMathod;
//import com.zxing_new.activity.CaptureActivity;
//import com.zxing_new.activity.CodeUtils;
//
//public class ShowBindGoodsActivity extends CommonActivity {
//
//    private String goodsInfoId;
//    private String goodsName;
//    private String goodsBarcode;
//    private String tagBarcode;
//    private String isBind;
//    private String tagInfoId;
//    private BroadcastReceiver mBrReceiver;
//    private EditText goods_code_str;
//    private EditText goods_name_str;
//    private EditText tag_code_str;
//    private Button scan;
//    private Button to_reset;
//    private Button blind;
//    private String webServiceUrl;
//
//    public static final int REQUEST_CODE = 111;
//
//    private String flag;//用于判断进哪个webservice方法1:goods;2:tags;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        setContentView(R.layout.activity_show_bind_goods);
//        super.onCreate(savedInstanceState);
//        getsystemscandata();//注册接收数据广播，以TOAST形式显示，退出界面也可以查看数据
//        Bundle bundle = this.getIntent().getExtras();
//        //这里将三个输入框禁止输入，如果需要开放，则去除就可
//        goods_code_str = (EditText) findViewById(R.id.goods_code);
////        goods_code_str.setEnabled(false);
//        goods_name_str = (EditText) findViewById(R.id.goods_name);
////        goods_name_str.setEnabled(false);
//        tag_code_str = (EditText) findViewById(R.id.tag_code);
////        tag_code_str.setEnabled(false);
//        if(bundle!=null){
//            goodsName = bundle.getString("goodsName");
//            if(!"".equals(goodsName)&&goodsName!=null){
//                goods_name_str.setText(goodsName);
//            }
//            goodsBarcode = bundle.getString("goodsBarcode");
//            if(!"".equals(goodsBarcode)&&goodsBarcode!=null){
//                goods_code_str.setText(goodsBarcode);
//            }
//            tagBarcode = bundle.getString("tagBarcode");
//            if(!"".equals(tagBarcode)&&tagBarcode!=null){
//                tag_code_str.setText(tagBarcode);
//            }
//            tagInfoId = bundle.getString("tagInfoId");
//            goodsInfoId = bundle.getString("goodsInfoId");
//        }
//
//        //首先取出保存的服务器地址信息
////        webServiceUrl = getPropertiesValue(SystemConstants.PROPERTY_NAME_WEBSERVICE_URL)+ShowBindGoodsActivity.this.getString(R.string.web_service_url_address);
//            webServiceUrl = bloziPreferenceManager.getCompliteURL();
//        //扫描功能
//        scan = (Button) findViewById(R.id.scan);
//        scan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TextView msgTextView = (TextView)findViewById(R.id.TEXT_VIEW_MSG);
//                msgTextView.setText("");
//                Intent openCameraIntent = new Intent(ShowBindGoodsActivity.this, CaptureActivity.class);
//                startActivityForResult(openCameraIntent, REQUEST_CODE);
//            }
//        });
//
//        //重置功能
//        to_reset = (Button) findViewById(R.id.to_reset);
//        to_reset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goods_code_str.setText("");
//                goods_name_str.setText("");
//                tag_code_str.setText("");
//                goodsInfoId = "";
//                tagInfoId = "";
//                isBind = "";
//            }
//        });
//
//        //绑定功能
//        blind = (Button) findViewById(R.id.blind);
//        blind.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String goodsCode = goods_code_str.getText().toString();
//                String goodsName = goods_name_str.getText().toString();
//                String tagCode = tag_code_str.getText().toString();
//                if("".equals(goodsCode) || goodsCode==null || "".equals(goodsName) || goodsName==null){
//                    String goods_str = ShowBindGoodsActivity.this.getString(R.string.goods_str);
//                    Toast.makeText(ShowBindGoodsActivity.this, goods_str, Toast.LENGTH_SHORT).show();
//                }
//                else if("".equals(tagCode) || tagCode==null){
//                    String tag_str = ShowBindGoodsActivity.this.getString(R.string.tag_str);
//                    Toast.makeText(ShowBindGoodsActivity.this, tag_str, Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    //进行绑定具体操作
//                    BlindTagGoodsTask blindTagGoodsService = new BlindTagGoodsTask(ShowBindGoodsActivity.this, webServiceUrl);
//                    blindTagGoodsService.execute(userSession, userPass, goodsInfoId, tagInfoId, tagCode,Global.curryentStore.get(Global.STOREID));
//                }
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQUEST_CODE) {
//            //处理扫描结果（在界面上显示）
//            if (null != data) {
//                Bundle bundle = data.getExtras();
//                if (bundle == null) {
//                    return;
//                }
//                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
//                    String result = bundle.getString(CodeUtils.RESULT_STRING);
//                    if(SystemMathod.isTagCode(result)){
//                        tag_code_str.setText(result);
//                    }else{
//                        ShowGoodsInfoTask showGoodsInfoService = new ShowGoodsInfoTask(ShowBindGoodsActivity.this, webServiceUrl);
//                        showGoodsInfoService.execute(userSession, userPass, result, Global.curryentStore.get(Global.STOREID));
//                    }
//                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
//                    Toast.makeText(ShowBindGoodsActivity.this, "解析失败", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
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
//    public Handler getHandler() {
//        return handler;
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
//                    if(SystemMathod.isTagCode(id)){
//                        tag_code_str.setText(id);
//                    }
//                    else{
//                        //商品条形码
//                        ShowGoodsInfoTask showGoodsInfoService = new ShowGoodsInfoTask(ShowBindGoodsActivity.this, webServiceUrl);
//                        showGoodsInfoService.execute(userSession, userPass, id, Global.curryentStore.get(Global.STOREID));
//                    }
//                }
//            }
//        };
//    }
//
//    /**
//     * 处理接口传来的参数
//     */
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            final Bundle bundle = msg.getData();
//            isBind = bundle.getString("isBind");
//            if (msg.what == 1) {
//                goodsInfoId = bundle.getString("goodsInfoId");
//                goodsName = bundle.getString("goodsName");
//                if(!"".equals(goodsName)&&goodsName!=null){
//                    goods_name_str.setText(goodsName);
//                }
//                goodsBarcode = bundle.getString("goodsBarcode");
//                if(!"".equals(goodsBarcode)&&goodsBarcode!=null){
//                    goods_code_str.setText(goodsBarcode);
//                }
////                String code = bundle.getString("tagBarcode");
////                tag_code_str.setText(code);
////                if ("y".equals(isBind)){
////                    if(!"".equals(tagBarcode)&&tagBarcode!=null){
////                        AlertDialog.Builder builder = new AlertDialog.Builder(ShowBindGoodsActivity.this);
////                        builder.setTitle("商品已绑定价签，是否重新绑定？") ;
////                        builder.setPositiveButton("确认",new DialogInterface.OnClickListener(){
////                            @Override
////                            public void onClick(DialogInterface dialog, int which){
////                                tagBarcode = bundle.getString("tagBarcode");
////                                tag_code_str.setText(tagBarcode);
////                                tagInfoId = bundle.getString("tagInfoId");
////                            }
////                        });
////                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
////                            @Override
////                            public void onClick(DialogInterface dialogInterface, int i) {
////
////                            }
////                        });
////                        builder.show();
////                    }
////                }
//            }
//            if(msg.what == 2){
//                tagInfoId = bundle.getString("tagInfoId");
//                tagBarcode = bundle.getString("physicalIpAddress");
//                if(!"".equals(tagBarcode)&&tagBarcode!=null){
//                    tag_code_str.setText(tagBarcode);
//                }
////                String name = bundle.getString("goodsName");
////                String code = bundle.getString("goodsBarcode");
////                goods_name_str.setText(name);
////                goods_code_str.setText(code);
////                if ("y".equals(isBind)){
////                    if(!"".equals(goodsName)&&goodsName!=null){
////                        AlertDialog.Builder builder = new AlertDialog.Builder(ShowBindGoodsActivity.this);
////                        builder.setTitle("价签已绑定商品，是否重新绑定？") ;
////                        builder.setPositiveButton("确认",new DialogInterface.OnClickListener(){
////                            @Override
////                            public void onClick(DialogInterface dialog, int which){
////                                goodsInfoId = bundle.getString("goodsInfoId");
////                                goodsName = bundle.getString("goodsName");
////                                goodsBarcode = bundle.getString("goodsBarcode");
////                                goods_name_str.setText(goodsName);
////                                goods_code_str.setText(goodsBarcode);
////                            }
////                        });
////                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
////                            @Override
////                            public void onClick(DialogInterface dialogInterface, int i) {
////
////                            }
////                        });
////                        builder.show();
////                    }
////                }
//            }
//            if (msg.what==3){
//                String m = bundle.getString("msg");
//                String isSuccess = bundle.getString("isSuccess");
//                Toast.makeText(ShowBindGoodsActivity.this,m,Toast.LENGTH_LONG).show();
//                if("y".equals(isSuccess)){
//                    goods_code_str.setText("");
//                    goods_name_str.setText("");
//                    tag_code_str.setText("");
//                    goodsInfoId = "";
//                    tagInfoId = "";
//                    isBind = "";
//                }
//            }
//        }
//    };
//}
