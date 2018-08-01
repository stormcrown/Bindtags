package com.blozi.bindtags.activities.fragment.mainTab;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.blozi.bindtags.R;
import com.blozi.bindtags.activities.MainActivity;
import com.blozi.bindtags.activities.TagAdapter;
import com.blozi.bindtags.activities.fragment.BaseFragment;
import com.blozi.bindtags.application.Global;
import com.blozi.bindtags.asyncTask.online.ToPushTagsTask;
import com.blozi.bindtags.asyncTask.online.UpdateTagsTask;
import com.blozi.bindtags.util.SystemMathod;
import com.blozi.bindtags.view.CommonItemLine;
//import com.scandecode.ScanDecode;
//import com.scandecode.inf.ScanInterface;
import com.zxing_new.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 骆长涛 on 2018/3/14.
 */

public class ImportTags extends BaseFragment implements CompoundButton.OnCheckedChangeListener{
    private EditText import_tags_tagId;//显示扫描到的价签的iD
    private TextView import_tags_is_update,import_tags_response_msg ;
    private Button import_tags_reset,import_tags_scan,import_tags_updateTags  ; // ,  import_tags_scan_repeat,import_tags_stop_scan ;
//    private LinearLayout import_tags_response;
    private ListView import_tags_tagsList;
    private CheckBox forceTag;
    private List<String> tagList;
    private BaseAdapter baseAdapter;
    public static final int REQUEST_CODE = 111;
    private boolean isDelete =false;
    private int isUpdate= R.string.upload;
    protected Activity context;
    private UpdateTagsTask updateTagsService;
    private Handler handler;
    private Runnable absortRequest;
    private RadioButton upper,push,delete;
    {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                String isSuccess = bundle.getString("isSuccess");
                String responseMsg = bundle.getString("msg");
                String doubleTagMsg = bundle.getString("doubleTagMsg");
                String nullTagMsg = bundle.getString("nullTagMsg");
                String reUpdateMsg = bundle.getString("reUpdateMsg");
                String response = "";
                if(responseMsg!=null)response+=(responseMsg+"\n" );
                if(doubleTagMsg!=null)response+=(doubleTagMsg+"\n");
                if(nullTagMsg!=null)response+=(nullTagMsg+"\n");
                if(reUpdateMsg!=null)response+=(reUpdateMsg+"\n");
//                import_tags_response.setVisibility(View.VISIBLE);
                import_tags_response_msg.setText(response);
                if ("y".equals(isSuccess)) {
                    import_tags_tagId.setText("");
                    tagList.clear();
                    baseAdapter.notifyDataSetChanged();
                    import_tags_is_update.setText(String.format(getResources().getString(R.string.totalOnes),tagList.size() ) );
                }
            }
        };
        context = getActivity();
        absortRequest = new Runnable() {
            @Override
            public void run() {
                if (updateTagsService != null) updateTagsService.cancelHttpPost();
            }
        };
    }
    public ImportTags(){}
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        context= null;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.import_tags_fragment,null);
        super.onCreateView(inflater, container, savedInstanceState);
