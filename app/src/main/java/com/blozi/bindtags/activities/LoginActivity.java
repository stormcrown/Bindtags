package com.blozi.bindtags.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blozi.bindtags.R;
import com.blozi.bindtags.adapter.AutoCompleteAdapter;
import com.blozi.bindtags.asyncTask.local.LocalDeskSocket;
import com.blozi.bindtags.asyncTask.local.LocalNetBaseTask;
import com.blozi.bindtags.asyncTask.online.LoginTask;
import com.blozi.bindtags.util.LoadingDialog;
import com.blozi.bindtags.util.SystemConstants;
import com.blozi.bindtags.util.SystemMathod;
import com.blozi.bindtags.util.SystemUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import static com.blozi.bindtags.util.SystemConstants.CAMERA_OK;
import static com.blozi.bindtags.util.SystemConstants.READ_PHONE_STATE_RequestCode;

public class LoginActivity extends CommonActivity implements View.OnClickListener {
    private long firstTime = 0;
    private TextView TEXT_VIEW_MSG,appName,SystemName;
    private Button login_in;
    private EditText EDIT_TEXT_PASSWORD;
    private AutoCompleteTextView EDIT_TEXT_USER_NAME;
    private String user_name;//登录账号
    private String password;//登录密码
    private RelativeLayout regbutton;
    private boolean showPass = Boolean.FALSE, loading = Boolean.FALSE, online = Boolean.TRUE;
    private ImageView logo, passRight, webUrl,  upTag_offline;//,background; //linkWifi,
    private TextView toggle;
    private long firstTouch;
    private Animation flipping, flippingReserve, flippingNone, flippingReserveNone, a, b, disappear, appear;
    private String[][] onlineAccounts, localAccounts;
    private String[] loginids, passwords, localLoginids, localPasswords;
    private AutoCompleteAdapter autoCompleteAdapter;
    private TextWatcher online_login_watcher, online_password_watcher, local_login_watcher, local_password_watcher;

    private final int commonAnimDur = 3000;
    private ConstraintLayout head, linearLayout5, userlayout, passlayout;
    private View.OnClickListener logoListenter;
    private ConstraintLayout mainLayout;
    private TextInputLayout loginLayout,passwordLayout;

