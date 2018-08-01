package com.blozi.bindtags.activities.fragment.mainTab;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.blozi.bindtags.R;
import com.blozi.bindtags.activities.MainActivity;
import com.blozi.bindtags.activities.MainActivityLocal;
import com.blozi.bindtags.activities.fragment.BaseFragment;
import com.blozi.bindtags.adapter.FragmentRecyclerViewAdapter;
import com.blozi.bindtags.application.Global;
import com.blozi.bindtags.asyncTask.local.LocalDeskSocket;
import com.blozi.bindtags.asyncTask.local.LocalNetBaseTask;
import com.blozi.bindtags.asyncTask.online.ShowGoodsInfoTask;
import com.blozi.bindtags.asyncTask.online.UpdateGoodsInfoAsyncTask;
import com.blozi.bindtags.util.LoadingDialog;
import com.blozi.bindtags.util.SystemConstants;
import com.blozi.bindtags.view.CommonEditTextLine;
import com.blozi.bindtags.view.CommonItemLine;
import com.zxing_new.activity.CodeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 骆长涛 on 2018/3/29.
 */

public class GoodInfoManage extends BaseFragment {
    private static final String tag = "GoodInfoManage", GOODINFOID = "goodsInfoId";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String goodscode = null, goodsInfoId = null;
    private JSONObject goodsInfo = null, goodsInfoEdit = new JSONObject();
    private RecyclerView mainView;
    private FragmentRecyclerViewAdapter mainViewAdapter;
    private LinearLayoutManager mPagerLayoutManager;
    protected Activity context = getActivity();
    private FloatingActionButton scanFloatBut, searchFloatBut, float_save, float_reset;
    private UpdateGoodsInfoAsyncTask updateGoodsInfoAsyncTask;
    private final int[] columnNamesLocal = {R.string.barcode, R.string.good_name, R.string.goods_price, R.string.PromotionPrice};
    private final String[] columnsLocal = {"goodsBarcode", "goodsName", "goodsPrice", "promotionPrice",};
    private final boolean[] editAbleLocal = {false, false, true, true};
    private final boolean[] allowNullLocal = {false, false, false, false};
    private final int[] inputTypeLocal = {
            InputType.TYPE_CLASS_TEXT,
            InputType.TYPE_CLASS_TEXT,
            InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL,
            InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
    };

    private final int[] columnNamesOnline = {R.string.barcode, R.string.good_name, R.string.brand, R.string.goods_price, R.string.MembershipPrice, R.string.PromotionPrice, R.string.DiscountPrice, R.string.Stock, R.string.QRcode, R.string.ActivityStartTime, R.string.ActivityEndTime, R.string.Grade};
    private final String[] columnsOnline = {"goodsBarcode", "goodsName", "brand", "goodsPrice", "memberPrice", "promotionPrice", "discountPrice", "stock", "goodsQrcode", "startTime", "endTime", "level"};
    private final boolean[] editAbleOnline = {false, true, true, true, true, true, true, true, true, true, true, true};
    private final boolean[] allowNullOnline = {false, false, true, false, true, true, true, true, true, true, true, true};
    private final int[] inputTypeOnline = {
            InputType.TYPE_CLASS_TEXT,
            InputType.TYPE_CLASS_TEXT,
            InputType.TYPE_CLASS_TEXT,
            InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL,
            InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL,
            InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL,
            InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL,
            InputType.TYPE_CLASS_TEXT,
            InputType.TYPE_CLASS_TEXT,
            InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_NORMAL,
            InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_NORMAL,
            InputType.TYPE_CLASS_TEXT
    };
    private int[] columnNames = null;
    private String[] columns = null;
    private boolean[] editAble = null;
    private boolean[] allowNull = null;
    private int[] inputType = null;
    private int preYear = 2018, preMonth = 1, preDay = 1, preHour = 1, preMin = 1, preSecond = 0;
    private String[] preValues = null;
    private String[] newvalues = null;
    private CommonEditTextLine.EditTextLineStyle[] edits = null;
    private CommonItemLine.ItemStyle[] items = null;
    private CommonItemLine.ItemStyle currentEdit;
    private int currentEditIndex;
    private boolean editable = false;
    private boolean isLocal = false;

