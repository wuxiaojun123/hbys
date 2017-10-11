package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.utils.ActivitySlideAnim;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 账户管理
 * Created by wuxiaojun on 2017/1/14.
 */

public class AccountManagerActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manager);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tv_title.setText(R.string.string_account_manager_title);
        tv_title_right.setVisibility(View.GONE);
    }

    @OnClick({R.id.iv_title_back, R.id.tv_person_info, R.id.tv_certification, R.id.tv_verify_ldentity,
            R.id.tv_address_manager, R.id.tv_login_pwd, R.id.tv_payment_pwd})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(AccountManagerActivity.this);

                break;
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
                Intent mIntent = new Intent(mContext, PwdUpdateActivity.class);
                startActivity(mIntent);
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