    private int currImg =0;
    private Handler handlerBackground = new Handler();
//    private Animation fadeOut,fadeIn;
    private Runnable runnable=new Runnable(){
        @Override
        public void run() {
            try{
//                if(background!=null) background.startAnimation(fadeOut);
                int x=  (int)  Math.floor( Math.random()* SystemConstants.backgroundImags.length);
//                if(currImg==x){
//                    x = (int)  Math.floor( Math.abs(imags.length - x));
//                }
                currImg=x;
//                if(background!=null){
//                    Glide.with(LoginActivity.this).clear(background);
//                    Glide.with(LoginActivity.this).load(SystemConstants.backgroundImags[x]).into(background);
//                }else {
//                    handlerBackground.postDelayed(this, 10);
//                }
              //  handlerBackground.postDelayed(this, 3000);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    };

    private ColorStateList primaryLight, accent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        primaryLight = getResources().getColorStateList(R.color.primary_light);
        accent = getResources().getColorStateList(R.color.accent);

        mainLayout = findViewById(R.id.activity_login);
        EDIT_TEXT_USER_NAME = findViewById(R.id.EDIT_TEXT_USER_NAME);
        EDIT_TEXT_PASSWORD = findViewById(R.id.EDIT_TEXT_PASSWORD);
        EDIT_TEXT_USER_NAME.setText(userSession);
        EDIT_TEXT_PASSWORD.setText(userPass);
        regbutton = findViewById(R.id.regbutton);
        logo = findViewById(R.id.login_logo);

//        background = findViewById(R.id.background);
        try{
//            Glide.with(LoginActivity.this).load(SystemConstants.backgroundImags[(int)  Math.floor( Math.random()*SystemConstants.backgroundImags.length)]).into(background);
            Glide.with(this).load(R.mipmap.app_logo_round).into(logo);
            Glide.with(getApplicationContext())
                    .load("file:///android_asset/WebRoot/images/13.jpg")
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            mainLayout.setBackground(resource);
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }

        loginLayout = findViewById(R.id.login_id_layout);
        passwordLayout = findViewById(R.id.password_layout);

//        RequestBuilder RB=

//        Glide.with(this).load("file:///android_asset/WebRoot/images/7.jpg").into(background);
//        Glide.with(this).load("file:///android_asset/WebRoot/images/7.jpg").transition(DrawableTransitionOptions.withCrossFade()).into(background);
//        fadeOut =  AnimationUtils.loadAnimation(LoginActivity.this ,R.anim.fade_out);
//        fadeIn =  AnimationUtils.loadAnimation(LoginActivity.this ,R.anim.fade_in);
//        fadeOut.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                int x=  (int)  Math.floor( Math.random()*SystemConstants.backgroundImags.length);
//                if(currImg==x){
//                    x = (int)  Math.floor( Math.abs(SystemConstants.backgroundImags.length - x));
//                }
//                currImg=x;
//                if(background!=null) Glide.with(LoginActivity.this).load(SystemConstants.backgroundImags[x]).into(background);
//                background.startAnimation(fadeIn);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        RequestOptions requestOptions = RequestOptions.
//        if(background!=null)

//        handlerBackground.postDelayed(runnable,3000);
//        handlerBackground.post(runnable);

        passRight = findViewById(R.id.password_right);
        toggle = findViewById(R.id.online_desktop);
        userlayout = findViewById(R.id.userlayout);
        passlayout = findViewById(R.id.passlayout);
        linearLayout5 = findViewById(R.id.linearLayout5);
        TEXT_VIEW_MSG = findViewById(R.id.TEXT_VIEW_MSG);
        head = findViewById(R.id.head);
        appName = findViewById(R.id.app_name);
        SystemName = findViewById(R.id.online_desktop);
//        localAcr = findViewById(R.id.local_acr);
        flipping = AnimationUtils.loadAnimation(this, R.anim.flipping);
        flippingReserve = AnimationUtils.loadAnimation(this, R.anim.flipping_reverse);
        flippingNone = AnimationUtils.loadAnimation(this, R.anim.flipping);
        flippingReserveNone = AnimationUtils.loadAnimation(this, R.anim.flipping_reverse);
        disappear = AnimationUtils.loadAnimation(this, R.anim.disappear_bottom);
        appear = AnimationUtils.loadAnimation(this, R.anim.appear_bottom);

        changeInputColor(primaryLight);
        changePassColor(primaryLight);

        appear.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationEnd(Animation animation) {
                linearLayout5.clearAnimation();
//                linearLayout5.setVisibility(View.VISIBLE);
                if(regbutton.getVisibility() == View.VISIBLE){
                    regbutton.clearAnimation();
//                    regbutton.setVisibility(View.INVISIBLE);
                }
                login_in.setOnClickListener(LoginActivity.this);
//                ableViews(Boolean.TRUE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        disappear.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationEnd(Animation animation) {
                linearLayout5.setVisibility(View.INVISIBLE);
                if(regbutton.getVisibility() == View.VISIBLE){
                    regbutton.setVisibility(View.INVISIBLE);
                }
                login_in.setOnClickListener(null);
//                ableViews(Boolean.FALSE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        simpleInitAnimation(disappear, commonAnimDur);
        simpleInitAnimation(appear, commonAnimDur);
        a = AnimationUtils.loadAnimation(this, R.anim.scale_alpha_rotate);
        repeatAnim(a, Animation.INFINITE, Animation.REVERSE);
        //判断用户当前登录状态
        onlineAccounts = bloziPreferenceManager.getOnlineAccounts();
        localAccounts = bloziPreferenceManager.getLocalAccounts();
        loginids = new String[onlineAccounts.length];
        passwords = new String[onlineAccounts.length];
        localLoginids = new String[localAccounts.length];
        localPasswords = new String[localAccounts.length];
        for (int i = 0; i < onlineAccounts.length; i++) {
            if (onlineAccounts[i] != null && onlineAccounts[i].length == 2) {
                loginids[i] = onlineAccounts[i][0];
                passwords[i] = onlineAccounts[i][1];
            }
        }
        online_login_watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                bloziPreferenceManager.setLoginid(s.toString());
                if(!TextUtils.isEmpty(s.toString()) ){
                    loginLayout.setError(null);
                    changeInputColor(primaryLight);
                }else{
                    changeInputColor(accent);
                    loginLayout.setError(getResources().getString( R.string.login_msg_enter_username_str) );
                }
                for (int i = 0; i < loginids.length; i++) {
                    if (s.toString().equals(loginids[i])) {
                        EDIT_TEXT_PASSWORD.setText(passwords[i]);
                        bloziPreferenceManager.setLoginid(s.toString());
                        bloziPreferenceManager.setPassword(passwords[i]);
                        break;
                    }
                }
            }
        };
        local_login_watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                bloziPreferenceManager.setLocalUserName(s.toString());
                for (int i = 0; i < localLoginids.length; i++) {
                    if (s.toString().equals(localLoginids[i])) {
                        EDIT_TEXT_PASSWORD.setText(localPasswords[i]);
                        bloziPreferenceManager.setLocalUserName(s.toString());
                        bloziPreferenceManager.setLocalPassword(localPasswords[i]);
                        break;
                    }
                }
                if(!TextUtils.isEmpty(s.toString()) ){
                    loginLayout.setError(null);
                    changeInputColor(primaryLight);
                }else{
                    changeInputColor(accent);
                    loginLayout.setError(getResources().getString( R.string.login_msg_enter_username_str) );
                }
            }
        };
        online_password_watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(s.toString()) ){
                    passwordLayout.setError(null);
                    changePassColor(primaryLight);
                }else {
                    changePassColor(accent);
                    passwordLayout.setError(getResources().getString(R.string.login_msg_enter_userpass_str));
                }
                bloziPreferenceManager.setPassword(s.toString());
            }
        };
        local_password_watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(s.toString()) ){
                    passwordLayout.setError(null);
                    changePassColor(primaryLight);
                }else {
                    changePassColor(accent);
                    passwordLayout.setError(getResources().getString(R.string.login_msg_enter_userpass_str));
                }
                bloziPreferenceManager.setLocalPassword(s.toString());
            }
        };

        autoCompleteAdapter = new AutoCompleteAdapter(this, loginids);
        EDIT_TEXT_USER_NAME.setAdapter(autoCompleteAdapter);
        EDIT_TEXT_USER_NAME.addTextChangedListener(online_login_watcher);
        EDIT_TEXT_PASSWORD.addTextChangedListener(online_password_watcher);
        EDIT_TEXT_PASSWORD.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login_in.performClick();
                }
                return false;
            }
        });
        flipping.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                if (online) {
//                    logo.setImageResource(R.drawable.blozi_dark);
//                    Glide.with(LoginActivity.this).clear(logo);
                    Glide.with(LoginActivity.this).load(R.drawable.logo_mini_primary_dark).into(logo);
                    ColorStateList colorStateList = getResources().getColorStateList(R.color.primary_light);

                    toggle.setText(R.string.Desktop);
                    EDIT_TEXT_USER_NAME.removeTextChangedListener(online_login_watcher);
                    EDIT_TEXT_PASSWORD.removeTextChangedListener(online_password_watcher);
                    EDIT_TEXT_USER_NAME.setText(bloziPreferenceManager.getLocalUserName());
                    EDIT_TEXT_PASSWORD.setText(bloziPreferenceManager.getLocalPassword());
                    EDIT_TEXT_USER_NAME.addTextChangedListener(local_login_watcher);
                    EDIT_TEXT_PASSWORD.addTextChangedListener(local_password_watcher);
                    autoCompleteAdapter = new AutoCompleteAdapter(LoginActivity.this, localLoginids);
                    EDIT_TEXT_USER_NAME.setAdapter(autoCompleteAdapter);
                } else {
//                    logo.setImageResource(R.drawable.blozi);
//                    Glide.with(LoginActivity.this).clear(logo);
                    Glide.with(LoginActivity.this).load(R.drawable.logo_mini_primary).into(logo);
                    toggle.setText(R.string.Online);
                    EDIT_TEXT_USER_NAME.removeTextChangedListener(local_login_watcher);
                    EDIT_TEXT_PASSWORD.removeTextChangedListener(local_password_watcher);
                    EDIT_TEXT_USER_NAME.setText(bloziPreferenceManager.getLoginid());
                    EDIT_TEXT_PASSWORD.setText(bloziPreferenceManager.getPassword());
                    EDIT_TEXT_USER_NAME.addTextChangedListener(online_login_watcher);
                    EDIT_TEXT_PASSWORD.addTextChangedListener(online_password_watcher);
                    autoCompleteAdapter = new AutoCompleteAdapter(LoginActivity.this, loginids);
                    EDIT_TEXT_USER_NAME.setAdapter(autoCompleteAdapter);
                }
                online = !online;
                logo.startAnimation(flippingReserve);
                toggle.startAnimation(flippingReserveNone);
                userlayout.startAnimation(flippingReserve);
                passlayout.startAnimation(flippingReserve);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        logoListenter = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logo.startAnimation(flipping);
                toggle.startAnimation(flipping);
                userlayout.startAnimation(flipping);
                passlayout.startAnimation(flipping);


            }
        };
        logo.setOnClickListener(logoListenter);
        passRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleShowPassword();
            }
        });
        //登录
        login_in = findViewById(R.id.login_btn);
        login_in.setOnClickListener(this);
        //配置服务器地址
        webUrl = findViewById(R.id.webUrl);
        webUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ConfigureUrlActivity.class);
                LoginActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.in_bottom,R.anim.out_top);
            }
        });
        upTag_offline = findViewById(R.id.upTag_offline);
        upTag_offline.setOnClickListener(
                (v) -> {
                    Intent intent = new Intent(LoginActivity.this, ScanBarcodeOfflineActivity.class);
                    intent.putExtra(ScanBarcodeOfflineActivity.Out,ScanBarcodeOfflineActivity.OutDown);
                    LoginActivity.this.startActivity(intent);
                    overridePendingTransition(R.anim.in_bottom,R.anim.out_top);
                }
            );
    }

    /**
     * 登录过程中，使许多控件不响应用户操作
     */