    /*缓存数据*/
    private List recycleCach;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_goods_info, null);
        if (getArguments() != null) {
            isLocal = getArguments().getBoolean("isLocal");
        }
        if (isLocal) {
            columnNames = columnNamesLocal;
            columns = columnsLocal;
            editAble = editAbleLocal;
            allowNull = allowNullLocal;
            inputType = inputTypeLocal;
        } else {
            columnNames = columnNamesOnline;
            columns = columnsOnline;
            editAble = editAbleOnline;
            allowNull = allowNullOnline;
            inputType = inputTypeOnline;
        }
        preValues = new String[columns.length];
        newvalues = new String[columns.length];
        edits = new CommonEditTextLine.EditTextLineStyle[columns.length];
        items = new CommonItemLine.ItemStyle[columns.length];
        mainView = view.findViewById(R.id.good_info_manage_recyclerView);
        float_save = view.findViewById(R.id.float_save);
        float_reset = view.findViewById(R.id.float_reset);
        scanFloatBut = view.findViewById(R.id.scan);
        searchFloatBut = view.findViewById(R.id.search);
        mainView.setHasFixedSize(true);
        mPagerLayoutManager = new LinearLayoutManager(context);
        mPagerLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mainView.setLayoutManager(mPagerLayoutManager);

        float_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (currentEdit != null && currentEditIndex != -1) {
                        changeMainView(currentEdit, currentEditIndex);
                    }
                    confirmUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        float_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder comfirmReset = new AlertDialog.Builder(context)
                        .setTitle(R.string.ifReset)
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.reset, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setGoodsInfo(goodsInfo, editable);
                                newvalues = new String[columns.length];
                            }
                        });
                comfirmReset.create().show();
            }
        });
        searchFloatBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGoodsInfo();
            }
        });
        scanFloatBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCaptureScan(SystemConstants.REQUEST_CODE);
