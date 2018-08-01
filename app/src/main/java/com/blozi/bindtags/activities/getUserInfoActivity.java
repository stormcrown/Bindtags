//package com.blozi.blindtags.activities;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.blozi.blindtags.R;
//import com.blozi.blindtags.util.BLOZIPreferenceManager;
//
///**  失效 */
//public class getUserInfoActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        setContentView(R.layout.activity_get_user_info);
//        super.onCreate(savedInstanceState);
//
//        Button suerToGo = (Button) findViewById(R.id.sure_to_go);
//        suerToGo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                EditText userName = (EditText) findViewById(R.id.user_name);
//                EditText pass = (EditText) findViewById(R.id.pass);
//                String user = userName.getText().toString();
//                String password = pass.getText().toString();
//                if("".equals(user)||user==null){
//                    Toast.makeText(getUserInfoActivity.this,"请输入登录账号",Toast.LENGTH_SHORT).show();
//                }
//                else if("".equals(password)||password==null){
//                    Toast.makeText(getUserInfoActivity.this,"请输入登录密码",Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    //判断用户当前登录状态
////                    SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
////                    //存入数据
////                    SharedPreferences.Editor editor = sharedPreferences.cancelEdit();
////                    editor.putString("userSession", user);
////                    editor.putString("userPass", password);
////                    editor.commit();
//                    BLOZIPreferenceManager bloziPreferenceManager= BLOZIPreferenceManager.Companion.getInstance(getUserInfoActivity.this);
//                    bloziPreferenceManager.setLoginid(user);
//                    bloziPreferenceManager.setPassword(password);
//
//                    Intent intent = new Intent(getUserInfoActivity.this, SelectGoActivity.class);
//                    getUserInfoActivity.this.startActivity(intent);
//                    finish();
//                }
//            }
//        });
//    }
//}
