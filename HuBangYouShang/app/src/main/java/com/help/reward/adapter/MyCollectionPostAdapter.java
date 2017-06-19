package com.help.reward.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.idotools.utils.DateUtil;
import com.help.reward.R;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.MyCollectionPostBean;
import com.help.reward.minterface.OnItemDeleteListener;
import com.help.reward.view.SwipeMenuView;

/**
 * 我的收藏--帖子
 * Created by wuxiaojun on 2017/2/26.
 */

public class MyCollectionPostAdapter extends BaseRecyclerAdapter {

    public MyCollectionPostAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_my_collection_post;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        TextView tv_content = holder.getView(R.id.tv_content);
        TextView tv_title = holder.getView(R.id.tv_title);
        TextView tv_time = holder.getView(R.id.tv_time);
        TextView tv_help = holder.getView(R.id.tv_help);
        TextView tv_post = holder.getView(R.id.tv_post);
        TextView tv_reward = holder.getView(R.id.tv_reward);
        TextView tv_delete = holder.getView(R.id.tv_delete);

        ((SwipeMenuView)holder.itemView).setIos(false).setLeftSwipe(true);

        MyCollectionPostBean bean = (MyCollectionPostBean) mDataList.get(position);

        tv_content.setText(bean.title);
        tv_title.setText(bean.board_name);
        tv_time.setText(DateUtil.getDateToString(bean.create_time));
        tv_help.setText(bean.u_name);
        tv_post.setText(bean.comment);
        tv_reward.setText(bean.admiration);

        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItenDeleteListener != null){
                    mOnItenDeleteListener.deleteItem(position);
                }
            }
        });
    }

    private OnItemDeleteListener mOnItenDeleteListener;

    public void setOnItemDeleteListener(OnItemDeleteListener lis){
        this.mOnItenDeleteListener = lis;
    }

}
