package com.help.reward.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.activity.HelpComplainedActivity;
import com.help.reward.bean.Response.StringResponse;
import com.help.reward.network.HelpNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.idotools.utils.ToastUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/*
 * 获赏详情右上角更多
 */
public class HelpRewardInfoMorePop {
    Activity activity;
    String post_id;
    String u_name;
    String content;
    String type="admiration";

    public HelpRewardInfoMorePop(String post_id, String u_name, String content, String type) {
        this.post_id = post_id;
        this.u_name = u_name;
        this.content = content;
        this.type = type;

    }

    public void showPopupWindow(
            Activity activity, View root) {
        this.activity = activity;
        final PopupWindow poup = initPopuptWindow(activity);
        poup.setWidth(LayoutParams.MATCH_PARENT);
        poup.setHeight(LayoutParams.MATCH_PARENT);
        poup.showAtLocation(root, Gravity.BOTTOM, 0, 0);
        final OnClickListener l = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (poup.isShowing())
                    poup.dismiss();
            }
        };

        poup.getContentView().setOnClickListener(l);
    }


    private PopupWindow initPopuptWindow(Activity activity) {
        // 获取自定义布局文件dialog.xml的视图
        View popupWindow_view = LayoutInflater.from(activity).inflate(
                R.layout.pop_helprewardinfomore, null);


        // 使用下面的构造方法
        PopupWindow popupWindow = new PopupWindow(popupWindow_view,
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        // 这里注意 必须要有一个背景 ，有了背景后
        // 当你点击对话框外部的时候或者按了返回键的时候对话框就会消失，当然前提是使用的构造函数中Focusable为true
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x7f000000));
        popupWindow_view.findViewById(R.id.tv1).setOnClickListener(
                new ClickListener(popupWindow));
        popupWindow_view.findViewById(R.id.tv2).setOnClickListener(
                new ClickListener(popupWindow));
        popupWindow_view.findViewById(R.id.tv3).setOnClickListener(
                new ClickListener(popupWindow));
        popupWindow_view.findViewById(R.id.tv4).setOnClickListener(
                new ClickListener(popupWindow));
        return popupWindow;
    }

    private class ClickListener implements OnClickListener {
        PopupWindow mPopupWindow;

        ClickListener(
                PopupWindow poup) {
            mPopupWindow = poup;
        }

        @Override
        public void onClick(View view) {
            dissPoup(mPopupWindow);
            switch (view.getId()) {
                case R.id.tv1:
                    //赏分
                    HelpRewardInfoGivePointsDialog.showDialog(activity, post_id);
                    break;
                case R.id.tv2:
                    //收藏
                    addFavorites(post_id);
                    break;
                case R.id.tv3:
                    //投诉
                    Intent intent = new Intent(activity, HelpComplainedActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("post_id", post_id);
                    bundle.putString("type", type);
                    bundle.putString("comment_id", "");
                    bundle.putString("post_title", content);
                    bundle.putString("u_name", u_name);
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                    ActivitySlideAnim.slideInAnim(activity);
                    break;
                default:
                    break;
            }
        }

        private void dissPoup(PopupWindow poup) {
            if (null != poup && poup.isShowing())
                poup.dismiss();
        }

    }

    private void addFavorites(String post_id) {
        MyProcessDialog.showDialog(activity);
        HelpNetwork
                .getHelpApi()
                .getFavoritesAddBean(App.APP_CLIENT_KEY, "favorites_add", post_id, "reward")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<StringResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        MyProcessDialog.closeDialog();
                        ToastUtils.show(activity, R.string.string_error);
                    }

                    @Override
                    public void onNext(StringResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            ToastUtils.show(activity, "收藏成功");
                        } else {
                            ToastUtils.show(activity, response.msg);
                        }
                    }
                });
    }


}