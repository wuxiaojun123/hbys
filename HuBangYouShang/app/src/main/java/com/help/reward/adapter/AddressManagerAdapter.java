package com.help.reward.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.AddressBean;
import com.help.reward.bean.Response.MyHelpPostResponse;
import com.idotools.utils.DateUtil;

/**
 * 地址管理
 * Created by wuxiaojun on 2017/2/26.
 */

public class AddressManagerAdapter extends BaseRecyclerAdapter {

    public AddressManagerAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_address_manager;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        TextView tv_name = holder.getView(R.id.tv_name);
        TextView tv_mobile = holder.getView(R.id.tv_mobile);
        TextView tv_address = holder.getView(R.id.tv_address);
        CheckBox cb_default = holder.getView(R.id.cb_default);
        TextView tv_edit = holder.getView(R.id.tv_edit);
        TextView tv_remove = holder.getView(R.id.tv_remove);

        AddressBean bean = (AddressBean) mDataList.get(position);

        tv_name.setText(bean.true_name);
        tv_mobile.setText(bean.mob_phone);
        tv_address.setText(bean.address);
        if (bean.is_default.equals("0")) {
            cb_default.setChecked(false);
        } else {
            cb_default.setChecked(true);
        }

        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

}
