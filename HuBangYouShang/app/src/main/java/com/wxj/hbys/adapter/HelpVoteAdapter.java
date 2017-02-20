package com.wxj.hbys.adapter;

import android.content.Context;
import android.view.View;

import com.wxj.hbys.R;

import java.util.List;

/**
 * Created by MXY on 2017/2/19.
 */

public class HelpVoteAdapter extends CommonAdapter<String> {
    public HelpVoteAdapter(Context context, List<String> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, String s) {
        holder.getView(R.id.item_help1_score).setVisibility(View.GONE);
        holder.getView(R.id.item_help1_count).setVisibility(View.GONE);
    }
}
