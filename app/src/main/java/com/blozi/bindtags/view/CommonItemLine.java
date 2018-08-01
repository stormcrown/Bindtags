package com.blozi.bindtags.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blozi.bindtags.R;
import com.bumptech.glide.Glide;

/**
 * Created by 骆长涛 on 2018/4/10.
 */

public class CommonItemLine extends RelativeLayout{
    private Context context;
    private int layout = R.layout.common_item;
    private FrameLayout frameLayout;
    private ImageView leftIcon,rightIcon,redPoint;
    private ColorStateList rightIconColor;
    private TextView mainTextView,subTextView;
    private String mainText,subText;
    private int mainTextInt,subTextInt,rightIconId;
    private boolean showRightIcon,showRedPoint;
    public CommonItemLine(Context context) {
        this(context,null);
    }
    public CommonItemLine(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }
    public CommonItemLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(layout,this,Boolean.TRUE);
        frameLayout = findViewById(R.id.left_icon_root);
        leftIcon = frameLayout.findViewById(R.id.left_icon);
        redPoint = frameLayout.findViewById(R.id.red_point);
        mainTextView = findViewById(R.id.main_text);
        subTextView =  findViewById(R.id.subText);
        rightIcon =findViewById(R.id.right_icon);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CommonItemLine);
        if (attributes != null) {

            mainText = attributes.getString(R.styleable.CommonItemLine_mainText);
            subText = attributes.getString(R.styleable.CommonItemLine_subText);
            showRightIcon = attributes.getBoolean(R.styleable.CommonItemLine_showRightIcon,false);
            showRedPoint = attributes.getBoolean(R.styleable.CommonItemLine_showRedPoint,false);
            if(!TextUtils.isEmpty(mainText))mainTextView.setText(mainText);
            if(!TextUtils.isEmpty(subText))subTextView.setText(subText);
            if(showRightIcon)rightIcon.setVisibility(VISIBLE);else rightIcon.setVisibility(INVISIBLE);
            if(showRedPoint)redPoint.setVisibility(VISIBLE);else redPoint.setVisibility(INVISIBLE);


            attributes.recycle();
        }
    }
    public  void setIntemStyle(ItemStyle itemStyle){
        if(itemStyle.getMainText()!=null )setMainText(itemStyle.getMainText());
        if(itemStyle.getMainTextInt()>0)setMainText(itemStyle.getMainTextInt());
        if(itemStyle.getSubText()!=null)setSubText(itemStyle.getSubText());
        if(itemStyle.getSubTextInt()>0)setSubText(itemStyle.getSubTextInt());
        showRightIcon(itemStyle.isShowRightIcon());
        showRedPoint(itemStyle.isShowRedPoint());
        if(itemStyle.getRightIconId()>0)setRightIconId(itemStyle.getRightIconId());
        if(itemStyle.getRightIconOnClickListenter()!=null)setRightIconOnClickListenter(itemStyle.getRightIconOnClickListenter());
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP
                && itemStyle.getRightIconColor()!=null)setRightIconColor(itemStyle.getRightIconColor());
    }
    /**测试数据*/
    public void showmsg(){
        //   Log.i("数据",mainText+"\t"+subText+"\t"+mainTextInt+"\t"+subTextInt);
    }
    /**设置主字符*/
    public void setMainText(String mainText) {
        this.mainText = mainText;
        mainTextView.setText(this.mainText);
    }
    /**设置主字符*/
    public void setMainText(int mainTextInt) {
        this.mainTextInt = mainTextInt;
        mainTextView.setText(this.mainTextInt);
    }
    /**设置下面说明*/
    public void setSubText(String subText) {
        this.subText = subText;
        subTextView.setText(this.subText);
    }
    /**设置下面说明*/
    public void setSubText(int subTextInt) {
        this.subTextInt = subTextInt;
        subTextView.setText(this.subTextInt);
    }
    /**显示右边图标*/
    public void showRightIcon(){
        rightIcon.setVisibility(VISIBLE);
    }
    public void showRightIcon(boolean show){
        if(show)rightIcon.setVisibility(VISIBLE);
        else rightIcon.setVisibility(INVISIBLE);
    }

    public ColorStateList getRightIconColor() {
        return rightIconColor;
    }
    @RequiresApi(21)
    public void setRightIconColor(ColorStateList rightIconColor) {
        this.rightIconColor = rightIconColor;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) rightIcon.setImageTintList(rightIconColor);
    }

    /**隐藏右边图标*/
    public void hideRightIcon(){
        rightIcon.setVisibility(INVISIBLE);
    }
    /**显示红点*/
    public void showRedPoint(){
        redPoint.setVisibility(VISIBLE);
    }
    public void showRedPoint(boolean show){
        if(show)redPoint.setVisibility(VISIBLE);
        else redPoint.setVisibility(INVISIBLE);
    }
    /**隐藏红点*/
    public void hideRedPoint(){
        redPoint.setVisibility(INVISIBLE);
    }


    public void setRightIconOnClickListenter(View.OnClickListener rightIconOnClickListenter ){
        rightIcon.setOnClickListener(rightIconOnClickListenter);
    }
    public void setRightIconId(int rightIconId) {
        this.rightIconId = rightIconId;
        Glide.with(this).load(this.rightIconId).into(rightIcon);
    }

    /**样式，方便使用代码生成*/
    public static class ItemStyle {
        private String mainText,subText;
        private int mainTextInt,subTextInt,rightIconId;
        private ColorStateList rightIconColor;
        private boolean showRightIcon,showRedPoint;
        private View.OnClickListener rightIconOnClickListenter;
        public ItemStyle() { }
        public ItemStyle(int mainTextInt, int subTextInt, boolean showRightIcon, boolean showRedPoint) {
            this.mainTextInt = mainTextInt;
            this.subTextInt = subTextInt;
            this.showRightIcon = showRightIcon;
            this.showRedPoint = showRedPoint;
        }

        public ItemStyle(String mainText, String subText, boolean showRightIcon, boolean showRedPoint) {
            this.mainText = mainText;
            this.subText = subText;
            this.showRightIcon = showRightIcon;
            this.showRedPoint = showRedPoint;
        }

        public ItemStyle(String mainText, int subTextInt, boolean showRightIcon, boolean showRedPoint, OnClickListener rightIconOnClickListenter) {
            this.mainText = mainText;
            this.subTextInt = subTextInt;
            this.showRightIcon = showRightIcon;
            this.showRedPoint = showRedPoint;
            this.rightIconOnClickListenter = rightIconOnClickListenter;
        }
        public ItemStyle(String mainText, int subTextInt, boolean showRightIcon, boolean showRedPoint) {
            this.mainText = mainText;
            this.subTextInt = subTextInt;
            this.showRightIcon = showRightIcon;
            this.showRedPoint = showRedPoint;
        }
        public void setMainText(String mainText) {
            this.mainText = mainText;
        }

        public void setSubText(String subText) {
            this.subText = subText;
        }

        public void setMainTextInt(int mainTextInt) {
            this.mainTextInt = mainTextInt;
        }

        public void setSubTextInt(int subTextInt) {
            this.subTextInt = subTextInt;
        }

        public void setShowRightIcon(boolean showRightIcon) {
            this.showRightIcon = showRightIcon;
        }

        public void setShowRedPoint(boolean showRedPoint) {
            this.showRedPoint = showRedPoint;
        }

        public String getMainText() {
            return mainText;
        }

        public String getSubText() {
            return subText;
        }

        public int getMainTextInt() {
            return mainTextInt;
        }

        public int getSubTextInt() {
            return subTextInt;
        }

        public boolean isShowRightIcon() {
            return showRightIcon;
        }

        public boolean isShowRedPoint() {
            return showRedPoint;
        }
        public OnClickListener getRightIconOnClickListenter() {
            return rightIconOnClickListenter;
        }
        public void setRightIconOnClickListenter(OnClickListener rightIconOnClickListenter) {
            this.rightIconOnClickListenter = rightIconOnClickListenter;
        }

        public int getRightIconId() {
            return rightIconId;
        }

        public void setRightIconId(int rightIconId) {
            this.rightIconId = rightIconId;
        }

        public ColorStateList getRightIconColor() {
            return rightIconColor;
        }

        public void setRightIconColor(ColorStateList rightIconColor) {
            this.rightIconColor = rightIconColor;
        }
    }

}
