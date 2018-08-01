package com.blozi.bindtags.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.blozi.bindtags.R;
import com.blozi.bindtags.view.CommonEditTextLine;
import com.blozi.bindtags.view.CommonItemLine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 骆长涛 on 2018/3/16.
 */

public class FragmentRecyclerViewAdapter extends RecyclerView.Adapter<FragmentRecyclerViewAdapter.ViewHolder> {
    public static final String WhiteEmpty = "WhiteEmpty", CommonItemLineItemStyle = "CommonItemLine.ItemStyle", CommonEditTextLineEditTextLineStyle = "CommonEditTextLineEditTextLineStyle", LoadingView = "loadingView";
    private static final int layout = R.layout.recycler_view_item;
    private Context context;
    private static final int viewHolder_mainViewTag = 1;
    private List datas = new ArrayList();

    private int limitRows;
    private boolean needLoading = false;

    public void setNeedLoading(boolean needLoading) {
        this.needLoading = needLoading;
    }

    public FragmentRecyclerViewAdapter(Context context) {
        this.context = context;
        this.datas = datas;

    }


    public FragmentRecyclerViewAdapter(List datas, Context context) {
        this.context = context;
        this.datas = datas;

    }

    public void setDatas(List datas) {
        this.datas = datas;
        //  if( datas!=null && datas.size()==0)datas.add_orange(WhiteEmpty);
        notifyDataSetChanged();
    }

    public List getDatas() {
        return datas;
    }
    public void setLimitRows(int limitRows) {
        this.limitRows = limitRows;
    }

    public int getLimitRows() {
        return limitRows;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(layout, parent, false);
        //if(datas!=null && index<datas.size()){
        return new ViewHolder(root);
        //  }
        //return new ViewHolder(root, this,null);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.i("onBindViewHolder", "" + position + datas.size());
        if (position < datas.size()) {
            if (datas != null && datas.size() > position && datas.get(position) != null && datas.get(position) instanceof CommonItemLine.ItemStyle) {
                CommonItemLine.ItemStyle itemStyle = (CommonItemLine.ItemStyle) datas.get(position);
                holder.changeCommonItemLine(itemStyle);
            } else if (datas != null && datas.size() > position && datas.get(position) != null && datas.get(position) instanceof CommonEditTextLine.EditTextLineStyle) {
                CommonEditTextLine.EditTextLineStyle style = (CommonEditTextLine.EditTextLineStyle) datas.get(position);
                holder.changeCommonEditTextLine(style);
            } else if (datas != null && datas.size() > position && datas.get(position) != null && datas.get(position) instanceof String) {
                if (WhiteEmpty.equals(datas.get(position))) {
                    holder.setWhiteEmpty();
                }
            } else {
                holder.setWhiteEmpty();
            }
        } else if (needLoading && position == datas.size()) {
            if (position < limitRows) holder.setLoadingView(Boolean.FALSE);
            else holder.setLoadingView(Boolean.TRUE);
        }
    }

    @Override
    public int getItemCount() {
//        if(views!=null)return views.length;
//        else
        if (datas != null && !needLoading) return datas.size();
        else if (datas != null && needLoading) return datas.size() + 1;
        return 0;
    }

    public void remove(int position) {
        datas.remove(position);
        notifyItemRemoved(position);
    }

    public void add(Object o, int position) {
        datas.add(position, o);
        notifyItemInserted(position);
//        notifyDataSetChanged();
    }

    public void addData(List datas) {
        int last = this.datas.size();
        this.datas.addAll(datas);
//        notifyItemInserted(last);
        notifyItemChanged(last);
//        notifyDataSetChanged();
    }

    public void changeData(Object object, int position) {
        datas.set(position, object);
        notifyItemChanged(position);
//        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private FragmentRecyclerViewAdapter mAdapter;
        private Context context;
        private LinearLayout main;
        private Animation recycle;
        private ImageView loadingImage;
        private TextView loadingText;

        {

        }

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            main = itemView.findViewById(R.id.recyclerView_root);
            recycle = AnimationUtils.loadAnimation(context, R.anim.recycle);
        }

