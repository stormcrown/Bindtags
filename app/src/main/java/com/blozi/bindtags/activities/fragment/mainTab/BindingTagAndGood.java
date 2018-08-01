package com.blozi.bindtags.activities.fragment.mainTab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.blozi.bindtags.R;
import com.blozi.bindtags.activities.MainActivity;
import com.blozi.bindtags.activities.MainActivityLocal;
import com.blozi.bindtags.activities.fragment.BaseFragment;
import com.blozi.bindtags.adapter.IndicatorExpandableListAdapter;
import com.blozi.bindtags.adapter.OnGroupExpandedListener;
import com.blozi.bindtags.adapter.SimpleTreeAdapter;
import com.blozi.bindtags.application.Global;
import com.blozi.bindtags.asyncTask.local.LocalDeskSocket;
import com.blozi.bindtags.asyncTask.local.LocalNetBaseTask;
import com.blozi.bindtags.asyncTask.online.BlindTagGoodsTask;
import com.blozi.bindtags.asyncTask.online.ShowGoodsInfoTask;
//import com.scandecode.ScanDecode;
//import com.scandecode.inf.ScanInterface;
import com.blozi.bindtags.model.TreeData;
import com.blozi.bindtags.util.LoadingDialog;
import com.blozi.bindtags.util.SystemConstants;
import com.blozi.bindtags.util.SystemMathod;
import com.blozi.bindtags.view.NestedExpandaleListView;
import com.zxing_new.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 骆长涛 on 2018/3/14.
 */

public class BindingTagAndGood extends BaseFragment {
    private String goodsInfoId, goodsName, goodsBarcode, tagBarcode, isBind, tagInfoId ;//, webServiceUrl;
    private EditText binding_fragment_goods_code_str, binding_fragment_goods_name_str, binding_fragment_tag_code_str;
//    private TextInputLayout layout1,layout2,layout3;
    private Button binding_fragment_scan, binding_fragment_to_reset, binding_fragment_blind;
    private CheckBox autoBind,forceTag;
    private NestedScrollView scrollView;
    private NestedExpandaleListView history;
    private SimpleTreeAdapter historyAdapter;
    private IndicatorExpandableListAdapter adapter;
    /*缓存数据*/
    private List<TreeData> historyCach;
    private OnGroupExpandedListener mOnGroupExpandedListener;
    protected Activity context = getActivity();
    public Activity getMyActivity() { return context; }
    public static final int REQUEST_CODE = 111;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(R.layout.binding_tag_good_fragment, null);
        scrollView =view .findViewById(R.id.scrollView);
        super.onCreateView(inflater, container, savedInstanceState);
        Bundle bundle = context.getIntent().getExtras();
        binding_fragment_goods_code_str = (EditText) view.findViewById(R.id.binding_fragment_goods_code);
        binding_fragment_goods_name_str = (EditText) view.findViewById(R.id.binding_fragment_goods_name);
        binding_fragment_tag_code_str = (EditText) view.findViewById(R.id.binding_fragment_tag_code);
        binding_fragment_to_reset = (Button) view.findViewById(R.id.binding_fragment_to_reset);
        binding_fragment_scan = (Button) view.findViewById(R.id.binding_fragment_scan);
        binding_fragment_blind = (Button) view.findViewById(R.id.binding_fragment_blind);
        msg = view.findViewById(R.id.binding_fragment_msg);
        autoBind = view.findViewById(R.id.auto_bind);
        forceTag = view.findViewById(R.id.forse_scan_tagcode);
        history = view.findViewById(R.id.history) ;
        adapter = new IndicatorExpandableListAdapter();
        historyAdapter = new  SimpleTreeAdapter(context);



        binding_fragment_goods_code_str.setOnClickListener(view1 -> { if(!bloziPreferenceManager.getEditable()) Toast.makeText(context,R.string.Pleasesetallowedinputintheupperrightcorner,Toast.LENGTH_SHORT).show();  });
        binding_fragment_goods_name_str.setOnClickListener(view1 -> { if(!bloziPreferenceManager.getEditable()) Toast.makeText(context,R.string.Pleasesetallowedinputintheupperrightcorner,Toast.LENGTH_SHORT).show();  });
        binding_fragment_tag_code_str.setOnClickListener(view1 -> { if(!bloziPreferenceManager.getEditable()) Toast.makeText(context,R.string.Pleasesetallowedinputintheupperrightcorner,Toast.LENGTH_SHORT).show();  });
//        tabs  = view.findViewById(R.id.bind_tabs);
//        success = view.findViewById(R.id.success);
//        failed = view.findViewById(R.id.failed);

