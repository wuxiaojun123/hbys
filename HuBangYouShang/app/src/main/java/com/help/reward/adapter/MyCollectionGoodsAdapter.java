package com.help.reward.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.MyCollectionGoodsBean;
import com.help.reward.minterface.OnItemDeleteListener;
import com.help.reward.minterface.OnMyItemClickListener;
import com.help.reward.utils.GlideUtils;
import com.help.reward.view.SwipeMenuView;

/**
 * 我的收藏--帖子
 * Created by wuxiaojun on 2017/2/26.
 */

public class MyCollectionGoodsAdapter extends BaseRecyclerAdapter<MyCollectionGoodsBean> {

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
        LinearLayout id_ll_content = holder.getView(R.id.id_ll_content);

        ((SwipeMenuView) holder.itemView).setIos(false).setLeftSwipe(true);

        MyCollectionGoodsBean bean = mDataList.get(position);

        GlideUtils.loadImage(bean.goods_image_url, iv_goods);
        tv_title.setText(bean.goods_name);
        tv_price.setText(bean.goods_price);

        id_ll_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myItemClickListener != null) {
                    myItemClickListener.onMyItemClickListener(position);
                }
            }
        });

        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItenDeleteListener != null) {
                    mOnItenDeleteListener.deleteItem(position);
                }
            }
        });
    }

    private OnMyItemClickListener myItemClickListener;

    public void setOnMyItemClickListener(OnMyItemClickListener onMyItemClickListener) {
        this.myItemClickListener = onMyItemClickListener;
    }

    private OnItemDeleteListener mOnItenDeleteListener;

    public void setOnItemDeleteListener(OnItemDeleteListener lis) {
        this.mOnItenDeleteListener = lis;
    }

}
