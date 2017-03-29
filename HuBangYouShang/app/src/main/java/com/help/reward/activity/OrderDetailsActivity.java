package com.help.reward.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.MyOrderListBean;
import com.help.reward.bean.MyOrderShopBean;
import com.help.reward.bean.OrderInfoBean;
import com.help.reward.bean.Response.GoodResponse;
import com.help.reward.bean.Response.MyOrderResponse;
import com.help.reward.bean.Response.OrderInfoResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.ShopMallNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.GlideUtils;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 订单详情
 * <p>
 * Created by wuxiaojun on 2017/1/8.
 */

public class OrderDetailsActivity extends BaseActivity implements View.OnClickListener {
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

    @BindView(R.id.tv_evaluate_order)
    TextView tv_evaluate_order;
    @BindView(R.id.tv_cancel_order)
    TextView tv_cancel_order;
    @BindView(R.id.tv_remove_order)
    TextView tv_remove_order;

    private LayoutInflater mInflater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ButterKnife.bind(this);
        mInflater = LayoutInflater.from(mContext);
        initView();
        initEvent();
    }

    private void initView() {
        tv_title.setText(R.string.string_order_details);
        tv_title_right.setVisibility(View.GONE);
    }

    private void initEvent() {
        String order_id = getIntent().getStringExtra("order_id");
        if (TextUtils.isEmpty(order_id)) {
            ToastUtils.show(mContext, "订单号不存在");
            return;
        }
        LogUtils.e("订单号是：" + order_id);
        PersonalNetwork
                .getResponseApi()
                .getMyOrderDetailsResponse("member_order", "order_info", order_id, App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<OrderInfoResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(OrderInfoResponse response) {
                        if (response.code == 200) {
                            if (response.data != null) { // 显示数据
                                OrderInfoBean bean = response.data;
                                bindData(bean);
                                setShopText(bean);
                                setOrderState(bean);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    private void bindData(OrderInfoBean bean) {
        tv_order_number.setText(bean.order_sn);
        tv_order_state.setText(bean.state_desc);
        tv_order_start_time.setText(bean.add_time);

        tv_express.setText("快递单号:" + bean.shipping_code);
        tv_receiver_person.setText(bean.extend_order_common.reciver_name);
        tv_receiver_phone.setText(bean.extend_order_common.reciver_info.mob_phone);
        tv_receiver_address.setText(bean.extend_order_common.reciver_info.address);
        tv_seller_name.setText(bean.store_name);
        tv_pay_way.setText(bean.payment_name);
        tv_shop_total_price.setText("￥" + bean.goods_amount);

        tv_free.setText("￥" + bean.shipping_fee);
        tv_discount.setText("暂无字段");
        tv_full_cut.setText("￥" + bean.extend_order_common.voucher_price);
        tv_real_price.setText("￥" + bean.real_pay_amount);

    }


    private void setShopText(OrderInfoBean bean) {
        int size = bean.goods_list.size();
        for (int i = 0; i < size; i++) {
            View shopView = mInflater.inflate(R.layout.layout_my_order_shop, ll_shop, false);
            ImageView iv_shop_img = (ImageView) shopView.findViewById(R.id.iv_shop_img); // 商品图片
            TextView tv_shop_name = (TextView) shopView.findViewById(R.id.tv_shop_name); // 商品名称
//            TextView tv_shop_atrribute = (TextView) shopView.findViewById(R.id.tv_shop_atrribute); // 商品属性:属性值
            TextView tv_single_shop_price = (TextView) shopView.findViewById(R.id.tv_single_shop_price); // 单个商品价格 ￥200.0
            TextView tv_shop_num = (TextView) shopView.findViewById(R.id.tv_shop_num); // 商品数量 x1

            MyOrderShopBean myOrderShopBean = bean.goods_list.get(i);

            GlideUtils.loadImage(myOrderShopBean.goods_image_url, iv_shop_img);
            tv_shop_name.setText(myOrderShopBean.goods_name);
//            tv_shop_atrribute.setText("商品属性:");
            tv_single_shop_price.setText(myOrderShopBean.goods_price);
            tv_shop_num.setText("x" + myOrderShopBean.goods_num);

            ll_shop.addView(shopView);
        }
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

    private void setOrderState(OrderInfoBean bean) {
        String order_state = bean.order_state; // 0:已取消,10(默认):未付款;20:已付款;30:已发货;40:已收货;50:已提交;60已确认;
        if ("0".equals(bean.lock_state)) {
            if ("0".equals(order_state)) { // 已取消 删除订单
                tv_remove_order.setVisibility(View.VISIBLE);
                tv_cancel_order.setVisibility(View.GONE);
                tv_evaluate_order.setVisibility(View.GONE);

            } else if ("10".equals(order_state)) { // 待付款  立即付款/取消订单/删除订单
                tv_remove_order.setVisibility(View.VISIBLE);
                tv_cancel_order.setVisibility(View.VISIBLE);
                tv_evaluate_order.setVisibility(View.VISIBLE);
                tv_evaluate_order.setText("立即付款");
                tv_evaluate_order.setTag("1");

            } else if ("20".equals(order_state)) { // 待发货  取消订单/删除订单
                tv_remove_order.setVisibility(View.VISIBLE);
                tv_cancel_order.setVisibility(View.VISIBLE);
                tv_evaluate_order.setVisibility(View.GONE);

            } else if ("30".equals(order_state)) { // 待收货  确认收货/删除订单
                tv_remove_order.setVisibility(View.VISIBLE);
                tv_cancel_order.setVisibility(View.GONE);
                tv_evaluate_order.setVisibility(View.VISIBLE);
                tv_evaluate_order.setText("确认收货");
                tv_evaluate_order.setTag("2");

            } else if ("40".equals(order_state)) { // 已完成 评价/删除订单
                tv_remove_order.setVisibility(View.VISIBLE);
                tv_cancel_order.setVisibility(View.GONE);
                tv_evaluate_order.setVisibility(View.VISIBLE);
                tv_evaluate_order.setText("评价");
                tv_evaluate_order.setTag("3");

            }
        } else if ("1".equals(bean.lock_state)) { // 退款中  确认收货/删除订单
            tv_remove_order.setVisibility(View.VISIBLE);
            tv_cancel_order.setVisibility(View.GONE);
            tv_evaluate_order.setVisibility(View.VISIBLE);
            tv_evaluate_order.setText("确认收货");
            tv_evaluate_order.setTag("2");
        }
    }

}
