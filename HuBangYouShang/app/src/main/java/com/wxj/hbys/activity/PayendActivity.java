package com.wxj.hbys.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wxj.hbys.R;
import com.wxj.hbys.view.MyGridView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by MXY on 2017/2/25.
 */

public class PayendActivity extends BaseActivity {

    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.tv_payend_type)
    TextView tvPayendType;
    @BindView(R.id.tv_payend_money)
    TextView tvPayendMoney;
    @BindView(R.id.tv_payend_seeorder)
    TextView tvPayendSeeorder;
    @BindView(R.id.tv_payend_gohome)
    TextView tvPayendGohome;
    @BindView(R.id.mgv_payend)
    MyGridView mgvPayend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payend);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        ivTitleBack.setVisibility(View.GONE);
        tvTitleRight.setVisibility(View.GONE);
        tvTitle.setText("订单支付成功");
    }

    @OnClick({R.id.tv_payend_seeorder,R.id.tv_payend_gohome})
    void click(View v){

        switch (v.getId()){
            case R.id.tv_payend_seeorder:
                break;
            case R.id.tv_payend_gohome:
                break;
        }
    }
}
