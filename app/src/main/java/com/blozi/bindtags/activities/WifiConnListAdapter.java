package com.blozi.bindtags.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blozi.bindtags.R;

import java.util.ArrayList;

/**
 * Created by fly_liu on 2017/8/10.
 */

public class WifiConnListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<WifiElement> mArr;

    public WifiConnListAdapter(Context context, ArrayList<WifiElement> list) {
        this.context = context;
        this.mArr = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mArr.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mArr.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = LayoutInflater.from(context).inflate(R.layout.widget_wifi_conn_lv, null);
        TextView ssid = (TextView) view.findViewById(R.id.wifi_conn_name);
        TextView level = (TextView) view.findViewById(R.id.wifi_conn_wpe);
        String name = mArr.get(position).getSsid();
        if(name.length()>15){
            name = name.substring(0,14)+"...";
        }
        ssid.setText(name);
        String level1 = String.valueOf(mArr.get(position).getLevel());
        level.setText(level1);
        return view;
    }

}
