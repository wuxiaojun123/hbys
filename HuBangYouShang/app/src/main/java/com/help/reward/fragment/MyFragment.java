package com.help.reward.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.help.reward.App;
import com.help.reward.activity.AccountManagerActivity;
import com.help.reward.utils.GlideUtils;
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

    @BindView(R.id.iv_photo)
    ImageView iv_photo; // 个人信息
    @BindView(R.id.tv_login)
    TextView tv_login;
    @BindView(R.id.tv_register)
    TextView tv_register;
    @BindView(R.id.tv_user_level)
    TextView tv_user_level;
    @BindView(R.id.tv_help_num)
    TextView tv_help_num; // 帮助人数
    @BindView(R.id.tv_account_help_reward)
    TextView tv_account_help_reward; // 帮赏分
    @BindView(R.id.tv_number_of_complaints)
    TextView tv_number_of_complaints; // 投诉人数
    @BindView(R.id.tv_number_of_complaints2)
    TextView tv_number_of_complaints2; // 被投诉人数

    @BindView(R.id.tv_available_predeposit)
    TextView tv_available_predeposit; // 余额
    @BindView(R.id.tv_voucher)
    TextView tv_voucher; // 优惠劵
    @BindView(R.id.tv_general_voucher)
    TextView tv_general_voucher; // 通用卷
    @BindView(R.id.tv_discount_level)
    TextView tv_discount_level; // 优惠百分比

    /*@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragment_my, null);
        }
        ButterKnife.bind(this, contentView);
        return contentView;
    }*/

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
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
                if(App.APP_CLIENT_KEY == null){
                    login();
                }else{
                    startActivity(new Intent(mContext, AccountManagerActivity.class));
                    ActivitySlideAnim.slideInAnim(getActivity());
                }

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

    /***
     */
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
                        // 设置会员信息
                        if(App.mLoginReponse != null){
                            GlideUtils.loadCircleImage(App.mLoginReponse.avator,iv_photo);
                            tv_register.setVisibility(View.GONE);
                            tv_login.setText(App.mLoginReponse.username);
                            tv_user_level.setText("用户等级："+App.mLoginReponse.level_name);
                            tv_help_num.setText(App.mLoginReponse.people_help);
                            tv_account_help_reward.setText(App.mLoginReponse.point);
                            tv_number_of_complaints.setText(App.mLoginReponse.complaint);
                            tv_number_of_complaints2.setText(App.mLoginReponse.complained);

                            tv_available_predeposit.setText(App.mLoginReponse.available_predeposit);
                            tv_voucher.setText(App.mLoginReponse.voucher);
                            tv_general_voucher.setText(App.mLoginReponse.general_voucher);
                            tv_discount_level.setText(App.mLoginReponse.discount_level);
                        }
                    }
                }
            }
        });
        startActivity(new Intent(getActivity(), LoginActivity.class));
        ActivitySlideAnim.slideInAnim(getActivity());
    }

}
