package com.help.reward.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.help.reward.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by MXY on 2017/2/22.
 */

public class PayTypeActivity extends BaseActivity {

    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.tv_paytype_money)
    TextView tvPaytypeMoney;
    @BindView(R.id.layout_paytype_wchat)
    RelativeLayout layoutPaytypeWchat;
    @BindView(R.id.layout_paytype_alipay)
    RelativeLayout layoutPaytypeAlipay;
    @BindView(R.id.layout_paytype_yinlian)
    RelativeLayout layoutPaytypeYinlian;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytype);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("选择支付方式");
        tvTitleRight.setVisibility(View.GONE);
    }

    @OnClick({R.id.iv_title_back,R.id.layout_paytype_wchat,R.id.layout_paytype_alipay,R.id.layout_paytype_yinlian})
    void click(View v){
        switch (v.getId()){
            case R.id.iv_title_back:
                finish();
            break;
            case R.id.layout_paytype_wchat:

                break;
            case R.id.layout_paytype_alipay:

                break;
            case R.id.layout_paytype_yinlian:

                break;

        }
    }
}
