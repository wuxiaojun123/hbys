package com.help.reward.adapter;

import android.content.Context;

import com.help.reward.R;
import com.help.reward.bean.HelpBoardBean;

import java.util.List;

/**
 * Created by MXY on 2017/2/19.
 */

public class HelpFilterTypeAdapter extends CommonAdapter<HelpBoardBean>{
    public HelpFilterTypeAdapter(Context context, List<HelpBoardBean> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, HelpBoardBean s) {
        holder.setText(R.id.tv_item_fiter,s.board_name);
    }
}
