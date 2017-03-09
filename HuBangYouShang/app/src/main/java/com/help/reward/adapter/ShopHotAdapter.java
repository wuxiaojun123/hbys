package com.help.reward.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.bean.ShopMallHotBean;
import com.help.reward.utils.GlideUtils;

import java.util.List;

/**
 * 商城首页--热门商品
 * Created by wuxiaojun on 2017/3/7.
 */

public class ShopHotAdapter extends CommonAdapter<ShopMallHotBean> {


    public ShopHotAdapter(Context context, List<ShopMallHotBean>  hot_goods_list, int layoutId) {
        super(context, hot_goods_list, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, ShopMallHotBean bean) {

        ImageView iv_shop_img = holder.getView(R.id.iv_shop_img);
        GlideUtils.loadImage(bean.goods_image_url, iv_shop_img);

        TextView tv_shop_name = holder.getView(R.id.tv_shop_name);
        tv_shop_name.setText(bean.goods_name);

        TextView tv_shop_price = holder.getView(R.id.tv_shop_price);
        tv_shop_price.setText(bean.goods_price);

        TextView tv_num = holder.getView(R.id.tv_num);
        tv_num.setText(bean.goods_salenum+"人付款");
    }

}
