package com.help.reward.view;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.Response.StringResponse;
import com.help.reward.network.HelpNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.rxbus.RxBus;
import com.help.reward.rxbus.event.type.UpdateLoginDataRxbusType;
import com.help.reward.utils.ScreenUtils;
import com.help.reward.utils.StringUtils;
import com.idotools.utils.ToastUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 给赏分
 */
public class HelpRewardInfoGivePointsDialog {

    public static void showDialog(final Activity context, final String post_id) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        int width = ScreenUtils.getScreenWidth(context) * 3 / 4;
        window.setLayout(width,
                android.view.WindowManager.LayoutParams.WRAP_CONTENT);
        View view = context.getLayoutInflater().inflate(
                R.layout.dialog_givepoints, null);
        window.setContentView(view);//

        TextView cancel = (TextView) view.findViewById(R.id.cancle);
        TextView confirm = (TextView) view.findViewById(R.id.confirm);
        final EditText et_points = (EditText) view.findViewById(R.id.et_points);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String points = et_points.getText().toString().trim();
                if (StringUtils.checkStr(points) && Integer.parseInt(points) > 0) {
                    setFavorites(context, dialog, post_id, points);
                } else {
                    ToastUtils.show(context, "请输入有效赏分");
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 给赏分
     *
     * @param mContext
     */
    private static void setFavorites(final Activity mContext, final Dialog dialog, String post_id, String points) {
        MyProcessDialog.showDialog(mContext);
        HelpNetwork
                .getHelpApi()
                .getGiveRewardPointsBean(App.APP_CLIENT_KEY, "give_points", post_id, points)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<StringResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        MyProcessDialog.closeDialog();
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(StringResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            dialog.dismiss();
                            ToastUtils.show(mContext,"打赏成功");
                            RxBus.getDefault().post(new UpdateLoginDataRxbusType(true));
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });

    }
}
