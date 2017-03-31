package com.help.reward.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.GoodsTypeBean;
import com.help.reward.utils.GlideUtils;


/**
 *
 */

public class GoodsTypeAdapter extends BaseRecyclerAdapter<GoodsTypeBean> {
    Activity mActivity;

    public GoodsTypeAdapter(Activity context) {
        super(context);
        this.mActivity = context;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_goodstype;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {


        final GoodsTypeBean item = mDataList.get(position);

        TextView tv_name = holder.getView(R.id.tv_name);
        tv_name.setText(item.gc_name);
        ImageView iv_icon = holder.getView(R.id.iv_icon);
        View bottomLine = holder.getView(R.id.bottomLine);
        if (item.isSelect) {
            GlideUtils.loadImage(item.image, iv_icon);
            bottomLine.setVisibility(View.VISIBLE);
            tv_name.setTextColor(Color.parseColor("#fa372d"));
        } else {
            GlideUtils.loadImage(item.image_inactive, iv_icon);
            bottomLine.setVisibility(View.GONE);
            tv_name.setTextColor(Color.parseColor("#bfbfbf"));
        }

    }
}