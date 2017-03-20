package com.reward.help.merchant.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.reward.help.merchant.R;
import com.reward.help.merchant.chat.ui.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


public class QueryApplyGroupActivity extends BaseActivity {

    @BindView(R.id.iv_title_back)
    ImageView mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_query_apply_group);
        ButterKnife.bind(this);
        mTvTitle.setText(getText(R.string.contact_apply_tip));


        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                QueryApplyGroupActivity.this.finish();
            }
        });
    }

}
