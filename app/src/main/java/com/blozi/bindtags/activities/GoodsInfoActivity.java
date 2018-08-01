package com.blozi.bindtags.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.blozi.bindtags.R;
import com.blozi.bindtags.adapter.FragmentRecyclerViewAdapter;
import com.blozi.bindtags.application.Global;
import com.blozi.bindtags.asyncTask.online.GetGoodsImagesAsyncTask;
import com.blozi.bindtags.asyncTask.online.ShowGoodsInfoTask;
import com.blozi.bindtags.asyncTask.online.UpdateGoodsInfoAsyncTask;
import com.blozi.bindtags.util.BLOZIPreferenceManager;
import com.blozi.bindtags.util.ScanUtil;
import com.blozi.bindtags.util.SystemConstants;
import com.blozi.bindtags.util.SystemMathod;
import com.blozi.bindtags.view.CommonEditTextLine;
import com.blozi.bindtags.view.CommonItemLine;
import com.bumptech.glide.Glide;
import com.zxing_new.activity.CaptureActivity;
import com.zxing_new.activity.CodeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.blozi.bindtags.util.SystemConstants.GOODSIMAGEMaxLength;
import static com.blozi.bindtags.util.SystemConstants.GOODSIMAGEMaxLength_M;
import static com.blozi.bindtags.util.SystemConstants.SELECT_PHOTO;

public class GoodsInfoActivity extends AppCompatActivity {
    private static final String TAG = GoodsInfoActivity.class.getName(), GOODINFOID = "goodsInfoId";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String goodscode = null, goodsInfoId = null;
    private JSONObject goodsInfo = null, goodsInfoEdit = new JSONObject();
    private AppBarLayout appBar;
    private CollapsingToolbarLayout collapsingToolbar;
    private Toolbar toolbar;
    private ScanUtil scanUtil;

    public ScanUtil getScanUtil() {
        return scanUtil;
    }

    private FloatingActionButton scanFloatBut, searchFloatBut, float_save, float_reset, float_appbarbatton;
    private RecyclerView mainView;
    private FragmentRecyclerViewAdapter mainViewAdapter;
    private LinearLayoutManager mainViewManager;
    //    private GifImageView top_image;
    private ImageView top_image;
    private Menu menu;
    private MenuItem ok, no, scan, showImages;
    private List<CommonItemLine> infoListView;
    private String[] goodImagesIds = null;
    private String[] pregoodImagesIds = null;
    private UpdateGoodsInfoAsyncTask updateGoodsInfoAsyncTask;
    private final List<GetGoodsImagesAsyncTask> getGoodsImagesAsyncTasks = new ArrayList<>();
    private final List<String> addonGoodsImagePath = new ArrayList<>();
    private boolean pressBack = false;

    private synchronized void cancelGetGoodImageAsyncTasks() {
        if (getGoodsImagesAsyncTasks != null) {
            for (int i = 0; i < getGoodsImagesAsyncTasks.size(); i++) {
                if (getGoodsImagesAsyncTasks.size() > i && getGoodsImagesAsyncTasks.get(i) != null) {
                    getGoodsImagesAsyncTasks.get(i).cancelHttpPost();
                    getGoodsImagesAsyncTasks.get(i).cancel(true);
                }
            }
            getGoodsImagesAsyncTasks.clear();
        }
    }

    private synchronized void clearAddonGoodsImagePath() {
        addonGoodsImagePath.clear();
    }

    private synchronized void AddAddonGoodsImagePath(String path) {
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (file != null && file.exists() && file.length() < SystemConstants.GOODSIMAGEMaxLength) {
                addonGoodsImagePath.add(path);
                float_save.show();
            } else {
                Toast.makeText(this, String.format(getResources().getString(R.string.GoodsImageisLargeThanORDonotExist), GOODSIMAGEMaxLength_M), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, R.string.failed, Toast.LENGTH_LONG).show();
        }

    }

