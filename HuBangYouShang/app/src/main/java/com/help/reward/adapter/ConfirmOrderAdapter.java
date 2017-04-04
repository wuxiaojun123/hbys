package com.help.reward.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.MyOrderListBean;
import com.help.reward.bean.MyOrderShopBean;
import com.help.reward.bean.Response.CartInfoBean;
import com.help.reward.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ADBrian on 04/04/2017.
 */

public class ConfirmOrderAdapter extends RecyclerView.Adapter<SuperViewHolder> {



    public static final int ITEM_HEAD = 1;
    public static final int ITEM_BOTTOM = ITEM_HEAD + 1;
    public static final int ITEM_NORMAL = ITEM_BOTTOM + 1;

    protected List<CartInfoBean> mDataList = new ArrayList<CartInfoBean>();

    protected Context mContext;

    protected LayoutInflater mInflater;

    public ConfirmOrderAdapter(Context mContext) {
        this.mDataList = mDataList;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);

    }

    public void setDataList(List<CartInfoBean> mDataList){
        if (mDataList != null) {
            this.mDataList.clear();
            this.mDataList.addAll(mDataList);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
             return ITEM_HEAD;
        } else if (position == mDataList.size() + 1) {
            return ITEM_BOTTOM;
        } else {
            return ITEM_NORMAL;
        }
    }

    @Override
    public SuperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;
        switch (viewType) {
            case ITEM_HEAD:
                itemView = mInflater.inflate(R.layout.item_my_order_confirm_title, parent, false);
                break;
            case ITEM_BOTTOM:
                itemView = mInflater.inflate(R.layout.item_my_order_confirm_bottom, parent, false);
                break;
           default:
               itemView = mInflater.inflate(R.layout.item_my_order_confirm, parent, false);
               break;
        }
        return new SuperViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(SuperViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case ITEM_HEAD:
                doHeadView(holder,position);
                break;
            case ITEM_BOTTOM:
                doBottomView(holder,position);
                break;
            default:
                doNormalView(holder,position);
                break;
        }

    }

    private void doNormalView(SuperViewHolder holder, int position) {
        TextView tv_store_name = holder.getView(R.id.tv_store_name); // 商家名称
        TextView tv_total_shop_and_money = holder.getView(R.id.tv_total_shop_and_money); // 共计：4件商品  合计：￥235元(含运费12元)

        LinearLayout ll_shop = holder.getView(R.id.ll_shop); // 商品列表

        CartInfoBean bean = mDataList.get(position - 1);

        int size = 0;
        if (ll_shop.getTag() == null && bean.goods != null) {
            size = bean.goods.size();
            setShopText(ll_shop, bean, size);
            ll_shop.setTag(bean.store_id);
        }

        tv_store_name.setText(bean.store_name);
    }

    private void setShopText(LinearLayout ll_shop, CartInfoBean bean, int size) {

        for (int i = 0; i < size; i++) {
            View shopView = mInflater.inflate(R.layout.layout_my_order_shop, ll_shop, false);
            ImageView iv_shop_img = (ImageView) shopView.findViewById(R.id.iv_shop_img); // 商品图片
            TextView tv_shop_name = (TextView) shopView.findViewById(R.id.tv_shop_name); // 商品名称
//            TextView tv_shop_atrribute = (TextView) shopView.findViewById(R.id.tv_shop_atrribute); // 商品属性:属性值
            TextView tv_single_shop_price = (TextView) shopView.findViewById(R.id.tv_single_shop_price); // 单个商品价格 ￥200.0
            TextView tv_shop_num = (TextView) shopView.findViewById(R.id.tv_shop_num); // 商品数量 x1

            CartInfoBean.GoodInfoBean goodInfoBean = bean.goods.get(i);

            GlideUtils.loadImage(goodInfoBean.goods_image_url, iv_shop_img);
            tv_shop_name.setText(goodInfoBean.goods_name);
//            tv_shop_atrribute.setText("商品属性:");
            tv_single_shop_price.setText(goodInfoBean.goods_price);
            tv_shop_num.setText("x" + goodInfoBean.goods_num);
            ll_shop.addView(shopView);
        }
    }

    private void doBottomView(SuperViewHolder holder, int position) {
    }

    private void doHeadView(SuperViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return mDataList.size() + 2;
    }
}