//        title_parent = view.findViewById(R.id.title_parent);
//        title = view.findViewById(R.id.title_title);
//        title_left = view.findViewById(R.id.title_left_image);
//        title_right=view.findViewById(R.id.title_right_image);


        if( getArguments()!=null && getArguments().get("isDelete")!=null){
            isDelete = Boolean.valueOf((String) getArguments().get("isDelete"));
            if(isDelete) isUpdate=R.string.delete;
        }
        tagList = new ArrayList();
        import_tags_is_update = view.findViewById(R.id.import_tags_is_update);
        import_tags_is_update.setText(String.format(getResources().getString(R.string.totalOnes),tagList.size() ) );
        import_tags_tagId = (EditText)view.findViewById(R.id.import_tags_tag_id);
        import_tags_tagId .setOnClickListener(view1 -> { if(!bloziPreferenceManager.getEditable()) Toast.makeText(context,R.string.Pleasesetallowedinputintheupperrightcorner,Toast.LENGTH_SHORT).show();  });
        import_tags_reset =  view.findViewById(R.id.import_tags_to_reset);
        import_tags_updateTags =view.findViewById(R.id.import_tags_update_tags);
        forceTag = view.findViewById(R.id.forse_scan_tagcode);
        upper = view.findViewById(R.id.import_tag);
        push = view.findViewById(R.id.push);
        delete = view.findViewById(R.id.delete);
        if(!bloziPreferenceManager.isAdminPlus()){
          delete.setVisibility(View.GONE);
        }else {
            delete.setVisibility(View.VISIBLE);
        }
        upper.setOnCheckedChangeListener(this);
        push.setOnCheckedChangeListener(this);
        delete.setOnCheckedChangeListener(this);

        switch (bloziPreferenceManager.getImportAction()){
            case 0:
                upper.setChecked(true);
                break;
            case 1:
                push.setChecked(true);
                break;
            case 2:
                if(!bloziPreferenceManager.isAdminPlus()){
                    delete.setVisibility(View.GONE);
                    upper.setChecked(true);
                    bloziPreferenceManager.setImportAction(0);
                }else{
                    delete.setChecked(true);
                    isDelete=true;
                }
                break;
        }
        if(isDelete){
            import_tags_updateTags.setTextColor(getResources().getColor(R.color.red));
            import_tags_updateTags.setText(R.string.delete);
        }
        baseAdapter = new TagAdapter(context,tagList);
        import_tags_tagsList = (ListView) view.findViewById(R.id.import_tags_tags_list);
        import_tags_tagsList.setAdapter(baseAdapter);

//        forceTag.setChecked(bloziPreferenceManager.getImportForceTag());
        forceTag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                bloziPreferenceManager.setImportForceTag(b);
            }
        });
        //重置按钮，将扫描的数据全部清空
        import_tags_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                import_tags_tagId.setText("");
                tagList.clear();
                baseAdapter.notifyDataSetChanged();
                import_tags_is_update.setText(String.format(getResources().getString(R.string.totalOnes),tagList.size() ) );
                import_tags_response_msg.setText(null);
//                import_tags_response.setVisibility(View.GONE);
            }
        });

        //扫描
        import_tags_scan = view.findViewById(R.id.import_tags_scan);
        import_tags_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //吊起摄像头
                startCaptureScan(REQUEST_CODE);
            }
        });
        //上传扫描的数据，成功后清空列表
        import_tags_updateTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadTags();
            }
        });