        autoBind.setChecked(bloziPreferenceManager.getAutoBind());
        autoBind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                bloziPreferenceManager.setAutoBind(b);
            }
        });

        if (bundle != null) {
            goodsName = bundle.getString("goodsName");
            if (!"".equals(goodsName) && goodsName != null) {
                binding_fragment_goods_name_str.setText(goodsName);
            }
            goodsBarcode = bundle.getString("goodsBarcode");
            if (!"".equals(goodsBarcode) && goodsBarcode != null) {
                binding_fragment_goods_code_str.setText(goodsBarcode);
            }
            tagBarcode = bundle.getString("tagBarcode");
            if (!"".equals(tagBarcode) && tagBarcode != null) {
                binding_fragment_tag_code_str.setText(tagBarcode);
            }
            tagInfoId = bundle.getString("tagInfoId");
            goodsInfoId = bundle.getString("goodsInfoId");
        }
        //首先取出保存的服务器地址信息
//        webServiceUrl = getPropertiesValue(SystemConstants.PROPERTY_NAME_WEBSERVICE_URL)+getActivity().getString(R.string.web_service_url_address);

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(oldScrollY<scrollY && getMyActivity() instanceof MainActivity ){
                    ((MainActivity)getMyActivity()).hideBottomBar();
                }else if(oldScrollY>scrollY && getMyActivity() instanceof MainActivity ){
                    ((MainActivity)getMyActivity()).showBottomBar();
                }
            }
        });
        //扫描功能
        binding_fragment_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg.setText("");

               startCaptureScan(REQUEST_CODE);
