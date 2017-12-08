package com.help.reward.biz;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.rxbus.RxBus;
import com.help.reward.rxbus.event.type.BooleanRxbusType;
import com.help.reward.view.AlertEditTextDialog;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.ToastUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我的订单业务类
 * Created by wuxiaojun on 2017/12/8.
 */

public class MyOrderBiz {
    private Context mContext;

    public MyOrderBiz(Context context){
        this.mContext = context;
    }

    public void showDialogConfirmReceiver(final String order_id) {
        final AlertEditTextDialog dialog = new AlertEditTextDialog(mContext).builder();
        dialog.setTitle("确认收货");
        dialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override public void onClick(View v) {
                String pwdEdit = dialog.getEditText();
                if(TextUtils.isEmpty(pwdEdit)){
                    ToastUtils.show(mContext,"请输入收货密码");
                    return;
                }
                confirmReceiver(order_id,pwdEdit);
            }
        });
        dialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override public void onClick(View v) {
            }
        });
        dialog.show();
    }

    /***
     * 确认收货
     */
    private void confirmReceiver(String order_id,String payPwd) {
        PersonalNetwork
                .getResponseApi()
                .getConfirmReceiveResponse(App.APP_CLIENT_KEY,order_id,payPwd)
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
                                ToastUtils.show(mContext, response.msg);
                            }
                            if(mOnSuccessConfirmReceiverListener != null){
                                mOnSuccessConfirmReceiverListener.onSuccessConfirmReceiverListener();
                            }
                            //刷新当前数据,发送给MyOrderAllFragment
                            RxBus.getDefault().post(new BooleanRxbusType(true));
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    public interface OnSuccessConfirmReceiverListener{
        void onSuccessConfirmReceiverListener();
    }

    private OnSuccessConfirmReceiverListener mOnSuccessConfirmReceiverListener;

    public void setOnSuccessConfirmReceiverListener(OnSuccessConfirmReceiverListener listener){
        this.mOnSuccessConfirmReceiverListener = listener;
    }

}
