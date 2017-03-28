package com.help.reward.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.utils.ActivitySlideAnim;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 * 订单详情
 *
 * Created by wuxiaojun on 2017/1/8.
 */

public class OrderDetailsActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.tv_order_number)
    TextView tv_order_number; // 订单号
    @BindView(R.id.tv_order_state)
    TextView tv_order_state; // 订单状态
    @BindView(R.id.tv_order_start_time)
    TextView tv_order_start_time; // 订单时间，下单
    @BindView(R.id.tv_express)
    TextView tv_express; // 某某快递：快递单号
    @BindView(R.id.tv_receiver_person)
    TextView tv_receiver_person; // 收货人
    @BindView(R.id.tv_receiver_phone)
    TextView tv_receiver_phone; // 收货人手机
    @BindView(R.id.tv_receiver_address)
    TextView tv_receiver_address; // 收货人地址
    @BindView(R.id.tv_seller_name)
    TextView tv_seller_name; // 商家名称
    @BindView(R.id.ll_shop)
    LinearLayout ll_shop; // 商品列表
    @BindView(R.id.tv_complaint)
    TextView tv_complaint; // 投诉
    @BindView(R.id.tv_pay_way)
    TextView tv_pay_way; // 支付方式
    @BindView(R.id.tv_shop_total_price)
    TextView tv_shop_total_price; // 商品总价
    @BindView(R.id.tv_free)
    TextView tv_free; // 运费
    @BindView(R.id.tv_discount)
    TextView tv_discount; // 通用卷折扣
    @BindView(R.id.tv_full_cut)
    TextView tv_full_cut; // 优惠劵满减
    @BindView(R.id.tv_real_price)
    TextView tv_real_price; // 实付款



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ButterKnife.bind(this);

        initView();
        initEvent();
    }

    private void initView() {
        tv_title.setText(R.string.string_order_details);
        tv_title_right.setVisibility(View.GONE);
    }

    private void initEvent() {

    }

    @OnClick({R.id.iv_title_back})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(OrderDetailsActivity.this);

                break;
        }
    }


}
