package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.help.reward.R;
import com.help.reward.utils.ActivitySlideAnim;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 账户管理
 * Created by wuxiaojun on 2017/1/14.
 */

public class AccountManagerActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manager);
        ButterKnife.bind(this);


    }

    @OnClick({R.id.tv_person_info, R.id.tv_certification, R.id.tv_verify_ldentity,
            R.id.tv_address_manager, R.id.tv_login_pwd, R.id.tv_payment_pwd})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_person_info:
                //个人信息
                startActivity(new Intent(mContext, PersonInfoActivity.class));
                ActivitySlideAnim.slideInAnim(AccountManagerActivity.this);

                break;
            case R.id.tv_certification:
                //实名认证
                startActivity(new Intent(mContext, CertificationActivity.class));
                ActivitySlideAnim.slideInAnim(AccountManagerActivity.this);

                break;
            case R.id.tv_verify_ldentity:
                //手机认证
                startActivity(new Intent(mContext, VerifyIdentityActivity.class));
                ActivitySlideAnim.slideInAnim(AccountManagerActivity.this);

                break;
            case R.id.tv_login_pwd:
                // 登陆密码
                startActivity(new Intent(mContext, PwdUpdateActivity.class));
                ActivitySlideAnim.slideInAnim(AccountManagerActivity.this);

                break;
            case R.id.tv_payment_pwd:
                // 支付密码
                startActivity(new Intent(mContext, PwdPaymentActivity.class));
                ActivitySlideAnim.slideInAnim(AccountManagerActivity.this);

                break;
            case R.id.tv_address_manager:
                //地址管理
                startActivity(new Intent(mContext, AddressManagerActivity.class));
                ActivitySlideAnim.slideInAnim(AccountManagerActivity.this);

                break;
        }

    }


}
