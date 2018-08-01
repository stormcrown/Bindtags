//package com.blozi.blindtags.activities;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.text.SpannableString;
//import android.text.Spanned;
//import android.text.style.DynamicDrawableSpan;
//import android.view.KeyEvent;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.blozi.blindtags.R;
//import com.blozi.blindtags.application.Global;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class UserInfoActivity extends CommonActivity {
//    private TextView current_store_name;
////    private ImageView user_info_logo;
//    private long firstTime=0;
//    final List<Map<String,String>> list= new ArrayList<>();
//
////    private int indexOfCurrent=0;
//    private Map<String ,String> tempStore = new HashMap<String, String>();
//    private Drawable blozi;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        setContentView(R.layout.activity_user_info);
//        super.onCreate(savedInstanceState);
////        user_info_logo=(ImageView)findViewById(R.id.user_info_logo);
////        user_info_logo.setMaxHeight(screen_height/10);
//
//        TextView textView = (TextView) findViewById(R.id.user_name);
//        textView.setText(userName);
//        TextView view = (TextView) findViewById(R.id.login_id);
//        view.setText(userSession);
//        current_store_name = (TextView)findViewById(R.id.current_store_name);
//
//            final String storeName =Global.curryentStore.get("storeName");
//            if(Global.childrenStores.size()<1){
//                setStoreNameWithImage(storeName,false);
//            }else{
//                list.addAll( Global.childrenStores);
//                list.add(Global.mainStore);
//                final String[] storeNames =new String[list.size()];
//                final String[] storeIds = new String[list.size()];
//                if(Global.currentStoreIndex>=list.size())Global.currentStoreIndex=list.size()-1;
//                for(int i=0;i<list.size();i++){
//                    Map<String,String> map=list.get(i);
//                    if(map!=null && map.get(Global.STORE_NAME)!=null && map.get(Global.STORE_NAME)!=null){
//                        storeNames[i]=map.get(Global.STORE_NAME);
//                        storeIds[i]=map.get(Global.STOREID);
//                    }
//                }
////                Global.currentStoreIndex=list.size()-1;
//                setStoreNameWithImage(storeName,true);
//                blozi = getResources().getDrawable(R.drawable.blozi);
//                blozi.setBounds(0, 0, (int)(screen_height/(screen_density* 20)),(int)(screen_height/(screen_density* 20)));
//
//                current_store_name.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        final AlertDialog.Builder dialog = new AlertDialog.Builder(UserInfoActivity.this);
//
//                        final android.content.DialogInterface.OnClickListener click= new android.content.DialogInterface.OnClickListener(){
//                            @Override
//                            public void onClick(DialogInterface dia,int index) {
//                                if(index>-1 && index<list.size()){
//                                    tempStore.put(Global.STOREID,storeIds[index]);
//                                    tempStore.put(Global.STORE_NAME,storeNames[index]);
//                                    Global.currentStoreIndex=index;
//                                }
//                            }
//                        };
//                        dialog.setTitle("选择当前门店")
//                                .setIcon(blozi)
//                                .setSingleChoiceItems(storeNames, Global.currentStoreIndex,click)
//                                .setPositiveButton("确认",new android.content.DialogInterface.OnClickListener(){
//                                    @Override
//                                    public void onClick(DialogInterface dia, int index) {
//                                        Global.curryentStore.put(Global.STOREID,tempStore.get(Global.STOREID));
//                                        Global.curryentStore.put(Global.STORE_NAME,tempStore.get(Global.STORE_NAME));
//                                        setStoreNameWithImage(tempStore.get(Global.STORE_NAME),true);
//                                    }
//                                } )
//                                .setNegativeButton("取消", new android.content.DialogInterface.OnClickListener(){
//                                    @Override
//                                    public void onClick(DialogInterface dia,int index) {
//
//                                    }
//                                });
//                        dialog.create().show();
//                    }
//                });
//
//            }
//
////        current_store_name.setCompoundDrawables(drawableSpan,drawableSpan,drawableSpan,drawableSpan);
//
//
//
//        //退出功能
//        Button button = (Button) findViewById(R.id.log_out);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                logOut();
//            }
//        });
//    }
//    private void setStoreNameWithImage(String storeName,boolean hasImage){
//
//        if(!hasImage){
//            current_store_name.setText(storeName);
//        }else{
//            SpannableString spannableString = new SpannableString(storeName+"(重选)");
//            DynamicDrawableSpan drawableSpan = new DynamicDrawableSpan(DynamicDrawableSpan.ALIGN_BASELINE) {
//                @Override
//                public Drawable getDrawable() {
//                    Drawable d = getResources().getDrawable(R.drawable.change);
//                    d.setBounds(0, 0, (int)(screen_height/(screen_density* 12)),(int)( screen_height/(screen_density*12)) );
//                    return d;
//                }
//            };
//            spannableString.setSpan(drawableSpan, storeName.length(), storeName.length()+4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//            current_store_name.setText(spannableString);
//        }
//    }
//    @Override
//    public boolean onKeyDown(int keyCode,KeyEvent event){
//        switch(keyCode){
//            case KeyEvent.KEYCODE_BACK:
//                if (System.currentTimeMillis()-firstTime>2000){
//                    String msgClose = UserInfoActivity.this.getString(R.string.quit);
//                    Toast.makeText(this,msgClose,Toast.LENGTH_SHORT).show();
//                    firstTime=System.currentTimeMillis();
//                }else{
//                    finish();
//                    System.exit(0);
//                }
//                return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//}
