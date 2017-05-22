package com.help.reward.adapter;

import android.content.Context;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.MyBalanceBean;
import com.idotools.utils.DateUtil;
import com.idotools.utils.LogUtils;

/**
 * 我的账户---余额明细
 * Created by wuxiaojun on 2017/2/26.
 */

public class MyBalanceAdapter extends BaseRecyclerAdapter {

    public MyBalanceAdapter(Context context) {
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


        MyBalanceBean bean = (MyBalanceBean) mDataList.get(position);

        tv_title.setText(bean.lg_desc);
        tv_price.setText(bean.lg_av_amount);
        LogUtils.e("时间戳是："+bean.lg_add_time);
        tv_date.setText(DateUtil.getDateToString(bean.lg_add_time));

    }

}
