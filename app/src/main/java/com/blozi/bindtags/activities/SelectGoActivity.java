//package com.blozi.blindtags.activities;
//
//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.KeyEvent;
//import android.view.View;
//import android.widget.Button;
//
//import com.blozi.blindtags.R;
//
//public class SelectGoActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_select_go);
//
//        Button addGoods = (Button) findViewById(R.id.addGoods);
//        addGoods.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(SelectGoActivity.this, SendDateToApActivity.class);
//                SelectGoActivity.this.startActivity(intent);
//                finish();
//            }
//        });
//
//        Button notGoods = (Button) findViewById(R.id.notGoods);
//        notGoods.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(SelectGoActivity.this, updateTagsActivity.class);
//                SelectGoActivity.this.startActivity(intent);
//                finish();
//            }
//        });
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode,KeyEvent event){
//        switch(keyCode){
//            case KeyEvent.KEYCODE_BACK:
//                Intent intent = new Intent(this,MainActivity.class);
//                this.startActivity(intent);
//                finish();
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//}
