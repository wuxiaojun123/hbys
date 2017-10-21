package com.reward.help.merchant.adapter;

import android.content.Context;
import android.widget.TextView;

import com.idotools.utils.DateUtil;
import com.reward.help.merchant.R;
import com.reward.help.merchant.adapter.viewholder.SuperViewHolder;
import com.reward.help.merchant.bean.GroupCouponBean;

/**
 * Created by wuxiaojun on 2017/2/26.
 */

public class CouponRecordAdapter extends BaseRecyclerAdapter<GroupCouponBean> {

    private String couponsContent;

    public CouponRecordAdapter(Context context) {
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

        GroupCouponBean bean = mDataList.get(position);
        //CouponsRecordResponse.GiveInfo bean = (CouponsRecordResponse.GiveInfo) mDataList.get(position);
        String content = "满"+bean.tpl_info.voucher_t_limit +"减"+ bean.tpl_info.voucher_t_price;
        tv_content.setText(content);

        tv_num.setText("已领取" + bean.num_given+"/"+bean.num);
        tv_date.setText(DateUtil.getDateToString(bean.created));
    }

    public void setCouponsContent(String couponsContent) {
        this.couponsContent = couponsContent;
    }
}
