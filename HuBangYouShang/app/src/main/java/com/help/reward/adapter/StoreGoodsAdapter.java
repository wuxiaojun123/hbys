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

public class StoreGoodsAdapter extends BaseRecyclerAdapter<ShopMallHotBean> {
    String type = "grid";//横竖排

    public StoreGoodsAdapter(Context context) {
        super(context);
    }


    @Override
    public int getLayoutId() {
        if ("grid".equals(type)) {
            return R.layout.item_hot_shop;
        } else {
            return R.layout.item_list_goods;
        }
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {

        if ("grid".equals(type)) {
            View root_layout = holder.getView(R.id.root_layout);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) root_layout.getLayoutParams();
//                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (position % 2 == 0) {
                lp.setMargins(0, DisplayUtil.dip2px(mContext, 10), DisplayUtil.dip2px(mContext, 5), 0);
                root_layout.setLayoutParams(lp);
            } else {
                lp.setMargins(DisplayUtil.dip2px(mContext, 5), DisplayUtil.dip2px(mContext, 10), 0, 0);
                root_layout.setLayoutParams(lp);
            }
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