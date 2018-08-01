package com.blozi.bindtags.adapter;

import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blozi.bindtags.R;
import com.blozi.bindtags.model.TreeData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Richie on 2017.07.31
 *         改过 Indicator 的 ExpandableListView 的适配器
 */
public class IndicatorExpandableListAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "IndicatorExpandableList";
    private List<TreeData> groupData = new ArrayList<>();
    //                用于存放Indicator的集合
    private SparseArray<ImageView> mIndicators;
    private OnGroupExpandedListener mOnGroupExpandedListener;

    public IndicatorExpandableListAdapter() {
        mIndicators = new SparseArray<>();
    }
    public void addGroup(TreeData group){
        for(int i=0;i<groupData.size();i++){
            if( groupData.get(i)!=null && groupData.get(i).getHideId()!=null && groupData.get(i).getHideId().equals(group.getHideId()) ){
                if(group.getChildren()!=null){
                    for( int j=0; j<group.getChildren().size();j++ ){
                        groupData.get(i).addTopChild(group.getChildren().get(j));
                    }
                }
                return ;
            }
        }
        groupData.add(group);
    }

    public List<TreeData> getGroupData() {
        return groupData;
    }

    public void setGroupData(List<TreeData> groupData) {
        this.groupData = groupData;
    }

    public void setOnGroupExpandedListener(OnGroupExpandedListener onGroupExpandedListener) {
        mOnGroupExpandedListener = onGroupExpandedListener;
    }

    //            根据分组的展开闭合状态设置指示器
    public void setIndicatorState(int groupPosition, boolean isExpanded) {
        if (isExpanded) {
            mIndicators.get(groupPosition).setImageResource(R.drawable.pull_down_orange);
        } else {
            mIndicators.get(groupPosition).setImageResource(R.drawable.pull_up);
        }
    }

    @Override
    public int getGroupCount() {
        return groupData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groupData.get(groupPosition).getChildren().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groupData.get(groupPosition).getChildren().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expand_group_indicator, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.label_group_indicator);
            groupViewHolder.ivIndicator = (ImageView) convertView.findViewById(R.id.iv_indicator);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.tvTitle.setText(groupData.get(groupPosition).getName());
        //      把位置和图标添加到Map
        mIndicators.put(groupPosition, groupViewHolder.ivIndicator);
        //      根据分组状态设置Indicator
        setIndicatorState(groupPosition, isExpanded);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View
            convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expand_child, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.label_expand_child);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.tvTitle.setText(groupData.get(groupPosition).getChildren().get(childPosition).getName() );
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        Log.d(TAG, "onGroupExpanded() called with: groupPosition = [" + groupPosition + "]");
        if (mOnGroupExpandedListener != null) {
            mOnGroupExpandedListener.onGroupExpanded(groupPosition);
        }
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        Log.d(TAG, "onGroupCollapsed() called with: groupPosition = [" + groupPosition + "]");
    }

    private static class GroupViewHolder {
        TextView tvTitle;
        ImageView ivIndicator;
    }

    private static class ChildViewHolder {
        TextView tvTitle;
    }
}
