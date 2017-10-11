package com.help.reward.adapter.viewholder;

import android.content.Context;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.adapter.BaseRecyclerAdapter;
import com.help.reward.bean.CouponRecordBean;
import com.idotools.utils.DateUtil;

/**
 * Created by wuxiaojun on 2017/2/26.
 */

public class PointsRecordAdapter extends BaseRecyclerAdapter {

    private String couponsContent;

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

        CouponRecordBean bean = (CouponRecordBean) mDataList.get(position);
        //CouponsRecordResponse.GiveInfo bean = (CouponsRecordResponse.GiveInfo) mDataList.get(position);
        String content = "满"+bean.tpl_info.voucher_t_limit +"减"+ bean.tpl_info.voucher_t_price;
        tv_content.setText(couponsContent);

        tv_num.setText("已领取" + bean.give_info.num_given+"/"+bean.give_info.num);
        tv_date.setText(DateUtil.getDateToString(bean.created));
    }

    public void setCouponsContent(String couponsContent) {
        this.couponsContent = couponsContent;
    }
}
