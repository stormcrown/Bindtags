package com.blozi.bindtags.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blozi.bindtags.R;

/**
 * Created by 骆长涛 on 2018/4/10.
 */

public class CommonEditTextLine extends RelativeLayout {
    private EditText editTextView;
    private TextView subTextView;
    private ImageView leftImage,rightImage,redPoint;
    private String hint,subText ,editText;
    private int hint_int,subTextInt,imeOptions,editTextInt;
    private int layout = R.layout.simple_edit_text;
    private RelativeLayout root;
    private Context context;
    private TextWatcher defaultEditTextChangeListenter ;
    public CommonEditTextLine(Context context) {
        this(context, null);
    }
    public CommonEditTextLine(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public CommonEditTextLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LayoutInflater.from(context).inflate(layout,this,Boolean.TRUE);
        root = findViewById(R.id.simple_edit_text_root);
        editTextView = findViewById(R.id.editText);
        subTextView = findViewById(R.id.subText);
        leftImage =findViewById(R.id.left_icon);
        rightImage = findViewById(R.id.right_icon);
        redPoint = findViewById(R.id.red_point);
        defaultEditTextChangeListenter = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(s!=null && !TextUtils.isEmpty(s.toString()))rightImage.setVisibility(VISIBLE);
                else rightImage.setVisibility(INVISIBLE);
            }
        };
        editTextView.addTextChangedListener(defaultEditTextChangeListenter);
        rightImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextView.setText(null);
            }
        });
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CommonEditTextLine);
        if (attributes != null) {
            hint = attributes.getString(R.styleable.CommonEditTextLine_hint);
            if(!TextUtils.isEmpty(hint))editTextView.setHint(hint);
            subText =attributes.getString(R.styleable.CommonEditTextLine_subText1);
            subTextView.setText(subText);
            editText = attributes.getString(R.styleable.CommonEditTextLine_editText);
            editTextView.setText(editText);

            attributes.recycle();
        }

    }
    public void removeDefaultEditTextListenter(){
        editTextView.removeTextChangedListener(defaultEditTextChangeListenter);
    }
    public void showRightImage(){
        rightImage.setVisibility(VISIBLE);
    }
    public void setLeftImageResource(int id){
        leftImage.setImageResource(id);
    }
    public void setRightImageResource(int id){
        rightImage.setImageResource(id);
    }
    public void setLeftImageOnClickListenter(View.OnClickListener leftImageOnClickListenter){
        leftImage.setOnClickListener(leftImageOnClickListenter);
    }
    public void setRightImageOnClickListenter(View.OnClickListener leftImageOnClickListenter){
        rightImage.setOnClickListener(leftImageOnClickListenter);
    }
    public void setEditTextLineStyle(EditTextLineStyle style){
        if(style.getHint()!=null)setHint(style.getHint());
        if(style.getHintInt()!=0)setHint(style.getHintInt());
        if(style.getSubText()!=null)setSubText(style.getSubText());
        if(style.getSubTextInt()!=0)setSubText(style.getSubTextInt());
        if(style.getImeOptions()!=0)setImeOptions(style.getImeOptions());
        if(style.getTextWatcher()!=null)editTextView.addTextChangedListener(style.getTextWatcher());
        if(style.getLeftImageResourceId()!=0)leftImage.setImageResource(style.getLeftImageResourceId());
        if(style.getLeftImageClick()!=null )leftImage.setOnClickListener(style.getLeftImageClick());
        if(style.getEditText()!=null)setEditText(style.getEditText());
        if(style.getEditTextInt()!=0)setEditText(style.getEditTextInt());
        if(style.getRightImageResourceId()!=0)rightImage.setImageResource(style.getRightImageResourceId());
        if(style.getRightImageClick()!=null)rightImage.setOnClickListener(style.getRightImageClick());
        setInputType(style.getInputType());
        if(style.isAllownNull())allowNull();
    }
    public static class EditTextLineStyle{
        private String hint,subText,editText;
        private int hintInt,subTextInt,imeOptions ,leftImageResourceId,rightImageResourceId,editTextInt,inputType=InputType.TYPE_CLASS_TEXT;
        private TextWatcher textWatcher;
        private View.OnClickListener leftImageClick,rightImageClick;
        private boolean showRightImage,allownNull;
        public EditTextLineStyle() {
        }

        public EditTextLineStyle(int hintInt, int subTextInt, String imeOptions) {
            this.hintInt = hintInt;
            this.subTextInt = subTextInt;
        }


        public EditTextLineStyle(String subText) {
            this.subText = subText;
        }

        public EditTextLineStyle(int subTextInt) {
            this.subTextInt = subTextInt;
        }

        public int getImeOptions() {
            return imeOptions;
        }

        public void setImeOptions(int imeOptions) {
            this.imeOptions = imeOptions;
        }

        public String getHint() {
            return hint;
        }

        public String getSubText() {
            return subText;
        }

        public int getHintInt() {
            return hintInt;
        }

        public int getSubTextInt() {
            return subTextInt;
        }

        public void setHint(String hint) {
            this.hint = hint;
        }
        public void setHint(int hint) {
            this.hintInt = hint;
        }
        public void setSubText(String subText) {
            this.subText = subText;
        }


        public void setSubTextInt(int subTextInt) {
            this.subTextInt = subTextInt;
        }

        public void setTextWatcher(TextWatcher textWatcher) {
            this.textWatcher = textWatcher;
        }

        public TextWatcher getTextWatcher() {
            return textWatcher;
        }

        public int getLeftImageResourceId() {
            return leftImageResourceId;
        }
        public void setLeftImageResourceId(int leftImageResourceId) {
            this.leftImageResourceId = leftImageResourceId;
        }

        public OnClickListener getLeftImageClick() {
            return leftImageClick;
        }

        public void setLeftImageClick(OnClickListener leftImageClick) {
            this.leftImageClick = leftImageClick;
        }

        public String getEditText() {
            return editText;
        }

        public int getEditTextInt() {
            return editTextInt;
        }

        public void setEditText(String editText) {
            this.editText = editText;
        }

        public void setEditTextInt(int editTextInt) {
            this.editTextInt = editTextInt;
        }

        public OnClickListener getRightImageClick() {
            return rightImageClick;
        }

        public void setRightImageClick(OnClickListener rightImageClick) {
            this.rightImageClick = rightImageClick;
        }

        public int getRightImageResourceId() {
            return rightImageResourceId;
        }

        public void setRightImageResourceId(int rightImageResourceId) {
            this.rightImageResourceId = rightImageResourceId;
        }

        public boolean isShowRightImage() {
            return showRightImage;
        }

        public void setShowRightImage(boolean showRightImage) {
            this.showRightImage = showRightImage;
        }

        public int getInputType() {
            return inputType;
        }
        public void setInputType(int inputType) {
            this.inputType = inputType;
        }

        public boolean isAllownNull() {
            return allownNull;
        }

        public void setAllownNull(boolean allownNull) {
            this.allownNull = allownNull;
        }
    }

    public void setHint(String hint) {
        this.hint = hint;
        this.editTextView.setHint(hint);
    }
    public void setHint(int hint) {
        this.hint_int = hint;
        this.editTextView.setHint(hint_int);
    }
    public void setSubText(String subText) {
        this.subText = subText;
        this.subTextView.setText(subText);
    }
    public void setSubText(int subTextInt) {
        this.subTextInt = subTextInt;
        this.subTextView.setText(subTextInt);
    }

    public void setEditText(String editText) {
        this.editText = editText;
        this.editTextView.setText(editText);
    }
    public void setEditText(int editText) {
        this.editTextInt = editText;
        this.editTextView.setText(editText);
    }
    public String getEditText(){
        return editTextView.getText().toString();
    }
    public void addTextChangedListener(TextWatcher watcher){
        editTextView.addTextChangedListener(watcher);
    }

    public void setImeOptions(int editorIme){
        editTextView.setImeOptions(editorIme);
    }

    @Override
    public void setBackgroundColor(int color){
        root.setBackgroundColor(color);
    };
    public void setInputType(int inputType){
        editTextView.setInputType(inputType);
    }
    public void allowNull(){
        editTextView.removeTextChangedListener(defaultEditTextChangeListenter);
        rightImage.setVisibility(VISIBLE);
    }
}
