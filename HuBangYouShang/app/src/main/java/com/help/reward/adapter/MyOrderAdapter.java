package com.help.reward.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.activity.BaseActivity;
import com.help.reward.activity.OrderDetailsActivity;
import com.help.reward.activity.OrderPulishedEvaluateActivity;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.MyOrderListBean;
import com.help.reward.bean.MyOrderShopBean;
import com.help.reward.bean.Response.MyHelpPostResponse;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.GlideUtils;
import com.idotools.utils.DateUtil;
import com.idotools.utils.LogUtils;

/**
 * 我的订单
 * Created by wuxiaojun on 2017/2/26.
 */

public class MyOrderAdapter extends BaseRecyclerAdapter {


    public MyOrderAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_my_order;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {

        TextView tv_store_name = holder.getView(R.id.tv_store_name); // 商家名称
        TextView tv_shop_state = holder.getView(R.id.tv_shop_state); // 商品状态，是已支付还是未支付

        TextView tv_total_shop_and_money = holder.getView(R.id.tv_total_shop_and_money); // 共计：4件商品  合计：￥235元(含运费12元)
        TextView tv_remove_order = holder.getView(R.id.tv_remove_order); // 删除订单
        TextView tv_evaluate_order = holder.getView(R.id.tv_evaluate_order); // 评价

        LinearLayout ll_shop = holder.getView(R.id.ll_shop); // 商品列表

        MyOrderListBean.OrderList bean = ((MyOrderListBean) mDataList.get(position)).order_list.get(0);
        int size = 0;
        if (bean.extend_order_goods != null) {
            size = bean.extend_order_goods.size();
            LogUtils.e("商品集合长度是：" + size);
            setShopText(ll_shop, bean, size);
        }

        tv_store_name.setText(bean.store_name);
        setEvaluationState(tv_shop_state, tv_evaluate_order, bean);
        double shippingFee = Double.parseDouble(bean.shipping_fee);
        if (shippingFee > 0) {
            tv_total_shop_and_money.setText("共计：" + size + "件商品  合计：￥" + bean.order_amount + "(含运费" + shippingFee + ")");
        } else {
            tv_total_shop_and_money.setText("共计：" + size + "件商品  合计：￥" + bean.order_amount + "(免运费)");
        }

        ll_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳往订单详情
                Intent mIntent = new Intent(mContext,OrderDetailsActivity.class);
                mContext.startActivity(mIntent);
                ActivitySlideAnim.slideInAnim((BaseActivity)mContext);
            }
        });

        tv_remove_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 删除订单逻辑

            }
        });
    }

    /***
     * 设置评论状态
     * @param tv_shop_state
     * @param tv_evaluate_order
     * @param bean
     */
    private void setEvaluationState(TextView tv_shop_state, TextView tv_evaluate_order, MyOrderListBean.OrderList bean) {
        if (bean.evaluation_state.equals("0")) {//评价状态 0未评价，1已评价，2已过期未评价
            tv_shop_state.setText("未评价");
            tv_evaluate_order.setVisibility(View.VISIBLE);
            tv_evaluate_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { // 评价页面
                    Intent mIntent = new Intent(mContext,OrderPulishedEvaluateActivity.class);
                    mContext.startActivity(mIntent);
                    ActivitySlideAnim.slideInAnim((BaseActivity)mContext);
                }
            });
        } else if (bean.evaluation_state.equals("1")) {
            tv_shop_state.setText("已评价");
            tv_evaluate_order.setVisibility(View.GONE);

        } else {
            tv_shop_state.setText("已过期未评价");
            tv_evaluate_order.setVisibility(View.GONE);
        }
    }

    /***
     * 设置商品数量
     * @param ll_shop
     * @param bean
     * @param size
     */
    private void setShopText(LinearLayout ll_shop, MyOrderListBean.OrderList bean, int size) {
        for (int i = 0; i < size; i++) {
            View shopView = mInflater.inflate(R.layout.layout_my_order_shop, ll_shop, false);
            ImageView iv_shop_img = (ImageView) shopView.findViewById(R.id.iv_shop_img); // 商品图片
            TextView tv_shop_name = (TextView) shopView.findViewById(R.id.tv_shop_name); // 商品名称
            TextView tv_shop_atrribute = (TextView) shopView.findViewById(R.id.tv_shop_atrribute); // 商品属性:属性值
            TextView tv_single_shop_price = (TextView) shopView.findViewById(R.id.tv_single_shop_price); // 单个商品价格 ￥200.0
            TextView tv_shop_num = (TextView) shopView.findViewById(R.id.tv_shop_num); // 商品数量 x1

            MyOrderShopBean myOrderShopBean = bean.extend_order_goods.get(i);

            GlideUtils.loadImage(myOrderShopBean.goods_image_url, iv_shop_img);
            tv_shop_name.setText(myOrderShopBean.goods_name);
            tv_shop_atrribute.setText("商品属性:");
            tv_single_shop_price.setText(myOrderShopBean.goods_price);
            tv_shop_num.setText(myOrderShopBean.goods_num);

            ll_shop.addView(shopView);
        }
    }


}
