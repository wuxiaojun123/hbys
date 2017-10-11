package com.reward.help.merchant.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.reward.help.merchant.R;
import com.reward.help.merchant.chat.ui.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ApplyCreateGroupActivity extends BaseActivity {
    @BindView(R.id.iv_title_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_create_group);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTvTitle.setText(getText(R.string.apply_new_group));

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                ApplyCreateGroupActivity.this.finish();
            }
        });
    }
}
