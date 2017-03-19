package com.help.reward.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.CouponTradingBean;
import com.help.reward.bean.HelpRewardBean;
import com.help.reward.bean.MyCouponBean;
import com.idotools.utils.DateUtil;
import com.idotools.utils.LogUtils;

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
        TextView tv_price = holder.getView(R.id.tv_price);
        TextView tv_price_bottom = holder.getView(R.id.tv_price_bottom);
        TextView tv_store_name = holder.getView(R.id.tv_store_name);
        TextView tv_date = holder.getView(R.id.tv_date);
        TextView tv_score = holder.getView(R.id.tv_score);
        TextView tv_coupon = holder.getView(R.id.tv_coupon);

        CouponTradingBean bean = (CouponTradingBean) mDataList.get(position);

        tv_price.setText(bean.voucher_price);
        tv_price_bottom.setText("满"+bean.voucher_limit+"可用");
        tv_store_name.setText(bean.voucher_t_storename);
        tv_date.setText(DateUtil.getDateToString(Long.parseLong(bean.voucher_start_date))+"-"+DateUtil.getDateToString(Long.parseLong(bean.voucher_end_date)));
        tv_score.setText(bean.voucher_owner_setting);
        tv_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击交易
                LogUtils.e("我要交易"+position);
            }
        });
    }

}