    /**
     * 获取商品信息后，将商品图片的ID保存在这里，并且自动取消上一个图片的获取图片的操作，然后根据ID发起新的获取图片的线程。
     */
    public void setGoodImagesIds(String[] goodImagesIds) {
        this.goodImagesIds = goodImagesIds;
        this.pregoodImagesIds = goodImagesIds;
        cancelGetGoodImageAsyncTasks();     // 清空获取图片的请求
        clearImagePaths();  // 清空图片路径
        changeImage.removeCallbacks(r);
        if (this.goodImagesIds != null) {
            if (this.top_image != null) {
                Glide.with(this).clear(top_image);
                Glide.with(this).load(R.drawable.nopic).into(top_image);
            }
        } else {
            if (this.top_image != null) {
                Glide.with(this).clear(top_image);
                Glide.with(this).load(R.drawable.nopic).into(top_image);
            }
        }
    }

    /**
     * 图片的路径
     */
    private List<String> goodsImagePaths = new ArrayList<>();

    public synchronized void addImagePath(String path) {
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (file != null && file.exists() && file.length() < SystemConstants.GOODSIMAGEMaxLength) {
                goodsImagePaths.add(path);
            } else {
                Toast.makeText(this, String.format(getResources().getString(R.string.GoodsImageisLargeThanORDonotExist), GOODSIMAGEMaxLength_M), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, R.string.failed, Toast.LENGTH_LONG).show();
        }

    }

    /**
     * ！！！ 清空图片路径
     */
    private synchronized void clearImagePaths() {
        goodsImagePaths.clear();
    }

    /**
     * 循环更新图片
     */
    private int imageIndex = 0;
    private Handler changeImage = new Handler();
    private Date date = new Date();
    private Runnable r = new Runnable() {
        @Override
        public void run() {
            Log.i("循环执行", imageIndex + "");
            Log.i("循环执行", "" + (new Date().getTime() - date.getTime()));
            date = new Date();
            if (goodsImagePaths.size() > 0) {
                if (imageIndex >= goodsImagePaths.size()) {
                    imageIndex = 0;
                }
                displayImage(goodsImagePaths.get(imageIndex));
                imageIndex++;
            }
            changeImage.postDelayed(r, 5000);
        }
    };

    //private final String[] columnNames = {"条形码", "商品名称", "品牌", "商品价格", "会员价", "促销价", "折扣价", "库存", "二维码", "活动开始时间：格式 年-月-日 时:分:秒", "活动结束时间：格式 年-月-日 时:分:秒", "品级"};
    private final int[] columnNames = {R.string.barcode, R.string.good_name, R.string.brand, R.string.goods_price, R.string.MembershipPrice, R.string.PromotionPrice, R.string.DiscountPrice, R.string.Stock, R.string.QRcode, R.string.ActivityStartTime, R.string.ActivityEndTime, R.string.Grade};
    private final String[] columns = {"goodsBarcode", "goodsName", "brand", "goodsPrice", "memberPrice", "promotionPrice", "discountPrice", "stock", "goodsQrcode", "startTime", "endTime", "level"};
    private final boolean[] editAble = {false, true, true, true, true, true, true, true, true, true, true, true};
    private final boolean[] allowNull = {false, false, true, false, true, true, true, true, true, true, true, true};
    private final int[] inputType = {
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
    int preYear = 2018, preMonth = 1, preDay = 1, preHour = 1, preMin = 1, preSecond = 0;
    private String[] preValues = new String[columns.length];
    private String[] newvalues = new String[columns.length];
    private CommonEditTextLine.EditTextLineStyle[] edits = new CommonEditTextLine.EditTextLineStyle[columns.length];
    private CommonItemLine.ItemStyle[] items = new CommonItemLine.ItemStyle[columns.length];
    private CommonItemLine.ItemStyle currentEdit;
    private int currentEditIndex;

    private BLOZIPreferenceManager bloziPreferenceManager;
    /*缓存数据*/
    private List recycleCach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodinfo);

