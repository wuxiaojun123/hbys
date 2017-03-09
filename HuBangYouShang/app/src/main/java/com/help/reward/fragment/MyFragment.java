package com.help.reward.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.idotools.utils.MetricsUtils;
import com.help.reward.R;
import com.help.reward.activity.LoginActivity;
import com.help.reward.activity.MyAccountActivity;
import com.help.reward.activity.MyCollectionActivity;
import com.help.reward.activity.MyHelpActivity;
import com.help.reward.activity.MyOrderActivity;
import com.help.reward.activity.MyRewardActivity;
import com.help.reward.activity.MyShareActivity;
import com.help.reward.activity.MyVoteActivity;
import com.help.reward.activity.RegisterActivity;
import com.help.reward.activity.SettingActivity;
import com.help.reward.rxbus.RxBus;
import com.help.reward.utils.ActivitySlideAnim;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by wuxiaojun on 2017/1/8.
 */

public class MyFragment extends BaseFragment implements View.OnClickListener {

    private View contentView;

    @BindView(R.id.ll_height)
    LinearLayout ll_login_height;
    @BindView(R.id.rl_user_info)
    RelativeLayout rl_user_info;
    @BindView(R.id.ll_logined)
    LinearLayout ll_logined;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragment_my, null);
        }
        ButterKnife.bind(this, contentView);
        return contentView;
    }

    @OnClick({R.id.rl_user_info, R.id.iv_setting, R.id.tv_login, R.id.tv_register, R.id.tv_account,
            R.id.tv_my_help, R.id.tv_my_reward, R.id.tv_my_vote, R.id.tv_my_collection, R.id.tv_share,
            R.id.tv_order})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.rl_user_info:
                // 需要判断是否已登陆
                login();

                break;
            case R.id.iv_setting:
                // 设置页面
                startActivity(new Intent(mContext, SettingActivity.class));
                ActivitySlideAnim.slideInAnim(getActivity());

                break;

            case R.id.tv_login:
                // 点击登陆完成，需要动态改变布局的高度
                login();

                break;
            case R.id.tv_register:
                startActivity(new Intent(mContext, RegisterActivity.class));
                ActivitySlideAnim.slideInAnim(getActivity());

                break;
            case R.id.tv_order:
                // 我的订单
                startActivity(new Intent(mContext, MyOrderActivity.class));
                ActivitySlideAnim.slideInAnim(getActivity());

                break;
            case R.id.tv_account:
                // 我的账户
                startActivity(new Intent(mContext, MyAccountActivity.class));
                ActivitySlideAnim.slideInAnim(getActivity());

                break;
            case R.id.tv_my_help:
                startActivity(new Intent(mContext, MyHelpActivity.class));
                ActivitySlideAnim.slideInAnim(getActivity());

                break;
            case R.id.tv_my_reward:
                startActivity(new Intent(mContext, MyRewardActivity.class));
                ActivitySlideAnim.slideInAnim(getActivity());

                break;
            case R.id.tv_my_vote:
                startActivity(new Intent(mContext, MyVoteActivity.class));
                ActivitySlideAnim.slideInAnim(getActivity());

                break;
            case R.id.tv_my_collection:
                startActivity(new Intent(mContext, MyCollectionActivity.class));
                ActivitySlideAnim.slideInAnim(getActivity());

                break;
            case R.id.tv_share:
                startActivity(new Intent(mContext, MyShareActivity.class));
                ActivitySlideAnim.slideInAnim(getActivity());

                break;
        }
    }


    private void login(){
        RxBus.getDefault().toObservable(String.class).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                if("loginSuccess".equals(s)){
                    if (ll_logined.getVisibility() == View.GONE) {
                        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ll_login_height.getLayoutParams();
                        lp.height = MetricsUtils.dipToPx(210);
                        ll_login_height.requestLayout();
                        ll_logined.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        startActivity(new Intent(getActivity(), LoginActivity.class));
        ActivitySlideAnim.slideInAnim(getActivity());
    }

}