        public ViewHolder(View itemView, FragmentRecyclerViewAdapter adapter, Object view) {
            super(itemView);
            context = itemView.getContext();
            mAdapter = adapter;
            main = itemView.findViewById(R.id.recyclerView_root);
            recycle = AnimationUtils.loadAnimation(context, R.anim.recycle);

        }

        private void hideLoadingView() {
            View view = main.findViewWithTag(LoadingView);
            if (view != null) view.setVisibility(View.GONE);
        }

        /**
         * 设置加载动画
         */
        public void setLoadingView(Boolean end) {
            View view = main.findViewWithTag(LoadingView);
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                view = inflater.inflate(R.layout.common_loading, null);
                view.setTag(LoadingView);
                main.addView(view);
            }
            loadingImage = view.findViewById(R.id.loadingImageView_image);
            loadingText = view.findViewById(R.id.loadingImageView_text);
            recycle.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    loadingImage.startAnimation(recycle);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            loadingImage.startAnimation(recycle);
            LinearLayout subLinear = view.findViewById(R.id.subLinear);
            if (end) {
                loadingText.setText(R.string.noMoreData);
                loadingImage.setVisibility(View.GONE);
                subLinear.setVisibility(View.GONE);
            } else {
                loadingText.setText(R.string.loading);
                loadingImage.setVisibility(View.VISIBLE);
                subLinear.setVisibility(View.VISIBLE);
            }
            view.setVisibility(View.VISIBLE);
            hideCommonEditTextLine();
            hideWhiteEmpty();
            hideCommonItemLine();

        }

        private void hideCommonItemLine() {
            CommonItemLine commonItemLine = main.findViewWithTag(CommonItemLineItemStyle);
            if (commonItemLine != null) commonItemLine.setVisibility(View.GONE);
        }

        /**
         * 简单item
         */
        public void changeCommonItemLine(CommonItemLine.ItemStyle itemStyle) {
            CommonItemLine commonItemLine = main.findViewWithTag(CommonItemLineItemStyle);
            if (commonItemLine == null) {
                commonItemLine = new CommonItemLine(context);
                commonItemLine.setTag(CommonItemLineItemStyle);
                main.addView(commonItemLine);
            }
            commonItemLine.setIntemStyle(itemStyle);
            commonItemLine.setVisibility(View.VISIBLE);
            hideCommonEditTextLine();
            hideWhiteEmpty();
            hideLoadingView();
        }

        private void hideCommonEditTextLine() {
            CommonEditTextLine commonEditTextLine = main.findViewWithTag(CommonEditTextLineEditTextLineStyle);
            if (commonEditTextLine != null) commonEditTextLine.setVisibility(View.GONE);
        }

        /**
         * 可编辑的item
         */
        public void changeCommonEditTextLine(CommonEditTextLine.EditTextLineStyle editTextLineStyle) {
            CommonEditTextLine commonEditTextLine = main.findViewWithTag(CommonEditTextLineEditTextLineStyle);
            if (commonEditTextLine == null) {
                commonEditTextLine = new CommonEditTextLine(context);
                commonEditTextLine.setTag(CommonEditTextLineEditTextLineStyle);
                main.addView(commonEditTextLine);
            }
            commonEditTextLine.setEditTextLineStyle(editTextLineStyle);
            commonEditTextLine.setVisibility(View.VISIBLE);
            hideCommonItemLine();
            hideWhiteEmpty();
            hideLoadingView();
        }

        private void hideWhiteEmpty() {
            LinearLayout whiteEmpty = main.findViewWithTag(WhiteEmpty);
            if (whiteEmpty != null) whiteEmpty.setVisibility(View.GONE);
        }

        /**
         * 空白
         */
        public void setWhiteEmpty() {
            LinearLayout whiteEmpty = main.findViewWithTag(WhiteEmpty);
            if (whiteEmpty == null) {
                whiteEmpty = new LinearLayout(context);
                whiteEmpty.setTag(WhiteEmpty);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 60);
                layoutParams.setMargins(0, 0, 0, 0);
                whiteEmpty.setLayoutParams(layoutParams);
                whiteEmpty.setBackgroundColor(context.getResources().getColor(R.color.white));
                main.addView(whiteEmpty);
            }
            hideCommonEditTextLine();
            hideCommonItemLine();
            hideLoadingView();
            whiteEmpty.setVisibility(View.VISIBLE);
        }


    }

}
