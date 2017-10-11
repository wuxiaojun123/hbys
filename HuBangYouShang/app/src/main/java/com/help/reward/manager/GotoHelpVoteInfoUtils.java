package com.help.reward.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.activity.HelpComplainedDetailActivity;
import com.help.reward.activity.HelpVoteInfoActivity;
import com.help.reward.activity.PostActivity;
import com.help.reward.bean.Response.ComplaintStatusResponse;
import com.help.reward.network.HelpNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.ToastUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wuxiaojun on 17-7-18.
 */

public class GotoHelpVoteInfoUtils {

    private Context mContext;

    public GotoHelpVoteInfoUtils(Context context){
        this.mContext = context;
    }


    public void gotoHelpVoteInfo(final String id) {
        MyProcessDialog.showDialog(mContext);
        HelpNetwork
                .getHelpApi()
                .getComplaintStatusBean(App.APP_CLIENT_KEY, "complaint_status", id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ComplaintStatusResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        MyProcessDialog.closeDialog();
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(ComplaintStatusResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            if ("待申诉".equalsIgnoreCase(response.data.status) || "已申诉".equalsIgnoreCase(response.data.status)) {
                                Intent intent = new Intent(mContext, HelpComplainedDetailActivity.class);
                                intent.putExtra("complaint_id", id);
                                mContext.startActivity(intent);

                            } else {
                                Intent intent = new Intent(mContext, HelpVoteInfoActivity.class);
                                intent.putExtra("id", id);
                                mContext.startActivity(intent);
                            }
                            ActivitySlideAnim.slideInAnim((Activity) mContext);

                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

}
