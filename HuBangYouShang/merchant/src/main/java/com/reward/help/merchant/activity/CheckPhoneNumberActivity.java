package com.reward.help.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.reward.help.merchant.R;
import com.reward.help.merchant.chat.ui.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CheckPhoneNumberActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.iv_title_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_check_phonenumber);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mIvBack.setOnClickListener(this);
        mTvTitle.setText(getText(R.string.forget_password_get));
    }

    @OnClick({R.id.btn_next})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next:
                startActivity(new Intent(CheckPhoneNumberActivity.this,SetNewPasswordActivity.class));
                break;
            case R.id.iv_title_back:
                hideSoftKeyboard();
                CheckPhoneNumberActivity.this.finish();
                break;
        }

    }
}
