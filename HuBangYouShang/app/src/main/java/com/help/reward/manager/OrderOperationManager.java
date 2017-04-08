package com.help.reward.manager;

import android.content.Context;
import android.view.View;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.adapter.MyOrderAdapter;
import com.help.reward.bean.MyOrderListBean;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.view.AlertDialog;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.ToastUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wuxiaojun on 2017/4/8.
 */

public class OrderOperationManager {

    private Context mContext;
    private MyOrderAdapter myOrderAdapter;

    public OrderOperationManager(Context mContext,MyOrderAdapter myOrderAdapter){
        this.mContext = mContext;
        this.myOrderAdapter = myOrderAdapter;
    }


    /***
     * 删除订单
     */
    public void showRemoveDialog(final MyOrderListBean.OrderList bean) {
        new AlertDialog(mContext).builder().setTitle(R.string.string_system_prompt).setMsg("确认删除订单?")
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeOrder(bean);
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();
    }

    private void removeOrder(final MyOrderListBean.OrderList bean) {
        MyProcessDialog.showDialog(mContext, "请稍后...");
        PersonalNetwork
                .getResponseApi()
                .getRemoveOrderResponse(bean.order_id, App.APP_CLIENT_KEY)
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
                            if (response.data != null) { // 显示数据
                                myOrderAdapter.getDataList().remove(bean);
                                myOrderAdapter.notifyDataSetChanged();
                                ToastUtils.show(mContext, response.msg);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    /***
     * 取消订单
     * @param bean
     */
    public void showCancelDialog(final MyOrderListBean.OrderList bean) {
        new AlertDialog(mContext).builder().setTitle(R.string.string_system_prompt).setMsg("确认取消订单?")
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelOrder(bean);
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();
    }

    private void cancelOrder(final MyOrderListBean.OrderList bean) {
        MyProcessDialog.showDialog(mContext, "请稍后...");
        PersonalNetwork
                .getResponseApi()
                .getCancelOrderResponse(bean.order_id, App.APP_CLIENT_KEY)
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
                            if (response.data != null) { // 显示数据
                                myOrderAdapter.getDataList().remove(bean);
                                myOrderAdapter.notifyDataSetChanged();
                                ToastUtils.show(mContext, response.msg);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

}