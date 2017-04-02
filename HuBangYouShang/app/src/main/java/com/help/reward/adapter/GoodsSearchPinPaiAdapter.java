package com.help.reward.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.bean.PinPaiBean;

import java.util.ArrayList;
import java.util.List;


/**
 * 搜索商品筛选品牌
 */

public class GoodsSearchPinPaiAdapter extends BaseAdapter {
    Activity mActivity;
    List<PinPaiBean> mDatas = new ArrayList<>();
    private LayoutInflater mInflater;
    String b_id = "";
    boolean isShowAll = false;

    public GoodsSearchPinPaiAdapter(Activity context) {
        this.mActivity = context;
        mInflater = LayoutInflater.from(context);

    }

    public void setShowAll() {
        isShowAll = !isShowAll;
        notifyDataSetChanged();
    }

    public void setDatas(List<PinPaiBean> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    public void setB_id(String b_id) {
        this.b_id = b_id;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mDatas == null) {
            return 0;
        }
        if (!isShowAll) {
            return mDatas.size() > 6 ? 6 : mDatas.size();
        }
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder vh = null;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.item_goodssearchpinpai, null);
            vh = new ViewHolder();
            vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        PinPaiBean item = mDatas.get(position);
        vh.tv_name.setText(item.b_name);
        if (b_id.equals(item.b_id)) {
            vh.tv_name.setTextColor(Color.parseColor("#ffffff"));
            vh.tv_name.setBackgroundResource(R.drawable.fa372d_bg);
        } else {
            vh.tv_name.setTextColor(Color.parseColor("#3a4a6b"));
            vh.tv_name.setBackgroundResource(R.drawable.dcdcdc_f8f8f9_bg);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_name;
    }


}