        appBar = findViewById(R.id.app_bar);
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainView = findViewById(R.id.recycle_view);
        top_image = findViewById(R.id.top_image);
        Glide.with(this).load(R.drawable.good_image).into(top_image);
        float_save = findViewById(R.id.float_save);
        float_reset = findViewById(R.id.float_reset);
        float_appbarbatton = findViewById(R.id.float_onappbar);
        float_appbarbatton.show();
        scanFloatBut = findViewById(R.id.scan);
        searchFloatBut = findViewById(R.id.search);
        bloziPreferenceManager = BLOZIPreferenceManager.Companion.getInstance(this);
        scanUtil = new ScanUtil(this);
        mainView.setHasFixedSize(true);
        mainViewManager = new LinearLayoutManager(this);
        mainViewManager.setOrientation(LinearLayoutManager.VERTICAL);
        mainView.setLayoutManager(mainViewManager);

//        toolbar.setTitleTextAppearance(this,R.style.blozi_collapseToolbar_collapsedTitleTextAppearance);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.more_vertical_orange));
        top_image.setOnClickListener((view) -> {
            if (GoodsInfoActivity.this.goodsInfo != null) {
                if (ContextCompat.checkSelfPermission(GoodsInfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(GoodsInfoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
        });
        float_appbarbatton.setOnClickListener((view) -> {
            getGoodsInfo();
        });


        collapsingToolbar.setTitle(Global.curryentStore.get(Global.STORE_NAME));
        ColorStateList colorStateList = getResources().getColorStateList(R.color.common_title_color_list);
        collapsingToolbar.setCollapsedTitleTextColor(colorStateList);
//        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.blozi_collapseToolbar_collapsedTitleTextAppearance);
//        collapsingToolbar.setExpandedTitleTextAppearance(R.style.blozi_collapseToolbar_expandedTitleTextAppearance);
        toolbar.setNavigationOnClickListener((v) -> {
                    pressBack = true;
                    GoodsInfoActivity.this.onBackPressed();
                }
        );
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
                AlertDialog.Builder comfirmReset = new AlertDialog.Builder(GoodsInfoActivity.this)
                        .setTitle(R.string.ifReset)
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.reset, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setGoodsInfo(goodsInfo);
                                clearAddonGoodsImagePath();
                                newvalues = new String[columns.length];
                                setGoodImagesIds(pregoodImagesIds);
                            }
                        });
                comfirmReset.create().show();
            }
        });
        searchFloatBut.setOnClickListener((v) -> {
            getGoodsInfo();
        });
        scanFloatBut.setOnClickListener((v) -> {
                    SystemMathod.startCaptureScan(GoodsInfoActivity.this,SystemConstants.REQUEST_CODE);
                }
        );

        initContent();
    }

    /**
     * 初始化界面的数据
     */
    private List initStarData(String goodsBarcode) {
        List list = new ArrayList();
        list.add(FragmentRecyclerViewAdapter.WhiteEmpty);
        CommonEditTextLine.EditTextLineStyle goodsCodeInput = new CommonEditTextLine.EditTextLineStyle();
        goodsCodeInput.setHint(R.string.goods_code);
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

    @Override
    public void onBackPressed() {
        pressBack = true;
        super.onBackPressed();

    }

    private Handler handler;

    public Handler getHandler() {
        return handler;
    }

    @Override
    protected void onResume() {
        super.onResume();

        scanUtil.initScan();
        pressBack = false;
        if (Global.currentActivity != this) Global.currentActivity = GoodsInfoActivity.this;
        if (handler == null) handler = new GoodsInfoActivityHandel();
    }

    @Override
    protected void onPause() {
        scanUtil.stopScan();
        handler = null;
        recycleCach = mainViewAdapter.getDatas();

        super.onPause();
        if (pressBack) overridePendingTransition(R.anim.in_left, R.anim.out_right);

    }

    /**
     * 初始化内容
     */
    private void initContent() {
        if(!TextUtils.isEmpty(this.goodsInfoId) && recycleCach!=null ){
            mainViewAdapter.setDatas(recycleCach);
            mainView.setAdapter(mainViewAdapter);
        }else {
            mainViewAdapter = new FragmentRecyclerViewAdapter(initStarData(null), this);
            mainView.setAdapter(mainViewAdapter);
        }
    }


    /**
     * 创建标题菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.goods_info_manage_toolbar_menu, menu);
        try {
            MenuBuilder menuBuilder = (MenuBuilder) menu;
            menuBuilder.setOptionalIconsVisible(true);
//            Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
//            m.setAccessible(true);
//            m.invoke(menu, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        scan = menu.findItem(R.id.scan_good_code);
//        ColorStateList colorStateList = getResources().getColorStateList(R.color.menu_item_color_list);
//        scan.setIconTintList(colorStateList);

        scan.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                SystemMathod.setScan(GoodsInfoActivity.this, false);
                SystemMathod.startCaptureScan(GoodsInfoActivity.this,SystemConstants.REQUEST_CODE);
                return false;
            }
        });
        ok = menu.findItem(R.id.ok);
        no = menu.findItem(R.id.no);
        no.setVisible(false);
        showImages = menu.findItem(R.id.showImages);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.ok:
                try {
                    if (currentEdit != null && currentEditIndex != -1) {
                        changeMainView(currentEdit, currentEditIndex);
                    }
                    confirmUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.no:
                AlertDialog.Builder comfirmReset = new AlertDialog.Builder(GoodsInfoActivity.this)
                        .setTitle(R.string.ifReset)
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.reset, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setGoodsInfo(goodsInfo);
                                clearAddonGoodsImagePath();
                                newvalues = new String[columns.length];
                                setGoodImagesIds(pregoodImagesIds);
                            }
                        });
                comfirmReset.create().show();
                break;
            case R.id.showImages:
                if (goodImagesIds != null && goodImagesIds.length > 0) {
                    changeGoodsImages();
                    changeImage.postDelayed(r, 1000);
                } else {
                    Toast.makeText(GoodsInfoActivity.this, R.string.NoPicOfThisGoods, Toast.LENGTH_SHORT).show();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化标题
     */
    private void initTitle() {

    }

    /**
     * 获取商品信息
     */
    private void getGoodsInfo() {
        if (!TextUtils.isEmpty(goodscode)) {
            ShowGoodsInfoTask showGoodsInfoService = new ShowGoodsInfoTask(this, bloziPreferenceManager.getCompliteURL());
            showGoodsInfoService.execute(bloziPreferenceManager.getLoginid(), bloziPreferenceManager.getPassword(), goodscode, Global.curryentStore.get(Global.STOREID), Boolean.TRUE.toString());
        } else Toast.makeText(this, R.string.GoodsBarCodeCannotBeEmpty, Toast.LENGTH_SHORT).show();
    }

    /**
     * 得到商品数据后显示
     */
    public void setGoodsInfo(final JSONObject goodsInfo) {
        this.goodsInfo = goodsInfo;
        clearAddonGoodsImagePath();
        scanFloatBut.hide();
        searchFloatBut.hide();
        currentEdit = null;
        currentEditIndex = -1;
        edits = new CommonEditTextLine.EditTextLineStyle[columns.length];
        items = new CommonItemLine.ItemStyle[columns.length];
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
                                final DatePickerDialog datePickerDialog = new DatePickerDialog(GoodsInfoActivity.this,
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
                                        TimePickerDialog timePicker = new TimePickerDialog(GoodsInfoActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                                edits[fi].setEditText(items[fi].getMainText());
                                edits[fi].setSubText(items[fi].getSubText());
                                edits[fi].setHint(columnNames[fi]);
                                TextWatcher goodsCodeWatcher = new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        newvalues[fi] = s.toString();
                                    }
                                };
                                edits[fi].setTextWatcher(goodsCodeWatcher);
                                edits[fi].setRightImageResourceId(R.drawable.checked_blue);
                                edits[fi].setRightImageClick(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        items[fi].setMainText(newvalues[fi]);
                                        if (preValues[fi] != null && newvalues[fi] != null && !preValues[fi].equals(newvalues[fi])) {
                                            ok.setVisible(true);
                                            no.setVisible(true);
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

                    items[i].setRightIconOnClickListenter(editClick);
                }
                infoListView.add(items[i]);
            }
//            mainViewAdapter = new FragmentRecyclerViewAdapter(infoListView, context);

            mainViewAdapter.setDatas(infoListView);
            mainViewAdapter.add(FragmentRecyclerViewAdapter.WhiteEmpty, infoListView.size());
            mainViewAdapter.notifyDataSetChanged();
        } else {
                Log.i("商品","获取失败");
//            mainViewAdapter = new FragmentRecyclerViewAdapter(initStarData(this.goodsInfoId), this);
            mainViewAdapter.setDatas(initStarData(this.goodsInfoId));
            mainViewAdapter.notifyDataSetChanged();
//            mainView.setAdapter(mainViewAdapter);
            float_appbarbatton.show();
        }

    }

    /**
     * 初始化开始视图
     */
    public void initStar(String barcode) {
        if (barcode == null) barcode = "";
        Log.i("商品信息修改", barcode);
        mainViewAdapter = new FragmentRecyclerViewAdapter(initStarData(barcode), this);
        mainView.setAdapter(mainViewAdapter);
        float_appbarbatton.show();
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

    /**
     * 变更视图
     */
    private void changeMainView(Object o, int i) {
        mainViewAdapter.changeData(o, i);
        float_appbarbatton.show();
    }

    /**
     * 提交前确认
     */
    private void confirmUpdate() throws JSONException {
        final JSONObject jb = new JSONObject();
        StringBuffer sb = new StringBuffer();
        Date start = null;
        Date end = null;
        int errorMsg = -1;
        if (this.goodsInfo == null) return;
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
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if (start == null && !TextUtils.isEmpty(preValues[9]))
                start = simpleDateFormat.parse(preValues[9]);
            if (end == null && !TextUtils.isEmpty(preValues[10]))
                end = simpleDateFormat.parse(preValues[10]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (addonGoodsImagePath != null && addonGoodsImagePath.size() > 0) {
            sb.append(getResources().getString(R.string.GoodsImageColon));
            StringBuffer files = new StringBuffer();
            for (int i = 0; i < addonGoodsImagePath.size(); i++) {
                sb.append(addonGoodsImagePath.get(i).substring(addonGoodsImagePath.get(i).lastIndexOf("/") + 1)).append("\n");
                File file = new File(addonGoodsImagePath.get(i));
                if (file != null && file.exists() && file.length() < GOODSIMAGEMaxLength) {
                    files.append(addonGoodsImagePath.get(i)).append("\n");
                } else {
                    Toast.makeText(this, String.format(getResources().getString(R.string.GoodsImageisLargeThanORDonotExist), GOODSIMAGEMaxLength_M), Toast.LENGTH_LONG).show();
                    return;
                }
            }
            jb.put("images", files.toString());
        }
        if (jb.length() == 0) {
            Toast.makeText(this, R.string.NoDataThatNeedsToBeSubmitted, Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder ab = new AlertDialog.Builder(this).setMessage(sb.toString()).setTitle(R.string.confirm).setPositiveButton(R.string.Submission, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BLOZIPreferenceManager bloziPreferenceManager = BLOZIPreferenceManager.Companion.getInstance(GoodsInfoActivity.this);
                updateGoodsInfoAsyncTask = new UpdateGoodsInfoAsyncTask(GoodsInfoActivity.this, bloziPreferenceManager.getCompliteURL());
                try {
                    Log.i("GoodsImage", jb.toString());
                    updateGoodsInfoAsyncTask.execute(goodsInfo.getString("goodsInfoId"), jb.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).setNegativeButton(R.string.cancel, null);
        ab.create().show();
    }


    /**
     * 根据ID获取图片
     */
    private void changeGoodsImages() {
        if (goodImagesIds != null) {
            for (int i = 0; i < goodImagesIds.length; i++) {
                GetGoodsImagesAsyncTask getGoodsImagesAsyncTask = new GetGoodsImagesAsyncTask(this, bloziPreferenceManager.getCompliteURL());
                getGoodsImagesAsyncTask.execute(goodImagesIds[i]);
                getGoodsImagesAsyncTasks.add(getGoodsImagesAsyncTask);
            }
        }
    }

    /**
     * 摄像头扫码结果处理
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SystemConstants.REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    //      Log.i("结果：",result);

                    ShowGoodsInfoTask showGoodsInfoService = new ShowGoodsInfoTask(this, bloziPreferenceManager.getCompliteURL());
                    showGoodsInfoService.execute(bloziPreferenceManager.getLoginid(), bloziPreferenceManager.getPassword(), result, Global.curryentStore.get(Global.STOREID), Boolean.TRUE.toString());
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this, R.string.ParsingFailure, Toast.LENGTH_LONG).show();
                }
            }
        } else if (requestCode == SystemConstants.SELECT_PHOTO) {
            if (resultCode == RESULT_OK) {
                //判断手机系统版本号
                if (Build.VERSION.SDK_INT >= 19) {
                    //4.4及以上系统使用这个方法处理图片
                    Log.i("执行4.4", "图片");
                    handleImgeOnKitKat(data);
                } else {
                    handleImageBeforeKitKat(data);
                }
            }
        }

    }

    /**
     * 打开相册的方法
     */
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_PHOTO);
    }

    /**
     * 4.4及以上系统处理图片的方法
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleImgeOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.i("图片路径", uri.getPath());
        if (DocumentsContract.isDocumentUri(this, uri)) {
            Log.i("图片路径", "文档" + uri.getPath());
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                //解析出数字格式的id
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的uri，直接获取图片路径即可
            imagePath = uri.getPath();
        } else {
            Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
        }
        //根据图片路径显示图片
        displayImage(imagePath);
        addImagePath(imagePath);
        AddAddonGoodsImagePath(imagePath);
        changeImage.removeCallbacks(r);
        changeImage.postDelayed(r, 1000);
    }

    /**
     * 4.4以下系统处理图片的方法
     */
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
        addImagePath(imagePath);
        AddAddonGoodsImagePath(imagePath);
        changeImage.removeCallbacks(r);
        changeImage.postDelayed(r, 1000);
    }

    /**
     * 根据图片路径显示图片的方法
     */
    private void displayImage(String imagePath) {
        Log.i("图片路径", imagePath);

        if (imagePath != null) {
            File file = new File(imagePath);
            if (file != null && file.exists() && file.length() < SystemConstants.GOODSIMAGEMaxLength) {
//                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//                top_image.setImageBitmap(bitmap);
                Glide.with(this).clear(top_image);
                Glide.with(this).load(imagePath).into(top_image);
            } else {
                Toast.makeText(this, String.format(getResources().getString(R.string.GoodsImageisLargeThanORDonotExist), GOODSIMAGEMaxLength_M), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, R.string.failed, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 通过uri和selection来获取真实的图片路径
     */
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "执行onDestroy");
        cancelGetGoodImageAsyncTasks();
        if (updateGoodsInfoAsyncTask != null) {
            updateGoodsInfoAsyncTask.cancelHttpPost();
            updateGoodsInfoAsyncTask.cancel(true);
        }
        if (changeImage != null) changeImage.removeCallbacks(r);

        super.onDestroy();
    }

    class GoodsInfoActivityHandel extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SystemConstants.scanSuccess) {
                Bundle bundle = msg.getData();
                String scanMsg = (String) bundle.get(SystemConstants.scanMsg);
                Log.i(TAG, "接收广播：" + scanMsg);
                if (TextUtils.isEmpty(scanMsg)) {
                    return;
                }
                ShowGoodsInfoTask showGoodsInfoService = new ShowGoodsInfoTask(GoodsInfoActivity.this, bloziPreferenceManager.getCompliteURL());
                showGoodsInfoService.execute(bloziPreferenceManager.getLoginid(), bloziPreferenceManager.getPassword(), scanMsg, Global.curryentStore.get(Global.STOREID), Boolean.TRUE.toString());
            }
        }
    }
}
