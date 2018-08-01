package com.blozi.bindtags.activities.fragment;

import android.app.Activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.blozi.bindtags.activities.MainActivity;
import com.blozi.bindtags.activities.MainActivityLocal;
import com.blozi.bindtags.util.BLOZIPreferenceManager;
import com.blozi.bindtags.util.SystemMathod;
import com.blozi.bindtags.util.SystemUtil;
import com.zxing_new.activity.CaptureActivity;

/**
 * Created by 骆长涛 on 2018/3/14.
 */

public abstract class BaseFragment extends Fragment {
    protected String userSession;
    protected String userPass;
    protected String userName;
    protected TextView msg;
    public static float screen_density = 1;
    public static int screen_width = 320;
    public static int screen_height = 480;
    protected Activity context = getActivity();
    public Activity getMyContext(){
        return context;
    }
    protected BLOZIPreferenceManager bloziPreferenceManager;
//    protected RelativeLayout title_parent;
//    protected ImageView title_left,title_right;
//    protected TextView title;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;//mCtx 是成员变量，上下文引用
    }
    @Override
    public void onDetach() {
        super.onDetach();
        context= null;
    }
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        screen_density = dm.density;
        screen_width = dm.widthPixels;
        screen_height = dm.heightPixels;
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
//        userSession = sharedPreferences.getString("userSession", "");
//        userPass = sharedPreferences.getString("userPass", "");
//        userName = sharedPreferences.getString("userName", "");
        bloziPreferenceManager = BLOZIPreferenceManager.Companion.getInstance(getActivity());
        userSession = bloziPreferenceManager.getLoginid();
        userPass = bloziPreferenceManager.getPassword();
        userName = bloziPreferenceManager.getUserName();
//        super.onCreate(savedInstanceState);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    public  abstract void accetpMsg(String str);


    public void setEditable(EditText editText){
        if(editText!=null && bloziPreferenceManager!=null ){
            editText.setFocusable(bloziPreferenceManager.getEditable());
            editText.setClickable(bloziPreferenceManager.getEditable());
            editText.setFocusableInTouchMode(bloziPreferenceManager.getEditable());
            editText.setCursorVisible(bloziPreferenceManager.getEditable());
        }
    }
    public abstract void editableToggle() ;
    public abstract boolean isForceTag() ;
    protected void startCaptureScan(int requestCode){
        SystemMathod.startCaptureScan(context,requestCode);

    }
    /*
     * 获取properties中保存的服务器地址信息
     */
//    public String getPropertiesValue(String key){
//        String value = null;
//        Properties properties = new Properties();
//        try {
//            properties.load(getActivity().getApplicationContext().openFileInput(SystemConstants.FILE_NAME_OF_WEBSERVICE));
//            value = properties.getProperty(key);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return value;
//    }

}