//    private void ableViews(boolean able) {
//        EDIT_TEXT_USER_NAME.setEnabled(able);
//        EDIT_TEXT_PASSWORD.setEnabled(able);
//        login_in.setEnabled(able);
//        passRight.setEnabled(able);
//        webUrl.setEnabled(able);
//      //  linkWifi.setEnabled(able);
//        upTag_offline.setEnabled(able);
//    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_btn) {
            //对账号密码进行判断
            user_name = EDIT_TEXT_USER_NAME.getText().toString();
            password = EDIT_TEXT_PASSWORD.getText().toString();
            TEXT_VIEW_MSG.setText(null);
            if (TextUtils.isEmpty(user_name)) {
                Toast.makeText(LoginActivity.this, R.string.login_msg_enter_username_str, Toast.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, R.string.login_msg_enter_userpass_str, Toast.LENGTH_LONG).show();
            } else {
                if (!online && !checkUrl(bloziPreferenceManager.getLocalIPPort()) ) {
                    return;
                }else if(online && !checkUrl(bloziPreferenceManager.getCompliteURL()) ){
                    return;
                }
                int oldHeadHeight = head.getHeight();
                int topHead = head.getTop();
                int bodyHeight = linearLayout5.getHeight();
                int bodyTop = linearLayout5.getTop();
                TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (bodyTop - topHead - oldHeadHeight + bodyHeight) / (float) 2);
                translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        linearLayout5.setVisibility(View.GONE);
                        regbutton.setVisibility(View.GONE);
                        linearLayout5.clearAnimation();
                        regbutton.clearAnimation();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                simpleInitAnimation(translateAnimation, 4000);
                repeatAnim(translateAnimation, Animation.INFINITE, Animation.REVERSE);
                Animation fateOut = AnimationUtils.loadAnimation(context, R.anim.fade_out_repeat);
                simpleInitAnimation(fateOut, commonAnimDur);
                repeatAnim(fateOut, Animation.INFINITE, Animation.REVERSE);
                Animation scaleAnimation = AnimationUtils.loadAnimation(context, R.anim.scale);
                simpleInitAnimation(scaleAnimation, commonAnimDur);
                repeatAnim(scaleAnimation, Animation.INFINITE, Animation.REVERSE);

                AnimationSet animationSet = new AnimationSet(true);
                repeatAnim(animationSet, 2, Animation.REVERSE);
                animationSet.addAnimation(fateOut);
                animationSet.addAnimation(translateAnimation);
                animationSet.addAnimation(scaleAnimation);




