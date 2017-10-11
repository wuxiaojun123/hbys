package com.help.reward.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.ShopMallHotBean;
import com.help.reward.utils.DisplayUtil;
import com.help.reward.utils.GlideUtils;

/**
 * Created by MXY on 2017/2/19.
 */

public class PayEndGoodsAdapter extends BaseRecyclerAdapter<ShopMallHotBean> {

    public PayEndGoodsAdapter(Context context) {
        super(context);
    }


    @Override
    public int getLayoutId() {
        return R.layout.item_hot_shop;
    }

    @Override
    public int getItemCount() {
        if (mDataList.size() == 0) {
            return 1;
        }
        return super.getItemCount();
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        View root_layout = holder.getView(R.id.root_layout);
        if (mDataList.size() == 0) {
            root_layout.setVisibility(View.GONE);
            return;
        }
        root_layout.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) root_layout.getLayoutParams();
        if (position % 2 == 0) {
            lp.setMargins(0, DisplayUtil.dip2px(mContext, 10), DisplayUtil.dip2px(mContext, 5), 0);
            root_layout.setLayoutParams(lp);
        } else {
            lp.setMargins(DisplayUtil.dip2px(mContext, 5), DisplayUtil.dip2px(mContext, 10), 0, 0);
            root_layout.setLayoutParams(lp);
        }
        ShopMallHotBean item = mDataList.get(position);

        ImageView iv_shop_img = holder.getView(R.id.iv_shop_img);
        GlideUtils.loadImage(item.goods_image_url, iv_shop_img);

        TextView tv_shop_name = holder.getView(R.id.tv_shop_name);
        tv_shop_name.setText(item.goods_name);

        TextView tv_shop_price = holder.getView(R.id.tv_shop_price);
        tv_shop_price.setText("￥" + item.goods_price);

        TextView tv_num = holder.getView(R.id.tv_num);
        tv_num.setText(item.goods_salenum + "人付款");

    }
}