package com.help.reward.adapter;

import android.content.Context;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.CouponRecordBean;
import com.help.reward.bean.GroupGrantHelpPointsBean;
import com.help.reward.bean.PointRecordBean;
import com.idotools.utils.DateUtil;

/**
 * Created by wuxiaojun on 2017/2/26.
 */

public class PointsRecordAdapter extends BaseRecyclerAdapter<GroupGrantHelpPointsBean> {


    public PointsRecordAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_coupons_record;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        TextView tv_content = holder.getView(R.id.record_info);
        TextView tv_num = holder.getView(R.id.tv_receive_cp);
        TextView tv_date = holder.getView(R.id.tv_receive_date);

        GroupGrantHelpPointsBean bean = mDataList.get(position);
        //CouponsRecordResponse.GiveInfo bean = (CouponsRecordResponse.GiveInfo) mDataList.get(position);
        tv_content.setText(bean.total_num);
        tv_num.setText("已领取" + bean.people_received+"/"+bean.num_given);
        tv_date.setText(DateUtil.getDateToString(bean.created));
    }

}