//                Intent openCameraIntent = new Intent(context, CaptureActivity.class);
//                startActivityForResult(openCameraIntent, SystemConstants.REQUEST_CODE);
            }
        });
        initContent(view);

        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
        recycleCach = mainViewAdapter.getDatas();
    }

    /**
     * 初始化内容
     */
    private void initContent(View view) {
        if (recycleCach != null && !TextUtils.isEmpty(this.goodsInfoId)) {
            Log.i("商品门店", goodscode);
            mainViewAdapter.setDatas(recycleCach);
            mainView.setAdapter(mainViewAdapter);
            scanFloatBut.hide();
        } else {
            mainViewAdapter = new FragmentRecyclerViewAdapter(initStarData(this.goodsInfoId), context);
            mainView.setAdapter(mainViewAdapter);
        }
    }

    private void getGoodsInfo() {
        if (!TextUtils.isEmpty(goodscode)) {
            LoadingDialog.INSTANCE.alertDialog(context, R.string.GettingGoodsInformation);
            if (context instanceof MainActivity) {
                ShowGoodsInfoTask showGoodsInfoService = new ShowGoodsInfoTask(context, bloziPreferenceManager.getCompliteURL());
                showGoodsInfoService.execute(userSession, userPass, goodscode, Global.curryentStore.get(Global.STOREID), Boolean.TRUE.toString());
            }
        } else {
            if (context != null)
                Toast.makeText(context, R.string.GoodsBarCodeCannotBeEmpty, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 初始化界面的数据
     */
    private List initStarData(String goodsBarcode) {
        List list = new ArrayList();
        list.add(FragmentRecyclerViewAdapter.WhiteEmpty);
        CommonEditTextLine.EditTextLineStyle goodsCodeInput = new CommonEditTextLine.EditTextLineStyle();
        if(context instanceof  MainActivityLocal )goodsCodeInput.setHint(R.string.tag_code);
        else goodsCodeInput.setHint(R.string.goods_code);
        goodsCodeInput.setEditText(goodsBarcode);
        TextWatcher goodsCodeWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !TextUtils.isEmpty(s.toString())) {
                    goodscode = s.toString();
                    scanFloatBut.hide();
                    searchFloatBut.show();
                } else if (s == null || TextUtils.isEmpty(s.toString())) {
                    scanFloatBut.show();
                    searchFloatBut.hide();
                    goodscode = "";
                }
            }
        };
        goodsCodeInput.setTextWatcher(goodsCodeWatcher);
        if (bloziPreferenceManager.getEditable()) {
            list.add(goodsCodeInput);
        }
        list.add(FragmentRecyclerViewAdapter.WhiteEmpty);
        return list;
    }

    /**
     * 摄像头扫码结果处理
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("处理扫描结果", "" + context.getClass().getName() + "\t" + requestCode + "\t" + resultCode);
        if (requestCode == SystemConstants.REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                Log.i("处理扫描结果", "" + context.getClass().getName() + "\t" + requestCode + "\t" + resultCode);
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    if (context instanceof MainActivity) {
                        LoadingDialog.INSTANCE.alertDialog(context, R.string.GettingGoodsInformation);
                        ShowGoodsInfoTask showGoodsInfoService = new ShowGoodsInfoTask(context, bloziPreferenceManager.getCompliteURL());
                        showGoodsInfoService.execute(userSession, userPass, result, Global.curryentStore.get(Global.STOREID), Boolean.TRUE.toString());
                    } else if (context instanceof MainActivityLocal) {
                        LoadingDialog.INSTANCE.alertDialog(context, R.string.GettingGoodsInformation);
                        LocalNetBaseTask localNetBaseTask = new LocalNetBaseTask(context);
                        localNetBaseTask.execute(LocalDeskSocket.Companion.getRequestCode(LocalDeskSocket.RequestCode.GetGoodsInfo, result), result);
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(context, R.string.ParsingFailure, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    public void regetGoodsInfo(){
        if (context instanceof MainActivity) {
            ShowGoodsInfoTask showGoodsInfoService = new ShowGoodsInfoTask(context, bloziPreferenceManager.getCompliteURL());
            showGoodsInfoService.execute(userSession, userPass, goodsInfoId, Global.curryentStore.get(Global.STOREID), Boolean.TRUE.toString());
        } else if (context instanceof MainActivityLocal) {
            LoadingDialog.INSTANCE.alertDialog(context, R.string.GettingGoodsInformation);
            LocalNetBaseTask localNetBaseTask = new LocalNetBaseTask(context);
            localNetBaseTask.execute(LocalDeskSocket.Companion.getRequestCode(LocalDeskSocket.RequestCode.GetGoodsInfo, goodsInfoId), goodsInfoId);
        }
    }
    /**
     * 得到商品数据后显示
     */
    public void setGoodsInfo(final JSONObject goodsInfo, boolean editable) {
        this.goodsInfo = goodsInfo;
        this.editable = editable;
        Log.i("goodsInfo", goodsInfo.toString());
        scanFloatBut.hide();
        searchFloatBut.hide();
        currentEdit = null;
        currentEditIndex = -1;
        edits = new CommonEditTextLine.EditTextLineStyle[columns.length];
        items = new CommonItemLine.ItemStyle[columns.length];
        //mainView.setItemViewCacheSize(goodsInfo.length());
        if (this.goodsInfo != null && this.goodsInfo.length() > 0) {
            try {
                if (TextUtils.isEmpty(this.goodsInfo.getString("goodsInfoId"))) {
                    return;
                } else {
                    this.goodsInfoId = this.goodsInfo.getString("goodsInfoId");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
          //  Iterator<String> keys = this.goodsInfo.keys();
            List<CommonItemLine.ItemStyle> infoListView = new ArrayList<>();
            for (int i = 0; i < columns.length; i++) {
                String key = columns[i];
                String value = null;
                try {
                    value = this.goodsInfo.getString(key);
                    if ("goodsBarcode".equals(key)) this.goodscode = value;
                } catch (JSONException e) {
                    //e.printStackTrace();
                    value = "";
                }
                final int fi = i;
                preValues[i] = value;
                newvalues[i] = value;
                if (items[i] == null) {
                    items[i] = new CommonItemLine.ItemStyle(value, columnNames[i], true, false);
                }
                items[i].setMainText(value);
                items[i].setShowRightIcon(editAble[i]);
                if (editAble[i]) {
                    View.OnClickListener editClick = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            exitEdit();
                            if (inputType[fi] == (InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_NORMAL)) {
                                Date preDate = new Date();
                                final Calendar c = Calendar.getInstance();
                                if (!TextUtils.isEmpty(preValues[fi])) {
                                    try {
                                        preDate = simpleDateFormat.parse(preValues[fi]);
                                    } catch (ParseException e) {
                                    }
                                    c.setTime(preDate);
                                    preYear = c.get(Calendar.YEAR);
                                    preMonth = c.get(Calendar.MONTH);
                                    preDay = c.get(Calendar.DAY_OF_MONTH);
                                    preHour = c.get(Calendar.HOUR_OF_DAY);
                                    preMin = c.get(Calendar.MINUTE);
                                    preSecond = c.get(Calendar.SECOND);
                                }
                                final DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                                        new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                                c.set(year, month, dayOfMonth, preHour, preMin, preSecond);
                                                preYear = c.get(Calendar.YEAR);
                                                preMonth = c.get(Calendar.MONTH);
                                                preDay = c.get(Calendar.DAY_OF_MONTH);
                                            }
                                        },
                                        preYear, preMonth, preDay);
                                datePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        TimePickerDialog timePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                c.set(preYear, preMonth, preDay, hourOfDay, minute, preSecond);
                                                newvalues[fi] = simpleDateFormat.format(c.getTime());
                                                items[fi].setMainText(newvalues[fi]);
                                                items[fi].setShowRedPoint(false);
                                                changeMainView(items[fi], fi);
                                            }
                                        }, preHour, preMin, true
                                        );
                                        timePicker.show();
                                    }
                                });
                                datePickerDialog.show();
                            } else {
                                currentEdit = items[fi];
                                currentEditIndex = fi;
                                if (edits[fi] == null)
                                    edits[fi] = new CommonEditTextLine.EditTextLineStyle();
                                if (allowNull[fi]) edits[fi].setAllownNull(allowNull[fi]);
                                edits[fi].setInputType(inputType[fi]);
                                if (!TextUtils.isEmpty(items[fi].getMainText()))
                                    edits[fi].setEditText(items[fi].getMainText());
                                else edits[fi].setEditTextInt(items[fi].getMainTextInt());
                                if (!TextUtils.isEmpty(items[fi].getSubText()))
                                    edits[fi].setSubText(items[fi].getSubText());
                                else edits[fi].setSubTextInt(items[fi].getSubTextInt());
                                edits[fi].setHint(columnNames[fi]);
                                final TextWatcher goodsCodeWatcher = new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) { }
                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        newvalues[currentEditIndex] = s.toString();
                                        Log.i("值"+fi,preValues[fi]+"\t"+newvalues[fi]);
                                    }
                                };
                                edits[fi].setTextWatcher(goodsCodeWatcher);
                                edits[fi].setRightImageResourceId(R.drawable.checked_blue);
                                edits[fi].setRightImageClick(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        items[fi].setMainText(newvalues[fi]);
                                        if (preValues[fi] != null && newvalues[fi] != null && !preValues[fi].equals(newvalues[fi])) {
                                            float_save.setVisibility(View.VISIBLE);
                                            //       float_reset.setVisibility(View.VISIBLE);
                                            items[fi].setShowRedPoint(true);
                                        } else if (TextUtils.isEmpty(preValues[fi]) && TextUtils.isEmpty(newvalues[fi])) {
                                            items[fi].setShowRedPoint(false);
                                        }
                                        currentEdit = null;
                                        currentEditIndex = -1;
                                        changeMainView(items[fi], fi);
                                    }
                                });
                                changeMainView(edits[fi], fi);
                            }

                        }
                    };
                    if (editable) items[i].setRightIconOnClickListenter(editClick);
                    else items[i].setShowRightIcon(editable);
                }
                infoListView.add(items[i]);
            }
            mainViewAdapter.setDatas(infoListView);
            mainViewAdapter.add(FragmentRecyclerViewAdapter.WhiteEmpty, infoListView.size());
            mainViewAdapter.notifyDataSetChanged();
        } else {
            mainViewAdapter = new FragmentRecyclerViewAdapter(initStarData(null), context);
            mainView.setAdapter(mainViewAdapter);
            scanFloatBut.show();
            searchFloatBut.hide();
        }
    }

    public void initStar(String barcode) {
        if (barcode == null) barcode = "";
//        Log.i("商品信息修改", barcode);
        mainViewAdapter = new FragmentRecyclerViewAdapter(initStarData(barcode), context);
        mainView.setAdapter(mainViewAdapter);
        scanFloatBut.show();
        searchFloatBut.hide();
    }

    /**
     * 退出编辑
     */
    public boolean exitEdit() {
        if (currentEdit != null && currentEditIndex > -1) {
            mainViewAdapter.changeData(currentEdit, currentEditIndex);
            currentEdit = null;
            currentEditIndex = -1;
            return true;
        }
        return false;
    }

    private void changeMainView(Object o, int i) {
        mainViewAdapter.changeData(o, i);
    }

    /**
     * 提交前确认
     */
    private void confirmUpdate() throws JSONException {
        final JSONObject jb = new JSONObject();
        StringBuffer sb = new StringBuffer();
        if (!isLocal) {
            Date start = null;
            Date end = null;
            int errorMsg = -1;
            for (int i = 0; i < columnNames.length; i++) {
                if (preValues[i] != null && newvalues[i] != null && !preValues[i].equals(newvalues[i])) {
                    if ("startTime".equals(columns[i]) && !TextUtils.isEmpty(newvalues[i])) {
                        try {
                            start = simpleDateFormat.parse(newvalues[i]);
                        } catch (Exception e) {
                            errorMsg = R.string.ActivityStartTimeError;
                            break;
                        }
                    } else if ("endTime".equals(columns[i]) && !TextUtils.isEmpty(newvalues[i])) {
                        try {
                            end = simpleDateFormat.parse(newvalues[i]);
                        } catch (Exception e) {
                            errorMsg = R.string.ActivityEndTimeError;
                            break;
                        }
                    }
                    sb.append(getResources().getString(columnNames[i])).append(" : ").append(newvalues[i]).append("\n");
                    jb.put(columns[i], newvalues[i]);
                }
            }
            if (errorMsg != -1) {
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                if (start == null && !TextUtils.isEmpty(preValues[9]))
                    start = simpleDateFormat.parse(preValues[9]);
                if (end == null && !TextUtils.isEmpty(preValues[10]))
                    end = simpleDateFormat.parse(preValues[10]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            for (int i = 0; i < columnNames.length; i++) {
                if (preValues[i] != null && newvalues[i] != null && !preValues[i].equals(newvalues[i])) {
                    sb.append(getResources().getString(columnNames[i])).append(" : ").append(newvalues[i]).append("\n");
                    jb.put(columns[i], newvalues[i]);
                }
            }
        }
        if (jb.length() == 0) {
            Toast.makeText(context, R.string.NoDataThatNeedsToBeSubmitted, Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder ab = new AlertDialog.Builder(context).setMessage(sb.toString()).setTitle(R.string.confirm).setPositiveButton(R.string.Submission, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {

                    if ( !isLocal &&   !TextUtils.isEmpty(GoodInfoManage.this.goodsInfoId) && bloziPreferenceManager.isAdminPlus()) {
                        LoadingDialog.INSTANCE.alertDialog(context);
                        updateGoodsInfoAsyncTask = new UpdateGoodsInfoAsyncTask(context, bloziPreferenceManager.getCompliteURL());
                        updateGoodsInfoAsyncTask.execute(GoodInfoManage.this.goodsInfoId, jb.toString());
                    }
                    else if(isLocal) {
//                        LoadingDialog.INSTANCE.alertDialog(context);
                        LocalNetBaseTask localNetBaseTask = new LocalNetBaseTask(context);
                        localNetBaseTask.execute(LocalDeskSocket.Companion.getRequestCode(LocalDeskSocket.RequestCode.UpdateGoodsInfo, newvalues[0],newvalues[2],newvalues[3] ) );
                    }
                    else {
                        Toast.makeText(GoodInfoManage.this.context, "错误", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).setNegativeButton(R.string.cancel, null);
        ab.create().show();
    }

    @Override
    public void onDestroy() {
        if (updateGoodsInfoAsyncTask != null) {
            updateGoodsInfoAsyncTask.cancelHttpPost();
            updateGoodsInfoAsyncTask.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    public void editableToggle() {
        if (this.goodsInfo == null) {
            initStar(null);
        }
    }

    @Override
    public boolean isForceTag() {
        return false;
    }

    @Override
    public void accetpMsg(String str) {

    }
}
