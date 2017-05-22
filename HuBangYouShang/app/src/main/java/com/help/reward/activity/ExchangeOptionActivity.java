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
 * 选择兑换方式
 * <p>
 * Created by wuxiaojun on 2017/1/8.
 */
public class ExchangeOptionActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_option);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tv_title.setText(R.string.string_xuanzeduihuanfangshi_title);
        tv_title_right.setVisibility(View.GONE);
    }

    @OnClick({R.id.iv_title_back, R.id.tv_balance_exchange, R.id.tv_bonus_points})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(ExchangeOptionActivity.this);

                break;
            case R.id.tv_balance_exchange:
                // 余额兑换通用卷
                startActivity(new Intent(ExchangeOptionActivity.this, BalanceExchangeVolumeActivity.class));
                ActivitySlideAnim.slideInAnim(ExchangeOptionActivity.this);

                break;
            case R.id.tv_bonus_points:
                // 帮赏分兑换通用卷
                startActivity(new Intent(ExchangeOptionActivity.this, GeneralExchangeVolumeActivity.class));
                ActivitySlideAnim.slideInAnim(ExchangeOptionActivity.this);

                break;
        }
    }


}