//                Intent openCameraIntent = new Intent(context, CaptureActivity.class);
//                startActivityForResult(openCameraIntent, REQUEST_CODE);
            }
        });
        binding_fragment_to_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding_fragment_goods_code_str.setText("");
                binding_fragment_goods_name_str.setText("");
                binding_fragment_tag_code_str.setText("");
                goodsInfoId = "";
                tagInfoId = "";
                isBind = "";
            }
        });
        //绑定功能
        binding_fragment_blind.setOnClickListener((v)-> {
                binding();
            } );
        List pathList = new ArrayList<String>();
        for(int i =0;i<2;i++){
            pathList.add(SystemConstants.productsImags[i]);
        }




        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(adapter!=null  ) historyCach = adapter.getGroupData();
    }
    private void tryToBind(){
        String goodsCode = binding_fragment_goods_code_str.getText().toString();
        String goodsName = binding_fragment_goods_name_str.getText().toString();
        String tagCode = binding_fragment_tag_code_str.getText().toString();
        if(!TextUtils.isEmpty(goodsCode) && !TextUtils.isEmpty(goodsName) && !TextUtils.isEmpty(tagCode) && bloziPreferenceManager.getAutoBind()  ){
            binding();
        }
    }
    private void binding(){
        String goodsCode=null,goodsName=null,tagCode=null;
        if(binding_fragment_goods_code_str!=null
                && binding_fragment_goods_name_str!=null
                && binding_fragment_tag_code_str!=null
                ){
            goodsCode = binding_fragment_goods_code_str.getText().toString();
            goodsName = binding_fragment_goods_name_str.getText().toString();
            tagCode = binding_fragment_tag_code_str.getText().toString();
        }else return;
        if ( TextUtils.isEmpty(goodsCode) || TextUtils.isEmpty(goodsName)  ) {
            Toast.makeText(context, R.string.goods_str, Toast.LENGTH_SHORT).show();
        } else if ( TextUtils.isEmpty(tagCode)     ) {
            Toast.makeText(context, R.string.TagCodeIsNotRight, Toast.LENGTH_SHORT).show();
        }
        else {
            //进行绑定具体操作
            LoadingDialog.INSTANCE.alertDialog(context,R.string.uploading);
            if (context instanceof MainActivity) {
                BlindTagGoodsTask blindTagGoodsService = new BlindTagGoodsTask(context, bloziPreferenceManager.getCompliteURL());
                blindTagGoodsService.execute(userSession, userPass, goodsInfoId, tagInfoId, tagCode, Global.curryentStore.get(Global.STOREID));
            }else if(context instanceof MainActivityLocal ){
                LocalNetBaseTask localNetBaseTask = new LocalNetBaseTask(context);
                localNetBaseTask.execute(LocalDeskSocket.Companion.getRequestCode(LocalDeskSocket.RequestCode.UploadTagAndGoodsCode, goodsCode,tagCode) );
            }
//            TreeData group = new TreeData(goodsInfoId,goodsName,goodsCode);
//            TreeData child = new TreeData(goodsInfoId,tagCode,null);
//            group.addTopChild(child);
//
//            adapter.addGroup(group);
//            history.setAdapter(adapter);
//            history.setGroupIndicator(null);
//            history.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//                @Override
//                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                    Log.d("TAG", "onGroupClick: groupPosition:" + groupPosition + ", id:" + id);
//                    boolean groupExpanded = parent.isGroupExpanded(groupPosition);
//                    adapter.setIndicatorState(groupPosition, groupExpanded);
//                    // 请务必返回 false，否则分组不会展开
//                    return false;
//                }
//            });
//            //  设置子选项点击监听事件
//            history.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//                @Override
//                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                    return true;
//                }
//            });
        }
    }


    public void toBingd(){
        binding();
    }
    @Override
    public void editableToggle() {
        setEditable(binding_fragment_goods_code_str);
        setEditable(binding_fragment_goods_name_str);
        setEditable(binding_fragment_tag_code_str);
    }

    @Override
    public void onResume() {
        super.onResume();
        editableToggle();
        if(historyCach!=null && historyCach.size()>0 ){
            adapter.setGroupData(historyCach);
            history.setAdapter(adapter);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("结果：",requestCode+"\t");
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                       Log.i("结果：",result);
//                    if(SystemMathod.isTagCode(result)){
                    if (result != null && (SystemMathod.isTagCodeTenChar(result) || forceTag.isChecked() )) {
                        binding_fragment_tag_code_str.setText(result);
                        if(forceTag.isChecked())forceTag.setChecked(false);
                    } else {
                        if (context != null && context instanceof MainActivity) {
                            ShowGoodsInfoTask showGoodsInfoService = new ShowGoodsInfoTask(context,bloziPreferenceManager.getCompliteURL());
                            showGoodsInfoService.execute(userSession, userPass, result, Global.curryentStore.get(Global.STOREID));
                        } else if (context != null && context instanceof MainActivityLocal) {
                            try {
                                LocalNetBaseTask localNetBaseTask = new LocalNetBaseTask(context);
                                localNetBaseTask.execute(LocalDeskSocket.Companion.getRequestCode(LocalDeskSocket.RequestCode.GetGoodsName, result), result);
                            }catch (Exception e){
                                Toast.makeText(context,R.string.ConnectionIsInterruptedPleaseWaitForReconnectionOrLoginDirectly,Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }

                    }
                    tryToBind();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(context, R.string.ParsingFailure, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    public Handler getHandler() {
        return handler;
    }

    /**
     * 处理接口传来的参数
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            final Bundle bundle = msg.getData();
            isBind = bundle.getString("isBind");
            if (msg.what == 1) {
                goodsInfoId = bundle.getString("goodsInfoId");
                goodsName = bundle.getString("goodsName");
//                if(!"".equals(goodsName)&&goodsName!=null){
                binding_fragment_goods_name_str.setText(goodsName);
//                }
                goodsBarcode = bundle.getString("goodsBarcode");
//                if(!"".equals(goodsBarcode)&&goodsBarcode!=null){
                binding_fragment_goods_code_str.setText(goodsBarcode);
//                }
                tryToBind();
            }
            if (msg.what == 2) {
                tagInfoId = bundle.getString("tagInfoId");
                tagBarcode = bundle.getString("physicalIpAddress");
                if (!"".equals(tagBarcode) && tagBarcode != null) {
                    binding_fragment_tag_code_str.setText(tagBarcode);
                }
                tryToBind();
            }
            if (msg.what == 3) {
                String m = bundle.getString("msg");
                String isSuccess = bundle.getString("isSuccess");
                Toast.makeText(context, m, Toast.LENGTH_LONG).show();
                accetpMsg(m);
                if ("y".equals(isSuccess)) {
               //     binding_fragment_goods_code_str.setText("");
                //    binding_fragment_goods_name_str.setText("");
                    binding_fragment_tag_code_str.setText("");
              //      goodsInfoId = "";
                    tagInfoId = "";
                    isBind = "";
                }
            }
        }
    };

    @Override
    public boolean isForceTag() {
        if(forceTag!=null)return forceTag.isChecked();
        return false;
    }

    public void setTagCodeStr(String str) {
        if (binding_fragment_tag_code_str != null) binding_fragment_tag_code_str.setText(str);
        if(forceTag!=null)forceTag.setChecked(false);
        tryToBind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if(scanDecode!=null)scanDecode.onDestroy();//回复初始状态
    }

    @Override
    public void accetpMsg(String str) {
        if(msg!=null)msg.setText(str);
    }
}
