package com.blozi.bindtags.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blozi.bindtags.R;
import com.blozi.bindtags.model.GoodsTagMiddle;

import java.util.List;

/**
 * Created by fly_liu on 2017/7/10.
 */

public class MyAdapter extends BaseAdapter {
    private Context context;
    private List<GoodsTagMiddle> list;
    public MyAdapter(Context context, List<GoodsTagMiddle> list) {
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView  = LayoutInflater.from(context).inflate(R.layout.item, null);
            holder = new ViewHolder();

            holder.tag_code = (TextView) convertView.findViewById(R.id.tag_code);
            holder.goods_name = (TextView) convertView.findViewById(R.id.goods_name);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tag_code.setText(list.get(position).getTagCode());
        String goodsName = list.get(position).getGoodsName();
        if(!"".equals(goodsName)&&goodsName!=null){
            holder.goods_name.setText(goodsName);
        }
        else{
            holder.goods_name.setText(R.string.is_not_bind);
        }
        return convertView;
    }
    class ViewHolder{
        TextView tag_code,goods_name;
    }
}
