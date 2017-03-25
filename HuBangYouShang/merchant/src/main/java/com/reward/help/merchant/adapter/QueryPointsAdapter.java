package com.reward.help.merchant.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.reward.help.merchant.R;
import com.reward.help.merchant.adapter.viewholder.SuperViewHolder;
import com.reward.help.merchant.bean.CouponLogBean;
import com.reward.help.merchant.bean.PointsLogBean;

public class QueryPointsAdapter extends BaseRecyclerAdapter<PointsLogBean.Member> {

    public QueryPointsAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_query_coupon_points;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        PointsLogBean.Member member = mDataList.get(position);
        ImageView mIvPhoto = holder.getView(R.id.iv_member_photo);
        TextView mTvName = holder.getView(R.id.tv_member_name);
        TextView mTvDate = holder.getView(R.id.tv_member_date);
        TextView mTvDes = holder.getView(R.id.tv_coupon_point_des);

        mTvName.setText(member.member_name);
        mTvDate.setText(member.created);
        mTvDes.setText(member.num);
    }
}
