package com.wxj.hbys.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wxj.hbys.R;
import com.wxj.hbys.adapter.viewholder.SuperViewHolder;
import com.wxj.hbys.bean.MyCollectionGoodsBean;
import com.wxj.hbys.bean.MyCollectionStoreBean;
import com.wxj.hbys.minterface.OnItemDeleteListener;
import com.wxj.hbys.utils.GlideUtils;
import com.wxj.hbys.view.SwipeMenuView;

/**
 * 我的收藏--帖子
 * Created by wuxiaojun on 2017/2/26.
 */

public class MyCollectionStoreAdapter extends BaseRecyclerAdapter {

    public MyCollectionStoreAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_my_collection_store;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        ImageView iv_store = holder.getView(R.id.iv_store);
        TextView tv_title = holder.getView(R.id.tv_title);
        TextView tv_describe = holder.getView(R.id.tv_describe);
        TextView tv_server = holder.getView(R.id.tv_server);
        TextView tv_logistics = holder.getView(R.id.tv_logistics);
        TextView tv_collection = holder.getView(R.id.tv_collection);

        TextView tv_delete = holder.getView(R.id.tv_delete);

        ((SwipeMenuView) holder.itemView).setIos(false).setLeftSwipe(true);

        MyCollectionStoreBean bean = (MyCollectionStoreBean) mDataList.get(position);

        GlideUtils.loadImage(bean.store_avatar_url, iv_store);
        tv_title.setText(bean.store_name);
        tv_collection.setText(bean.store_collect);

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
