package com.reward.help.merchant.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.reward.help.merchant.R;
import com.reward.help.merchant.adapter.viewholder.SuperViewHolder;
import com.reward.help.merchant.bean.CouponListBean;

/**
 * Created by fanjunqing on 21/03/2017.
 */

public class CouponListAdapter extends BaseRecyclerAdapter<CouponListBean>{

    public CouponListAdapter(Context context) {
        super(context);
    }
    @Override
    public int getLayoutId() {
        return R.layout.item_coupon_list;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        CouponListBean item = mDataList.get(position);
        TextView mTvDiscount = holder.getView(R.id.tv_couponitem_discount);
        TextView mTvLimit = holder.getView(R.id.tv_couponitem_limit);
        TextView mTvStore = holder.getView(R.id.tv_couponitem_store);
        TextView mTvDate = holder.getView(R.id.tv_couponitem_date);
        TextView mTvCount = holder.getView(R.id.tv_couponitem_count);
        ImageView mIvCheck = holder.getView(R.id.iv_coupon_checked);

        mTvDiscount.setText(item.getDiscount());
        mTvLimit.setText(item.getLimit());
        mTvStore.setText(item.getStoreName());
        mTvDate.setText(item.getDate());
        mTvCount.setText(item.getCount());

        if (item.isChecked()) {
            mIvCheck.setImageResource(R.mipmap.coupon_xuan_a);
        } else {
            mIvCheck.setImageResource(R.mipmap.coupon_xuan_b);
        }
    }
}
