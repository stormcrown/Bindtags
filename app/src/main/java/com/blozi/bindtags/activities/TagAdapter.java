package com.blozi.bindtags.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blozi.bindtags.R;

import java.util.List;

/**
 * Created by fly_liu on 2017/8/14.
 */
public class TagAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;
    public TagAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder = new ViewHolder();
        view = LayoutInflater.from(context).inflate(R.layout.barcode_tag, null);
        holder.tag_barcode = (TextView) view.findViewById(R.id.tag_barcode);
        view.setTag(holder);
        holder.tag_barcode.setText(list.get(i));

        return view;
    }
    class ViewHolder{
        TextView tag_barcode;
    }
}
