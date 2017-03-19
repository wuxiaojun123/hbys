package com.help.reward.adapter;

import android.content.Context;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.HelpRewardBean;
import com.idotools.utils.DateUtil;

/**
 * 优惠劵交易大厅
 * Created by wuxiaojun on 2017/2/26.
 */

public class CouponTradingAdapter extends BaseRecyclerAdapter {

    public CouponTradingAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_coupon_trading;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
       /* TextView tv_title = holder.getView(R.id.tv_title);
        TextView tv_price = holder.getView(R.id.tv_price);
        TextView tv_date = holder.getView(R.id.tv_date);


        HelpRewardBean bean = (HelpRewardBean) mDataList.get(position);

        tv_title.setText(bean.pl_desc);
        tv_price.setText(bean.pl_points);
        tv_date.setText(DateUtil.getDateToString(Long.parseLong(bean.pl_addtime)));*/

    }



}
