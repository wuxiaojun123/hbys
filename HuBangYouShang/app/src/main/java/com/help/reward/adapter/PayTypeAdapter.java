package com.help.reward.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.Response.PayTypeResponse;

/**
 * Created by wuxiaojun on 17-6-21.
 */

public class PayTypeAdapter extends BaseRecyclerAdapter {


    public PayTypeAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_pay_type;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {

        TextView tv_pay_sn = holder.getView(R.id.tv_pay_sn);
        TextView tv_amount = holder.getView(R.id.tv_amount);

        PayTypeResponse.PayTypeOrderBean bean = (PayTypeResponse.PayTypeOrderBean) mDataList.get(position);
        tv_pay_sn.setText(bean.order_sn);
        tv_amount.setText("￥"+bean.order_amount);

    }


}