//        import_tags_response = view.findViewById(R.id.import_tags_response);
//        import_tags_response.setVisibility(View.GONE);
        import_tags_response_msg = view.findViewById(R.id.import_tags_response_msg);
        Intent intent =new Intent("com.android.service_settings");
        intent.putExtra("pda_sn","string");//机器码设置成“string”
        context.sendBroadcast(intent);
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        setEditable(import_tags_tagId);

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        if(checked){
            switch (compoundButton.getId()){
                case R.id.import_tag:
                    import_tags_updateTags.setText(R.string.upload);
                    bloziPreferenceManager.setImportAction(0);
                    break;
                case R.id.push:
                    import_tags_updateTags.setText(R.string.PushAgain);
                    bloziPreferenceManager.setImportAction(1);
                    break;
                case R.id.delete:
                    if(bloziPreferenceManager.isAdminPlus()){
                        bloziPreferenceManager.setImportAction(2);
                        import_tags_updateTags.setText(R.string.delete);
                        import_tags_updateTags.setTextColor(getResources().getColor(R.color.red));
                        isDelete=true;
                    }
                    break;
            }
        }else{
            switch (compoundButton.getId()){
                case R.id.delete:
                    import_tags_updateTags.setTextColor(getResources().getColor(R.color.colorPrimary));
                    isDelete=false;
                    break;
            }
        }



    }

    public void editableToggle() {
        setEditable(import_tags_tagId);
    }

    public void addTagId(String str){
        if(SystemMathod.isTagCodeTenChar(str) || isForceTag() ){
            if(import_tags_tagId!=null)import_tags_tagId.setText(str);
            isAddToList(str);
        }
    }

    private void isAddToList(String id){
        //判断是否加入list中
        boolean flag = false;//false表示没有重复的
        for (int i = 0; i<tagList.size(); i++){
            if(id.equals(tagList.get(i))){
                flag = true;//表示已经存在与列表中
            }
        }
        if(flag){
            if(context==null ) context=getActivity();
            accetpMsg(String.format(getResources().getString(R.string.TagCodeHasBeenScanned),id  ));
        }
        else if(SystemMathod.isTagCodeTenChar(id) || isForceTag() ){
            tagList.add(id);
            baseAdapter.notifyDataSetChanged();
            import_tags_is_update.setText(String.format(getResources().getString(R.string.totalOnes),tagList.size() ) );
//            import_tags_response.setVisibility(View.GONE);
            if(tagList.size()>9){
                uploadTags();
            }
        }else {
            if(context!=null) Toast.makeText(getActivity(), R.string.NotTagcode,Toast.LENGTH_SHORT).show();
            accetpMsg(getResources().getString(R.string.NotTagcode));
        }
    }
    private void uploadTags(){
        if(tagList.size()>0){
            //首先取出保存的服务器地址信息
            final String webServiceUrl0 = bloziPreferenceManager.getCompliteURL();
            final String realUrl =webServiceUrl0;
            final StringBuilder tagsId = new StringBuilder();
            for(String id : tagList){
                tagsId.append(id+"_");
            }
            if(isDelete){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.confirmToDelete) ;
//                builder.setIcon(R.drawable.warning);
                builder.setMessage(R.string.ThePriceTagToBeDeletedWillBeUploadedToTheServerAndThenTheAdministratorWillManuallyDeleteThePriceTagInformation);
                builder.setPositiveButton(isUpdate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(context instanceof MainActivity){
                            ((MainActivity)context).showLoad(context,R.string.InformationIsBeingSubmittedToDeletePriceTagInformation);
                        }
                        updateTagsService = new UpdateTagsTask(context, realUrl,isDelete);
                        updateTagsService.execute(userSession, userPass, tagsId.toString(), Global.curryentStore.get(Global.STOREID));
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.show();
            }else if(bloziPreferenceManager.getImportAction() ==1){
                ToPushTagsTask toPushTagsTask = new ToPushTagsTask(context,bloziPreferenceManager.getCompliteURL());
                toPushTagsTask.execute(bloziPreferenceManager.getLoginid(),bloziPreferenceManager.getPassword(),tagsId.toString(),Global.curryentStore.get(Global.STOREID));
            } else{
                if(context instanceof MainActivity){
                    ((MainActivity)context).showLoad(context,isUpdate);
                }
                updateTagsService = new UpdateTagsTask(context, realUrl,isDelete);
                updateTagsService.execute(userSession, userPass, tagsId.toString(), Global.curryentStore.get(Global.STOREID));
            }
        }
        else{
            if(context==null ) context=getActivity();
            if(context!=null) Toast.makeText(context,R.string.PleaseScanThePricetag,Toast.LENGTH_SHORT).show();
        }
    }

    public Handler getHandler() {
        return handler;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    if(SystemMathod.isTagCodeTenChar(result) || isForceTag()  ){
                        import_tags_tagId.setText(result);
                        import_tags_response_msg.setText(null);
                        isAddToList(result);
                    }else {
                        Toast.makeText(context, R.string.NotTagcode, Toast.LENGTH_LONG).show();
                        import_tags_response_msg.setText(R.string.NotTagcode);
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(context, R.string.ParsingFailure, Toast.LENGTH_LONG).show();
                    import_tags_response_msg.setText(R.string.ParsingFailure);
                }
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean isForceTag() {
        if(forceTag!=null)return forceTag.isChecked();
        return false;
    }

    @Override
    public void accetpMsg(String str) {
        if(import_tags_response_msg!=null) import_tags_response_msg.setText(str);
    }
}
