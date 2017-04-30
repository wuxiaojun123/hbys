package com.help.reward.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.activity.AddAddressActivity;
import com.help.reward.activity.OrderComplaintsMerchantActivity;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.AddressBean;
import com.help.reward.bean.MyOrderListBean;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.view.AlertDialog;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        TextView tv_name = holder.getView(R.id.tv_name);
        TextView tv_mobile = holder.getView(R.id.tv_mobile);
        TextView tv_address = holder.getView(R.id.tv_address);
        CheckBox cb_default = holder.getView(R.id.cb_default);
        TextView tv_edit = holder.getView(R.id.tv_edit);
        TextView tv_remove = holder.getView(R.id.tv_remove);

        final AddressBean bean = (AddressBean) mDataList.get(position);

        tv_name.setText(bean.true_name);
        tv_mobile.setText(bean.mob_phone);
        tv_address.setText(bean.address);
        if (bean.is_default.equals("0")) {
            cb_default.setChecked(false);
        } else {
            cb_default.setChecked(true);
        }

        cb_default.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LogUtils.e("是否选中isChecked=" + isChecked);
                if (isChecked) { // 选中
                    initCheckout();
                    bean.is_default = "1";
                } else { // 未选中
                    bean.is_default = "0";
                }
                mDataList.set(position, bean);
                notifyDataSetChanged();
            }
        });

        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, AddAddressActivity.class);
                mIntent.putExtra("address_id", bean.address_id);
                mContext.startActivity(mIntent);
                ActivitySlideAnim.slideInAnim((Activity) mContext);
            }
        });
        tv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRemove(bean, position);
            }
        });

    }

    /***
     * 初始化所有的默认地址
     */
    private void initCheckout() {
        if (mDataList != null) {
            int size = mDataList.size();
            for (int i = 0; i < size; i++) {
                AddressBean bean = (AddressBean) mDataList.get(i);
                bean.is_default = "0";
                mDataList.set(i, bean);
            }
        }
    }

    /***
     * 确认删除
     */
    private void showDialogRemove(final AddressBean bean, final int position) {
        new AlertDialog(mContext).builder()
                .setTitle(R.string.exit_title).setMsg("确认要删除此收货地址吗?")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { // 请求接口
                        requestRemove(bean.address_id, position);
                    }
                }).show();
    }

    private void requestRemove(String address_id, final int position) {
        MyProcessDialog.showDialog(mContext, "请稍后...", true, false);
        PersonalNetwork
                .getResponseApi()
                .getRemoveAddressResponse(App.APP_CLIENT_KEY, address_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<String>>() {
                    @Override
                    public void onError(Throwable e) {
                        MyProcessDialog.closeDialog();
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(BaseResponse<String> response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            if (response.data != null) { // 删除成功
                                ToastUtils.show(mContext, response.msg);
                                remove(position);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

}
