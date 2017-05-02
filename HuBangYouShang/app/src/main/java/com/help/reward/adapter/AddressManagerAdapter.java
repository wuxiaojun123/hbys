package com.help.reward.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.activity.AddAddressActivity;
import com.help.reward.activity.AddressManagerActivity;
import com.help.reward.activity.OrderComplaintsMerchantActivity;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.AddressBean;
import com.help.reward.bean.MyOrderListBean;
import com.help.reward.bean.Response.AddressResponse;
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

    private int lastCheckedIndex = -1;

    public AddressManagerAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_address_manager;
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
//        super.onViewRecycled(holder);
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

        cb_default.setOnCheckedChangeListener(null);

        if (bean.is_default.equals("0")) {
            cb_default.setChecked(false);
        } else {
            cb_default.setChecked(true);
            cb_default.setEnabled(false);
            lastCheckedIndex = position;
        }

        cb_default.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setDefault(position, bean.address_id);
                }
            }
        });

        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, AddAddressActivity.class);
                mIntent.putExtra("AddressBean", bean);
                ((Activity) mContext).startActivityForResult(mIntent, AddressManagerActivity.REQUEST_CODE1);
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
     * 设为默认
     *
     * @param address_id
     */
    private void setDefault(final int position, String address_id) {
        AddressBean bean = (AddressBean) mDataList.get(position);
        LogUtils.e("提交的名称是：" + bean.true_name);
        PersonalNetwork
                .getResponseApi()
                .getSetDefaultAddressResponse(App.APP_CLIENT_KEY, address_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<AddressResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        MyProcessDialog.closeDialog();
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(AddressResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            if (response.data != null) {
                                ToastUtils.show(mContext, response.msg);
                                if (mDataList != null) {
                                    mDataList.clear();
                                }
                                mDataList = response.data.address_list;
                                notifyDataSetChanged();
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    /***
     * 初始化所有的默认地址
     */
    private void initCheckout(int position) {
        if (mDataList != null) {
            if (lastCheckedIndex != -1) {
                AddressBean bean = (AddressBean) mDataList.get(lastCheckedIndex);
                bean.is_default = "0";
                mDataList.set(lastCheckedIndex, bean);
            }
            AddressBean bean2 = (AddressBean) mDataList.get(position);
            bean2.is_default = "1";
            mDataList.set(position, bean2);
            lastCheckedIndex = position;
        }
        notifyDataSetChanged();
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
