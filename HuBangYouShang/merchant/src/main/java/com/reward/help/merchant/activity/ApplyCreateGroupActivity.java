package com.reward.help.merchant.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.reward.help.merchant.R;
import com.reward.help.merchant.chat.ui.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fanjunqing on 20/03/2017.
 */

public class ApplyCreateGroupActivity extends BaseActivity implements View.OnClickListener{
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_title_back:
                hideSoftKeyboard();
                ApplyCreateGroupActivity.this.finish();
                break;
        }
    }

}
