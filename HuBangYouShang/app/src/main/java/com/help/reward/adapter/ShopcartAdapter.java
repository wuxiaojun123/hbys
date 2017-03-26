package com.help.reward.adapter;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.activity.BaseActivity;
import com.help.reward.activity.OrderDetailsActivity;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.MyOrderListBean;
import com.help.reward.bean.MyOrderShopBean;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.GlideUtils;
import com.hyphenate.easeui.widget.SwipeMenuView;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的订单
 * Created by wuxiaojun on 2017/2/26.
 */

public class ShopcartAdapter extends BaseRecyclerAdapter {

    private OnCheckListener mCheckListener;


    private List<MyOrderShopBean> mCheckList = new ArrayList<MyOrderShopBean>();

    public ShopcartAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_shop_cart_store;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {

        ImageView mIvStoreAll = holder.getView(R.id.iv_store_check);
        TextView tv_store_name = holder.getView(R.id.tv_store_name); // 商家名称

        LinearLayout ll_shop = holder.getView(R.id.ll_shop_cart); // 商品列表

//        MyOrderListBean.OrderList bean = ((MyOrderListBean) mDataList.get(position)).order_list.get(0);
        final MyOrderListBean.OrderList bean = (MyOrderListBean.OrderList) mDataList.get(position);

        int size = 0;
        if (ll_shop.getTag() == null && bean.extend_order_goods != null) {
            size = bean.extend_order_goods.size();
            setShopText(ll_shop, bean, size);
            ll_shop.setTag(bean.order_id);
        }

        tv_store_name.setText(bean.store_name);

        if (bean.extend_order_goods != null && bean.extend_order_goods.size() > 0) {
            if (isCurrentStoreSelectAll(bean.extend_order_goods)) {
                mIvStoreAll.setImageResource(R.mipmap.img_address_checkbox_checked);
            } else {
                mIvStoreAll.setImageResource(R.mipmap.img_address_checkbox);
            }

            mIvStoreAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isCurrentStoreSelectAll(bean.extend_order_goods)) {
                        mCheckList.removeAll(bean.extend_order_goods);
                    } else {
                        mCheckList.addAll(bean.extend_order_goods);
                    }
                }
            });
        }

    }

    /***
     * 设置商品数量
     *
     * @param ll_shop
     * @param bean
     * @param size
     */
    private void setShopText(LinearLayout ll_shop, MyOrderListBean.OrderList bean, int size) {
        for (int i = 0; i < size; i++) {
            View shopView = mInflater.inflate(R.layout.item_shop_cart_info, ll_shop, false);
            ImageView iv_check = (ImageView) shopView.findViewById(R.id.iv_item_check);
            ImageView iv_shop_img = (ImageView) shopView.findViewById(R.id.iv_shop_img); // 商品图片
            TextView tv_shop_name = (TextView) shopView.findViewById(R.id.tv_shop_name); // 商品名称
            TextView tv_shop_atrribute = (TextView) shopView.findViewById(R.id.tv_shop_atrribute); // 商品属性:属性值
            TextView tv_single_shop_price = (TextView) shopView.findViewById(R.id.tv_single_shop_price); // 单个商品价格 ￥200.0
            TextView mTvDelete = (TextView) shopView.findViewById(R.id.tv_delete);


            final MyOrderShopBean myOrderShopBean = bean.extend_order_goods.get(i);

            GlideUtils.loadImage(myOrderShopBean.goods_image_url, iv_shop_img);
            tv_shop_name.setText(myOrderShopBean.goods_name);
//            tv_shop_atrribute.setText("商品属性:");
            tv_single_shop_price.setText(myOrderShopBean.goods_price);

            if (isCurrentGoodSelect(myOrderShopBean)) {
                iv_check.setImageResource(R.mipmap.img_address_checkbox_checked);
            } else {
                iv_check.setImageResource(R.mipmap.img_address_checkbox);
            }

            iv_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isCurrentGoodSelect(myOrderShopBean)) {
                        mCheckList.remove(myOrderShopBean);
                    } else {
                        mCheckList.add(myOrderShopBean);
                    }
                }
            });

            mTvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


            ll_shop.addView(shopView);
        }
    }

    public void setmCheckListener(OnCheckListener mCheckListener) {
        this.mCheckListener = mCheckListener;
    }


    /**
     *
     * @return
     * @param extend_order_goods
     */
    private boolean isCurrentStoreSelectAll(List<MyOrderShopBean> extend_order_goods){
        if (mCheckList.size() == 0) {
            return false;
        }

        if (extend_order_goods == null || extend_order_goods.isEmpty()) {
            return false;
        }

        if (mCheckList.size() < extend_order_goods.size()) {
            return false;
        }

        if (mCheckList.containsAll(extend_order_goods)) {
            return true;
        } else {
            return false;
        }

    }


    private boolean isCurrentGoodSelect(MyOrderShopBean order_goods){
        if (mCheckList.size() == 0) {
            return false;
        }

        if (mCheckList.contains(order_goods)) {
            return true;
        } else {
            return false;
        }

    }


    interface OnCheckListener{
        void OnItemCheck(MyOrderShopBean myOrderShopBean);
    }
}
