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

/**
 * Created by fanjunqing on 20/03/2017.
 */

public class SetNewPasswordActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.iv_title_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_set_password);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mIvBack.setOnClickListener(this);
        mTvTitle.setText(getText(R.string.forget_password_get));
    }

    @OnClick(R.id.btn_commit)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_commit:
                startActivity(new Intent(SetNewPasswordActivity.this,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;
            case R.id.iv_title_back:
                hideSoftKeyboard();
                SetNewPasswordActivity.this.finish();
                break;
        }

    }
}
