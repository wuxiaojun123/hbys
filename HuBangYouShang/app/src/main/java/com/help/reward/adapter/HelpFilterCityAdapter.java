package com.help.reward.adapter;

import android.content.Context;

import com.help.reward.R;
import com.help.reward.bean.AreaBean;

import java.util.List;

/**
 * Created by MXY on 2017/2/19.
 */

public class HelpFilterCityAdapter extends CommonAdapter<AreaBean> {
    public HelpFilterCityAdapter(Context context, List<AreaBean> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, AreaBean s) {
        holder.setText(R.id.tv_item_fiter, s.area_name);
    }
}
