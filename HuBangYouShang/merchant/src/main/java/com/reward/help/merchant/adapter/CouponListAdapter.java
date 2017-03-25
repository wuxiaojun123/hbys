package com.reward.help.merchant.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.widget.ImageView;
import android.widget.TextView;

import com.reward.help.merchant.R;
import com.reward.help.merchant.adapter.viewholder.SuperViewHolder;
import com.reward.help.merchant.bean.CouponListBean;
import com.reward.help.merchant.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanjunqing on 21/03/2017.
 */

public class CouponListAdapter extends BaseRecyclerAdapter<CouponListBean>{

    private List<CouponListBean> mCheckList;

    public CouponListAdapter(Context context) {
        super(context);
        mCheckList = new ArrayList<CouponListBean>();
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

        mTvDiscount.setText(String.format(mContext.getString(R.string.format_discount_money),item.getVoucher_t_price()));
        mTvLimit.setText(String.format(mContext.getString(R.string.format_limit_money),item.getVoucher_t_limit()));
        mTvStore.setText(item.getVoucher_t_storename());
        mTvDate.setText(DateUtils.strDateToStr(item.getVoucher_t_start_date()) + "-" + DateUtils.strDateToStr(item.getVoucher_t_end_date()));
        mTvCount.setText("剩余" +item.getVoucher_t_total());

        for (CouponListBean mCoupon: mCheckList){
            if (item.getVoucher_t_id().equals(mCoupon.getVoucher_t_id())) {
                mIvCheck.setImageResource(R.mipmap.coupon_xuan_a);
            } else {
                mIvCheck.setImageResource(R.mipmap.coupon_xuan_b);
            }
        }
    }

    public void setmCheckList(List<CouponListBean> checkList) {
        this.mCheckList.clear();
        if (checkList != null) {
            this.mCheckList.addAll(checkList);
        }
        notifyDataSetChanged();
    }
}
