package com.blozi.bindtags.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.blozi.bindtags.R;
import com.blozi.bindtags.util.BLOZIPreferenceManager;

/**
 * Created by fly_liu on 2017/6/22.
 */

public class CommonActivity extends BaseActivity {
    public String userSession;
    public String userPass;
    public String userName;
    public static float screen_density = 1;
    public static int screen_width = 320;
    public static int screen_height = 480;
    protected BLOZIPreferenceManager bloziPreferenceManager;
    protected Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        bloziPreferenceManager = BLOZIPreferenceManager.Companion.getInstance(CommonActivity.this);
        context=this;
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        screen_density = dm.density;
        screen_width = dm.widthPixels;
        screen_height = dm.heightPixels;
        Log.i("屏幕数据：",screen_density+"\t"+screen_width+"\t"+screen_height);

        userSession = bloziPreferenceManager.getLoginid();
        userPass = bloziPreferenceManager.getPassword();
        userName = bloziPreferenceManager.getUserName();

//        Log.i("屏幕数据：",userSession+"\t"+userPass+"\t"+userName);
        //跳转绑定页面
//        TextView to_blind = (TextView) findViewById(R.id.to_blind);
//        if(to_blind!=null){
//            to_blind.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(CommonActivity.this, ShowBindGoodsActivity.class);
//                    CommonActivity.this.startActivity(intent);
//                    finish();
//                }
//            });
//        }

        //跳转列表页面
//        TextView blind_goods_list = (TextView) findViewById(R.id.blind_goods_list);
//        if(blind_goods_list!=null){
//            blind_goods_list.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(CommonActivity.this, ShowBindGoodsListActivity.class);
//                    CommonActivity.this.startActivity(intent);
//                    finish();
////                    Toast.makeText(CommonActivity.this,"功能已经被取消",Toast.LENGTH_SHORT).show();
//                }
//            });
//        }

        //跳转到登陆人信息首页
//        TextView user_info = (TextView) findViewById(R.id.user_info);
//        if(user_info!=null){
//            user_info.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(CommonActivity.this, UserInfoActivity.class);
//                    CommonActivity.this.startActivity(intent);
//                    finish();
//                }
//            });
//        }
        //跳转到上传价签的页面
//        TextView importTags = (TextView) findViewById(R.id.import_tag);
//        if(user_info!=null){
//            importTags.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(CommonActivity.this, ImportTagsActivity.class);
//                    CommonActivity.this.startActivity(intent);
//                    finish();
//                }
//            });
//        }
    }

    public void logOut(){
//        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
//        sharedPreferences.cancelEdit().clear().commit();
        bloziPreferenceManager = BLOZIPreferenceManager.Companion.getInstance(CommonActivity.this);
        bloziPreferenceManager.clearLoginMsg();

        Intent intent = new Intent(CommonActivity.this, LoginActivity.class);
        CommonActivity.this.startActivity(intent);
        finish();
    }
    protected void alertMessage(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(msg);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }
    public void setEditable(EditText editText){
        editText.setFocusable(bloziPreferenceManager.getEditable());
        editText.setClickable(bloziPreferenceManager.getEditable());
        editText.setFocusableInTouchMode(bloziPreferenceManager.getEditable());
        editText.setCursorVisible(bloziPreferenceManager.getEditable());
    }
    public void setEditable(EditText editText,boolean editable){
        editText.setFocusable(editable);
        editText.setClickable(editable);
        editText.setFocusableInTouchMode(editable);
        editText.setCursorVisible(editable);
    }
    public Handler getHandler() {
        return handler;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            if (msg.what == 111) {
                msg.what=0;
                String m = (String) bundle.get("msg");
                Toast.makeText(CommonActivity.this,m,Toast.LENGTH_LONG).show();
            }
        }
    };
}
