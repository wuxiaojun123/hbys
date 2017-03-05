package com.wxj.hbys.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.wxj.hbys.R;
import com.wxj.hbys.adapter.viewholder.SuperViewHolder;
import com.wxj.hbys.bean.AdvertisementBean;
import com.wxj.hbys.utils.GlideUtils;

/**
 * 加群购买
 * Created by wuxiaojun on 17-3-2.
 */

public class IntegrationIntegrationBuyingAdapter extends BaseRecyclerAdapter {

    public IntegrationIntegrationBuyingAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_ad;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        TextView tv_title = holder.getView(R.id.tv_title);
        TextView tv_name = holder.getView(R.id.tv_name);
        TextView tv_score = holder.getView(R.id.tv_score);
        ImageView iv_img_ad = holder.getView(R.id.iv_img_ad);

        AdvertisementBean bean = (AdvertisementBean) mDataList.get(position);
        tv_title.setText(bean.name);
        tv_name.setText(bean.user_name);
        tv_score.setText(bean.credit);
        GlideUtils.loadImage(bean.url,iv_img_ad);

    }

}
