package com.blozi.bindtags.activities.fragment.mainTab;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.blozi.bindtags.R;
import com.blozi.bindtags.activities.RackDetailActivity;
import com.blozi.bindtags.activities.ScanBarcodeOfflineActivity;
import com.blozi.bindtags.activities.fragment.BaseFragment;
import com.blozi.bindtags.adapter.FragmentRecyclerViewAdapter;
import com.blozi.bindtags.application.Global;
import com.blozi.bindtags.asyncTask.online.GetRackListAsyncTask;
import com.blozi.bindtags.model.RackInfo;
import com.blozi.bindtags.util.SystemConstants;
import com.blozi.bindtags.view.CommonItemLine;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class RackListFragment extends BaseFragment {
    private int maxRows = 10,currentPage=1,firstRow=0;
    private FragmentRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView rackList;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayoutManager layoutManager;
//    private ImageView imageView;
//    private Animation a;
    private GetRackListAsyncTask getRackListAsyncTask;
    private int totalLows=0;
//    private List list = new ArrayList();
    private RecyclerView.OnScrollListener mainScrollistenter;
    private ArrayList<RackInfo> rackInfos = new ArrayList<>();
    /*缓存数据*/
    private List recycleCach;
    private String StoreIdCach="";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.rack_list_fragment, null);
        super.onCreateView(inflater,container,savedInstanceState);
        rackList = view.findViewById(R.id.rack_list);
        refreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                maxRows = 10;currentPage=1;firstRow=0;
                getRackListAsyncTask = new GetRackListAsyncTask(context,bloziPreferenceManager.getCompliteURL(),maxRows,currentPage,firstRow ,Boolean.FALSE);
                getRackListAsyncTask.execute(Global.curryentStore.get(Global.STOREID));
            }
        });
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rackList.setLayoutManager(layoutManager);


        recyclerViewAdapter = new FragmentRecyclerViewAdapter(context);
        recyclerViewAdapter.setNeedLoading(Boolean.TRUE);


        rackList.setAdapter(recyclerViewAdapter);
        mainScrollistenter = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastVisibleItemPosition=layoutManager.findLastVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition + 1 == recyclerViewAdapter.getItemCount() && lastVisibleItemPosition<totalLows  ) {
                    currentPage++;
                    firstRow = (currentPage-1)*maxRows;
//                    Log.i("总数",totalLows+"");
                    getRackListAsyncTask = new GetRackListAsyncTask(context,bloziPreferenceManager.getCompliteURL(),maxRows,currentPage,firstRow,Boolean.TRUE );
                    getRackListAsyncTask.execute(Global.curryentStore.get(Global.STOREID));
                }
            }
        };
        rackList.addOnScrollListener(mainScrollistenter);