//                head.startAnimation(animationSet);
//                linearLayout5.startAnimation(disappear);
//                if (regbutton.getVisibility() == View.VISIBLE) {
//                    regbutton.startAnimation(disappear);
//                }

                if (online) {
                    LoadingDialog.INSTANCE.alertDialog(this);
                    if (checkUrl(bloziPreferenceManager.getCompliteURL())) {
                        LoginTask loginService = new LoginTask(LoginActivity.this, bloziPreferenceManager.getCompliteURL());
                        loginService.execute(user_name, password);
                    }else return ;
                } else {
                    if (checkUrl(bloziPreferenceManager.getLocalIPPort())) {
                        if( Build.VERSION.SDK_INT>22 &&  ContextCompat.checkSelfPermission(LoginActivity.this,android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, SystemConstants.READ_PHONE_STATE_RequestCode);
                        }else {
                            LoadingDialog.INSTANCE.alertDialog(this);
                            LocalNetBaseTask localNetBaseTask = new LocalNetBaseTask(this);
                            String phoneCode = "";
                            try{
                                phoneCode = SystemUtil.getDeviceId(this) ;
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            localNetBaseTask.execute(LocalDeskSocket.Companion.getRequestCode(LocalDeskSocket.RequestCode.Login, user_name, password,phoneCode ));
                            LocalDeskSocket.Companion.setLoginMsg(LocalDeskSocket.Companion.getRequestCode(LocalDeskSocket.RequestCode.Login, user_name, password, phoneCode ));
                        }

                    }else return ;

                }
//                ableViews(Boolean.FALSE);
                loading = Boolean.TRUE;
                logo.setOnClickListener(null);
                TEXT_VIEW_MSG.setText(null);
                v.setOnClickListener(null);

            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        switch (requestCode){
            case READ_PHONE_STATE_RequestCode:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    onClick(login_in);
                }else{
                    Toast.makeText(this,"需要获取手机串号",Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }

    }
    private void changeInputColor(ColorStateList colorStateList){
        loginLayout.setErrorTextColor(colorStateList);
        loginLayout.setDefaultHintTextColor(colorStateList);
        EDIT_TEXT_USER_NAME.setTextColor(colorStateList);

    }
    private void changePassColor(ColorStateList colorStateList){
        passwordLayout.setErrorTextColor(colorStateList);
        passwordLayout.setDefaultHintTextColor(colorStateList);
        EDIT_TEXT_PASSWORD.setTextColor(colorStateList);
        api(colorStateList);
    }

    private void api(ColorStateList colorStateList){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            passRight.setImageTintList(colorStateList);
        }
    }
    public boolean checkUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(context, R.string.PleaseFillTheServerAddress, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LoginActivity.this, ConfigureUrlActivity.class);
            LoginActivity.this.startActivity(intent);
            return false;
        }
        return true;
    }

    /**
     * 简单初始化动画
     */
    private void simpleInitAnimation(Animation ainm, int during) {
        ainm.setFillEnabled(true);
        ainm.setFillAfter(true);
        ainm.setFillBefore(false);
        ainm.setDuration(during);
    }

    private void repeatAnim(Animation animation, int count, int model) {
        animation.setRepeatCount(count);
        animation.setRepeatMode(model);
    }

    /**
     * 显示/隐藏密码切换
     */
    private void toggleShowPassword() {
        if (showPass) {
            EDIT_TEXT_PASSWORD.setTransformationMethod(PasswordTransformationMethod.getInstance());
            passRight.setImageResource(R.drawable.eye_close_primary);
        } else {
            EDIT_TEXT_PASSWORD.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            passRight.setImageResource(R.drawable.eye_open_primary);
        }
        EDIT_TEXT_PASSWORD.setSelection(EDIT_TEXT_PASSWORD.getText().length());
        showPass = !showPass;
    }

    protected void onResume() {
        super.onResume();
//        EDIT_TEXT_PASSWORD.setPointerIcon();
        regbutton.clearAnimation();
        regbutton.setVisibility(View.INVISIBLE);
        EDIT_TEXT_PASSWORD.setTransformationMethod(PasswordTransformationMethod.getInstance());
        showPass = Boolean.FALSE;
        passRight.setImageResource(R.drawable.eye_close_primary);
//        regbutton.setVisibility(View.GONE);
        context = this;
    }

    /**
     * 删除线上账号
     */
    public void deleteOnlineAccounts(String id) {
        if (bloziPreferenceManager != null) bloziPreferenceManager.removeOnLineAccount(id);
        if (!TextUtils.isEmpty(id) && id.indexOf(EDIT_TEXT_USER_NAME.getText().toString()) > -1) {
            EDIT_TEXT_USER_NAME.setText("");
            EDIT_TEXT_PASSWORD.setText("");
        }
        onlineAccounts = bloziPreferenceManager.getOnlineAccounts();
        loginids = new String[onlineAccounts.length];
        for (int i = 0; i < onlineAccounts.length; i++) {
            loginids[i] = onlineAccounts[i][0];
        }
        autoCompleteAdapter = new AutoCompleteAdapter(this, loginids);
        EDIT_TEXT_USER_NAME.setAdapter(autoCompleteAdapter);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!loading && regbutton.getVisibility() != View.VISIBLE) {
            linearLayout5.clearAnimation();
            regbutton.clearAnimation();
            regbutton.setVisibility(View.VISIBLE);

        }
//        Log.i("触碰","触发");
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (System.currentTimeMillis() - firstTime > 2000) {
                    String msgClose = LoginActivity.this.getString(R.string.quit);
                    Toast.makeText(this, msgClose, Toast.LENGTH_SHORT).show();
                    firstTime = System.currentTimeMillis();
                } else {
                    finish();
                    System.exit(0);
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public Handler getHandler() {
        return handler;
    }

    private Handler handler = new LoginHandler();

    private class LoginHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            if (msg.what == 1) {
                String m = (String) bundle.get("msg");
                TEXT_VIEW_MSG.setText(m);
                head.clearAnimation();
                logo.clearAnimation();
                loading = Boolean.FALSE;
                logo.setOnClickListener(logoListenter);

//                linearLayout5.startAnimation(appear);

                linearLayout5.setVisibility(View.VISIBLE);
                login_in.setOnClickListener(LoginActivity.this);
//                ableViews(Boolean.TRUE);
//                regbutton.clearAnimation();
              //  if (regbutton.getVisibility() == View.VISIBLE) regbutton.startAnimation(appear);

//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle(m);
//                builder.setPositiveButton(R.string.confirm, ((dialog, which) ->{  } ));
//                builder.create().show();

            }
        }
    }


}
