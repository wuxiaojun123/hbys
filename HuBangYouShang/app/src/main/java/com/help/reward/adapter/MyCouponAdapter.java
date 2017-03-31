package com.help.reward.adapter;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.MyCouponBean;
import com.idotools.utils.DateUtil;

/**
 * 优惠劵
 * Created by wuxiaojun on 2017/2/26.
 */

public class MyCouponAdapter extends BaseRecyclerAdapter {

    private String voucher_state = "1"; // 1 未使用 2 已使用 3 已过期
    private int color1;
    private int color2;

    public MyCouponAdapter(Context context,String voucher_state) {
        super(context);
        this.voucher_state = voucher_state;
        color1 = mContext.getResources().getColor(R.color.color_fa);
        color2 = mContext.getResources().getColor(R.color.color_d2);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_my_coupon;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        TextView tv_price = holder.getView(R.id.tv_price);
        TextView tv_price_bottom = holder.getView(R.id.tv_price_bottom);
        TextView tv_store_name = holder.getView(R.id.tv_store_name);
        TextView tv_date = holder.getView(R.id.tv_date);
        TextView tv_state = holder.getView(R.id.tv_state);
        LinearLayout ll_left = holder.getView(R.id.ll_left); // 根据不同状态显示不同的颜色

        if(voucher_state.equals("1")){
            ll_left.setBackgroundColor(color1);
        }else{
            ll_left.setBackgroundColor(color2);
        }

        MyCouponBean bean = (MyCouponBean) mDataList.get(position);

        tv_price.setText(bean.voucher_price);
        tv_price_bottom.setText("满"+bean.voucher_limit+"可用");
        tv_store_name.setText(bean.store_name);
        tv_date.setText(DateUtil.getDateToString(Long.parseLong(bean.voucher_start_date))+"-"+DateUtil.getDateToString(Long.parseLong(bean.voucher_end_date)));
        tv_state.setText(bean.voucher_state_text);
    }



}
