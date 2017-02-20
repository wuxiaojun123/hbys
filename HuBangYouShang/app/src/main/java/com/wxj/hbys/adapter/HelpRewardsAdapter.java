package com.wxj.hbys.adapter;

import android.content.Context;

import com.wxj.hbys.R;

import java.util.List;

/**
 * Created by MXY on 2017/2/19.
 */

public class HelpRewardsAdapter extends CommonAdapter<String> {
    public HelpRewardsAdapter(Context context, List<String> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, String s) {
        holder.setText(R.id.item_help1_score,"获赏 4分");
    }
}
