package com.wxj.hbys.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wxj.hbys.R;
import com.wxj.hbys.adapter.viewholder.SuperViewHolder;
import com.wxj.hbys.bean.HelpPostBean;

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
//        HelpPostViewHolder viewHolder = (HelpPostViewHolder) holder;
        TextView textView = holder.getView(R.id.tv_content);
        HelpPostBean bean = (HelpPostBean) mDataList.get(position);

        textView.setText(bean.content);
    }


    class HelpPostViewHolder extends SuperViewHolder{

        @BindView(R.id.tv_content)
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
        TextView tv_reward;


        public HelpPostViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
