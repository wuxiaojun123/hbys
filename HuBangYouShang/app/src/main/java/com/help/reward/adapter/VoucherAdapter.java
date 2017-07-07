package com.help.reward.adapter;

import android.content.Context;

import com.help.reward.R;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.VoucherBean;


/**
 * Created by wuxiaojun on 17-7-7.
 */

public class VoucherAdapter extends BaseRecyclerAdapter<VoucherBean> {


    public VoucherAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_dialog_voucher;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {

    }


}
