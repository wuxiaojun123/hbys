package com.help.reward.adapter;

import android.content.Context;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.GeneralVolumeBean;
import com.idotools.utils.DateUtil;

/**
 * 我的账户---通用卷明细
 * Created by wuxiaojun on 2017/2/26.
 */

public class MyGeneralVolumeAdapter extends BaseRecyclerAdapter {

    public MyGeneralVolumeAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_my_balance;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        TextView tv_title = holder.getView(R.id.tv_title);
        TextView tv_price = holder.getView(R.id.tv_price);
        TextView tv_date = holder.getView(R.id.tv_date);


        GeneralVolumeBean bean = (GeneralVolumeBean) mDataList.get(position);

        tv_title.setText(bean.gvl_desc);
        tv_price.setText(bean.gvl_points);
        tv_date.setText(DateUtil.getDateToString(bean.gvl_addtime));

    }



}
