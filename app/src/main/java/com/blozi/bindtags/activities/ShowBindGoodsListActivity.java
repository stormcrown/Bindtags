//package com.blozi.blindtags.activities;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.view.KeyEvent;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import com.blozi.blindtags.R;
//import com.blozi.blindtags.asyncTask.online.ShowBindTagGoodsListTask;
//import com.blozi.blindtags.model.GoodsTagMiddle;
//
//import java.util.ArrayList;
//import java.util.List;
///**功能实际被取消*/
//public class ShowBindGoodsListActivity extends CommonActivity  {
//
//    private String goodsTagMiddle;
//    private String goodsBarcode;
//    private String goodsName;
//    private int firstRow;
//    private int totalPages;
//    private ListView list;
//    private List<GoodsTagMiddle> goodsTagMiddleList;
//    private Button is_bind;
//    private Button un_bind;
//    private BaseAdapter adapter;
//    private RefreshLayout myRefreshListView;
//    private String flag;
//
//    public ShowBindGoodsListActivity() {
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        setContentView(R.layout.activity_show_bind_goods_list);
//        super.onCreate(savedInstanceState);
//
//        //选中一个商品
//        list = (ListView) findViewById(R.id.list_view);
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //选中一个商品，跳转到绑定页面
//                GoodsTagMiddle goodsTagMiddle = goodsTagMiddleList.get(position);
//                String tagId = goodsTagMiddle.getTagId();
//                String tagCode = goodsTagMiddle.getTagCode();
//                String tagStatus = goodsTagMiddle.getTagStatus();
//                String goodsId = goodsTagMiddle.getGoodsId();
//                String goodsName = goodsTagMiddle.getGoodsName();
//                String goodsCode = goodsTagMiddle.getGoodsCode();
//                String goodsBarcode = goodsTagMiddle.getGoodsBarcode();
//                String physicalIpAddress = goodsTagMiddle.getPhysicalIpAddress();
//                Intent intent = new Intent(ShowBindGoodsListActivity.this, ShowBindGoodsActivity.class);
//                intent.putExtra("tagInfoId", tagId);
//                intent.putExtra("tagCode", tagCode);
//                intent.putExtra("tagStatus", tagStatus);
//                intent.putExtra("goodsInfoId", goodsId);
//                intent.putExtra("goodsName", goodsName);
//                intent.putExtra("goodsCode", goodsCode);
//                intent.putExtra("tagBarcode", physicalIpAddress);
//                intent.putExtra("goodsBarcode", goodsBarcode);
//                ShowBindGoodsListActivity.this.startActivity(intent);
//                finish();
//            }
//        });
//
//        //显示绑定商品列表
//        is_bind = (Button) findViewById(R.id.is_bind);
//        is_bind.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                is_bind.setBackgroundResource(R.color.blozi_blue);
//                un_bind.setBackgroundResource(0);
//                flag = "n";
////                String webServiceUrl = getPropertiesValue(SystemConstants.PROPERTY_NAME_WEBSERVICE_URL);
//                String webServiceUrl = bloziPreferenceManager.getWebServiceUrl();
//                String url = ShowBindGoodsListActivity.this.getString(R.string.web_service_url_address);
//                webServiceUrl = webServiceUrl+url;
//                ShowBindTagGoodsListTask showBindTagGoodsListService = new ShowBindTagGoodsListTask(ShowBindGoodsListActivity.this, webServiceUrl);
//                showBindTagGoodsListService.execute(userSession, userPass , "", "", flag, "0");
//            }
//        });
//        //显示未绑定商品列表
//        un_bind = (Button) findViewById(R.id.un_bind);
//        un_bind.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                un_bind.setBackgroundResource(R.color.blozi_blue);
//                is_bind.setBackgroundResource(0);
//                flag = "y";
////                String webServiceUrl = getPropertiesValue(SystemConstants.PROPERTY_NAME_WEBSERVICE_URL);
//                String webServiceUrl = bloziPreferenceManager.getWebServiceUrl();
//                String url = ShowBindGoodsListActivity.this.getString(R.string.web_service_url_address);
//                webServiceUrl = webServiceUrl+url;
//                ShowBindTagGoodsListTask showBindTagGoodsListService = new ShowBindTagGoodsListTask(ShowBindGoodsListActivity.this, webServiceUrl);
//                showBindTagGoodsListService.execute(userSession, userPass , "", "", flag, "0");
//            }
//        });
//
//        // 获取RefreshLayout实例
//        myRefreshListView = (RefreshLayout) findViewById(R.id.swipe_layout);
//
//        // 设置下拉刷新监听器
//        myRefreshListView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//
//            @Override
//            public void onRefresh() {
//
//                Toast.makeText(ShowBindGoodsListActivity.this, R.string.refresh_ing, Toast.LENGTH_SHORT).show();
//
//                myRefreshListView.postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        // 更新数据
//                        goodsTagMiddleList.clear();
//                        firstRow = 1;
//                        myRefreshListView.setLoading(false);
//                        if(!"".equals(goodsTagMiddle)&&goodsTagMiddle!=null) {
//                            addValue();
//                        }
//                        adapter.notifyDataSetChanged();
//                        // 更新完后调用该方法结束刷新
//                        myRefreshListView.setRefreshing(false);
//                    }
//                }, 1000);
//            }
//        });
//
//        // 加载监听器
//        myRefreshListView.setOnLoadListener(new RefreshLayout.OnLoadListener() {
//
//            @Override
//            public void onLoad() {
//                if(totalPages == firstRow){
//                    //没有更多数据，不进行加载
//                    Toast.makeText(ShowBindGoodsListActivity.this, R.string.is_bottom, Toast.LENGTH_SHORT).show();
//                    myRefreshListView.setLoading(false);
//                }
//                else{
//                    Toast.makeText(ShowBindGoodsListActivity.this, R.string.is_loading, Toast.LENGTH_SHORT).show();
//                    myRefreshListView.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
////                            String webServiceUrl = getPropertiesValue(SystemConstants.PROPERTY_NAME_WEBSERVICE_URL);
//                            String webServiceUrl = bloziPreferenceManager.getWebServiceUrl();
//                            String url = ShowBindGoodsListActivity.this.getString(R.string.web_service_url_address);
//                            webServiceUrl = webServiceUrl+url;
//                            ShowBindTagGoodsListTask showBindTagGoodsListService = new ShowBindTagGoodsListTask(ShowBindGoodsListActivity.this, webServiceUrl);
//                            showBindTagGoodsListService.execute(userSession, userPass , "", "", flag, String.valueOf(firstRow));
//                        }
//                    }, 1500);
//                }
//            }
//        });
//    }
//
//    public Handler getHandler() {
//        return handler;
//    }
//
//    /**
//     * 处理接口传来的参数
//     */
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            Bundle bundle = msg.getData();
//            goodsTagMiddle = bundle.getString("goodsTagMiddle");
//            goodsName = bundle.getString("goodsName");
//            goodsBarcode = bundle.getString("goodsBarcode");
//            firstRow = Integer.parseInt(bundle.getString("firstRow"))+1;
//            totalPages = Integer.parseInt(bundle.getString("totalPages"));
//            goodsTagMiddleList = new ArrayList();
//            addValue();
//            if (msg.what == 1) {
//                if(goodsTagMiddleList.size()>0){
//                    if(!"".equals(goodsTagMiddle)&&goodsTagMiddle!=null){
//                        adapter = new MyAdapter(ShowBindGoodsListActivity.this, goodsTagMiddleList);
//                        list = (ListView) findViewById(R.id.list_view);
//                        list.setAdapter(adapter);
//                    }
//                    else{
//                        adapter = new ArrayAdapter(
//                                ShowBindGoodsListActivity.this,R.layout.item, 0);
//
//                        list = (ListView) findViewById(R.id.list_view);
//                        list.setAdapter(adapter);
//                    }
//                }
//                else{
//                    Toast.makeText(ShowBindGoodsListActivity.this,"没有找到相关数据",Toast.LENGTH_SHORT).show();
//                    list.setEmptyView(findViewById(R.id.empty));
//                }
//            }
//            else if (msg.what==2){
//                adapter.notifyDataSetChanged();
//                // 加载完后调用该方法
//                myRefreshListView.setLoading(false);
//            }
//        }
//    };
//
//    @Override
//    public boolean onKeyDown(int keyCode,KeyEvent event){
//        switch(keyCode){
//            case KeyEvent.KEYCODE_BACK:
//                Intent intent = new Intent(this,UserInfoActivity.class);
//                this.startActivity(intent);
//                finish();
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    private void addValue(){
//        String[] arr = goodsTagMiddle.split("_");
//        for(String string : arr){
//            String[]value = string.split("=");
//            String[]tag = value[0].split(";");
//            GoodsTagMiddle goodsTagMiddle = new GoodsTagMiddle();
//            if (tag.length>0){
//                goodsTagMiddle.setTagId(tag[0]);
//                goodsTagMiddle.setTagCode(tag[1]);
//                goodsTagMiddle.setTagStatus(tag[2]);
//                goodsTagMiddle.setPhysicalIpAddress(tag[3]);
//            }
//            String[]goods = value[1].split(";");
//            if (goods.length>1){
//                goodsTagMiddle.setGoodsId(goods[0]);
//                goodsTagMiddle.setGoodsName(goods[1]);
//                goodsTagMiddle.setGoodsCode(goods[2]);
//                goodsTagMiddle.setGoodsBarcode(goods[3]);
//            }
//            goodsTagMiddleList.add(goodsTagMiddle);
//        }
//    }
//
//}
