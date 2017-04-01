package com.help.reward.adapter;

import android.content.Context;
import android.widget.TextView;

import com.idotools.utils.DateUtil;
import com.help.reward.R;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.Response.MyRewardCommentResponse;

/**
 * Created by wuxiaojun on 2017/2/26.
 */

public class MyRewardCommentAdapter extends BaseRecyclerAdapter {

    public MyRewardCommentAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_my_reward_comment;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        TextView tv_content = holder.getView(R.id.tv_content);
        TextView tv_title = holder.getView(R.id.tv_title);
        TextView tv_time = holder.getView(R.id.tv_time);
//        TextView tv_state = holder.getView(R.id.tv_state);
        TextView tv_post = holder.getView(R.id.tv_post);
        TextView tv_reward = holder.getView(R.id.tv_reward);
        TextView tv_help = holder.getView(R.id.tv_help);


        MyRewardCommentResponse bean = (MyRewardCommentResponse) mDataList.get(position);

        tv_content.setText(bean.title);
        tv_title.setText(bean.board_name);
        tv_time.setText(DateUtil.getDateToString(Long.parseLong(bean.create_time)*1000));
//        tv_state.setText(bean.status);
        tv_post.setText(bean.comment);
        tv_reward.setText(bean.admiration);
        tv_help.setText(bean.u_name);

    }

}
