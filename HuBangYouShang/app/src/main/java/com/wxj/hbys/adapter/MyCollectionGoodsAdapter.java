package com.wxj.hbys.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.idotools.utils.DateUtil;
import com.wxj.hbys.R;
import com.wxj.hbys.adapter.viewholder.SuperViewHolder;
import com.wxj.hbys.bean.MyCollectionGoodsBean;
import com.wxj.hbys.bean.MyCollectionPostBean;
import com.wxj.hbys.minterface.OnItemDeleteListener;
import com.wxj.hbys.utils.GlideUtils;
import com.wxj.hbys.view.SwipeMenuView;

/**
 * 我的收藏--帖子
 * Created by wuxiaojun on 2017/2/26.
 */

public class MyCollectionGoodsAdapter extends BaseRecyclerAdapter {

    public MyCollectionGoodsAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_my_collection_goods;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        ImageView iv_goods = holder.getView(R.id.iv_goods);
        TextView tv_title = holder.getView(R.id.tv_title);
        TextView tv_price = holder.getView(R.id.tv_price);
        TextView tv_delete = holder.getView(R.id.tv_delete);

        ((SwipeMenuView) holder.itemView).setIos(false).setLeftSwipe(true);

        MyCollectionGoodsBean bean = (MyCollectionGoodsBean) mDataList.get(position);

        GlideUtils.loadImage(bean.goods_image_url, iv_goods);
        tv_title.setText(bean.goods_name);
        tv_price.setText(bean.goods_price);

        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItenDeleteListener != null) {
                    mOnItenDeleteListener.deleteItem(position);
                }
            }
        });
    }

    private OnItemDeleteListener mOnItenDeleteListener;

    public void setOnItemDeleteListener(OnItemDeleteListener lis) {
        this.mOnItenDeleteListener = lis;
    }

}
