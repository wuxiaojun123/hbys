package com.wxj.hbys.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wxj.hbys.R;
import com.wxj.hbys.bean.ShopMallStoreBean;
import com.wxj.hbys.utils.GlideUtils;

import java.util.List;

/**
 * 商城首页--店铺推荐
 * Created by wuxiaojun on 2017/3/7.
 */

public class StoreRecommandAdapter extends CommonAdapter<ShopMallStoreBean> {


    public StoreRecommandAdapter(Context context, List<ShopMallStoreBean> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, ShopMallStoreBean bean) {

        ImageView iv_store_img = holder.getView(R.id.iv_store_img);
        GlideUtils.loadImage(bean.store_avatar, iv_store_img);
        TextView tv_store_name = holder.getView(R.id.tv_store_name);
        tv_store_name.setText(bean.store_name);
    }

}
