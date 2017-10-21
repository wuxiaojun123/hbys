package com.help.reward.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.activity.AccountManagerActivity;
import com.help.reward.activity.DiscountAmountActivity;
import com.help.reward.activity.HelpPeopleNumberActivity;
import com.help.reward.activity.MsgCenterActivity;
import com.help.reward.activity.MyAccountHelpRewardActivity;
import com.help.reward.activity.MyBalanceActivity;
import com.help.reward.activity.MyCouponActivity;
import com.help.reward.activity.MyGeneralVolumeActivity;
import com.help.reward.activity.PayendActivity;
import com.help.reward.activity.ShopcartActivity;
import com.help.reward.bean.Response.LoginResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.rxbus.event.type.UpdateLoginDataRxbusType;
import com.help.reward.rxbus.event.type.UpdateMessageDataRxbusType;
import com.help.reward.utils.GlideUtils;
import com.help.reward.utils.StatusBarUtil;
import com.idotools.utils.LogUtils;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.observers.Subscribers;
import rx.schedulers.Schedulers;

/**
 * Created by wuxiaojun on 2017/1/8.
 */

public class MyFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.ll_not_logged_in)
    LinearLayout ll_not_logged_in;
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
    TextView tv_discount_level; // 优惠百分比in
    @BindView(R.id.tv_title_help_msgcount)
    TextView tv_title_help_msgcount; // 消息红点


    @BindView(R.id.ll_total)
    LinearLayout ll_total;

    private Subscription loginSubscribe;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void init() {
        updateData();
    }

    /***
     * 更新数据接口
     * // /mobile/index.php?act=member_index&op=index
     */
    private void updateData() {
        RxBus.getDefault().toObservable(UpdateLoginDataRxbusType.class).subscribe(new Action1<UpdateLoginDataRxbusType>() {
            @Override
            public void call(UpdateLoginDataRxbusType type) {
                if (type.isUpdate) {
                    // 更新个人中心数据
                    getPersonData();
                } else if (type.updatePersonInfo) {
                    // 更新本地修改的个人头像和昵称
                    GlideUtils.loadCircleImage(App.mLoginReponse.avator, iv_photo);
                    tv_login.setText(App.mLoginReponse.username);
                }
            }
        });
        RxBus.getDefault().toObservable(UpdateMessageDataRxbusType.class).subscribe(new Action1<UpdateMessageDataRxbusType>() {
            @Override
            public void call(UpdateMessageDataRxbusType type) {
                if (type.hasNew) { // 更新数据
                    tv_title_help_msgcount.setVisibility(View.VISIBLE);
                } else {
                    tv_title_help_msgcount.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    public void getPersonData() {
        if (App.APP_CLIENT_KEY == null) {
            return;
        }
//        LogUtils.e("更新个人中心数据333333333333");
        PersonalNetwork.getResponseApi()
                .getLoggedInResponse(App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<LoginResponse>() {
                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        // 更新数据
                        LogUtils.e("获取个人中心数据，已登录,,," + loginResponse.code + "-" + loginResponse.msg);
                        if (loginResponse.code == 200) {
                            App.mLoginReponse = loginResponse.data;
                            setUserData();
                        }
                    }
                });
    }

    @OnClick({R.id.rl_user_info, R.id.iv_setting, R.id.tv_msg, R.id.tv_login, R.id.tv_register,
            R.id.tv_payment, R.id.tv_take_delivery, R.id.tv_evaluate, R.id.tv_return_goods,
            R.id.ll_available_predeposit, R.id.ll_voucher, R.id.ll_general_voucher, R.id.ll_discount_level,
            R.id.tv_account, R.id.tv_my_help, R.id.tv_my_reward, R.id.tv_my_vote, R.id.tv_my_collection,
            R.id.tv_share, R.id.tv_order, R.id.tv_help_num, R.id.tv_account_help_reward})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_help_num:
                // 帮助人数
                startActivity(new Intent(mContext, HelpPeopleNumberActivity.class));
                ActivitySlideAnim.slideInAnim(getActivity());

                break;
            case R.id.tv_account_help_reward:
                // 帮赏分
                startActivity(new Intent(mContext, MyAccountHelpRewardActivity.class));
                ActivitySlideAnim.slideInAnim(getActivity());

                break;
            case R.id.rl_user_info:
                // 需要判断是否已登陆
                if (App.APP_CLIENT_KEY == null) {
                    login();
                } else {
                    startActivity(new Intent(mContext, AccountManagerActivity.class));
                    ActivitySlideAnim.slideInAnim(getActivity());
                }

                break;
            case R.id.iv_setting:
                // 设置页面
                startActivity(new Intent(mContext, SettingActivity.class));
                ActivitySlideAnim.slideInAnim(getActivity());

                break;

            case R.id.tv_msg:
                // 消息中心
                startActivity(new Intent(mContext, MsgCenterActivity.class));
                ActivitySlideAnim.slideInAnim(getActivity());

                break;

            case R.id.tv_login:
                // 点击登陆完成，需要动态改变布局的高度
                login();

                break;

            case R.id.tv_register:
                // 注册
                startActivity(new Intent(mContext, RegisterActivity.class));
                ActivitySlideAnim.slideInAnim(getActivity());

                break;

            case R.id.tv_payment:
                // 待付款
                Intent mIntent = new Intent(mContext, MyOrderActivity.class);
                mIntent.putExtra("firstPage", 1);
                startActivity(mIntent);
                ActivitySlideAnim.slideInAnim(getActivity());

                break;

            case R.id.tv_take_delivery:
                // 待收货
                Intent mIntent1 = new Intent(mContext, MyOrderActivity.class);
                mIntent1.putExtra("firstPage", 2);
                startActivity(mIntent1);
                ActivitySlideAnim.slideInAnim(getActivity());

                break;

            case R.id.tv_evaluate:
                // 待评价
                Intent mIntent2 = new Intent(mContext, MyOrderActivity.class);
                mIntent2.putExtra("firstPage", 3);
                startActivity(mIntent2);
                ActivitySlideAnim.slideInAnim(getActivity());

                break;

            case R.id.tv_return_goods:
                // 退款/退货
                Intent mIntent3 = new Intent(mContext, MyOrderActivity.class);
                mIntent3.putExtra("firstPage", 4);
                startActivity(mIntent3);
                ActivitySlideAnim.slideInAnim(getActivity());

                break;
            case R.id.ll_available_predeposit:
                // 余额
                startActivity(new Intent(mContext, MyBalanceActivity.class));
                ActivitySlideAnim.slideInAnim(getActivity());

                break;

            case R.id.ll_voucher:
                // 优惠劵
                startActivity(new Intent(mContext, MyCouponActivity.class));
                ActivitySlideAnim.slideInAnim(getActivity());

                break;

            case R.id.ll_general_voucher:
                // 通用卷
                startActivity(new Intent(mContext, MyGeneralVolumeActivity.class));
                ActivitySlideAnim.slideInAnim(getActivity());

                break;

            case R.id.ll_discount_level:
                // 优惠百分比
                startActivity(new Intent(mContext, DiscountAmountActivity.class));
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
                // 我的求助
                startActivity(new Intent(mContext, MyHelpActivity.class));
                ActivitySlideAnim.slideInAnim(getActivity());

                break;

            case R.id.tv_my_reward:
                // 我的获赏
                startActivity(new Intent(mContext, MyRewardActivity.class));
                ActivitySlideAnim.slideInAnim(getActivity());

                break;

            case R.id.tv_my_vote:
                // 我的投票
                startActivity(new Intent(mContext, MyVoteActivity.class));
                ActivitySlideAnim.slideInAnim(getActivity());

                break;

            case R.id.tv_my_collection:
                // 我的收藏
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
     * 获取到是否登录的信息
     */
    private void login() {
        loginSubscribe = RxBus.getDefault().toObservable(String.class).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                if ("loginSuccess".equals(s)) {
                    loginSuccess();
                } else if ("logout".equals(s)) {
                    logout();
                }
            }
        });
        startActivity(new Intent(getActivity(), LoginActivity.class));
        ActivitySlideAnim.slideInAnim(getActivity());
    }

    private void logout() {
        if (ll_logined.getVisibility() == View.VISIBLE) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ll_not_logged_in.getLayoutParams();
            lp.height = MetricsUtils.dipToPx(155);
            ll_not_logged_in.requestLayout();
            ll_logined.setVisibility(View.GONE);

            iv_photo.setImageResource(R.mipmap.img_my_default_photo);
            tv_register.setVisibility(View.VISIBLE);
            tv_login.setText("登录/");
            tv_user_level.setText("这里是用户等级");
            tv_help_num.setText("0");
            tv_account_help_reward.setText(null);
            tv_number_of_complaints.setText(null);
            tv_number_of_complaints2.setText(null);

            tv_available_predeposit.setText("0");
            tv_voucher.setText("0");
            tv_general_voucher.setText("0");
            tv_discount_level.setText("0");
        }
    }

    private void loginSuccess() {
        if (ll_logined.getVisibility() == View.GONE) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ll_not_logged_in.getLayoutParams();
            lp.height = MetricsUtils.dipToPx(235);
            ll_not_logged_in.requestLayout();
            ll_logined.setVisibility(View.VISIBLE);
        }
        // 设置会员信息
        setUserData();
    }

    private void setUserData() {
        LogUtils.e("设置会员信息");
        if (App.mLoginReponse != null) {
            GlideUtils.loadCircleImage(App.mLoginReponse.avator, iv_photo);
            tv_register.setVisibility(View.GONE);
            tv_login.setText(App.mLoginReponse.username);
            tv_user_level.setText("用户等级：" + App.mLoginReponse.level_name);
            tv_help_num.setText(App.mLoginReponse.people_help);
            tv_account_help_reward.setText(App.mLoginReponse.point);
            tv_number_of_complaints.setText(App.mLoginReponse.complaint);
            tv_number_of_complaints2.setText(App.mLoginReponse.complained);

            tv_available_predeposit.setText(App.mLoginReponse.available_predeposit);
            tv_voucher.setText(App.mLoginReponse.voucher);
            tv_general_voucher.setText(App.mLoginReponse.general_voucher);
            tv_discount_level.setText(App.mLoginReponse.discount_level);

            if (App.mLoginReponse.new_message) {
                tv_title_help_msgcount.setVisibility(View.VISIBLE);
            } else {
                tv_title_help_msgcount.setVisibility(View.INVISIBLE);
            }
            RxBus.getDefault().post(new UpdateMessageDataRxbusType(App.mLoginReponse.new_message));
        }
    }

}
