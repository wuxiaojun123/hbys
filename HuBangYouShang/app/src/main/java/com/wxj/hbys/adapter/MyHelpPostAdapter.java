package com.wxj.hbys.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wxj.hbys.R;
import com.wxj.hbys.adapter.viewholder.SuperViewHolder;
import com.wxj.hbys.bean.HelpPostBean;
import com.wxj.hbys.bean.Response.MyHelpPostResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wuxiaojun on 2017/2/26.
 */

public class MyHelpPostAdapter extends BaseRecyclerAdapter {

    public MyHelpPostAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_my_help_post;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        TextView tv_content = holder.getView(R.id.tv_content);
        TextView tv_title = holder.getView(R.id.tv_title);
        TextView tv_time = holder.getView(R.id.tv_time);
        TextView tv_state = holder.getView(R.id.tv_state);
        TextView tv_post = holder.getView(R.id.tv_post);
        TextView tv_reward = holder.getView(R.id.tv_reward);

        MyHelpPostResponse bean = (MyHelpPostResponse) mDataList.get(position);

        tv_content.setText(bean.title);
        tv_title.setText(bean.board_name);
        tv_time.setText(bean.create_time);
        tv_state.setText(bean.status);
        tv_post.setText(bean.comment);
        tv_reward.setText(bean.offer);

    }


    /*class HelpPostViewHolder extends SuperViewHolder{
        *//*@BindView(R.id.tv_content)
        TextView tv_content;
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.tv_state)
        TextView tv_state;
        @BindView(R.id.tv_post)
        TextView tv_post;
        @BindView(R.id.tv_reward)
        TextView tv_reward;*//*

        public HelpPostViewHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind(this,itemView);
        }
    }*/

}