//        Log.i("RackList" , (recycleCach ==null )+"\t" +(!TextUtils.isEmpty(StoreIdCach))+"\t" );
        if(recycleCach!=null && recycleCach.size()>0  && !TextUtils.isEmpty(StoreIdCach) && StoreIdCach.equals(Global.curryentStore.get(Global.STOREID))  ){
            recyclerViewAdapter.setDatas(recycleCach);
            recyclerViewAdapter.setLimitRows(totalLows);
        }
        else {
//            list.clear();
            List list = new ArrayList();
            list.add(FragmentRecyclerViewAdapter.WhiteEmpty);
            recyclerViewAdapter.setDatas(list);
            maxRows = 10;currentPage=1;firstRow=0;
//            refreshLayout.setRefreshing(true);
            recyclerViewAdapter.setLimitRows(1);
            recyclerViewAdapter.setDatas(new ArrayList());
            getRackListAsyncTask = new GetRackListAsyncTask(context,bloziPreferenceManager.getCompliteURL(),maxRows,currentPage,firstRow,Boolean.FALSE);
            getRackListAsyncTask.execute(Global.curryentStore.get(Global.STOREID));
        }
        return view;

    }
    /** 切换门店时重置 */
    public void reset(){
//        refreshLayout.setRefreshing(true);
        maxRows = 10;currentPage=1;firstRow=0;
        recyclerViewAdapter.setLimitRows(1);
        recyclerViewAdapter.setDatas(new ArrayList());
        getRackListAsyncTask = new GetRackListAsyncTask(context,bloziPreferenceManager.getCompliteURL(),maxRows,currentPage,firstRow ,Boolean.FALSE);
        getRackListAsyncTask.execute(Global.curryentStore.get(Global.STOREID));
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("暂停","RackList");
        recycleCach = recyclerViewAdapter.getDatas();
        StoreIdCach = Global.curryentStore.get(Global.STOREID);
    }

    public void setRefresh(boolean refreshing){
        if(refreshLayout!=null)refreshLayout.setRefreshing(refreshing);
//        if(!refreshing)         imageView.setVisibility(View.GONE);
    }

    public void setTotalLows(int totalLows) {
        this.totalLows = totalLows;
        if(recyclerViewAdapter==null  && rackList!=null ){
            recyclerViewAdapter = new FragmentRecyclerViewAdapter(context);
            recyclerViewAdapter.setNeedLoading(Boolean.TRUE);
            rackList.setAdapter(recyclerViewAdapter);
        }
        if(recyclerViewAdapter!=null)recyclerViewAdapter.setLimitRows(this.totalLows);
    }
    /**将货架数据转换成Adapter使用的数据*/
    public void setDatas(ArrayList<RackInfo> datas) {
        if(datas!=null){
            rackInfos.clear();
            rackInfos.addAll(datas);
            if(recyclerViewAdapter ==null){
                recyclerViewAdapter = new FragmentRecyclerViewAdapter(context);
                recyclerViewAdapter.setNeedLoading(Boolean.TRUE);
            }
            recyclerViewAdapter.setDatas(rackInfosToItems(datas));
            rackList.setAdapter(recyclerViewAdapter);

        }

    }
    public void addDatas(ArrayList<RackInfo> datas) {
        if(datas!=null && recyclerViewAdapter!=null ){
            rackInfos.addAll(datas);
            recyclerViewAdapter.addData(rackInfosToItems(datas));
        }
    }
    private List<CommonItemLine.ItemStyle> rackInfosToItems(List<RackInfo> datas ){
        List<CommonItemLine.ItemStyle> datass =new ArrayList();
        for(int i=0;i<datas.size();i++){
            RackInfo rackInfo = datas.get(i);
            int index = rackInfos.size() -datas.size()+i;
            datass.add(rackInfoToItem(rackInfo,index));
        }
        return datass;
    }
    private CommonItemLine.ItemStyle rackInfoToItem(RackInfo rackInfo ,int index){
        StringBuffer subText = new StringBuffer().append(rackInfo.getMaxLay()).append("*").append(rackInfo.getPlaceLength());
        String s=null;
        if(!TextUtils.isEmpty( rackInfo.getRemarks() ))subText.append(":").append(rackInfo.getRemarks());
        s = subText.toString();
        if(s.length()>11)s= s.substring(0,10)+"...";
        StringBuffer mainText = new StringBuffer(rackInfo.getRackName());
        String m=null;
        if( !TextUtils.isEmpty(rackInfo.getRackName()) && !TextUtils.isEmpty(rackInfo.getRackCode()))mainText.append(" ; ").append(rackInfo.getRackCode());
        else if( !TextUtils.isEmpty(rackInfo.getRackCode()) )mainText.append(rackInfo.getRackCode()  );
        m=mainText.toString();
        if(m.length()>20)m=m.substring(0,19)+"...";
        CommonItemLine.ItemStyle itemStyle = new CommonItemLine.ItemStyle(
                s,
                m,
                true,
                false );
        itemStyle.setRightIconId(R.drawable.pull_right);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            addColor(itemStyle);
        }
        itemStyle.setRightIconOnClickListenter(view -> {
            Intent intent = new Intent(context, RackDetailActivity.class);
            Bundle bundle =new Bundle();
            bundle.putParcelableArrayList(SystemConstants.RACKS_TRANI,rackInfos );
            bundle.putInt(SystemConstants.INDEX_TRANI , index);
            intent.putExtras(bundle);
            startActivity(intent);
            context.overridePendingTransition(R.anim.in_right,R.anim.out_left);
        });
        return itemStyle;
    }
    @RequiresApi(23)
    private void addColor(CommonItemLine.ItemStyle itemStyle){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            ColorStateList colorStateList = getResources().getColorStateList(R.color.click,null);
            itemStyle.setRightIconColor(colorStateList);
        }
    }
    @Override
    public void editableToggle() {

    }
    @Override
    public boolean isForceTag() {
        return false;
    }

    @Override
    public void accetpMsg(String str) {

    }
}
